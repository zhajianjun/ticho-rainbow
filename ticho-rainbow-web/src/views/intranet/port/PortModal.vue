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
  import { BasicForm, useForm } from '@/components/Form';
  import { getModalFormColumns } from './port.data';
  import { modifyPort, savePort } from '@/api/intranet/port';
  import { PortModifyfCommand, PortSaveCommand } from '@/api/intranet/model/portModel';

  export default defineComponent({
    name: 'PortModal',
    components: { BasicModal, BasicForm },
    emits: ['success', 'register'],
    setup(_, { emit }) {
      const modalType = ref(1);
      const [registerForm, { setFieldsValue, resetFields, validate, updateSchema }] = useForm({
        labelWidth: 120,
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
        modalType.value = data?.modalType;
        if (unref(modalType) !== 1) {
          await setFieldsValue({
            ...data.record,
          });
        }
        const disable = unref(modalType) == 2;
        await updateSchema([
          {
            field: 'accessKey',
            componentProps: {
              disabled: disable,
            },
          },
          {
            field: 'port',
            componentProps: {
              disabled: disable,
            },
          },
          {
            field: 'endpoint',
            componentProps: {
              disabled: disable,
            },
          },
          {
            field: 'port',
            componentProps: {
              disabled: disable,
            },
          },
        ]);
      });

      const getTitle = computed(() => (unref(modalType) !== 2 ? '新增端口信息' : '编辑端口信息'));

      async function handleSubmit() {
        try {
          setModalProps({ confirmLoading: true });
          if (unref(modalType) === 2) {
            const values = (await validate()) as PortModifyfCommand;
            await modifyPort(values);
          } else {
            const values = (await validate()) as PortSaveCommand;
            await savePort(values);
          }
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
