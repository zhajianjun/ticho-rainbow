<template>
  <div>
    <BasicTable @register="registerTable" @fetch-success="onFetchSuccess">
      <template #toolbar>
        <a-button type="primary" v-auth="'MenuAdd'" @click="handleCreate">新增</a-button>
        <a-button type="primary" v-if="showSelect" @click="expandAll">展开</a-button>
        <a-button type="primary" v-if="showSelect" @click="collapseAll">收缩</a-button>
      </template>
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'perms'">
          <Space>
            <Tag color="success" v-for="item in record.perms" :key="item">
              {{ item }}
            </Tag>
          </Space>
        </template>
        <template v-if="column.key === 'status'">
          <Switch
            :checked-children="getDictLabelByCodeAndValue('commonStatus', 1)"
            :un-checked-children="getDictLabelByCodeAndValue('commonStatus', 0)"
            :checked="record.status === 1"
            :loading="record.pendingStatus"
            @change="handleSwitchChange(record)"
          />
        </template>
      </template>
      <template #action="{ record }">
        <TableAction
          :actions="[
            {
              icon: 'ant-design:file-add-outlined',
              type: 'link',
              color: 'warning',
              ifShow: hasPermission('MenuCopy'),
              onClick: handleCreate.bind(null, record),
              tooltip: '新增',
            },
            {
              icon: 'clarity:note-edit-line',
              ifShow: hasPermission('MenuEdit'),
              onClick: handleEdit.bind(null, record),
              tooltip: '修改',
            },
            {
              icon: 'ant-design:delete-outlined',
              color: 'error',
              ifShow: hasPermission('MenuDel'),
              tooltip: '删除',
              popConfirm: {
                title: '是否确认删除',
                confirm: handleDelete.bind(null, record),
              },
            },
          ]"
        />
      </template>
    </BasicTable>
    <MenuDrawer @register="registerDrawer" :treeData="treeData" @success="handleSuccess" />
  </div>
</template>
<script lang="ts">
  import { defineComponent, ref } from 'vue';
  import { BasicTable, TableAction, useTable } from '@/components/Table';
  import { delMenu, disableMenu, enableMenu, getMenuList } from '@/api/system/menu';
  import { useDrawer } from '@/components/Drawer';
  import MenuDrawer from './MenuDrawer.vue';
  import { columns } from './menu.data';
  import { cloneDeep } from 'lodash-es';
  import { usePermission } from '@/hooks/web/usePermission';
  import { Space, Switch, Tag } from 'ant-design-vue';
  import { getDictLabelByCodeAndValue } from '@/store/modules/dict';
  import { useMessage } from '@/hooks/web/useMessage';
  import { VersionModifyCommand } from '@/api/system/model/baseModel';

  export default defineComponent({
    name: 'MenuManagement',
    components: { Tag, Space, BasicTable, MenuDrawer, TableAction, Switch },
    setup() {
      const { hasPermission } = usePermission();
      const treeData = ref([]);
      const [registerDrawer, { openDrawer }] = useDrawer();
      let showSelect = hasPermission('MenuSelect');
      const [registerTable, { reload, expandAll, collapseAll }] = useTable({
        title: '菜单列表',
        api: async () => {
          const res = await getMenuList();
          treeData.value = cloneDeep(res) as any;
          return {
            rows: res,
          };
        },
        rowKey: 'id',
        columns,
        useSearchForm: showSelect,
        formConfig: {
          labelWidth: 120,
          autoSubmitOnEnter: true,
          showActionButtonGroup: showSelect,
          showSubmitButton: showSelect,
          showResetButton: showSelect,
        },
        tableSetting: {
          redo: showSelect,
        },
        isTreeTable: true,
        immediate: showSelect,
        striped: true,
        showTableSetting: true,
        canColDrag: true,
        bordered: true,
        showIndexColumn: false,
        canResize: true,
        actionColumn: {
          width: 120,
          title: '操作',
          dataIndex: 'action',
          slots: { customRender: 'action' },
          fixed: undefined,
        },
        pagination: false,
        // rowSelection: {
        //   type: 'radio',
        //   onSelect: onSelect,
        //   onSelectAll: onSelectAll,
        // },
      });

      function handleCreate(record: Recordable) {
        openDrawer(true, {
          record,
          isUpdate: false,
        });
      }

      function handleEdit(record: Recordable) {
        openDrawer(true, {
          record,
          isUpdate: true,
        });
      }

      function handleDelete(record: Recordable) {
        const params = { ...record } as VersionModifyCommand;
        delMenu(params).then(() => {
          reload();
        });
      }

      function handleSuccess() {
        reload();
      }

      function onFetchSuccess() {
        // nextTick(expandAll);
      }

      function handleSwitchChange(record: Recordable) {
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
          oprate = checked ? disableMenu([param]) : enableMenu([param]);
        } catch (error) {
          record.pendingStatus = false;
          return;
        }
        oprate
          .then(() => {
            // 仅在请求成功后更新状态
            record.status = checked ? 0 : 1;
            createMessage.success(messagePrefix + `成功`);
            reload();
          })
          .finally(() => {
            record.pendingStatus = false;
          });
      }

      return {
        treeData,
        registerTable,
        registerDrawer,
        handleCreate,
        handleEdit,
        handleDelete,
        handleSuccess,
        onFetchSuccess,
        reload,
        hasPermission,
        showSelect,
        expandAll,
        collapseAll,
        handleSwitchChange,
      };
    },
    methods: { getDictLabelByCodeAndValue },
  });
</script>
