<template>
  <BasicModal
    width="800px"
    :title="t('component.upload.upload')"
    :okText="t('component.upload.save')"
    v-bind="$attrs"
    @register="register"
    @ok="handleOk"
    :closeFunc="handleCloseFunc"
    :maskClosable="false"
    :keyboard="false"
    class="upload-modal"
    :okButtonProps="getOkButtonProps"
    :cancelButtonProps="{ disabled: isUploadingRef }"
  >
    <template #centerFooter>
      <a-button
        @click="handleStartUpload"
        color="success"
        :disabled="!getIsSelectFile"
        :loading="isUploadingRef"
      >
        {{ getUploadBtnText }}
      </a-button>
    </template>

    <div class="upload-modal-toolbar">
      <Alert :message="getHelpText" type="info" banner class="upload-modal-toolbar__text" />

      <Upload
        :accept="getStringAccept"
        :multiple="multiple"
        :before-upload="beforeUpload"
        :show-upload-list="false"
        class="upload-modal-toolbar__btn"
      >
        <a-button type="primary">
          {{ t('component.upload.choose') }}
        </a-button>
      </Upload>
    </div>
    <FileList
      v-model:dataSource="fileListRef"
      :columns="columns"
      :actionColumn="actionColumn"
      :openDrag="fileListOpenDrag"
      :dragOptions="fileListDragOptions"
    />
  </BasicModal>
</template>
<script lang="ts" setup>
  import { computed, PropType, ref, toRefs, unref } from 'vue';
  import { Alert, Upload } from 'ant-design-vue';
  import { BasicModal, useModalInner } from '@/components/Modal';
  // hooks
  import { useUploadType } from '@/components/Upload/src/hooks/useUpload';
  import { useMessage } from '@/hooks/web/useMessage';
  //   types
  import { FileItem, UploadResultStatus } from '@/components/Upload/src/types/typing';
  import { basicProps } from '@/components/Upload/src/props';
  import { createActionColumn, createTableColumns } from '@/components/Upload/src/components/data';
  // utils
  import { checkImgType, getBase64WithFile } from '@/components/Upload/src/helper';
  import { buildUUID } from '@/utils/uuid';
  import { isFunction } from '@/utils/is';
  import { warn } from '@/utils/log';
  import FileList from '@/components/Upload/src/components/FileList.vue';
  import { useI18n } from '@/hooks/web/useI18n';
  import { get } from 'lodash-es';
  import { uploadFileHandler } from './upload';

  const props = defineProps({
    ...basicProps,
    previewFileList: {
      type: Array as PropType<string[]>,
      default: () => [],
    },
    customApi: {
      type: Function as PropType<PromiseFn>,
      default: uploadFileHandler,
      required: false,
    },
  });

  const emit = defineEmits(['change', 'register', 'delete']);

  const columns = createTableColumns();
  const actionColumn = createActionColumn(handleRemove);

  // 是否正在上传
  const isUploadingRef = ref(false);
  const fileListRef = ref<FileItem[]>([]);
  const { accept, helpText, maxNumber, maxSize } = toRefs(props);

  const { t } = useI18n();

  const uidRef = ref(null);

  const [register, { closeModal }] = useModalInner(async (uid) => {
    console.log('uid', uid);
    uidRef.value = uid;
  });

  const { getStringAccept, getHelpText } = useUploadType({
    acceptRef: accept,
    helpTextRef: helpText,
    maxNumberRef: maxNumber,
    maxSizeRef: maxSize,
  });

  const { createMessage } = useMessage();

  const getIsSelectFile = computed(() => {
    return (
      fileListRef.value.length > 0 &&
      !fileListRef.value.every((item) => item.status === UploadResultStatus.SUCCESS)
    );
  });

  const getOkButtonProps = computed(() => {
    const someSuccess = fileListRef.value.some(
      (item) => item.status === UploadResultStatus.SUCCESS,
    );
    return {
      disabled: isUploadingRef.value || fileListRef.value.length === 0 || !someSuccess,
    };
  });

  const getUploadBtnText = computed(() => {
    const someError = fileListRef.value.some((item) => item.status === UploadResultStatus.ERROR);
    return isUploadingRef.value
      ? t('component.upload.uploading')
      : someError
        ? t('component.upload.reUploadFailed')
        : t('component.upload.startUpload');
  });

  // 上传前校验
  function beforeUpload(file: File) {
    const { size, name } = file;
    const { maxSize } = props;
    // 设置最大值，则判断
    if (maxSize && file.size / 1024 / 1024 >= maxSize) {
      createMessage.error(t('component.upload.maxSizeMultiple', [maxSize]));
      return false;
    }

    const commonItem = {
      uuid: buildUUID(),
      file,
      size,
      name,
      percent: 0,
      type: name.split('.').pop(),
    };
    // 生成图片缩略图
    if (checkImgType(file)) {
      // beforeUpload，如果异步会调用自带上传方法
      // file.thumbUrl = await getBase64(file);
      getBase64WithFile(file).then(({ result: thumbUrl }) => {
        fileListRef.value = [
          ...unref(fileListRef),
          {
            thumbUrl,
            ...commonItem,
          },
        ];
      });
    } else {
      fileListRef.value = [...unref(fileListRef), commonItem];
    }
    return false;
  }

  // 删除
  function handleRemove(record: FileItem) {
    const index = fileListRef.value.findIndex((item) => item.uuid === record.uuid);
    index !== -1 && fileListRef.value.splice(index, 1);
    isUploadingRef.value = fileListRef.value.some(
      (item) => item.status === UploadResultStatus.UPLOADING,
    );
    emit('delete', record);
  }

  async function uploadApiByItem(item: FileItem) {
    const { customApi } = props;
    if (!customApi || !isFunction(customApi)) {
      return warn('upload customApi must exist and be a function');
    }
    try {
      item.status = UploadResultStatus.UPLOADING;
      let uid: string | null;
      let isContinued: boolean;
      if (unref(uidRef)) {
        uid = unref(uidRef);
        isContinued = true;
      } else {
        uid = item.uuid;
        isContinued = false;
      }
      const ret = await props.customApi?.(
        {
          data: {
            ...(props.uploadParams || {}),
            uid: uid,
            isContinued: isContinued,
          },
          file: item.file,
          name: props.name,
          filename: props.filename,
        },
        function onUploadProgress(progressEvent: ProgressEvent) {
          item.percent = ((progressEvent.loaded / progressEvent.total) * 100) | 0;
        },
      );
      const { data } = ret;
      item.status = UploadResultStatus.SUCCESS;
      item.response = data;
      if (props.resultField) {
        // 适配预览组件而进行封装
        item.response = {
          code: 0,
          message: 'upload Success!',
          url: get(ret, props.resultField),
        };
      }
      return {
        success: true,
        error: null,
      };
    } catch (e) {
      console.log(e);
      item.status = UploadResultStatus.ERROR;
      return {
        success: false,
        error: e,
      };
    }
  }

  // 点击开始上传
  async function handleStartUpload() {
    const { maxNumber } = props;
    if (fileListRef.value.length + props.previewFileList.length > maxNumber) {
      return createMessage.warning(t('component.upload.maxNumber', [maxNumber]));
    }
    try {
      isUploadingRef.value = true;
      // 只上传不是成功状态的
      const uploadFileList =
        fileListRef.value.filter((item) => item.status !== UploadResultStatus.SUCCESS) || [];
      const data = await Promise.all(
        uploadFileList.map((item) => {
          return uploadApiByItem(item);
        }),
      );
      isUploadingRef.value = false;
      // 生产环境:抛出错误
      const errorList = data.filter((item: any) => !item.success);
      if (errorList.length > 0) throw errorList;
    } catch (e) {
      isUploadingRef.value = false;
      throw e;
    }
  }

  //   点击保存
  function handleOk() {
    const { maxNumber } = props;

    if (fileListRef.value.length > maxNumber) {
      return createMessage.warning(t('component.upload.maxNumber', [maxNumber]));
    }
    if (isUploadingRef.value) {
      return createMessage.warning(t('component.upload.saveWarn'));
    }
    const fileList: string[] = [];

    for (const item of fileListRef.value) {
      const { status, response } = item;
      if (status === UploadResultStatus.SUCCESS && response) {
        fileList.push(response.data);
      }
    }
    // 存在一个上传成功的即可保存
    if (fileList.length <= 0) {
      return createMessage.warning(t('component.upload.saveError'));
    }
    fileListRef.value = [];
    closeModal();
    emit('change', fileList);
  }

  // 点击关闭：则所有操作不保存，包括上传的
  async function handleCloseFunc() {
    if (!isUploadingRef.value) {
      fileListRef.value = [];
      return true;
    } else {
      createMessage.warning(t('component.upload.uploadWait'));
      return false;
    }
  }
</script>
<style lang="less">
  .upload-modal {
    .ant-upload-list {
      display: none;
    }

    .ant-table-wrapper .ant-spin-nested-loading {
      padding: 0;
    }

    &-toolbar {
      display: flex;
      align-items: center;
      margin-bottom: 8px;

      &__btn {
        flex: 1;
        margin-left: 8px;
        text-align: right;
      }
    }
  }
</style>
