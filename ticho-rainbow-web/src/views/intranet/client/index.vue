<template>
  <div>
    <BasicTable @register="registerTable">
      <template #toolbar>
        <a-button
          type="primary"
          v-auth="'ClientAdd'"
          preIcon="ant-design:plus-outlined"
          @click="handleCreate"
        >
          新增
        </a-button>
        <a-button
          type="primary"
          ghost
          v-auth="'ClientExport'"
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
              auth: 'ClientEdit',
              divider: true,
            },
            {
              icon: 'ant-design:delete-outlined',
              color: 'error',
              auth: 'ClientDel',
              popConfirm: {
                title: '是否确认删除',
                confirm: handleDelete.bind(null, record),
              },
              tooltip: '删除',
              divider: true,
            },
          ]"
        />
      </template>
    </BasicTable>
    <ClientModel @register="registerModal" @success="handleSuccess" />
  </div>
</template>
<script lang="ts">
  import { defineComponent, ref } from 'vue';
  import { Switch } from 'ant-design-vue';
  import { BasicTable, TableAction, useTable } from '@/components/Table';
  import { useModal } from '@/components/Modal';
  import ClientModel from './ClientModal.vue';
  import { getSearchColumns, getTableColumns } from './client.data';
  import {
    clientPage,
    delClient,
    disableClient,
    enableClient,
    expExcel,
  } from '@/api/intranet/client';
  import { useMessage } from '@/hooks/web/useMessage';
  import { ClientQuery } from '@/api/intranet/model/clientModel';
  import { usePermission } from '@/hooks/web/usePermission';
  import { getDictLabelByCodeAndValue } from '@/store/modules/dict';
  import { downloadByData } from '@/utils/file/download';
  import { VersionModifyCommand } from '@/api/system/model/baseModel';
  import { formatToDateTime } from '@/utils/dateUtil';

  export default defineComponent({
    name: 'ClientManagement',
    components: { BasicTable, ClientModel, TableAction, Switch },
    setup() {
      const { hasPermission } = usePermission();
      let showSelect = hasPermission('ClientSelect');
      const [registerModal, { openModal }] = useModal();
      const [registerTable, { reload, getSelectRowKeys, getForm }] = useTable({
        title: '客户端信息列表',
        api: clientPage,
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
          width: 60,
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
          isUpdate: false,
        });
      }

      function handleEdit(record: Recordable) {
        openModal(true, {
          record,
          isUpdate: true,
        });
      }

      function handleDelete(record: Recordable) {
        const params = { ...record } as VersionModifyCommand;
        delClient(params).then(() => {
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
            oprate = disableClient([param]);
          } else {
            if (record.expireAt === null || record.expireAt === '') {
              createMessage.error(`客户端[` + record.name + `]过期日期不能为空`);
              record.pendingStatus = false;
              return;
            }
            if (formatToDateTime(new Date()) >= formatToDateTime(record.expireAt)) {
              createMessage.error(`客户端[` + record.name + `]已过期`);
              record.pendingStatus = false;
              return;
            }
            oprate = enableClient([param]);
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
        let params: ClientQuery;
        if (selectRowKeys && selectRowKeys.length > 0) {
          params = { ids: selectRowKeys } as ClientQuery;
        } else {
          // 获取查询参数
          const { getFieldsValue } = getForm();
          params = getFieldsValue() as ClientQuery;
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
