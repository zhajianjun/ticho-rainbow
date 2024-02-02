<template>
  <div class="h-full flex">
    <div class="w-1/2">
      <BasicTable @register="registerTable">
        <template #toolbar>
          <a-button type="primary" v-auth="'DictAdd'" @click="openDictAddModel"> 新增 </a-button>
          <a-button type="primary" @click="flushDicts"> 刷新缓存 </a-button>
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
                onClick: openDictEditModel.bind(null, record),
                tooltip: '修改',
                ifShow: hasPermission('DictEdit'),
              },
              {
                icon: 'ant-design:delete-outlined',
                color: 'error',
                popConfirm: {
                  title: '是否确认删除',
                  confirm: handleDictDel.bind(null, record),
                },
                tooltip: '删除',
                ifShow: hasPermission('DictDel'),
              },
            ]"
          />
        </template>
      </BasicTable>
      <DictModal @register="registerModal" @success="handleDictSuccess" />
    </div>
    <div class="w-1/2 p-4">
      <BasicTable @register="registerDictLabelTable">
        <template #toolbar>
          <a-button type="primary" v-auth="'DictAdd'" @click="openDictLabelAddModel"> 新增 </a-button>
        </template>
        <template #action="{ record }">
          <TableAction
            :actions="[
              {
                icon: 'clarity:note-edit-line',
                onClick: openDictLabeEditModel.bind(null, record),
                tooltip: '修改',
                ifShow: hasPermission('DictEdit'),
              },
              {
                icon: 'ant-design:delete-outlined',
                color: 'error',
                popConfirm: {
                  title: '是否确认删除',
                  confirm: handleDictLabelDel.bind(null, record),
                },
                tooltip: '删除',
                ifShow: hasPermission('DictDel'),
              },
            ]"
          />
        </template>
      </BasicTable>
      <DictLabelModal @register="registerDictLabelModal" @success="handleDictLabelSuccess" />
    </div>
  </div>
</template>
<script lang="ts">
  import { defineComponent, ref, unref } from 'vue';
  import { BasicTable, useTable, TableAction } from '@/components/Table';
  import { useModal } from '@/components/Modal';
  import DictModal from './DictModal.vue';
  import DictLabelModal from './DictLabelModal.vue';
  import {
    getTableColumns as getDictTableColumns,
    getSearchColumns as getDictSearchColumns,
  } from './dict.data';
  import { getTableColumns as getDictLabelTableColumns } from './dictLabel.data';
  import { dictPage, delDict } from '@/api/system/dict';
  import { getByCode, delDictLabel } from '@/api/system/dictLabel';
  import { usePermission } from '@/hooks/web/usePermission';
  import { Tag } from 'ant-design-vue';
  import { flushDicts } from '@/store/modules/dict';

  export default defineComponent({
    name: 'DictType',
    components: { Tag, BasicTable, DictModal, TableAction, DictLabelModal },
    setup() {
      const codeRef = ref<string | null>(null);
      const nameRef = ref<string>('');
      const { hasPermission } = usePermission();
      let showSelect = hasPermission('DictSelect');
      const [registerDictLabelModal, { openModal }] = useModal();
      const [registerDictLabelTable, { reload: reloadDictLabel }] = useTable({
        title: () => {
          const code = unref(nameRef);
          if (code) {
            return '标签列表-' + unref(nameRef) + '(' + unref(codeRef) + ')';
          }
          return '标签列表';
        },
        api: async () => {
          let code = unref(codeRef);
          if (!code) {
            return Promise.resolve();
          }
          return getByCode(code);
        },
        rowKey: 'id',
        columns: getDictLabelTableColumns(),
        useSearchForm: false,
        formConfig: {
          labelWidth: 120,
          showActionButtonGroup: showSelect,
          showSubmitButton: showSelect,
          showResetButton: showSelect,
          autoSubmitOnEnter: showSelect,
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
      const [registerModal, { openModal: openDictModal }] = useModal();
      const [registerTable, { reload: reloadDict }] = useTable({
        title: '字典列表',
        api: dictPage,
        afterFetch: (res) => {
          if (res && res.length > 0) {
            const firstRecord = res[0];
            codeRef.value = firstRecord.code;
            nameRef.value = firstRecord.name;
            reloadDictLabel();
          }
          return res;
        },
        rowKey: 'id',
        columns: getDictTableColumns(),
        useSearchForm: true,
        formConfig: {
          labelWidth: 120,
          schemas: getDictSearchColumns(),
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

      function openDictAddModel() {
        openDictModal(true, {
          isUpdate: false,
        });
      }

      function openDictEditModel(record: Recordable) {
        openDictModal(true, {
          record,
          isUpdate: true,
        });
      }

      function handleDictDel(record: Recordable) {
        delDict(record.id).then(() => {
          reloadDict();
        });
      }

      function handleDict(record: Recordable) {
        codeRef.value = record.code;
        nameRef.value = record.name;
        reloadDictLabel();
      }

      function openDictLabelAddModel() {
        if (!unref(codeRef)) {
          return;
        }
        const record = { code: unref(codeRef), name: unref(nameRef) } as Recordable;
        openModal(true, {
          record,
          isUpdate: false,
        });
      }

      function openDictLabeEditModel(record: Recordable) {
        openModal(true, {
          record,
          isUpdate: true,
        });
      }

      function handleDictLabelDel(record: Recordable) {
        delDictLabel(record.id).then(() => {
          reloadDictLabel();
        });
      }

      function handleDictSuccess() {
        reloadDict();
      }

      function handleDictLabelSuccess() {
        reloadDictLabel();
      }

      return {
        registerTable,
        registerModal,
        openDictAddModel,
        openDictEditModel,
        handleDictDel,
        handleDictSuccess,
        registerDictLabelTable,
        registerDictLabelModal,
        openDictLabelAddModel,
        openDictLabeEditModel,
        handleDictLabelDel,
        handleDictLabelSuccess,
        handleDict,
        hasPermission,
        flushDicts,
      };
    },
  });
</script>
