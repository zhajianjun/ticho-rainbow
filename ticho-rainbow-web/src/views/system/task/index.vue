<template>
  <div>
    <BasicTable @register="registerTable">
      <template #toolbar>
        <a-button
          type="primary"
          v-auth="'TaskAdd'"
          preIcon="ant-design:plus-outlined"
          @click="handleCreate"
        >
          新增
        </a-button>
        <a-button
          type="primary"
          ghost
          v-auth="'TaskExport'"
          preIcon="ant-design:download-outlined"
          :loading="exportLoding"
          @click="handleExport"
          style="color: #2a7dc9"
        >
          导出
        </a-button>
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
              icon: 'ant-design:play-circle-twotone',
              tooltip: '执行一次',
              onClick: openRunOnceTaskModal.bind(null, record),
              auth: 'TaskRunOnce',
            },
            {
              icon: 'ant-design:login-outlined',
              tooltip: '查看日志',
              onClick: goTaskLog.bind(null, record),
              auth: 'GoTaskLog',
            },
          ]"
          :dropDownActions="[
            {
              label: '启动',
              onClick: handleResume.bind(null, record),
              disabled: record.status == 1,
              auth: 'TaskResume',
            },
            {
              label: '暂停',
              onClick: handlePause.bind(null, record),
              disabled: record.status !== 1,
              auth: 'TaskPause',
            },
            {
              label: '删除',
              popConfirm: {
                title: '是否确认删除?',
                confirm: handleDelete.bind(null, record),
              },
              auth: 'TaskDel',
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
  import { taskPage, delTask, pauseTask, resumeTask, expExcel } from '@/api/system/task';
  import { usePermission } from '@/hooks/web/usePermission';
  import { useGo } from '@/hooks/web/usePage';
  import { useMessage } from '@/hooks/web/useMessage';
  import { downloadByData } from '@/utils/file/download';
  import { TaskQuery } from '@/api/system/model/taskModel';

  export default defineComponent({
    name: 'Task',
    components: { BasicTable, TaskModal, TableAction, TaskRunOnce },
    setup() {
      const cronValue = ref(null);
      const { hasPermission } = usePermission();
      let showSelect = hasPermission('TaskSelect');
      const [registerModal, { openModal }] = useModal();
      const [registerRunOnceModal, { openModal: openRunOnceModal }] = useModal();
      const [registerTable, { reload, getSelectRowKeys, getForm }] = useTable({
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
          width: 100,
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

      const go = useGo();

      function goTaskLog(record: Recordable) {
        go(`/system/log/tasklog?taskId=${record.id}`);
      }

      const exportLoding = ref<Boolean>(false);
      const { createMessage } = useMessage();

      function handleExport() {
        exportLoding.value = true;
        // 是否有选中，优先下载选中数据
        const selectRowKeys = getSelectRowKeys();
        let params: TaskQuery;
        if (selectRowKeys && selectRowKeys.length > 0) {
          params = { ids: selectRowKeys } as TaskQuery;
        } else {
          // 获取查询参数
          const { getFieldsValue } = getForm();
          params = getFieldsValue() as TaskQuery;
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
        openTaskModal,
        handleDelete,
        handlePause,
        handleResume,
        handleSuccess,
        hasPermission,
        cronValue,
        registerRunOnceModal,
        openRunOnceTaskModal,
        goTaskLog,
        exportLoding,
        handleExport,
      };
    },
  });
</script>
