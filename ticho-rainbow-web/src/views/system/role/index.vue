<template>
  <div>
    <BasicTable @register="registerTable">
      <template #toolbar>
        <a-button
          type="primary"
          v-auth="'RoleAdd'"
          preIcon="ant-design:plus-outlined"
          @click="handleCreate"
        >
          新增
        </a-button>
        <a-button
          type="primary"
          ghost
          v-auth="'RoleExport'"
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
              auth: 'RoleEdit',
            },
            {
              icon: 'ant-design:delete-outlined',
              color: 'error',
              auth: 'RoleDel',
              popConfirm: {
                title: '是否确认删除',
                confirm: handleDelete.bind(null, record),
              },
            },
          ]"
        />
      </template>
    </BasicTable>
    <RoleDrawer @register="registerDrawer" @success="handleSuccess" />
  </div>
</template>
<script lang="ts">
  import { defineComponent, ref } from 'vue';
  import { BasicTable, TableAction, useTable } from '@/components/Table';
  import { useDrawer } from '@/components/Drawer';
  import RoleDrawer from './RoleDrawer.vue';
  import { columns, searchFormSchema } from './role.data';
  import { delRole, disableRole, enableRole, expExcel, rolePage } from '@/api/system/role';
  import { usePermission } from '@/hooks/web/usePermission';
  import { downloadByData } from '@/utils/file/download';
  import { RoleQuery } from '@/api/system/model/roleModel';
  import { useMessage } from '@/hooks/web/useMessage';
  import { VersionModifyCommand } from '@/api/system/model/baseModel';
  import { Switch } from 'ant-design-vue';
  import { getDictLabelByCodeAndValue } from '@/store/modules/dict';

  export default defineComponent({
    name: 'RoleManagement',
    components: { Switch, BasicTable, RoleDrawer, TableAction },
    setup() {
      const { hasPermission } = usePermission();
      let showSelect = hasPermission('RoleSelect');
      const [registerDrawer, { openDrawer }] = useDrawer();
      const [registerTable, { reload, getSelectRowKeys, getForm }] = useTable({
        title: '角色列表',
        api: rolePage,
        rowKey: 'id',
        columns,
        useSearchForm: showSelect,
        formConfig: {
          labelWidth: 120,
          schemas: searchFormSchema,
          showActionButtonGroup: showSelect,
          showSubmitButton: showSelect,
          showResetButton: showSelect,
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
        openDrawer(true, {
          isUpdate: false,
        });
      }

      function handleEdit(record: Recordable) {
        openDrawer(true, {
          record,
          isUpdate: true,
        });
      }

      function handleDelete(record: Recordable) {
        const params = { ...record } as VersionModifyCommand;
        delRole(params).then(() => {
          reload();
        });
      }

      function handleSuccess() {
        reload();
      }

      const exportLoding = ref<Boolean>(false);
      const { createMessage } = useMessage();

      function handleExport() {
        exportLoding.value = true;
        // 是否有选中，优先下载选中数据
        const selectRowKeys = getSelectRowKeys();
        let params: RoleQuery;
        if (selectRowKeys && selectRowKeys.length > 0) {
          params = { ids: selectRowKeys } as RoleQuery;
        } else {
          // 获取查询参数
          const { getFieldsValue } = getForm();
          params = getFieldsValue() as RoleQuery;
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
          oprate = checked ? disableRole([param]) : enableRole([param]);
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

      return {
        registerTable,
        registerDrawer,
        handleCreate,
        handleEdit,
        handleDelete,
        handleSuccess,
        hasPermission,
        exportLoding,
        handleExport,
        handleSwitchChange,
      };
    },
    methods: { getDictLabelByCodeAndValue },
  });
</script>
