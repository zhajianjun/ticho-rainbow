<template>
  <PageWrapper title="上传组件">
    <Alert message="基础示例" />
    <BasicUpload
      :maxSize="10240000"
      :maxNumber="10"
      @change="handleChange"
      :api="chunkUpload"
      class="my-5"
      :accept="['*']"
    />
  </PageWrapper>
</template>
<script lang="ts" setup>
  import { BasicUpload } from '@/components/Upload';
  import { useMessage } from '@/hooks/web/useMessage';
  import { PageWrapper } from '@/components/Page';
  import { Alert } from 'ant-design-vue';
  import { upload, uploadChunk, composeChunk } from '@/api/system/upload';
  import { UploadFileParams } from '#/axios';
  import { AxiosProgressEvent } from 'axios';
  import SparkMD5 from 'spark-md5';
  import { ChunkFileDTO } from '@/api/system/model/uploadModel';

  const { createMessage } = useMessage();
  function handleChange(list: string[]) {
    createMessage.info(`已上传文件${JSON.stringify(list)}`);
  }

  async function chunkUpload(
    params: UploadFileParams,
    onUploadProgress: (progressEvent: AxiosProgressEvent) => void,
  ) {
    const file = params.file;
    console.log(file);
    // 30MB
    const chunkSize = 50 * 1024 * 1024;
    const fileSize = file.size;
    if (fileSize <= chunkSize) {
      return upload(params, onUploadProgress).then((res) => {
        res.data.data = 'https://localhost:5122/file/download?filename=123165464.png';
        return Promise.resolve(res);
      });
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
        md5: fileMd5,
        fileName: fileName,
        chunkCount: chunkCount,
        file: chunkFile,
        index: i,
      } as ChunkFileDTO;
      // 循环上传
      await uploadChunk(formdata);
      axiosProgressEvent.loaded = ((i + 1) / chunkCount) * 100;
      onUploadProgress(axiosProgressEvent);
    }
    // 合并
    await composeChunk(fileMd5);
    return Promise.resolve({ data: { url: '', type: '' || '', name: '' } });
  }

  function getFileMd5(file: File, chunkCount: number, chunkSize: number) {
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
</script>
