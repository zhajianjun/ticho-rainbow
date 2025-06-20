<template>
  <div>
    <BasicTable @register="registerTable">
      <template #toolbar>
        <a-button
          type="primary"
          v-auth="'PortAdd'"
          preIcon="ant-design:plus-outlined"
          @click="handleCreate"
        >
          新增
        </a-button>
        <a-button
          type="primary"
          ghost
          v-auth="'PortExport'"
          preIcon="ant-design:download-outlined"
          :loading="exportLoding"
          @click="handleExport"
          style="color: #2a7dc9"
        >
          导出
        </a-button>
      </template>
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'status'">
          <Switch
            :checked-children="getDictLabelByCodeAndValue('commonStatus', 1)"
            :un-checked-children="getDictLabelByCodeAndValue('commonStatus', 0)"
            :checked="record.status === 1"
            :loading="record.pendingStatus"
            @change="handleSwitchChange(record)"
          />
        </template>
      </template>
      <template #action="{ record }">
        <TableAction
          :actions="[
            {
              icon: 'clarity:note-edit-line',
              onClick: handleEdit.bind(null, record),
              tooltip: '修改',
              auth: 'PortEdit',
            },
            {
              icon: 'ant-design:copy-outlined',
              onClick: handleCopy.bind(null, record),
              tooltip: '复制',
              auth: 'PortCopy',
            },
            {
              icon: 'ant-design:delete-outlined',
              color: 'error',
              popConfirm: {
                title: '是否确认删除',
                confirm: handleDelete.bind(null, record),
              },
              tooltip: '删除',
              auth: 'PortDel',
            },
          ]"
        />
      </template>
    </BasicTable>
    <PortModal @register="registerModal" @success="handleSuccess" />
  </div>
</template>
<script lang="ts">
  import { defineComponent, ref } from 'vue';
  import { BasicTable, TableAction, useTable } from '@/components/Table';
  import { useModal } from '@/components/Modal';
  import PortModal from './PortModal.vue';
  import { getSearchColumns, getTableColumns } from './port.data';
  import { delPort, disablePort, enablePort, expExcel, portPage } from '@/api/intranet/port';
  import { Switch } from 'ant-design-vue';
  import { useMessage } from '@/hooks/web/useMessage';
  import { PortQuery } from '@/api/intranet/model/portModel';
  import { usePermission } from '@/hooks/web/usePermission';
  import { getDictLabelByCodeAndValue } from '@/store/modules/dict';
  import { downloadByData } from '@/utils/file/download';
  import { VersionModifyCommand } from '@/api/system/model/baseModel';
  import { formatToDateTime } from '@/utils/dateUtil';

  export default defineComponent({
    name: 'Port',
    components: { Switch, BasicTable, PortModal, TableAction },
    setup() {
      const { hasPermission } = usePermission();
      let showSelect = hasPermission('PortSelect');
      const [registerModal, { openModal }] = useModal();
      const [registerTable, { reload, getSelectRowKeys, getForm }] = useTable({
        title: '端口信息列表',
        api: portPage,
        rowKey: 'id',
        columns: getTableColumns(),
        useSearchForm: true,
        formConfig: {
          labelWidth: 120,
          schemas: getSearchColumns(),
          showActionButtonGroup: showSelect,
          showSubmitButton: showSelect,
          showResetButton: showSelect,
          autoSubmitOnEnter: showSelect,
          submitButtonOptions: {
            preIcon: 'ant-design:search-outlined',
          },
          resetButtonOptions: {
            preIcon: 'ant-design:sync-outlined',
          },
        },
        tableSetting: {
          redo: showSelect,
        },
        immediate: showSelect,
        showTableSetting: true,
        bordered: true,
        showIndexColumn: false,
        actionColumn: {
          width: 80,
          title: '操作',
          dataIndex: 'action',
          slots: { customRender: 'action' },
          fixed: undefined,
        },
        pagination: {
          simple: false,
          position: ['bottomCenter'],
        },
        showSelectionBar: true,
        rowSelection: {
          type: 'checkbox',
        },
      });

      function handleCreate() {
        openModal(true, {
          modalType: 1,
        });
      }

      function handleEdit(record: Recordable) {
        openModal(true, {
          record,
          modalType: 2,
        });
      }

      function handleCopy(record: Recordable) {
        openModal(true, {
          record,
          modalType: 3,
        });
      }

      function handleDelete(record: Recordable) {
        const params = { ...record } as VersionModifyCommand;
        delPort(params).then(() => {
          reload();
        });
      }

      function handleSuccess() {
        reload();
      }

      function handleSwitchChange(record: Recordable) {
        if (!record || typeof record.status !== 'number') {
          console.warn('Invalid record provided to handleSwitchChange');
          return;
        }
        const { createMessage } = useMessage();
        const checked = record.status === 1;
        let oprate: Promise<any>;
        const messagePrefix = getDictLabelByCodeAndValue('commonStatus', checked ? 0 : 1);
        record.pendingStatus = true;

        try {
          const param = {
            id: record.id,
            version: record.version,
          } as VersionModifyCommand;
          if (checked) {
            oprate = disablePort([param]);
          } else {
            if (record.expireAt === null || record.expireAt === '') {
              createMessage.error(`端口[` + record.port + `]过期日期不能为空`);
              record.pendingStatus = false;
              return;
            }
            if (formatToDateTime(new Date()) >= formatToDateTime(record.expireAt)) {
              createMessage.error(`端口[` + record.port + `]已过期`);
              record.pendingStatus = false;
              return;
            }
            oprate = enablePort([param]);
          }
        } catch (error) {
          record.pendingStatus = false;
          return;
        }
        oprate
          .then(() => {
            // 仅在请求成功后更新状态
            record.status = checked ? 0 : 1;
            createMessage.success(messagePrefix + `成功`);
            reload();
          })
          .finally(() => {
            record.pendingStatus = false;
          });
      }

      const exportLoding = ref<Boolean>(false);
      const { createMessage } = useMessage();

      function handleExport() {
        exportLoding.value = true;
        // 是否有选中，优先下载选中数据
        const selectRowKeys = getSelectRowKeys();
        let params: PortQuery;
        if (selectRowKeys && selectRowKeys.length > 0) {
          params = { ids: selectRowKeys } as PortQuery;
        } else {
          // 获取查询参数
          const { getFieldsValue } = getForm();
          params = getFieldsValue() as PortQuery;
        }
        expExcel(params)
          .then((res) => {
            // 提取文件名
            let fileName = decodeURI(res.headers['content-disposition'].split('filename=')[1]);
            downloadByData(res.data, fileName);
            createMessage.info(`导出成功, ${fileName}已下载`);
          })
          .finally(() => {
            exportLoding.value = false;
          });
      }

      return {
        registerTable,
        registerModal,
        handleCreate,
        handleCopy,
        handleEdit,
        handleDelete,
        handleSuccess,
        handleSwitchChange,
        exportLoding,
        handleExport,
      };
    },
    methods: { getDictLabelByCodeAndValue },
  });
</script>
