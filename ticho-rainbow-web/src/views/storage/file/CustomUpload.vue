<template>
  <div>
    <Space>
      <a-button
        type="primary"
        @click="openUploadModal"
        preIcon="carbon:cloud-upload"
        :disabled="disabled"
      >
        {{ t('component.upload.upload') }}
      </a-button>
      <Tooltip placement="bottom" v-if="showPreview">
        <template #title>
          {{ t('component.upload.uploaded') }}
          <template v-if="fileList.length">
            {{ fileList.length }}
          </template>
        </template>
      </Tooltip>
    </Space>
    <CustomUploadModal
      v-bind="bindValue"
      :previewFileList="fileList"
      :fileListOpenDrag="fileListOpenDrag"
      :fileListDragOptions="fileListDragOptions"
      @register="registerUploadModal"
      @change="handleChange"
      @delete="handleDelete"
    />
  </div>
</template>
<script lang="ts" setup>
  import { ref, watch, unref, computed, useAttrs } from 'vue';
  import { Recordable } from '@vben/types';
  import { Tooltip, Space } from 'ant-design-vue';
  import { useModal } from '@/components/Modal';
  import { uploadContainerProps } from '@/components/Upload/src/props';
  import { omit } from 'lodash-es';
  import { useI18n } from '@/hooks/web/useI18n';
  import { isArray } from '@/utils/is';
  import CustomUploadModal from './CustomUploadModal.vue';

  defineOptions({ name: 'BasicUpload' });

  const props = defineProps(uploadContainerProps);

  const emit = defineEmits(['change', 'delete', 'preview-delete', 'update:value']);

  const attrs = useAttrs();
  const { t } = useI18n();
  // 上传modal
  const [registerUploadModal, { openModal, setModalProps }] = useModal();

  function openUploadModal() {
    openModal(true);
    setModalProps({ okText: '确定', cancelText: '取消' });
  }

  const fileList = ref<string[]>([]);

  const showPreview = computed(() => {
    const { emptyHidePreview } = props;
    if (!emptyHidePreview) return true;
    return emptyHidePreview ? fileList.value.length > 0 : true;
  });

  const bindValue = computed(() => {
    const value = { ...attrs, ...props };
    return omit(value, 'onChange');
  });

  watch(
    () => props.value,
    (value = []) => {
      fileList.value = isArray(value) ? value : [];
    },
    { immediate: true },
  );

  // 上传modal保存操作
  function handleChange(urls: string[]) {
    fileList.value = [...unref(fileList), ...(urls || [])];
    emit('update:value', fileList.value);
    emit('change', fileList.value);
  }

  function handleDelete(record: Recordable<any>) {
    emit('delete', record);
  }
</script>
