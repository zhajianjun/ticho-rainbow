<template>
  <div class="h-full flex">
    <div class="w-1/2">
      <BasicTable @register="registerTable">
        <template #toolbar>
          <a-button type="primary" @click="handleCreate"> 新增 </a-button>
        </template>
        <template #action="{ record }">
          <TableAction
            :actions="[
              {
                icon: 'clarity:note-edit-line',
                onClick: handleEdit.bind(null, record),
                tooltip: '修改',
                ifShow: hasPermission('DictTypeEdit'),
              },
              {
                icon: 'ant-design:delete-outlined',
                color: 'error',
                popConfirm: {
                  title: '是否确认删除',
                  confirm: handleDelete.bind(null, record),
                },
                tooltip: '删除',
                ifShow: hasPermission('DictTypeDel'),
              },
            ]"
          />
        </template>
      </BasicTable>
      <DictTypeModal @register="registerModal" @success="handleSuccess" />
    </div>
    <div class="w-1/2">
      <!-- 右侧部分的内容 -->
    </div>
  </div>
</template>
<script lang="ts">
  import { defineComponent } from 'vue';
  import { BasicTable, useTable, TableAction } from '@/components/Table';
  import { useModal } from '@/components/Modal';
  import DictTypeModal from './DictTypeModal.vue';
  import { getTableColumns, getSearchColumns } from './dictType.data';
  import { dictTypePage, delDictType } from '@/api/system/dictType';
  import { usePermission } from '@/hooks/web/usePermission';

  export default defineComponent({
    name: 'DictType',
    components: { BasicTable, DictTypeModal, TableAction },
    setup() {
      const { hasPermission } = usePermission();
      let showSelect = hasPermission('DictTypeSelect');
      const [registerModal, { openModal }] = useModal();
      const [registerTable, { reload }] = useTable({
        title: '数据字典列表',
        api: dictTypePage,
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

      function handleDelete(record: Recordable) {
        delDictType(record.id).then(() => {
          reload();
        });
      }

      function handleSuccess() {
        reload();
      }

      return {
        registerTable,
        registerModal,
        handleCreate,
        handleEdit,
        handleDelete,
        handleSuccess,
        hasPermission,
      };
    },
  });
</script>
