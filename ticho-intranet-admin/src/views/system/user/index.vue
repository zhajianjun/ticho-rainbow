<template>
  <div>
    <BasicTable @register="registerTable">
      <template #toolbar>
        <a-button type="primary" @click="handleCreate"> 新增 </a-button>
      </template>
      <template #action="{ record }">
        <TableAction
          :actions="[
            {
              icon: 'clarity:note-edit-line',
              onClick: handleEdit.bind(null, record),
              tooltip: '修改',
            },
            {
              icon: 'ant-design:security-scan-outlined',
              onClick: handleEditPassword.bind(null, record),
              tooltip: '修改密码',
            },
            {
              icon: 'ant-design:delete-outlined',
              color: 'error',
              popConfirm: {
                title: '是否确认删除',
                confirm: handleDelete.bind(null, record),
              },
              tooltip: '删除',
            },
          ]"
        />
      </template>
    </BasicTable>
    <UserModel @register="registerModal" @success="handleSuccess" />
    <UserPasswordModal @register="registerPasswordModal" @success="handleSuccess" />
  </div>
</template>
<script lang="ts">
  import { defineComponent } from 'vue';
  import { BasicTable, useTable, TableAction } from '@/components/Table';
  import { useModal } from '@/components/Modal';
  import UserModel from './UserModal.vue';
  import UserPasswordModal from './UserPasswordModal.vue';
  import { getTableColumns, getSearchColumns } from './user.data';
  import { userPage, delUser } from '@/api/system/user';

  export default defineComponent({
    name: 'UserManagement',
    components: { BasicTable, UserModel, UserPasswordModal, TableAction },
    setup() {
      const [registerModal, { openModal }] = useModal();
      const [registerPasswordModal, { openModal: openPassModal }] = useModal();
      const [registerTable, { reload }] = useTable({
        title: '用户列表',
        api: userPage,
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
          width: 80,
          title: '操作',
          dataIndex: 'action',
          slots: { customRender: 'action' },
          fixed: undefined,
        },
        pagination: {
          pageSizeOptions: ['10', '15', '20', '50', '100'],
          pageSize: 15,
          defaultPageSize: 15,
          position: ['bottomLeft'],ve
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

      function handleEditPassword(record: Recordable) {
        openPassModal(true, {
          record,
        });
      }

      function handleDelete(record: Recordable) {
        delUser(record.id).then(() => {
          reload();
        });
      }

      function handleSuccess() {
        reload();
      }

      return {
        registerTable,
        registerModal,
        registerPasswordModal,
        handleCreate,
        handleEdit,
        handleEditPassword,
        handleDelete,
        handleSuccess,
      };
    },
  });
</script>
