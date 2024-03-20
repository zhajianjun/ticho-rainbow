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
  import { userFormSchema } from './user.data';
  import { modifyUser, saveUser } from '@/api/system/user';
  import {UserDTO} from '@/api/system/model/userModel';
  import {useMessage} from '@/hooks/web/useMessage';

  export default defineComponent({
    name: 'UserModal',
    components: { BasicModal, BasicForm },
    emits: ['success', 'register'],
    setup(_, { emit }) {
      const isUpdate = ref(true);
      const rowId = ref('');
      const checkedRoleKeys = ref<string[]>([]);
      const [registerForm, { setFieldsValue, updateSchema, resetFields, validate }] = useForm({
        labelWidth: 100,
        baseColProps: { span: 24 },
        schemas: userFormSchema,
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
          rowId.value = data.record.id;
          await setFieldsValue({
            ...data.record,
          });
        }
        // const treeData = await getDeptList();
        const treeData = [];
        await updateSchema([
          {
            field: 'password',
            show: !unref(isUpdate),
          },
          {
            field: 'dept',
            componentProps: { treeData },
          },
        ]);
      });

      const getTitle = computed(() => (!unref(isUpdate) ? '新增账号' : '编辑账号'));

      async function handleSubmit() {
        const { createMessage } = useMessage();
        try {
          const values = (await validate()) as UserDTO;
          setModalProps({ confirmLoading: true });
          if (unref(isUpdate)) {
            await modifyUser(values);
          } else {
            await saveUser(values);
          }
          closeModal();
          createMessage.success('操作成功');
          emit('success', { isUpdate: unref(isUpdate), values: { ...values, id: rowId.value } });
        } finally {
          setModalProps({ confirmLoading: false });
        }
      }

      return { registerModal, registerForm, getTitle, handleSubmit, checkedRoleKeys };
    },
  });
</script>
