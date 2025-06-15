<template>
  <div>
    <BasicTable @register="registerTable">
      <template #toolbar>
        <a-button
          type="primary"
          v-auth="'SettingAdd'"
          preIcon="ant-design:plus-outlined"
          @click="handleCreate"
        >
          新增
        </a-button>
        <a-button
          type="primary"
          ghost
          v-auth="'SettingExport'"
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
              onClick: handleEdit.bind(null, record),
              tooltip: '修改',
              auth: 'SettingEdit',
            },
            {
              icon: 'ant-design:delete-outlined',
              color: 'error',
              popConfirm: {
                title: '是否确认删除',
                confirm: handleDelete.bind(null, record),
              },
              tooltip: '删除',
              auth: 'SettingDel',
            },
          ]"
        />
      </template>
    </BasicTable>
    <SettingModal @register="registerModal" @success="handleSuccess" />
  </div>
</template>
<script lang="ts">
  import { defineComponent, ref } from 'vue';
  import { BasicTable, TableAction, useTable } from '@/components/Table';
  import { useModal } from '@/components/Modal';
  import SettingModal from './SettingModal.vue';
  import { getSearchColumns, getTableColumns } from './setting.data';
  import { useMessage } from '@/hooks/web/useMessage';
  import { usePermission } from '@/hooks/web/usePermission';
  import { getDictLabelByCodeAndValue } from '@/store/modules/dict';
  import { downloadByData } from '@/utils/file/download';
  import { VersionModifyCommand } from '@/api/system/model/baseModel';
  import { SettingQuery } from '@/api/system/model/settingModel';
  import { delSetting, expSetting, settingPage } from '@/api/system/setting';

  export default defineComponent({
    name: 'Port',
    components: { BasicTable, SettingModal, TableAction },
    setup() {
      const { hasPermission } = usePermission();
      let showSelect = hasPermission('SettingSelect');
      const [registerModal, { openModal }] = useModal();
      const [registerTable, { reload, getSelectRowKeys, getForm }] = useTable({
        title: '配置信息列表',
        api: settingPage,
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
          width: 80,
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
          modalType: 1,
        });
      }

      function handleEdit(record: Recordable) {
        openModal(true, {
          record,
          modalType: 2,
        });
      }

      function handleDelete(record: Recordable) {
        const params = { ...record } as VersionModifyCommand;
        delSetting(params).then(() => {
          reload();
        });
      }

      function handleSuccess() {
        reload();
      }

      const exportLoding = ref<Boolean>(false);
      const { createMessage } = useMessage();

      function handleExport() {
        exportLoding.value = true;
        // 是否有选中，优先下载选中数据
        const selectRowKeys = getSelectRowKeys();
        let params: SettingQuery;
        if (selectRowKeys && selectRowKeys.length > 0) {
          params = { ids: selectRowKeys } as SettingQuery;
        } else {
          // 获取查询参数
          const { getFieldsValue } = getForm();
          params = getFieldsValue() as SettingQuery;
        }
        expSetting(params)
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
        handleEdit,
        handleDelete,
        handleSuccess,
        exportLoding,
        handleExport,
      };
    },
    methods: { getDictLabelByCodeAndValue },
  });
</script>
