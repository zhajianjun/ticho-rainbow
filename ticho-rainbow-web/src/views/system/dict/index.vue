<template>
  <div class="h-full flex">
    <div class="w-1/2">
      <BasicTable @register="registerTable">
        <template #toolbar>
          <a-button
            type="primary"
            v-auth="'DictAdd'"
            preIcon="ant-design:plus-outlined"
            @click="openDictAddModel"
          >
            新增
          </a-button>
          <a-button
            type="primary"
            v-auth="'DictFlush'"
            preIcon="ant-design:sync-outlined"
            :loading="flushLoading"
            @click="handleFlush"
          >
            刷新
          </a-button>
          <a-button
            type="primary"
            ghost
            v-auth="'DictExport'"
            preIcon="ant-design:download-outlined"
            :loading="exportLoding"
            @click="handleExport"
            style="color: #2a7dc9"
          >
            导出
          </a-button>
        </template>
        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'code'">
            <Tag color="success" @click="handleDict(record)">
              {{ record.code }}
            </Tag>
          </template>
          <template v-if="column.key === 'status'">
            <Switch
              :checked-children="getDictLabelByCodeAndValue('commonStatus', 1)"
              :un-checked-children="getDictLabelByCodeAndValue('commonStatus', 0)"
              :checked="record.status === 1"
              :loading="record.pendingStatus"
              @change="handleSwitchDictChange(record)"
            />
          </template>
        </template>
        <template #action="{ record }">
          <TableAction
            :actions="[
              {
                icon: 'clarity:note-edit-line',
                onClick: openDictEditModel.bind(null, record),
                tooltip: '修改',
                auth: 'DictEdit',
              },
              {
                icon: 'ant-design:delete-outlined',
                color: 'error',
                popConfirm: {
                  title: '是否确认删除',
                  confirm: handleDictDel.bind(null, record),
                },
                tooltip: '删除',
                auth: 'DictDel',
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
          <a-button
            type="primary"
            v-auth="'DictLabelAdd'"
            preIcon="ant-design:plus-outlined"
            @click="openDictLabelAddModel"
          >
            新增
          </a-button>
        </template>
        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'status'">
            <Switch
              :checked-children="getDictLabelByCodeAndValue('commonStatus', 1)"
              :un-checked-children="getDictLabelByCodeAndValue('commonStatus', 0)"
              :checked="record.status === 1"
              :loading="record.pendingStatus"
              @change="handleSwitchDictLabelChange(record)"
            />
          </template>
        </template>
        <template #action="{ record }">
          <TableAction
            :actions="[
              {
                icon: 'clarity:note-edit-line',
                onClick: openDictLabeEditModel.bind(null, record),
                tooltip: '修改',
                auth: 'DictLabelEdit',
              },
              {
                icon: 'ant-design:delete-outlined',
                color: 'error',
                popConfirm: {
                  title: '是否确认删除',
                  confirm: handleDictLabelDel.bind(null, record),
                },
                tooltip: '删除',
                auth: 'DictLabelDel',
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
  import { BasicTable, TableAction, useTable } from '@/components/Table';
  import { useModal } from '@/components/Modal';
  import DictModal from './DictModal.vue';
  import DictLabelModal from './DictLabelModal.vue';
  import {
    getSearchColumns as getDictSearchColumns,
    getTableColumns as getDictTableColumns,
  } from './dict.data';
  import { getTableColumns as getDictLabelTableColumns } from './dictLabel.data';
  import { delDict, dictPage, expExcel, disableDict, enableDict } from '@/api/system/dict';
  import {
    delDictLabel,
    disableDictLabel,
    enableDictLabel,
    findDictLabel,
  } from '@/api/system/dictLabel';
  import { usePermission } from '@/hooks/web/usePermission';
  import { Switch, Tag } from 'ant-design-vue';
  import { flushDicts, getDictLabelByCodeAndValue } from '@/store/modules/dict';
  import { downloadByData } from '@/utils/file/download';
  import { DictQuery } from '@/api/system/model/dictModel';
  import { useMessage } from '@/hooks/web/useMessage';
  import { VersionModifyCommand } from '@/api/system/model/baseModel';

  export default defineComponent({
    name: 'DictType',
    components: { Switch, Tag, BasicTable, DictModal, TableAction, DictLabelModal },
    setup() {
      const codeRef = ref<string | null>(null);
      const isSysRef = ref<boolean>(false);
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
          return findDictLabel(code);
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
      const [registerTable, { reload: reloadDict, getSelectRowKeys, getForm }] = useTable({
        title: '字典列表',
        api: dictPage,
        afterFetch: (res) => {
          if (res && res.length > 0 && !unref(codeRef)) {
            const record = res[0];
            codeRef.value = record.code;
            nameRef.value = record.name;
            isSysRef.value = record.isSys === 1;
          }
          reloadDictLabel();
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
          width: 100,
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
        const params = { ...record } as VersionModifyCommand;
        delDict(params).then(() => {
          reloadDict();
        });
      }

      function handleDict(record: Recordable) {
        codeRef.value = record.code;
        nameRef.value = record.name;
        isSysRef.value = record.isSys === 1;
        reloadDictLabel();
      }

      function openDictLabelAddModel() {
        if (!unref(codeRef)) {
          return;
        }
        const record = { code: unref(codeRef), name: unref(nameRef) } as Recordable;
        openModal(true, {
          record,
          isUpdate: true,
          isSys: false,
        });
      }

      function openDictLabeEditModel(record: Recordable) {
        openModal(true, {
          record,
          isUpdate: true,
          isSys: unref(isSysRef),
        });
      }

      function handleDictLabelDel(record: Recordable) {
        const params = { ...record } as VersionModifyCommand;
        delDictLabel(params).then(() => {
          reloadDictLabel();
        });
      }

      function handleDictSuccess() {
        reloadDict();
      }

      function handleDictLabelSuccess() {
        reloadDictLabel();
      }

      const exportLoding = ref<Boolean>(false);
      const { createMessage } = useMessage();

      function handleExport() {
        exportLoding.value = true;
        // 是否有选中，优先下载选中数据
        const selectRowKeys = getSelectRowKeys();
        let params: DictQuery;
        if (selectRowKeys && selectRowKeys.length > 0) {
          params = { ids: selectRowKeys } as DictQuery;
        } else {
          // 获取查询参数
          const { getFieldsValue } = getForm();
          params = getFieldsValue() as DictQuery;
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

      const flushLoading = ref<Boolean>(false);

      function handleFlush() {
        flushLoading.value = true;
        flushDicts().finally(() => {
          flushLoading.value = false;
        });
      }

      function handleSwitchDictChange(record: Recordable) {
        if (!record || typeof record.status !== 'number') {
          console.warn('Invalid record provided to handleSwitchChange');
          return;
        }
        const { createMessage } = useMessage();
        const checked = record.status === 1;
        let oprate: Promise<any>;
        const messagePrefix = getDictLabelByCodeAndValue('commonStatus', checked ? 0 : 1);
        record.pendingStatus = true;
        try {
          const param = {
            id: record.id,
            version: record.version,
          } as VersionModifyCommand;
          oprate = checked ? disableDict([param]) : enableDict([param]);
        } catch (error) {
          record.pendingStatus = false;
          return;
        }
        oprate
          .then(() => {
            // 仅在请求成功后更新状态
            record.status = checked ? 0 : 1;
            createMessage.success(messagePrefix + `成功`);
          })
          .finally(() => {
            record.pendingStatus = false;
            reloadDict();
          });
      }

      function handleSwitchDictLabelChange(record: Recordable) {
        if (!record || typeof record.status !== 'number') {
          console.warn('Invalid record provided to handleSwitchChange');
          return;
        }
        const { createMessage } = useMessage();
        const checked = record.status === 1;
        let oprate: Promise<any>;
        const messagePrefix = getDictLabelByCodeAndValue('commonStatus', checked ? 0 : 1);
        record.pendingStatus = true;
        try {
          const param = {
            id: record.id,
            version: record.version,
          } as VersionModifyCommand;
          oprate = checked ? disableDictLabel([param]) : enableDictLabel([param]);
        } catch (error) {
          record.pendingStatus = false;
          return;
        }
        oprate
          .then(() => {
            // 仅在请求成功后更新状态
            record.status = checked ? 0 : 1;
            createMessage.success(messagePrefix + `成功`);
          })
          .finally(() => {
            record.pendingStatus = false;
            reloadDictLabel();
          });
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
        flushLoading,
        handleFlush,
        handleExport,
        exportLoding,
        handleSwitchDictChange,
        handleSwitchDictLabelChange,
      };
    },
    methods: { getDictLabelByCodeAndValue },
  });
</script>
