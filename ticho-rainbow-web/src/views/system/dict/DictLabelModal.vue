<template>
  <BasicModal
    v-bind="$attrs"
    @register="registerModal"
    :title="getTitle"
    :maskClosable="false"
    @ok="handleSubmit"
  >
    <BasicForm @register="registerForm">
      <template #colorSlot="{ model, field }">
        <a-input-group compact>
          <a-input v-model:value="model[field]" style="width: calc(100% - 32px)" />
          <a-input
            type="color"
            v-model:value="model[field]"
            style="width: 32px; margin: auto; padding: 0"
          />
        </a-input-group>
      </template>
    </BasicForm>
  </BasicModal>
</template>
<script lang="ts">
  import { computed, defineComponent, ref, unref } from 'vue';
  import { BasicModal, useModalInner } from '@/components/Modal';
  import { BasicForm, useForm } from '@/components/Form/index';
  import { getModalFormColumns } from './dictLabel.data';
  import { modifyDictLabel, saveDictLabel } from '@/api/system/dictLabel';
  import { DictLabelModifyCommand, DictLabelSaveCommand } from '@/api/system/model/dictLabelModel';

  export default defineComponent({
    name: 'DictLabelModal',
    components: { BasicModal, BasicForm },
    emits: ['success', 'register'],
    setup(_, { emit }) {
      const isUpdate = ref(true);
      const isSys = ref<boolean>(false);
      const [registerForm, { setFieldsValue, resetFields, validate, updateSchema }] = useForm({
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
        isSys.value = !!data?.isSys;
        if (unref(isUpdate)) {
          await updateSchema([
            {
              field: 'code',
              componentProps: { disabled: true },
            },
            {
              field: 'label',
              componentProps: { disabled: unref(isSys) },
            },
            {
              field: 'value',
              componentProps: { disabled: unref(isSys) },
            },
          ]);
          await setFieldsValue({
            ...data.record,
          });
        }
      });

      const getTitle = computed(() => (!unref(isUpdate) ? '新增字典标签' : '编辑字典标签'));

      async function handleSubmit() {
        try {
          const values = await validate();
          setModalProps({ confirmLoading: true });
          if (unref(isUpdate)) {
            const saves = values as DictLabelModifyCommand;
            await modifyDictLabel(saves);
          } else {
            const modifys = values as DictLabelSaveCommand;
            await saveDictLabel(modifys);
          }
          closeModal();
          emit('success');
        } finally {
          setModalProps({ confirmLoading: false });
        }
      }

      return { registerModal, registerForm, getTitle, handleSubmit };
    },
  });
</script>
