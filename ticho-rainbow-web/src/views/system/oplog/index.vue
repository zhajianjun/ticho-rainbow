<template>
  <div>
    <BasicTable @register="registerTable">
      <template #toolbar>
        <a-button
          type="primary"
          ghost
          v-auth="'OpLogExport'"
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
              icon: 'clarity:info-standard-line',
              color: 'success',
              onClick: handleDetail.bind(null, record),
              tooltip: '查看详情',
              auth: 'OpLogDetail',
            },
          ]"
        />
      </template>
    </BasicTable>
    <LogDetailModal @register="registerModal" />
  </div>
</template>
<script lang="ts">
  import { defineComponent, ref } from 'vue';
  import { BasicTable, TableAction, useTable } from '@/components/Table';
  import { getSearchColumns, getTableColumns } from './opLog.data';
  import { expExcel, opLogPage } from '@/api/system/opLog';
  import { usePermission } from '@/hooks/web/usePermission';
  import LogDetailModal from './LogDetailModal.vue';
  import { useModal } from '@/components/Modal';
  import { downloadByData } from '@/utils/file/download';
  import { useMessage } from '@/hooks/web/useMessage';
  import { OpLogQuery } from '@/api/system/model/opLogModel';

  export default defineComponent({
    name: 'OpLog',
    components: { BasicTable, TableAction, LogDetailModal },
    setup() {
      const { hasPermission } = usePermission();
      let showSelect = hasPermission('OpLogSelect');
      const [registerTable, { reload, getSelectRowKeys, getForm }] = useTable({
        title: '操作日志列表',
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
          width: 40,
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

      function handleDetail(record: Recordable) {
        openModal(true, record, true);
      }

      function handleSuccess() {
        reload();
      }

      const [registerModal, { openModal }] = useModal();

      const exportLoding = ref<Boolean>(false);
      const { createMessage } = useMessage();

      function handleExport() {
        exportLoding.value = true;
        // 是否有选中，优先下载选中数据
        const selectRowKeys = getSelectRowKeys();
        let params: OpLogQuery;
        if (selectRowKeys && selectRowKeys.length > 0) {
          params = { ids: selectRowKeys } as OpLogQuery;
        } else {
          // 获取查询参数
          const { getFieldsValue } = getForm();
          params = getFieldsValue() as OpLogQuery;
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
        handleDetail,
        handleSuccess,
        registerModal,
        exportLoding,
        handleExport,
      };
    },
  });
</script>
