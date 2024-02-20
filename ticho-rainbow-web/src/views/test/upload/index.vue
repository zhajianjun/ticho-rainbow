<template>
  <PageWrapper title="上传组件">
    <Alert message="基础示例" />
    <BasicUpload
      :maxSize="20"
      :maxNumber="10"
      @change="handleChange"
      :api="chunkUpload"
      class="my-5"
      :accept="['image/*']"
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

  const { createMessage } = useMessage();
  function handleChange(list: string[]) {
    createMessage.info(`已上传文件${JSON.stringify(list)}`);
  }

  function chunkUpload(
    params: UploadFileParams,
    onUploadProgress: (progressEvent: AxiosProgressEvent) => void,
  ) {
    console.log(params);
    console.log(onUploadProgress);
    // 分片
    // 循环上传
    // 合并
    // 返回成功
    onUploadProgress = (progressEvent: AxiosProgressEvent) => {
      progressEvent.loaded = 100;
      progressEvent.total = 100;
      return progressEvent;
    };
    return Promise.resolve({ data: null });
  }
</script>
