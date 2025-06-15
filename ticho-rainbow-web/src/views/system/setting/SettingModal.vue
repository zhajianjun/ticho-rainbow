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
  import { getModalFormColumns } from './setting.data';
  import { modifySetting, saveSetting } from '@/api/system/setting';
  import { SettingModifyComman, SettingSaveCommand } from '@/api/system/model/settingModel';

  export default defineComponent({
    name: 'PortModal',
    components: { BasicModal, BasicForm },
    emits: ['success', 'register'],
    setup(_, { emit }) {
      const modalType = ref(1);
      const [registerForm, { setFieldsValue, resetFields, validate }] = useForm({
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
      });

      const getTitle = computed(() => (unref(modalType) !== 2 ? '新增配置信息' : '编辑配置信息'));

      async function handleSubmit() {
        try {
          setModalProps({ confirmLoading: true });
          if (unref(modalType) === 2) {
            const values = (await validate()) as SettingModifyComman;
            await modifySetting(values);
          } else {
            const values = (await validate()) as SettingSaveCommand;
            await saveSetting(values);
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
