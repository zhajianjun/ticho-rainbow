<template>
  <BasicModal
    v-bind="$attrs"
    @register="registerModal"
    :title="getTitle"
    :maskClosable="false"
    @ok="handleSubmit"
  >
    <BasicForm @register="registerForm">
      <template #cronExpressionSlot="{ model, field }">
        <a-input-group compact>
          <a-input v-model:value="model[field]" style="width: calc(100% - 100px)" />
          <a-button @click="openCronModal" style="width: 100px">Cron生成</a-button>
        </a-input-group>
      </template>
    </BasicForm>
  </BasicModal>
  <TaskCron @register="registerCronModel" @success="handleCronSuccess" />
</template>
<script lang="ts">
  import { computed, defineComponent, ref, unref } from 'vue';
  import { BasicModal, useModal, useModalInner } from '@/components/Modal';
  import { BasicForm, useForm } from '@/components/Form/index';
  import { getModalFormColumns } from './task.data';
  import { modifyTask, saveTask } from '@/api/system/task';
  import { TaskModifyCommand, TaskSaveCommand } from '@/api/system/model/taskModel';
  import TaskCron from './TaskCron.vue';

  export default defineComponent({
    name: 'TaskModal',
    components: { BasicModal, BasicForm, TaskCron },
    emits: ['success', 'register'],
    setup(_, { emit }) {
      const isUpdate = ref(true);
      const [registerForm, { setFieldsValue, resetFields, validate, getFieldsValue }] = useForm({
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
      });

      const getTitle = computed(() => (!unref(isUpdate) ? '新增定时任务' : '编辑定时任务'));

      async function handleSubmit() {
        try {
          const values = await validate();
          setModalProps({ confirmLoading: true });
          if (unref(isUpdate)) {
            const saves = values as TaskModifyCommand;
            await modifyTask(saves);
          } else {
            const modifys = values as TaskSaveCommand;
            await saveTask(modifys);
          }
          closeModal();
          emit('success');
        } finally {
          setModalProps({ confirmLoading: false });
        }
      }

      const [registerCronModel, { openModal }] = useModal();

      function handleCronSuccess(cronValue: string) {
        setFieldsValue({
          cronExpression: cronValue,
        });
      }

      function openCronModal() {
        if (!unref(isUpdate)) {
          openModal(true, '* * * * * ? *', true);
          return;
        }
        let fieldsValue = getFieldsValue();
        openModal(true, fieldsValue.cronExpression, true);
      }

      return {
        registerModal,
        registerForm,
        getTitle,
        handleSubmit,
        registerCronModel,
        openCronModal,
        handleCronSuccess,
      };
    },
  });
</script>
