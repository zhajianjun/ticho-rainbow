<template>
  <div class="h-full flex">
    <div class="w-1/2">
      <BasicTable @register="registerTypeTable">
        <template #toolbar>
          <a-button type="primary" @click="handleTypeCreate"> 新增 </a-button>
        </template>
        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'code'">
            <Tag color="success" @click="handleDict(record)">
              {{ record.code }}
            </Tag>
          </template>
        </template>
        <template #action="{ record }">
          <TableAction
            :actions="[
              {
                icon: 'clarity:note-edit-line',
                onClick: handleTypeEdit.bind(null, record),
                tooltip: '修改',
                ifShow: hasPermission('DictTypeEdit'),
              },
              {
                icon: 'ant-design:delete-outlined',
                color: 'error',
                popConfirm: {
                  title: '是否确认删除',
                  confirm: handleTypeDelete.bind(null, record),
                },
                tooltip: '删除',
                ifShow: hasPermission('DictTypeDel'),
              },
            ]"
          />
        </template>
      </BasicTable>
      <DictTypeModal @register="registerTypeModal" @success="handleTypeSuccess" />
    </div>
    <div class="w-1/2 p-4">
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
      <DictModal @register="registerModal" @success="handleSuccess" />
    </div>
  </div>
</template>
<script lang="ts">
  import { defineComponent, ref, unref } from 'vue';
  import { BasicTable, useTable, TableAction } from '@/components/Table';
  import { useModal } from '@/components/Modal';
  import DictTypeModal from './DictTypeModal.vue';
  import DictModal from './DictModal.vue';
  import {
    getTableColumns as getTypeTableColumns,
    getSearchColumns as getTypeSearchColumns,
  } from './dictType.data';
  import { getTableColumns } from './dict.data';
  import { dictTypePage, delDictType } from '@/api/system/dictType';
  import { getByCode, delDict } from '@/api/system/dict';
  import { usePermission } from '@/hooks/web/usePermission';
  import { Tag } from 'ant-design-vue';

  export default defineComponent({
    name: 'DictType',
    components: { Tag, BasicTable, DictTypeModal, TableAction, DictModal },
    setup() {
      const { hasPermission } = usePermission();
      let showSelect = hasPermission('DictTypeSelect');
      const [registerTypeModal, { openModal: openTypeModal }] = useModal();
      const [registerTypeTable, { reload: reloadType }] = useTable({
        title: '字典列表',
        api: dictTypePage,
        rowKey: 'id',
        columns: getTypeTableColumns(),
        useSearchForm: true,
        formConfig: {
          labelWidth: 120,
          schemas: getTypeSearchColumns(),
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
      const codeRef = ref<string | null>(null);
      const nameRef = ref<string>('');
      const [registerModal, { openModal }] = useModal();
      const [registerTable, { reload }] = useTable({
        title: () => {
          const code = unref(nameRef);
          if (code) {
            return '字典列表-' + unref(nameRef) + '(' + unref(codeRef) + ')';
          }
          return '字典列表';
        },
        api: async () => {
          let code = unref(codeRef);
          if (!code) {
            return Promise.resolve();
          }
          return getByCode(code);
        },
        rowKey: 'id',
        columns: getTableColumns(),
        useSearchForm: false,
        formConfig: {
          labelWidth: 120,
        },
        tableSetting: {
          redo: showSelect,
        },
        immediate: false,
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
        pagination: false,
      });

      function handleTypeCreate() {
        openTypeModal(true, {
          isUpdate: false,
        });
      }

      function handleTypeEdit(record: Recordable) {
        openTypeModal(true, {
          record,
          isUpdate: true,
        });
      }

      function handleTypeDelete(record: Recordable) {
        delDictType(record.id).then(() => {
          reloadType();
        });
      }

      function handleTypeSuccess() {
        reloadType();
      }

      function handleDict(record: Recordable) {
        codeRef.value = record.code;
        nameRef.value = record.name;
        reload();
      }

      function handleCreate() {
        if (!unref(codeRef)) {
          return;
        }
        const record = { code: unref(codeRef), name: unref(nameRef) } as Recordable;
        openModal(true, {
          record,
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
        delDict(record.id).then(() => {
          reload();
        });
      }

      function handleSuccess() {
        reload();
      }

      return {
        registerTypeTable,
        registerTable,
        registerTypeModal,
        registerModal,
        handleTypeCreate,
        handleTypeEdit,
        handleDict,
        handleCreate,
        handleEdit,
        handleTypeDelete,
        handleDelete,
        handleTypeSuccess,
        handleSuccess,
        hasPermission,
      };
    },
  });
</script>
