<template>
  <Tooltip
    :title="t('layout.header.tooltipErrorLog')"
    placement="bottom"
    :mouseEnterDelay="0.5"
    @click="handleToErrorList"
  >
    <Badge :count="getCount" :offset="[0, 10]" :overflowCount="99">
      <Icon icon="ion:bug-outline" />
    </Badge>
  </Tooltip>
</template>
<script lang="ts" setup>
  import { computed } from 'vue';
  import { Badge, Tooltip } from 'ant-design-vue';
  import Icon from '@/components/Icon/Icon.vue';
  import { useI18n } from '@/hooks/web/useI18n';
  import { useErrorLogStore } from '@/store/modules/errorLog';
  import { PageEnum } from '@/enums/pageEnum';

  import { useRouter } from 'vue-router';

  defineOptions({ name: 'ErrorAction' });

  const { t } = useI18n();
  const { push } = useRouter();
  const errorLogStore = useErrorLogStore();

  const getCount = computed(() => errorLogStore.getErrorLogListCount);

  function handleToErrorList() {
    push(PageEnum.ERROR_LOG_PAGE).then(() => {
      errorLogStore.setErrorLogListCount(0);
    });
  }
</script>
