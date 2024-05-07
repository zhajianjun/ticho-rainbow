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
              ifShow: hasPermission('OpLogDetail'),
            },
          ]"
        />
      </template>
    </BasicTable>
    <LogDetailModal @register="registerModal" />
  </div>
</template>
<script lang="ts">
  import { defineComponent } from 'vue';
  import { BasicTable, useTable, TableAction } from '@/components/Table';
  import { getTableColumns, getSearchColumns } from './opLog.data';
  import { opLogPage } from '@/api/system/opLog';
  import { usePermission } from '@/hooks/web/usePermission';
  import LogDetailModal from './LogDetailModal.vue';
  import { useModal } from '@/components/Modal';

  export default defineComponent({
    name: 'OpLog',
    components: { BasicTable, TableAction, LogDetailModal },
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
          width: 40,
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
