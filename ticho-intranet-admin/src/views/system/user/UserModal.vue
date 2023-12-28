<template>
  <BasicModal
    v-bind="$attrs"
    @register="registerModal"
    :title="getTitle"
    :maskClosable="false"
    @ok="handleSubmit"
  >
    <BasicForm @register="registerForm" />
  </BasicModal>
</template>
<script lang="ts">
  import { computed, defineComponent, ref, unref } from 'vue';
  import { BasicModal, useModalInner } from '@/components/Modal';
  import { BasicForm, useForm } from '@/components/Form/index';
  import { getModalFormColumns } from './user.data';
  import { modifyUser, saveUser } from '@/api/system/user';
  import { UserDTO } from '@/api/system/model/userModel';
  import { useMessage } from "@/hooks/web/useMessage";

  export default defineComponent({
    name: 'UserModal',
    components: { BasicModal, BasicForm },
    emits: ['success', 'register'],
    setup(_, { emit }) {
      const isUpdate = ref(true);
      const [registerForm, { setFieldsValue, updateSchema, resetFields, validate }] = useForm({
        labelWidth: 100,
        baseColProps: { span: 24 },
        schemas: getModalFormColumns(),
        showActionButtonGroup: false,
        actionColOptions: {
          span: 23,
        },
      });

      const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
        await resetFields();
        setModalProps({ confirmLoading: false });
        isUpdate.value = !!data?.isUpdate;
        if (unref(isUpdate)) {
          await setFieldsValue({
            ...data.record,
          });
        }
        await updateSchema([
          {
            field: 'password',
            show: !unref(isUpdate),
          },
          {
            field: 'username',
            componentProps: {
              disabled: unref(isUpdate),
            },
          },
        ]);
      });

      const getTitle = computed(() => (!unref(isUpdate) ? '新增用户' : '编辑用户'));

      async function handleSubmit() {
        try {
          const values = (await validate()) as UserDTO;
          setModalProps({ confirmLoading: true });
          if (unref(isUpdate)) {
            await modifyUser(values);
          } else {
            await saveUser(values);
          }
          const { createMessage } = useMessage();
          createMessage.success('操作成功');
          closeModal();
          // 触发父组件方法
          emit('success');
        } finally {
          setModalProps({ confirmLoading: false });
        }
      }

      return { registerModal, registerForm, getTitle, handleSubmit };
    },
  });
</script>
