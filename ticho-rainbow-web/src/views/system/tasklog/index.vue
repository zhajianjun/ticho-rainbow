<template>
  <div>
    <BasicTable @register="registerTable">
      <template #action="{ record }">
        <TableAction
          :actions="[
            {
              icon: 'clarity:note-edit-line',
              onClick: handleEdit.bind(null, record),
              tooltip: '修改',
              auth: 'TaskLogEdit',
            },
          ]"
        />
      </template>
    </BasicTable>
  </div>
</template>
<script lang="ts">
  import { defineComponent } from 'vue';
  import { BasicTable, useTable, TableAction } from '@/components/Table';
  import { getTableColumns, getSearchColumns } from './taskLog.data';
  import { taskLogPage } from '@/api/system/taskLog';
  import { usePermission } from '@/hooks/web/usePermission';

  export default defineComponent({
    name: 'TaskLog',
    components: { BasicTable, TableAction },
    setup() {
      const { hasPermission } = usePermission();
      let showSelect = hasPermission('TaskLogSelect');
      const [registerTable, { reload }] = useTable({
        title: '任务日志信息列表',
        api: taskLogPage,
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
          position: ['bottomLeft'],
        },
      });

      function handleCreate() {}

      function handleEdit(record: Recordable) {
        console.log(record);
      }

      function handleSuccess() {
        reload();
      }

      return {
        registerTable,
        handleCreate,
        handleEdit,
        handleSuccess,
        hasPermission,
      };
    },
  });
</script>
