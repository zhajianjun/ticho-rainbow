<template>
  <PageWrapper dense contentFullHeight fixedHeight contentClass="flex">
    <BasicTable @register="registerTable" :searchInfo="searchInfo">
      <template #toolbar>
        <a-button type="primary" v-auth="'UserAdd'" @click="handleCreate">
          <Icon icon="ant-design:plus-outlined" />
          新增
        </a-button>
        <a-button type="primary" danger ghost v-auth="'UserLock'" @click="handleBatch(Action.lockUser)">
          <Icon icon="ant-design:lock-outlined" />
          锁定
        </a-button>
        <a-button type="dashed" v-auth="'UserUnLock'" @click="handleBatch(Action.unLockUser)">
          <Icon icon="ant-design:unlock-outlined" />
          解锁
        </a-button>
        <a-button
          type="primary"
          ghost
          v-auth="'UserImport'"
          @click="handleCreate"
          style="color: #2a7dc9"
        >
          <Icon icon="ant-design:upload-outlined" />
          导入
        </a-button>
        <a-button
          type="primary"
          ghost
          v-auth="'UserExport'"
          @click="handleCreate"
          style="color: #2a7dc9"
        >
          <Icon icon="ant-design:download-outlined" />
          导出
        </a-button>
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
              disabled: record.status !== 1 || record.username === 'admin',
            },
            {
              icon: 'ant-design:logout-outlined',
              color: 'error',
              popConfirm: {
                title: '是否确认注销',
                confirm: handleLogOut.bind(null, record),
              },
              tooltip: '注销',
              disabled: record.status !== 1 || record.username === 'admin',
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
  import { userPage, resetUserPassword, lockUser, unlockUser, logOutUser } from '@/api/system/user';
  import { PageWrapper } from '@/components/Page';
  import { useModal } from '@/components/Modal';
  import UserModel from './UserModal.vue';
  import { columns, searchFormSchema } from './user.data';
  import { useGo } from '@/hooks/web/usePage';
  import { usePermission } from '@/hooks/web/usePermission';
  import { Tag, Space } from 'ant-design-vue';
  import { useMessage } from '@/hooks/web/useMessage';
  import Icon from '@/components/Icon/Icon.vue';

  enum Action {
    lockUser,
    unLockUser,
  }

  export default defineComponent({
    name: 'AccountManagement',
    components: { Icon, BasicTable, PageWrapper, UserModel, TableAction, Tag, Space },
    setup() {
      const { hasPermission } = usePermission();
      let showSelect = hasPermission('UserSelect');
      const go = useGo();
      const [registerModal, { openModal }] = useModal();
      const searchInfo = reactive<Recordable>({});
      const [registerTable, { reload, getSelectRows }] = useTable({
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
          position: ['bottomCenter'],
        },
        showSelectionBar: true,
        rowSelection: {
          type: 'checkbox',
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

      function handleLogOut(record: Recordable) {
        logOutUser([record.username]).then(() => {
          reload();
        });
      }

      async function handleBatch(type: Action) {
        const selectRows = getSelectRows();
        const usernames = selectRows.map((item) => item.username) as string[];
        let api: Promise<any>;
        switch (type) {
          case Action.lockUser:
            api = lockUser(usernames);
            break;
          case Action.unLockUser:
            api = unlockUser(usernames);
            break;
          default:
            return;
        }
        api.then(() => {
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
        handleBatch,
        handleLogOut,
        handleSuccess,
        handleSelect,
        handleView,
        searchInfo,
        hasPermission,
        Action,
      };
    },
  });
</script>
