<template>
  <div>
    <BasicTable @register="registerTable">
      <template #toolbar>
        <a-button type="primary" @click="handleCreate"> 新增 </a-button>
      </template>
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'enabled'">
          <Switch
            checked-children="开启"
            un-checked-children="关闭"
            :checked="record.enabled === 1"
            :loading="record.pendingEnabled"
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
              label: '修改',
              divider: true,
            },
            {
              icon: 'ant-design:delete-outlined',
              color: 'error',
              popConfirm: {
                title: '是否确认删除',
                confirm: handleDelete.bind(null, record),
              },
              tooltip: '删除',
              label: '删除',
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
  import { defineComponent } from 'vue';
  import { Switch } from 'ant-design-vue';
  import { BasicTable, TableAction, useTable } from '@/components/Table';
  import { useModal } from '@/components/Modal';
  import ClientModel from './ClientModal.vue';
  import { getSearchColumns, getTableColumns } from './client.data';
  import { clientPage, delClient, modifyClientEnabled } from '@/api/system/client';
  import { useMessage } from '@/hooks/web/useMessage';
  import { ClientDTO } from '@/api/system/model/clientModel';

  export default defineComponent({
    name: 'ClientManagement',
    components: { BasicTable, ClientModel, TableAction, Switch },
    setup() {
      const [registerModal, { openModal }] = useModal();
      const [registerTable, { reload }] = useTable({
        title: '客户端信息列表',
        api: clientPage,
        rowKey: 'id',
        columns: getTableColumns(),
        useSearchForm: true,
        formConfig: {
          labelWidth: 120,
          schemas: getSearchColumns(),
          showActionButtonGroup: true,
          showSubmitButton: true,
          showResetButton: true,
          autoSubmitOnEnter: true,
        },
        tableSetting: {
          redo: true,
        },
        immediate: true,
        showTableSetting: true,
        bordered: true,
        showIndexColumn: false,
        actionColumn: {
          width: 100,
          title: '操作',
          dataIndex: 'action',
          slots: { customRender: 'action' },
          fixed: undefined,
        },
        pagination: {
          pageSizeOptions: ['10', '20', '50', '100'],
          defaultPageSize: 20,
          position: ['bottomLeft'],
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
        delClient(record.id).then(() => {
          reload();
        });
      }

      function handleSuccess() {
        reload();
      }

      function handleSwitchChange(record: Recordable) {
        record.pendingEnabled = true;
        const { createMessage } = useMessage();
        let checked = record.enabled === 1;
        if (checked) {
          record.enabled = 0;
        } else {
          record.enabled = 1;
        }
        const params = { id: record.id, enabled: record.enabled } as ClientDTO;
        const messagePrefix = !checked ? '启动' : '关闭';
        modifyClientEnabled(params)
          .then(() => {
            createMessage.success(messagePrefix + `成功`);
          })
          .catch(() => {
            createMessage.error(messagePrefix + `失败`);
          })
          .finally(() => {
            record.pendingEnabled = false;
            reload();
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
      };
    },
  });
</script>
