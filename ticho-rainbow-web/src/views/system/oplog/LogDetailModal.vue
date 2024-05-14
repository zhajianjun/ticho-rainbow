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
  import { OpLogDTO } from '@/api/system/model/opLogModel';
  import { Description, useDescription } from '@/components/Description';
  import { getDescColumns } from './opLog.data';

  export default defineComponent({
    name: 'TaskModal',
    components: { BasicModal, Description },
    emits: ['success', 'register'],
    setup(_) {
      const dataRef = reactive<OpLogDTO>({
        consume: 0,
        createTime: '',
        endTime: '',
        errMessage: '',
        id: '',
        ip: '',
        isErr: 0,
        mdc: '',
        name: '',
        operateBy: '',
        position: '',
        reqBody: '',
        reqParams: '',
        resBody: '',
        resStatus: 0,
        startTime: '',
        traceId: '',
        type: '',
        url: '',
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
