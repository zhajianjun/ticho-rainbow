<template>
  <BasicModal
    v-bind="$attrs"
    @register="registerModal"
    title="修改密码"
    :maskClosable="false"
    @ok="handleSubmit"
  >
    <BasicForm @register="registerForm" />
  </BasicModal>
</template>
<script lang="ts">
  import { defineComponent } from 'vue';
  import { BasicModal, useModalInner } from '@/components/Modal';
  import { BasicForm, useForm } from '@/components/Form/index';
  import { getPasswordModalFormColumns } from './user.data';
  import { modifyUserPassword } from '@/api/system/user';
  import { useMessage } from '@/hooks/web/useMessage';
  import { UserPasswordDTO } from '@/api/system/model/userModel';

  export default defineComponent({
    name: 'UserPasswordModal',
    components: { BasicModal, BasicForm },
    emits: ['success', 'register'],
    setup(_, { emit }) {
      const [registerForm, { setFieldsValue, resetFields, validate }] = useForm({
        labelWidth: 100,
        baseColProps: { span: 24 },
        schemas: getPasswordModalFormColumns(),
        showActionButtonGroup: false,
        actionColOptions: {
          span: 23,
        },
      });

      const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
        await resetFields();
        await setFieldsValue({
          ...data.record,
        });
      });

      async function handleSubmit() {
        try {
          const values = (await validate()) as UserPasswordDTO;
          setModalProps({ confirmLoading: true });
          await modifyUserPassword(values);
          const { createMessage } = useMessage();
          createMessage.success('操作成功');
          closeModal();
          // 触发父组件success方法
          emit('success');
        } finally {
          setModalProps({ confirmLoading: false });
        }
      }

      return { registerModal, registerForm, handleSubmit };
    },
  });
</script>
