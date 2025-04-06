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
        <a-button
          type="primary"
          ghost
          v-auth="'FileExport'"
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
              // icon: 'ant-design:download-outlined',
              onClick: handleDownload.bind(null, record),
              label: '下载',
              tooltip: '下载',
              color: 'success',
              type: 'primary',
              auth: 'FileDownload',
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
              auth: 'FileDel',
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
  import { defineComponent, ref } from 'vue';
  import { BasicTable, TableAction, useTable } from '@/components/Table';
  import { useModal } from '@/components/Modal';
  import FileInfoModal from './FileInfoModal.vue';
  import { getSearchColumns, getTableColumns } from './fileInfo.data';
  import {
    cancelFileInfo,
    delFileInfo,
    disableFileInfo,
    enableFileInfo,
    expExcel,
    fileInfoPage,
    presigned,
  } from '@/api/storage/fileInfo';
  import { usePermission } from '@/hooks/web/usePermission';
  import { useMessage } from '@/hooks/web/useMessage';
  import CustomUploadModal from '@/views/component/file/CustomUploadModal.vue';
  import CustomUpload from '@/views/component/file/CustomUpload.vue';
  import { downloadByData, downloadByUrl } from '@/utils/file/download';
  import { FileItem } from '@/components/Upload/src/types/typing';
  import { FileInfoQuery } from '@/api/storage/model/fileInfoModel';

  export default defineComponent({
    name: 'FileInfo',
    components: { CustomUploadModal, CustomUpload, BasicTable, FileInfoModal, TableAction },
    setup() {
      const { hasPermission } = usePermission();
      let showSelect = hasPermission('FileSelect');
      const [registerModal, { openModal }] = useModal();
      const [registerTable, { reload, getSelectRowKeys, getForm }] = useTable({
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
          width: 140,
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
          presigned(record.id, null, true).then((res) => {
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

      const exportLoding = ref<Boolean>(false);

      function handleExport() {
        exportLoding.value = true;
        // 是否有选中，优先下载选中数据
        const selectRowKeys = getSelectRowKeys();
        let params: FileInfoQuery;
        if (selectRowKeys && selectRowKeys.length > 0) {
          params = { ids: selectRowKeys } as FileInfoQuery;
        } else {
          // 获取查询参数
          const { getFieldsValue } = getForm();
          params = getFieldsValue() as FileInfoQuery;
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
        exportLoding,
        handleExport,
      };
    },
  });
</script>
