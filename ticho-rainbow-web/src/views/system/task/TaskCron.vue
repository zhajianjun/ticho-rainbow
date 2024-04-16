<template>
  <BasicModal
    v-bind="$attrs"
    @register="registerModal"
    title="Cron表达式"
    :maskClosable="false"
    @ok="handleSubmit"
  >
    <Cron :value="cronValue" :inputArea="true" @change="cronChange" />
  </BasicModal>
</template>
<script lang="ts">
  import { defineComponent, ref, unref } from 'vue';
  import { BasicModal, useModalInner } from '@/components/Modal';
  import { Cron } from '@/components/Cron';

  export default defineComponent({
    name: 'TaskModal',
    components: { Cron, BasicModal },
    emits: ['success', 'register'],
    setup(_, { emit }) {
      const cronValue = ref('');

      const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
        setModalProps({ confirmLoading: false, width: 1000 });
        cronValue.value = data;
      });

      async function handleSubmit() {
        try {
          setModalProps({ confirmLoading: true });
          emit('success', unref(cronValue));
          closeModal();
        } finally {
          setModalProps({ confirmLoading: false });
        }
      }

      function cronChange(newCronValue: string) {
        cronValue.value = newCronValue;
      }

      return { registerModal, handleSubmit, cronValue, cronChange };
    },
  });
</script>
