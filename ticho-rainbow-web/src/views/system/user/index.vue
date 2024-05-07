<template>
  <PageWrapper dense contentFullHeight fixedHeight contentClass="flex">
    <BasicTable @register="registerTable" :searchInfo="searchInfo">
      <template #toolbar>
        <a-button type="primary" v-auth="'UserAdd'" @click="handleCreate">新增账号</a-button>
      </template>
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'roles'">
          <Space>
            <Tag color="success" v-for="item in record.roles" :key="item.code">
              {{ item.name }}
            </Tag>
          </Space>
        </template>
      </template>
      <template #action="{ record }">
        <TableAction
          :actions="[
            // {
            //   icon: 'clarity:info-standard-line',
            //   onClick: handleView.bind(null, record),
            //   tooltip: '查看',
            // },
            {
              icon: 'clarity:note-edit-line',
              onClick: handleEdit.bind(null, record),
              tooltip: '修改',
            },
            {
              icon: 'ant-design:security-scan-outlined',
              popConfirm: {
                title: '是否重置密码',
                confirm: resetPassword.bind(null, record),
              },
              tooltip: '重置密码',
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
  </PageWrapper>
</template>
<script lang="ts">
  import { defineComponent, reactive } from 'vue';
  import { BasicTable, useTable, TableAction } from '@/components/Table';
  import { userPage, delUser, resetUserPassword } from '@/api/system/user';
  import { PageWrapper } from '@/components/Page';
  import { useModal } from '@/components/Modal';
  import UserModel from './UserModal.vue';
  import { columns, searchFormSchema } from './user.data';
  import { useGo } from '@/hooks/web/usePage';
  import { usePermission } from '@/hooks/web/usePermission';
  import { Tag, Space } from 'ant-design-vue';
  import { useMessage } from '@/hooks/web/useMessage';

  export default defineComponent({
    name: 'AccountManagement',
    components: { BasicTable, PageWrapper, UserModel, TableAction, Tag, Space },
    setup() {
      const { hasPermission } = usePermission();
      let showSelect = hasPermission('UserSelect');
      const go = useGo();
      const [registerModal, { openModal }] = useModal();
      const searchInfo = reactive<Recordable>({});
      const [registerTable, { reload }] = useTable({
        title: '用户列表',
        api: userPage,
        rowKey: 'id',
        columns,
        useSearchForm: showSelect,
        formConfig: {
          labelWidth: 120,
          schemas: searchFormSchema,
          autoSubmitOnEnter: true,
          showActionButtonGroup: showSelect,
          showSubmitButton: showSelect,
          showResetButton: showSelect,
        },
        tableSetting: {
          redo: showSelect,
        },
        immediate: showSelect,
        showTableSetting: true,
        canResize: true,
        showIndexColumn: false,
        bordered: true,
        actionColumn: {
          width: 120,
          title: '操作',
          dataIndex: 'action',
          slots: { customRender: 'action' },
          fixed: undefined,
        },
        pagination: {
          simple: false,
          position: ['bottomLeft'],
        },
      });

      const { createMessage } = useMessage();

      function handleCreate() {
        openModal(true, {
          isUpdate: false,
        });
      }

      function handleEdit(record: Recordable) {
        console.log(record);
        openModal(true, {
          record,
          isUpdate: true,
        });
      }

      function resetPassword(record: Recordable) {
        resetUserPassword(record.username).then(() => {
          createMessage.success(`重置${record.nickname}密码成功`);
        });
      }

      function handleDelete(record: Recordable) {
        delUser(record.username).then(() => {
          createMessage.success('删除成功');
          reload();
        });
      }

      function handleSuccess() {
        reload();
      }

      function handleSelect(deptId = '') {
        searchInfo.deptId = deptId;
        reload();
      }

      function handleView(record: Recordable) {
        go(`/system/user/userDetail/${record.username}`);
      }

      return {
        registerTable,
        registerModal,
        handleCreate,
        handleEdit,
        resetPassword,
        handleDelete,
        handleSuccess,
        handleSelect,
        handleView,
        searchInfo,
        hasPermission,
      };
    },
  });
</script>
