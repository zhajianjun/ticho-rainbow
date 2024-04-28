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
              onClick: openTaskModal.bind(null, record),
              tooltip: '修改',
              auth: 'TaskEdit',
            },
            {
              icon: 'ant-design:delete-outlined',
              color: 'error',
              popConfirm: {
                title: '是否确认删除',
                confirm: handleDelete.bind(null, record),
              },
              auth: 'TaskDel',
              tooltip: '删除',
            },
          ]"
          :dropDownActions="[
            {
              label: '执行一次',
              onClick: openRunOnceTaskModal.bind(null, record),
              auth: 'TaskRunOnce',
            },
            {
              label: '启动',
              popConfirm: {
                title: '是否启动？',
                confirm: handleResume.bind(null, record),
              },
              disabled: record.status == 1,
              auth: 'TaskResume',
            },
            {
              label: '暂停',
              popConfirm: {
                title: '是否暂停？',
                confirm: handlePause.bind(null, record),
              },
              disabled: record.status !== 1,
              auth: 'TaskPause',
            },
          ]"
        />
      </template>
    </BasicTable>
    <TaskModal @register="registerModal" @success="handleSuccess" />
    <TaskRunOnce @register="registerRunOnceModal" />
  </div>
</template>
<script lang="ts">
  import { defineComponent, ref } from 'vue';
  import { BasicTable, useTable, TableAction } from '@/components/Table';
  import { useModal } from '@/components/Modal';
  import TaskModal from './TaskModal.vue';
  import TaskRunOnce from './TaskRunOnce.vue';
  import { getTableColumns, getSearchColumns } from './task.data';
  import { taskPage, delTask, pauseTask, resumeTask } from '@/api/system/task';
  import { usePermission } from '@/hooks/web/usePermission';

  export default defineComponent({
    name: 'Task',
    components: { BasicTable, TaskModal, TableAction, TaskRunOnce },
    setup() {
      const cronValue = ref(null);
      const { hasPermission } = usePermission();
      let showSelect = hasPermission('TaskSelect');
      const [registerModal, { openModal }] = useModal();
      const [registerRunOnceModal, { openModal: openRunOnceModal }] = useModal();
      const [registerTable, { reload }] = useTable({
        title: '计划任务列表',
        api: taskPage,
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
          width: 100,
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

      function handleCreate() {
        openModal(true, {
          isUpdate: false,
        });
      }

      function openTaskModal(record: Recordable) {
        openModal(true, {
          record,
          isUpdate: true,
        });
      }

      function handleDelete(record: Recordable) {
        delTask(record.id).then(() => {
          reload();
        });
      }

      function handlePause(record: Recordable) {
        pauseTask(record.id).then(() => {
          reload();
        });
      }

      function handleResume(record: Recordable) {
        resumeTask(record.id).then(() => {
          reload();
        });
      }

      function handleSuccess() {
        reload();
      }

      function openRunOnceTaskModal(record: Recordable) {
        openRunOnceModal(true, record, true);
      }

      return {
        registerTable,
        registerModal,
        handleCreate,
        openTaskModal,
        handleDelete,
        handlePause,
        handleResume,
        handleSuccess,
        hasPermission,
        cronValue,
        registerRunOnceModal,
        openRunOnceTaskModal,
      };
    },
  });
</script>