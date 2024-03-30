<template>
  <div>
    <BasicTable @register="registerTable">
      <template #toolbar>
        <a-button type="primary" v-auth="'PortAdd'" @click="handleCreate"> 新增 </a-button>
      </template>
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'status'">
          <Switch
            checked-children="开启"
            un-checked-children="禁用"
            :checked="record.status === 1"
            :loading="record.pendingStatus"
            @change="handleStatusSwitchChange(record)"
          />
        </template>
        <template v-if="column.key === 'forever'">
          <Switch
            checked-children="是"
            un-checked-children="否"
            :checked="record.forever === 1"
            :loading="record.pendingForever"
            @change="handleForeverSwitchChange(record)"
          />
        </template>
      </template>
      <template #action="{ record }">
        <TableAction
          :actions="[
            {
              icon: 'clarity:note-edit-line',
              onClick: handleEdit.bind(null, record),
              tooltip: '修改',
              ifShow: hasPermission('PortEdit'),
            },
            {
              icon: 'ant-design:copy-outlined',
              onClick: handleCopy.bind(null, record),
              tooltip: '复制',
              ifShow: hasPermission('PortCopy'),
            },
            {
              icon: 'ant-design:delete-outlined',
              color: 'error',
              popConfirm: {
                title: '是否确认删除',
                confirm: handleDelete.bind(null, record),
              },
              tooltip: '删除',
              ifShow: hasPermission('PortDel'),
            },
          ]"
        />
      </template>
    </BasicTable>
    <PortModal @register="registerModal" @success="handleSuccess" />
  </div>
</template>
<script lang="ts">
  import { defineComponent } from 'vue';
  import { BasicTable, useTable, TableAction } from '@/components/Table';
  import { useModal } from '@/components/Modal';
  import PortModal from './PortModal.vue';
  import { getTableColumns, getSearchColumns } from './port.data';
  import { portPage, delPort, modifyPort } from '@/api/intranet/port';
  import { Switch } from 'ant-design-vue';
  import { useMessage } from '@/hooks/web/useMessage';
  import { PortDTO } from '@/api/intranet/model/portModel';
  import { usePermission } from '@/hooks/web/usePermission';

  export default defineComponent({
    name: 'Port',
    components: { Switch, BasicTable, PortModal, TableAction },
    setup() {
      const { hasPermission } = usePermission();
      let showSelect = hasPermission('PortSelect');
      const [registerModal, { openModal }] = useModal();
      const [registerTable, { reload }] = useTable({
        title: '端口信息列表',
        api: portPage,
        rowKey: 'id',
        columns: getTableColumns(),
        useSearchForm: true,
        formConfig: {
          labelWidth: 120,
          schemas: getSearchColumns(),
          showActionButtonGroup: true,
          showSubmitButton: true,
          showResetButton: true,
          autoSubmitOnEnter: true,
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
          position: ['bottomLeft'],
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

      function handleCopy(record: Recordable) {
        openModal(true, {
          record,
          modalType: 3,
        });
      }

      function handleDelete(record: Recordable) {
        delPort(record.id).then(() => {
          reload();
        });
      }

      function handleSuccess() {
        reload();
      }

      function handleStatusSwitchChange(record: Recordable) {
        record.pendingStatus = true;
        const { createMessage } = useMessage();
        let checked = record.status === 1;
        if (checked) {
          record.status = 0;
        } else {
          record.status = 1;
        }
        const params: PortDTO = { ...record } as PortDTO;
        const messagePrefix = !checked ? '启动' : '关闭';
        modifyPort(params)
          .then(() => {
            createMessage.success(messagePrefix + `成功`);
          })
          .catch(() => {
            createMessage.error(messagePrefix + `失败`);
          })
          .finally(() => {
            record.pendingStatus = false;
            reload();
          });
      }

      function handleForeverSwitchChange(record: Recordable) {
        record.pendingForever = true;
        const { createMessage } = useMessage();
        let checked = record.forever === 1;
        if (checked) {
          record.forever = 0;
        } else {
          record.forever = 1;
        }
        const params: PortDTO = { ...record } as PortDTO;
        const messagePrefix = !checked ? '启动' : '关闭';
        modifyPort(params)
          .then(() => {
            createMessage.success(messagePrefix + `成功`);
          })
          .catch(() => {
            createMessage.error(messagePrefix + `失败`);
          })
          .finally(() => {
            record.pendingForever = false;
            reload();
          });
      }

      return {
        registerTable,
        registerModal,
        handleCreate,
        handleCopy,
        handleEdit,
        handleDelete,
        handleSuccess,
        handleStatusSwitchChange,
        handleForeverSwitchChange,
        hasPermission,
      };
    },
  });
</script>
