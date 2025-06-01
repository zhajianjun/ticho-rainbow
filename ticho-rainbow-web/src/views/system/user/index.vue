<template>
  <PageWrapper dense contentFullHeight fixedHeight contentClass="flex">
    <BasicTable @register="registerTable" :searchInfo="searchInfo">
      <template #toolbar>
        <a-button
          type="primary"
          v-auth="'UserAdd'"
          preIcon="ant-design:plus-outlined"
          @click="handleCreate"
        >
          新增
        </a-button>
        <a-button
          type="primary"
          danger
          ghost
          v-auth="'UserLock'"
          preIcon="ant-design:lock-outlined"
          :loading="lockLoading"
          @click="handleBatch(Action.lock)"
        >
          锁定
        </a-button>
        <a-button
          type="dashed"
          v-auth="'UserUnLock'"
          preIcon="ant-design:unlock-outlined"
          :loading="unLockLoading"
          @click="handleBatch(Action.unLock)"
        >
          解锁
        </a-button>
        <a-button
          type="primary"
          danger
          v-auth="'UserLogOut'"
          preIcon="ant-design:logout-outlined"
          :loading="logOutLoading"
          @click="handleBatch(Action.logOut)"
        >
          注销
        </a-button>
        <a-button
          type="primary"
          ghost
          v-auth="'UserImport'"
          preIcon="ant-design:upload-outlined"
          @click="handleImp"
          style="color: #2a7dc9"
        >
          导入
        </a-button>
        <a-button
          type="primary"
          ghost
          v-auth="'UserExport'"
          preIcon="ant-design:download-outlined"
          :loading="exportLoding"
          @click="handleExport"
          style="color: #2a7dc9"
        >
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
            {
              icon: 'clarity:note-edit-line',
              auth: 'UserEdit',
              onClick: handleEdit.bind(null, record),
              tooltip: '修改',
            },
            {
              icon: 'ant-design:eye-outlined',
              auth: 'UserEditPassword',
              onClick: handleEditPassword.bind(null, record),
              tooltip: '修改密码',
            },
            {
              icon: 'ant-design:security-scan-outlined',
              auth: 'UserResetPwd',
              popConfirm: {
                title: '是否重置密码?',
                confirm: resetPassword.bind(null, record),
              },
              tooltip: '重置密码',
              disabled: record.status !== 1 || record.username === 'admin',
            },
            {
              icon: 'ant-design:logout-outlined',
              auth: 'UserLogOut',
              color: 'error',
              popConfirm: {
                title: '是否确认注销?',
                confirm: handleLogOut.bind(null, record),
              },
              tooltip: '注销',
              disabled: record.status !== 1 || record.username === 'admin',
            },
            {
              icon: 'ant-design:delete-outlined',
              auth: 'UserDel',
              color: 'error',
              popConfirm: {
                title: '是否确认删除?',
                confirm: handleRemove.bind(null, record),
              },
              tooltip: '删除',
              disabled: record.status !== 4 || record.username === 'admin',
            },
          ]"
        />
      </template>
    </BasicTable>
    <UserModel @register="registerModal" @success="handleSuccess" />
    <UserPasswordModal @register="registerPasswordModal" @success="handleSuccess" />
    <ImpModal
      title="用户导入"
      :download-model-api="impTemplate"
      :upload-api="impExcel"
      @register="registerImpModal"
      @success="handleImpSuccess"
    />
  </PageWrapper>
</template>
<script lang="ts">
  import { defineComponent, reactive, ref } from 'vue';
  import { BasicTable, useTable, TableAction } from '@/components/Table';
  import {
    userPage,
    resetUserPassword,
    lockUser,
    unlockUser,
    logOutUser,
    impTemplate,
    impExcel,
    expExcel,
    removeUser,
  } from '@/api/system/user';
  import { PageWrapper } from '@/components/Page';
  import { useModal } from '@/components/Modal';
  import UserModel from './UserModal.vue';
  import UserPasswordModal from './UserPasswordModal.vue';
  import ImpModal from '@/views/component/imp/ImpModal.vue';
  import { columns, searchFormSchema } from './user.data';
  import { usePermission } from '@/hooks/web/usePermission';
  import { Tag, Space } from 'ant-design-vue';
  import { useMessage } from '@/hooks/web/useMessage';
  import { downloadByData } from '@/utils/file/download';
  import { UserQuery, UseVersionModifyCommand } from '@/api/system/model/userModel';

  enum Action {
    lock,
    unLock,
    logOut,
  }

  export default defineComponent({
    name: 'UserManagement',
    components: {
      BasicTable,
      PageWrapper,
      UserModel,
      UserPasswordModal,
      ImpModal,
      TableAction,
      Tag,
      Space,
    },
    setup() {
      const { hasPermission } = usePermission();
      let showSelect = hasPermission('UserSelect');
      const [registerModal, { openModal }] = useModal();
      const [registerPasswordModal, { openModal: openPasswordModal }] = useModal();
      const [registerImpModal, { openModal: openImpModal }] = useModal();
      const searchInfo = reactive<Recordable>({});
      const [registerTable, { reload, getSelectRows, getSelectRowKeys, getForm }] = useTable({
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
        openModal(true, {
          record,
          isUpdate: true,
        });
      }

      function handleEditPassword(record: Recordable) {
        openPasswordModal(true, {
          record,
          isUpdate: true,
        });
      }

      function resetPassword(record: Recordable) {
        resetUserPassword([{ id: record.id, version: record.version }]).then(() => {
          createMessage.success(`重置${record.nickname}密码成功`);
        });
      }

      function handleLogOut(record: Recordable) {
        logOutUser([{ id: record.id, version: record.version }]).then(() => {
          reload();
        });
      }

      function handleRemove(record: Recordable) {
        removeUser({ id: record.id, version: record.version }).then(() => {
          reload();
        });
      }

      const lockLoading = ref<Boolean>(false);
      const unLockLoading = ref<Boolean>(false);
      const logOutLoading = ref<Boolean>(false);
      const exportLoding = ref<Boolean>(false);

      async function handleBatch(type: Action) {
        const selectRows = getSelectRows();
        const commands = selectRows.map((item) => {
          return {
            id: item.id,
            version: item.version,
          };
        }) as UseVersionModifyCommand[];
        if (!commands || commands.length === 0) {
          createMessage.warn(`至少选择一条数据`);
          return;
        }
        let api: Promise<any>;
        let loading: any;
        switch (type) {
          case Action.lock:
            loading = lockLoading;
            api = lockUser(commands);
            break;
          case Action.unLock:
            loading = unLockLoading;
            api = unlockUser(commands);
            break;
          case Action.logOut:
            loading = unLockLoading;
            api = logOutUser(commands);
            break;
          default:
            return;
        }
        loading.value = true;
        api
          .then(() => {
            reload();
          })
          .finally(() => {
            loading.value = false;
          });
      }

      function handleSuccess() {
        reload();
      }

      function handleExport() {
        exportLoding.value = true;
        // 是否有选中，优先下载选中数据
        const selectRowKeys = getSelectRowKeys();
        let params: UserQuery;
        if (selectRowKeys && selectRowKeys.length > 0) {
          params = { ids: selectRowKeys } as UserQuery;
        } else {
          // 获取查询参数
          const { getFieldsValue } = getForm();
          params = getFieldsValue() as UserQuery;
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

      function handleImp() {
        openImpModal(true);
      }

      function handleImpSuccess() {
        reload();
      }

      return {
        registerTable,
        registerModal,
        registerPasswordModal,
        handleCreate,
        handleEdit,
        handleEditPassword,
        resetPassword,
        handleBatch,
        handleLogOut,
        handleRemove,
        handleSuccess,
        searchInfo,
        Action,
        handleExport,
        exportLoding,
        lockLoading,
        unLockLoading,
        logOutLoading,
        registerImpModal,
        handleImp,
        impTemplate,
        impExcel,
        handleImpSuccess,
      };
    },
  });
</script>
