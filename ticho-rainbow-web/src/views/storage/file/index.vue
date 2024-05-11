<template>
  <div>
    <BasicTable @register="registerTable">
      <template #toolbar>
        <CustomUpload
          :maxSize="100"
          :maxNumber="10"
          @save="handleSave"
          :multiple="true"
          v-auth="'FileUpload'"
        />
      </template>
      <template #action="{ record }">
        <TableAction
          :actions="[
            {
              // icon: 'ant-design:download-outlined',
              onClick: handleDownload.bind(null, record),
              label: '下载',
              tooltip: '下载',
              color: 'success',
              type: 'primary',
              auth: 'FileContinueUpload',
              disabled: record.status !== 1,
              ifShow: record.status !== 3,
            },
            {
              // icon: 'ant-design:upload-outlined',
              onClick: openUploadModalProxy.bind(null, record),
              color: 'success',
              type: 'primary',
              label: '续传',
              tooltip: '断点续传',
              auth: 'FileContinueUpload',
              ifShow: record.status === 3,
            },
            {
              onClick: handleEnable.bind(null, record),
              label: '启用',
              type: 'primary',
              color: 'success',
              tooltip: '启用',
              auth: 'FileEnable',
              disabled: record.status !== 2,
            },
            {
              onClick: handleDisable.bind(null, record),
              label: '停用',
              tooltip: '停用',
              color: 'warning',
              type: 'primary',
              auth: 'FileDisable',
              disabled: record.status !== 1,
            },
          ]"
          :dropDownActions="[
            {
              icon: 'ant-design:delete-outlined',
              onClick: handleCancel.bind(null, record),
              label: '作废',
              color: 'error',
              type: 'primary',
              tooltip: '作废',
              auth: 'FileCancel',
              disabled: record.status === 99 || record.status === 3,
            },
            {
              icon: 'ant-design:delete-filled',
              color: 'error',
              type: 'primary',
              label: '删除',
              popConfirm: {
                title: '是否确认删除',
                confirm: handleDelete.bind(null, record),
              },
              tooltip: '删除',
              auth: 'FileInfoDel',
              disabled: record.status !== 99 && record.status !== 3,
            },
          ]"
        />
      </template>
    </BasicTable>
    <CustomUploadModal
      @register="registerUploadModal"
      @save="handleSave"
      @delete="handleDelete"
      :maxSize="100"
    />
    <FileInfoModal @register="registerModal" @success="handleSuccess" />
  </div>
</template>
<script lang="ts">
  import { defineComponent } from 'vue';
  import { BasicTable, useTable, TableAction } from '@/components/Table';
  import { useModal } from '@/components/Modal';
  import FileInfoModal from './FileInfoModal.vue';
  import { getTableColumns, getSearchColumns } from './fileInfo.data';
  import {
    fileInfoPage,
    getUrl,
    delFileInfo,
    enableFileInfo,
    disableFileInfo,
    cancelFileInfo,
  } from '@/api/storage/fileInfo';
  import { usePermission } from '@/hooks/web/usePermission';
  import { useMessage } from '@/hooks/web/useMessage';
  import CustomUploadModal from '@/views/component/file/CustomUploadModal.vue';
  import CustomUpload from '@/views/component/file/CustomUpload.vue';
  import { downloadByUrl } from '@/utils/file/download';
  import { FileItem } from '@/components/Upload/src/types/typing';

  export default defineComponent({
    name: 'FileInfo',
    components: { CustomUploadModal, CustomUpload, BasicTable, FileInfoModal, TableAction },
    setup() {
      const { hasPermission } = usePermission();
      let showSelect = hasPermission('FileInfoSelect');
      const [registerModal, { openModal }] = useModal();
      const [registerTable, { reload }] = useTable({
        title: '文件信息列表',
        api: fileInfoPage,
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
          width: 140,
          title: '操作',
          dataIndex: 'action',
          slots: { customRender: 'action' },
          fixed: undefined,
        },
        pagination: {
          position: ['bottomLeft'],
        },
      });

      const { createMessage } = useMessage();
      function handleSave(fileItems: FileItem[]) {
        console.log(fileItems);
        reload();
      }

      function handleSuccess() {
        reload();
      }

      // 上传modal
      const [registerUploadModal, { openModal: openUploadModal, setModalProps }] = useModal();

      function openUploadModalProxy(record: Recordable) {
        openUploadModal(true, { uid: record.chunkId, type: record.type });
        setModalProps({ okText: '确定', cancelText: '取消' });
      }

      function handleDownload(record: Recordable) {
        try {
          getUrl(record.id, null, true).then((res) => {
            if (!res) {
              createMessage.error('文件不存在');
              return;
            }
            downloadByUrl({ url: res, target: '_blank' });
            createMessage.info('文件下载中');
          });
        } catch (e) {
          createMessage.error('文件下载失败');
        }
      }

      function handleEdit(record: Recordable) {
        openModal(true, {
          record,
          isUpdate: true,
        });
      }

      function handleDelete(record: Recordable) {
        delFileInfo(record.id).then(() => {
          reload();
        });
      }

      /** 启用 */
      function handleEnable(record: Recordable) {
        enableFileInfo(record.id).then(() => {
          reload();
        });
      }

      /** 停用 */
      function handleDisable(record: Recordable) {
        disableFileInfo(record.id).then(() => {
          reload();
        });
      }

      /** 作废 */
      function handleCancel(record: Recordable) {
        cancelFileInfo(record.id).then(() => {
          reload();
        });
      }

      return {
        registerTable,
        registerModal,
        handleSave,
        handleEdit,
        handleDelete,
        handleEnable,
        handleDisable,
        handleCancel,
        handleSuccess,
        hasPermission,
        registerUploadModal,
        openUploadModalProxy,
        handleDownload,
      };
    },
  });
</script>
