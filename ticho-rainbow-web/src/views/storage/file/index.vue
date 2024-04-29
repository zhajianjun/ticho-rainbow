<template>
  <div>
    <BasicTable @register="registerTable">
      <template #toolbar>
        <CustomUpload
          :maxSize="10240000"
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
              icon: 'ant-design:upload-outlined',
              onClick: openUploadModalProxy.bind(null, record),
              tooltip: '断点续传',
              auth: 'FileContinueUpload',
              ifShow: record.status === 3,
            },
            {
              icon: 'ant-design:download-outlined',
              onClick: handleDownload.bind(null, record),
              tooltip: '文件下载',
              auth: 'FileContinueUpload',
              ifShow: record.status === 1,
            },
            {
              icon: 'ant-design:delete-outlined',
              color: 'error',
              popConfirm: {
                title: '是否确认删除',
                confirm: handleDelete.bind(null, record),
              },
              tooltip: '删除',
              auth: 'FileInfoDel',
            },
          ]"
        />
      </template>
    </BasicTable>
    <CustomUploadModal
      @register="registerUploadModal"
      @save="handleSave"
      @delete="handleDelete"
      :maxSize="10240000"
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
  import { fileInfoPage, delFileInfo, getUrl } from '@/api/storage/fileInfo';
  import { usePermission } from '@/hooks/web/usePermission';
  import { useMessage } from '@/hooks/web/useMessage';
  import CustomUploadModal from './CustomUploadModal.vue';
  import CustomUpload from './CustomUpload.vue';
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

      const { createMessage } = useMessage();
      function handleSave(fileItems: FileItem[]) {
        console.log(fileItems);
        reload();
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

      function handleSuccess() {
        reload();
      }

      // 上传modal
      const [registerUploadModal, { openModal: openUploadModal, setModalProps }] = useModal();

      function openUploadModalProxy(record: Recordable) {
        openUploadModal(true, record.chunkId);
        setModalProps({ okText: '确定', cancelText: '取消' });
      }

      function handleDownload(record: Recordable) {
        try {
          getUrl(record.id, null, true).then((res) => {
            downloadByUrl({ url: res, target: '_self' });
            createMessage.info('文件下载中');
          });
        } catch (e) {
          createMessage.error('文件下载失败');
        }
      }

      return {
        registerTable,
        registerModal,
        handleSave,
        handleEdit,
        handleDelete,
        handleSuccess,
        hasPermission,
        registerUploadModal,
        openUploadModalProxy,
        handleDownload,
      };
    },
  });
</script>
