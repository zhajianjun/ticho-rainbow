<template>
  <div>
    <BasicTable @register="registerTable">
      <template #toolbar>
        <a-button
          type="primary"
          ghost
          v-auth="'TaskLogExport'"
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
              auth: 'TaskLogDetail',
            },
          ]"
        />
      </template>
    </BasicTable>
    <TaskLogDetailModal @register="registerModal" />
  </div>
</template>
<script lang="ts">
  import { defineComponent, ref, unref } from 'vue';
  import { BasicTable, TableAction, useTable } from '@/components/Table';
  import { getSearchColumns, getTableColumns, task } from './taskLog.data';
  import { expExcel, taskLogPage } from '@/api/system/taskLog';
  import { usePermission } from '@/hooks/web/usePermission';
  import TaskLogDetailModal from './TaskLogDetailModal.vue';
  import { useModal } from '@/components/Modal';
  import { useRouter } from 'vue-router';
  import { useTabs } from '@/hooks/web/useTabs';
  import { useMessage } from '@/hooks/web/useMessage';
  import { downloadByData } from '@/utils/file/download';
  import { TaskLogQuery } from '@/api/system/model/taskLogModel';

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
      const [registerTable, { reload, getSelectRowKeys, getForm }] = useTable({
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
        let params: TaskLogQuery;
        if (selectRowKeys && selectRowKeys.length > 0) {
          params = { ids: selectRowKeys } as TaskLogQuery;
        } else {
          // 获取查询参数
          const { getFieldsValue } = getForm();
          params = getFieldsValue() as TaskLogQuery;
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
