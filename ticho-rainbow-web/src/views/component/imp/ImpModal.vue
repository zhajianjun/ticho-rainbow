<template>
  <BasicModal
    v-bind="$attrs"
    @register="registerModal"
    :maskClosable="false"
    @ok="handleSubmit"
    @cancel="handleCancel"
  >
    <UploadDragger
      :beforeUpload="beforeUpload"
      :file-list="filesRef"
      @remove="handleCancel"
      accept=".xlsx, .xls"
    >
      <p></p>
      <p></p>
      <Icon icon="ant-design:inbox-outlined" size="60" color="#3560bd" />
      <p></p>
      <p></p>
      <p></p>
      <p class="ant-upload-text">将文件拖至此处进行上传</p>
    </UploadDragger>
    <p></p>
    <p class="ant-upload-text"
      >仅允许导入xls、xlsx格式文件。
      <a style="color: #3560bd" @click="downlodModel()">下载模板</a></p
    >
  </BasicModal>
</template>
<script lang="ts" setup>
  import { type PropType, ref, unref } from 'vue';
  import { BasicModal, useModalInner } from '@/components/Modal';
  import { UploadDragger, UploadProps } from 'ant-design-vue';
  import Icon from '@/components/Icon/Icon.vue';
  import { isFunction } from '@/utils/is';
  import { downloadByData } from '@/utils/file/download';
  import { warn } from '@/utils/log';
  import { useMessage } from '@/hooks/web/useMessage';
  import { useI18n } from '@/hooks/web/useI18n';

  const emit = defineEmits(['success', 'register']);
  const props = defineProps({
    // 文件最大多少MB
    maxSize: {
      type: Number as PropType<number>,
      default: 2,
    },
    uploadApi: {
      type: Function as PropType<PromiseFn>,
      default: null,
      required: true,
    },
    downloadModelApi: {
      type: Function as PropType<PromiseFn>,
      default: null,
      required: true,
    },
  });

  const [registerModal, { closeModal, changeOkLoading }] = useModalInner(async () => {});

  async function downlodModel() {
    const { downloadModelApi } = props;
    if (!downloadModelApi || !isFunction(downloadModelApi)) {
      warn('upload api must exist and be a function');
      return;
    }
    await props?.downloadModelApi().then((res) => {
      // 提取文件名
      let fileName = decodeURI(res.headers['content-disposition'].split('filename=')[1]);
      downloadByData(res.data, fileName);
      createMessage.info(`模板${fileName}下载成功`);
    });
  }

  const { createMessage } = useMessage();

  const filesRef = ref<UploadProps['fileList']>([]);
  const { t } = useI18n();

  const beforeUpload: UploadProps['beforeUpload'] = (file) => {
    // 设置最大值，则判断
    const { maxSize } = props;
    if (maxSize && file.size / 1024 / 1024 >= maxSize) {
      createMessage.error(t('component.upload.maxSizeMultiple', [maxSize]));
      return false;
    }
    filesRef.value = [file];
    return false;
  };

  async function handleSubmit() {
    changeOkLoading(true);
    let files = unref(filesRef);
    if (!files || files.length === 0) {
      return;
    }
    const { uploadApi } = props;
    if (!uploadApi || !isFunction(uploadApi)) {
      warn('upload api must exist and be a function');
      return;
    }
    try {
      const res = await props.uploadApi?.(files[0]);
      // 提取文件名
      let fileName = decodeURI(res.headers['content-disposition'].split('filename=')[1]);
      downloadByData(res.data, fileName);
      filesRef.value = [];
      createMessage.info(`导入成功, 导入结果${fileName}已下载`);
      emit('success');
      closeModal();
      filesRef.value = [];
    } catch (e: any) {
      console.log(e);
    } finally {
      changeOkLoading(false);
    }
  }

  function handleCancel() {
    filesRef.value = [];
  }
</script>
