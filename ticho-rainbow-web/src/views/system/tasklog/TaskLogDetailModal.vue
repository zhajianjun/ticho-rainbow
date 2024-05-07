<template>
  <BasicModal
    v-bind="$attrs"
    @register="registerModal"
    title="日志详情"
    :maskClosable="false"
    @ok="handleSubmit"
  >
    <Description @register="registerDescription" style="margin-left: 20px" />
  </BasicModal>
</template>
<script lang="ts">
  import { defineComponent, reactive } from 'vue';
  import { BasicModal, useModalInner } from '@/components/Modal';
  import { TaskLogDTO } from '@/api/system/model/taskLogModel';
  import { Description, useDescription } from '@/components/Description';
  import { getDescColumns } from './taskLog.data';

  export default defineComponent({
    name: 'TaskModal',
    components: { BasicModal, Description },
    emits: ['success', 'register'],
    setup(_) {
      const dataRef = reactive<TaskLogDTO>({
        consume: 0,
        content: '',
        createTime: '',
        endTime: '',
        errMessage: '',
        executeTime: '',
        id: 0,
        isErr: 0,
        mdc: '',
        operateBy: '',
        param: '',
        startTime: '',
        status: 0,
        taskId: 0,
        traceId: '',
      });
      const [registerDescription] = useDescription({
        data: dataRef,
        schema: getDescColumns(),
        column: 3,
        bordered: false,
      });
      const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
        setModalProps({
          confirmLoading: false,
          width: 1000,
          showOkBtn: false,
          cancelText: '关闭',
        });
        Object.assign(dataRef, data);
      });

      async function handleSubmit() {
        try {
          setModalProps({ confirmLoading: true });
          closeModal();
        } finally {
          setModalProps({ confirmLoading: false });
        }
      }

      return { registerModal, handleSubmit, dataRef, registerDescription };
    },
  });
</script>
