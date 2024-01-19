<template>
  <div>
    <BasicTable @register="registerTable">
      <template #toolbar>
        <a-button type="primary" v-auth="'ClientAdd'" @click="handleCreate"> 新增 </a-button>
      </template>
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'status'">
          <Switch
            checked-children="开启"
            un-checked-children="禁用"
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
              ifShow: hasPermission('ClientEdit'),
              divider: true,
            },
            {
              icon: 'ant-design:delete-outlined',
              color: 'error',
              ifShow: hasPermission('ClientDel'),
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
  import { defineComponent } from 'vue';
  import { Switch } from 'ant-design-vue';
  import { BasicTable, TableAction, useTable } from '@/components/Table';
  import { useModal } from '@/components/Modal';
  import ClientModel from './ClientModal.vue';
  import { getSearchColumns, getTableColumns } from './client.data';
  import { clientPage, delClient, modifyClientStatus } from '@/api/intranet/client';
  import { useMessage } from '@/hooks/web/useMessage';
  import { ClientDTO } from '@/api/intranet/model/clientModel';
  import { usePermission } from '@/hooks/web/usePermission';

  export default defineComponent({
    name: 'ClientManagement',
    components: { BasicTable, ClientModel, TableAction, Switch },
    setup() {
      const { hasPermission } = usePermission();
      let showSelect = hasPermission('ClientSelect');
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
          showActionButtonGroup: showSelect,
          showSubmitButton: showSelect,
          showResetButton: showSelect,
          autoSubmitOnEnter: showSelect,
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
        record.pendingStatus = true;
        const { createMessage } = useMessage();
        let checked = record.status === 1;
        if (checked) {
          record.status = 0;
        } else {
          record.status = 1;
        }
        const params = { id: record.id, status: record.status } as ClientDTO;
        const messagePrefix = !checked ? '启动' : '关闭';
        modifyClientStatus(params)
          .then(() => {
            createMessage.success(messagePrefix + `成功`);
          })
          .catch(() => {
            createMessage.error(messagePrefix + `失败`);
          })
          .finally(() => {
            record.pendingStatus = false;
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
        hasPermission,
      };
    },
  });
</script>
