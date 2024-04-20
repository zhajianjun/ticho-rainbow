<template>
  <BasicModal
    v-bind="$attrs"
    @register="registerModal"
    title="执行一次"
    :maskClosable="false"
    @ok="handleSubmit"
  >
    <BasicForm @register="registerForm" />
  </BasicModal>
</template>
<script lang="ts">
  import { defineComponent } from 'vue';
  import { BasicModal, useModalInner } from '@/components/Modal';
  import { runOnceTask } from '@/api/system/task';
  import { BasicForm, useForm } from '@/components/Form';
  import { getRunOnceModalFormColumns } from '@/views/system/task/task.data';

  export default defineComponent({
    name: 'TaskModal',
    components: { BasicForm, BasicModal },
    emits: ['success', 'register'],
    setup(_) {
      const [registerForm, { setFieldsValue, resetFields, validate }] = useForm({
        labelWidth: 100,
        baseColProps: { span: 24 },
        schemas: getRunOnceModalFormColumns(),
        showActionButtonGroup: false,
        actionColOptions: {
          span: 23,
        },
      });

      const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
        await resetFields();
        setModalProps({ confirmLoading: false });
        await setFieldsValue({
          ...data,
        });
      });

      async function handleSubmit() {
        try {
          const values = await validate();
          setModalProps({ confirmLoading: true });
          await runOnceTask(values);
          closeModal();
        } finally {
          setModalProps({ confirmLoading: false });
        }
      }

      return { registerModal, handleSubmit, registerForm };
    },
  });
</script>
