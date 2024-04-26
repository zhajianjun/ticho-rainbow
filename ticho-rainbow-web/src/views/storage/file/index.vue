<template>
  <div>
    <BasicTable @register="registerTable">
      <template #toolbar>
        <CustomUpload
          :maxSize="10240000"
          :maxNumber="10"
          @change="handleChange"
          :api="uploadFile"
        />
      </template>
      <template #action="{ record }">
        <TableAction
          :actions="[
            {
              icon: 'clarity:note-edit-line',
              onClick: openUploadModalProxy.bind(null, record),
              tooltip: '修改',
              auth: 'FileInfoEdit',
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
    <UploadModal
      @register="registerUploadModal"
      @change="handleChange"
      @delete="handleDelete"
      :maxSize="10240000"
      :uploadParams="{ uid: uidRef }"
      :api="uploadFile"
    />
    <FileInfoModal @register="registerModal" @success="handleSuccess" />
  </div>
</template>
<script lang="ts">
  import { defineComponent, ref } from 'vue';
  import { BasicTable, useTable, TableAction } from '@/components/Table';
  import { useModal } from '@/components/Modal';
  import FileInfoModal from './FileInfoModal.vue';
  import { getTableColumns, getSearchColumns } from './fileInfo.data';
  import {
    fileInfoPage,
    delFileInfo,
    composeChunk,
    upload,
    uploadChunk,
  } from '@/api/system/fileInfo';
  import { usePermission } from '@/hooks/web/usePermission';
  import { CustomUpload } from '@/components/Upload';
  import { useMessage } from '@/hooks/web/useMessage';
  import { UploadFileParams } from '#/axios';
  import { AxiosProgressEvent } from 'axios';
  import { ChunkFileDTO } from '@/api/system/model/uploadModel';
  import SparkMD5 from 'spark-md5';
  import UploadModal from '@/components/Upload/src/components/UploadModal.vue';

  export default defineComponent({
    name: 'FileInfo',
    components: { UploadModal, CustomUpload, BasicTable, FileInfoModal, TableAction },
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
      function handleChange(list: string[]) {
        createMessage.info(`已上传文件${JSON.stringify(list)}`);
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

      const chunkSize = 5 * 1024 * 1024;

      async function uploadFile(
        params: UploadFileParams,
        onUploadProgress: (progressEvent: AxiosProgressEvent) => void,
      ) {
        console.log(params);
        const file = params.file;
        const fileSize = file.size;
        if (fileSize <= chunkSize) {
          return upload(params, onUploadProgress).then((res) => {
            res.data.data = 'https://localhost:5122/file/download?filename=123165464.png';
            return Promise.resolve(res);
          });
        }
        return uploadBigFile(params, file, onUploadProgress);
      }

      const uidRef = ref(null);

      async function uploadBigFile(
        params: UploadFileParams,
        file: File,
        onUploadProgress: (progressEvent: AxiosProgressEvent) => void,
      ) {
        const paramData = params.data;
        const fileSize = file.size;
        let uid;
        if (paramData && paramData.uid && paramData.uid !== '' && paramData.uid !== undefined) {
          uid = paramData.uid;
        } else {
          uid = file.uid;
        }
        // 分片数量
        const chunkCount = Math.ceil(fileSize / chunkSize);
        const fileName = file.name;
        const fileMd5 = (await getFileMd5(file, chunkCount, chunkSize)) as string;
        const axiosProgressEvent = { loaded: 0, total: 100 } as AxiosProgressEvent;
        // 上传分片
        for (let i = 0; i < chunkCount; i++) {
          // 分片开始
          const start = i * chunkSize;
          // 分片结束
          const end = Math.min(fileSize, start + chunkSize);
          // 分片文件
          const chunkFile = file.slice(start, end);
          // 定义分片上传接口参数，跟后端商定
          const formdata = {
            chunkId: uid,
            md5: fileMd5,
            fileName: fileName,
            fileSize: fileSize,
            chunkCount: chunkCount,
            chunkfile: chunkFile,
            index: i,
          } as ChunkFileDTO;
          let response;
          try {
            // 调用分片上传接口
            response = await uploadChunk(formdata);
          } catch (e) {
            createMessage.error(`${e}`);
            return Promise.reject(e);
          }
          const { code, msg } = response.data;
          if (response.status !== 200 || code !== 0) {
            createMessage.error(`${msg}`);
            return Promise.reject(msg);
          }
          axiosProgressEvent.loaded = ((i + 1) / chunkCount) * 100;
          onUploadProgress(axiosProgressEvent);
        }
        // 合并
        await composeChunk(uid);
        return Promise.resolve({ data: { url: '', type: '' || '', name: '' } });
      }

      async function getFileMd5(file: File, chunkCount: number, chunkSize: number) {
        return new Promise((resolve, reject) => {
          const blobSlice = File.prototype.slice;
          const chunks = chunkCount;
          let currentChunk = 0;
          const spark = new SparkMD5.ArrayBuffer();
          const fileReader = new FileReader();
          fileReader.onload = (e) => {
            let result = e.target?.result as ArrayBuffer;
            spark.append(result);
            currentChunk++;
            if (currentChunk < chunks) {
              loadNext();
            } else {
              const md5 = spark.end();
              resolve(md5);
            }
          };
          fileReader.onerror = (e) => {
            reject(e);
          };
          function loadNext() {
            const start = currentChunk * chunkSize;
            let end = start + chunkSize;
            if (end > file.size) {
              end = file.size;
            }
            fileReader.readAsArrayBuffer(blobSlice.call(file, start, end));
          }
          loadNext();
        });
      }

      // 上传modal
      const [registerUploadModal, { openModal: openUploadModal, setModalProps }] = useModal();

      function openUploadModalProxy(record: Recordable) {
        openUploadModal(true);
        setModalProps({ okText: '确定', cancelText: '取消' });
        uidRef.value = record.chunkId;
      }

      return {
        registerTable,
        registerModal,
        handleChange,
        uploadFile,
        uploadBigFile,
        handleEdit,
        handleDelete,
        handleSuccess,
        hasPermission,
        registerUploadModal,
        openUploadModalProxy,
        uidRef,
      };
    },
  });
</script>
