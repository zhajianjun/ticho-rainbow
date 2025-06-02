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
  import { getModalFormColumns } from './client.data';
  import { modifyClient, saveClient } from '@/api/intranet/client';
  import { ClientModifyCommand, ClientSaveCommand } from '@/api/intranet/model/clientModel';

  export default defineComponent({
    name: 'ClientModal',
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
            field: 'accessKey',
            componentProps: {
              disabled: unref(isUpdate),
            },
          },
        ]);
      });

      const getTitle = computed(() => (!unref(isUpdate) ? '新增客户端信息' : '编辑客户端信息'));

      async function handleSubmit() {
        try {
          setModalProps({ confirmLoading: true });
          if (unref(isUpdate)) {
            const values = (await validate()) as ClientModifyCommand;
            await modifyClient(values);
          } else {
            const values = (await validate()) as ClientSaveCommand;
            await saveClient(values);
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
