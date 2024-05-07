<template>
  <div>
    <BasicTable @register="registerTable">
      <template #action="{ record }">
        <TableAction
          :actions="[
            {
              icon: 'clarity:info-standard-line',
              color: 'success',
              onClick: handleDetail.bind(null, record),
              tooltip: '查看详情',
              ifShow: hasPermission('TaskLogDetail'),
            },
          ]"
        />
      </template>
    </BasicTable>
    <TaskLogDetailModal @register="registerModal" />
  </div>
</template>
<script lang="ts">
  import { defineComponent, unref } from 'vue';
  import { BasicTable, useTable, TableAction } from '@/components/Table';
  import { getTableColumns, getSearchColumns, task } from './taskLog.data';
  import { taskLogPage } from '@/api/system/taskLog';
  import { usePermission } from '@/hooks/web/usePermission';
  import TaskLogDetailModal from './TaskLogDetailModal.vue';
  import { useModal } from '@/components/Modal';
  import { useRouter } from 'vue-router';
  import { useTabs } from '@/hooks/web/useTabs';

  export default defineComponent({
    name: 'TaskLog',
    components: { TaskLogDetailModal, BasicTable, TableAction },
    setup() {
      const { hasPermission } = usePermission();
      let showSelect = hasPermission('TaskLogSelect');
      const router = useRouter();
      const { currentRoute } = router;
      const route = unref(currentRoute);
      const taskId = route.query?.taskId ?? null;
      const searchColumns = getSearchColumns();
      const { setTitle } = useTabs();
      if (taskId) {
        searchColumns.find((item) => {
          if (item.field === 'taskId') {
            item.defaultValue = taskId;
          }
        });
        const taskData = task.find((item) => item.id === taskId);
        setTitle(`任务日志:${taskData?.name}`);
      }
      const [registerTable, { reload }] = useTable({
        title: '任务日志信息列表',
        api: taskLogPage,
        rowKey: 'id',
        columns: getTableColumns(),
        useSearchForm: true,
        formConfig: {
          labelWidth: 120,
          schemas: searchColumns,
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
          width: 40,
          title: '操作',
          dataIndex: 'action',
          slots: { customRender: 'action' },
          fixed: undefined,
        },
        pagination: {
          position: ['bottomLeft'],
        },
      });

      function handleDetail(record: Recordable) {
        openModal(true, record, true);
      }

      function handleSuccess() {
        reload();
      }

      const [registerModal, { openModal }] = useModal();

      return {
        registerTable,
        handleDetail,
        handleSuccess,
        hasPermission,
        registerModal,
      };
    },
  });
</script>
