<template>
  <BasicModal
    v-bind="$attrs"
    @register="registerModal"
    title="人机校验"
    :minHeight="100"
    :maskClosable="false"
    @open-change="handleOpen"
    @ok="handleSubmit"
  >
    <BasicForm @register="registerForm" class="imgCodeModel">
      <template #imgCode="{ model, field }">
        <Input
          class="rainbow"
          size="large"
          v-model:value="model[field]"
          :placeholder="t('sys.login.imgCode')"
        >
          <template #addonAfter>
            <img :src="imgCodeShow" alt="imgCode" @click="flushImgCode" />
          </template>
        </Input>
      </template>
    </BasicForm>
  </BasicModal>
</template>
<script lang="ts">
  import { defineComponent, ref } from 'vue';
  import { BasicModal, useModalInner } from '@/components/Modal';
  import { BasicForm, useForm } from '@/components/Form/index';
  import { Input } from 'ant-design-vue';
  import { useI18n } from '@/hooks/web/useI18n';
  import { buildUUID } from '@/utils/uuid';
  import { getImgCode } from '@/api/system/login';
  import { ImgCodeDTO } from '@/api/system/model/userModel';

  export default defineComponent({
    name: 'ImgCodeModal',
    components: { Input, BasicModal, BasicForm },
    emits: ['success', 'register'],
    setup(_, { emit }) {
      const imgKey = buildUUID();
      const [registerForm, { setFieldsValue, resetFields, validate }] = useForm({
        labelWidth: 100,
        baseColProps: { span: 24 },
        schemas: [
          {
            field: 'imgKey',
            defaultValue: imgKey,
            component: 'Input',
            show: false,
          },
          {
            field: 'imgCode',
            slot: 'imgCode',
            required: true,
            componentProps: {
              placeholder: '请输入验证码',
            },
          },
        ],
        showActionButtonGroup: false,
        actionColOptions: {
          span: 23,
        },
      });

      const { t } = useI18n();
      const imgCodeShow = ref('');
      async function flushImgCode() {
        getImgCode(imgKey).then((res) => {
          imgCodeShow.value = window.URL.createObjectURL(res);
        });
      }

      const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
        await resetFields();
        await setFieldsValue({
          ...data.record,
        });
      });
      function handleOpen(v: boolean) {
        if (!v) {
          return;
        }
        resetFields();
        flushImgCode();
      }

      async function handleSubmit() {
        try {
          const data = (await validate()) as ImgCodeDTO;
          setModalProps({ confirmLoading: true });
          emit('success', data);
          closeModal();
        } finally {
          setModalProps({ confirmLoading: false });
        }
      }
      return {
        registerModal,
        registerForm,
        handleSubmit,
        handleOpen,
        t,
        imgCodeShow,
        flushImgCode,
      };
    },
  });
</script>
<style lang="less">
  .imgCodeModel {
    width: 488px;

    .ant-input-group-addon {
      padding-right: 0;
      border: none;
      background-color: transparent;
      img {
        margin: 0 0 0 0;
        padding: 0 0 0 0;
        border-radius: 8px;
        width: 120px;
        height: 40px;
        border: 1px solid #d4d4d4;
      }
    }
  }
</style>
