<template>
  <PageWrapper>
    <template #headerContent> SSE测试 </template>
    <div class="lg:flex"> {{ url }} </div>
  </PageWrapper>
</template>
<script lang="ts" setup>
  import { PageWrapper } from '@/components/Page';
  import { useGlobSetting } from '@/hooks/setting';
  import { isString } from '@/utils/is';
  import { sign } from '@/api/system/sse';
  import { successHandle, failHandle } from './eventHandle.ts';

  const globSetting = useGlobSetting();
  const joinPrefix = true;
  const urlPrefix = globSetting.urlPrefix;
  const apiUrl = globSetting.apiUrl;
  let url = '/sse/connect';
  if (joinPrefix) {
    url = `${urlPrefix}${url}`;
  }
  if (apiUrl && isString(apiUrl)) {
    url = `${apiUrl}${url}`;
  }
  sign().then((res) => {
    if (res && isString(res)) {
      url = `${url}?id=${res}`;
      const eventSource = new EventSource(url);

      eventSource.onmessage = function (event) {
        successHandle(event.data);
      };

      eventSource.onerror = function (err) {
        failHandle(err);
      };
    }
  });
</script>
