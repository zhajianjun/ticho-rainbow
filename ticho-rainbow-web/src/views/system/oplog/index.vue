<template>
  <div>
    <BasicTable @register="registerTable">
      <template #action="{ record }">
        <TableAction
          :actions="[
            {
              icon: 'ant-design:delete-outlined',
              color: 'error',
              onClick: handleDetail.bind(null, record),
              tooltip: '查看详情',
              ifShow: hasPermission('OpLogDetail'),
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
  import { getTableColumns, getSearchColumns } from './opLog.data';
  import { opLogPage } from '@/api/system/opLog';
  import { usePermission } from '@/hooks/web/usePermission';

  export default defineComponent({
    name: 'OpLog',
    components: { BasicTable, TableAction },
    setup() {
      const { hasPermission } = usePermission();
      let showSelect = hasPermission('OpLogSelect');
      const [registerTable, { reload }] = useTable({
        title: '日志信息列表',
        api: opLogPage,
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
          pageSize: 15,
          simple: false,
          position: ['bottomLeft'],
        },
      });

      function handleDetail(record: Recordable) {
        console.log(record);
      }

      function handleSuccess() {
        reload();
      }

      return {
        registerTable,
        handleDetail,
        handleSuccess,
        hasPermission,
      };
    },
  });
</script>
