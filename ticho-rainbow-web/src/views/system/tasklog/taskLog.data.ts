import { BasicColumn, FormSchema } from '@/components/Table';
import { getDictByCode, getDictByCodeAndValue } from '@/store/modules/dict';
import { isNull } from '@/utils/is';
import { h } from 'vue';
import { Tag } from 'ant-design-vue';
import dayjs from 'dayjs';
import { taskList } from '@/api/system/task';

const yesOrNo = 'yesOrNo';
const planTask = 'planTask';
const taskLogStatus = 'taskLogStatus';
const task = await taskList();

export function getTableColumns(): BasicColumn[] {
  return [
    {
      title: '日志编号',
      dataIndex: 'id',
      resizable: true,
      width: 100,
      ifShow: false,
    },
    {
      title: '任务名称',
      dataIndex: 'taskId',
      resizable: true,
      width: 100,
      customRender: ({ text }) => {
        const taskData = task.find((item) => item.id === text);
        return taskData?.name ?? text;
      },
    },
    {
      title: '任务类型',
      dataIndex: 'content',
      resizable: true,
      width: 100,
      customRender({ text }) {
        const dict = getDictByCodeAndValue(planTask, text);
        if (text === undefined || isNull(text) || isNull(dict)) {
          return text;
        }
        return h(Tag, { color: dict.color }, () => dict.label);
      },
    },
    {
      title: '任务参数',
      dataIndex: 'param',
      resizable: true,
      width: 100,
    },
    {
      title: '执行时间',
      dataIndex: 'executeTime',
      resizable: true,
      width: 100,
    },
    {
      title: '执行开始时间',
      dataIndex: 'startTime',
      resizable: true,
      width: 100,
    },
    {
      title: '执行结束时间',
      dataIndex: 'endTime',
      resizable: true,
      width: 100,
    },
    {
      title: '执行间隔(毫秒)',
      dataIndex: 'consume',
      resizable: true,
      width: 100,
    },
    {
      title: 'mdc信息',
      dataIndex: 'mdc',
      resizable: true,
      width: 100,
      ifShow: false,
    },
    {
      title: '链路id',
      dataIndex: 'traceId',
      resizable: true,
      width: 100,
    },
    {
      title: '任务状态',
      dataIndex: 'status',
      resizable: true,
      width: 100,
      customRender({ text }) {
        const dict = getDictByCodeAndValue(taskLogStatus, text);
        if (text === undefined || isNull(text) || isNull(dict)) {
          return text;
        }
        return h(Tag, { color: dict.color }, () => dict.label);
      },
    },
    {
      title: '操作人',
      dataIndex: 'operateBy',
      resizable: true,
      width: 100,
    },
    {
      title: '是否异常',
      dataIndex: 'isErr',
      resizable: true,
      width: 100,
      customRender({ text }) {
        const dict = getDictByCodeAndValue(yesOrNo, text);
        if (text === undefined || isNull(text) || isNull(dict)) {
          return text;
        }
        return h(Tag, { color: dict.color }, () => dict.label);
      },
    },
  ];
}

export function getSearchColumns(): FormSchema[] {
  return [
    {
      field: `taskId`,
      label: `任务名称`,
      component: 'Select',
      colProps: { span: 8 },
      componentProps: {
        placeholder: '请选择任务',
        options: task.map((item) => {
          return {
            label: item.name,
            value: item.id,
          };
        }),
      },
    },
    {
      field: `content`,
      label: `任务类型`,
      colProps: { span: 8 },
      component: 'Select',
      componentProps: {
        placeholder: '请选择任务类型',
        options: getDictByCode(planTask, false),
      },
    },
    {
      field: `param`,
      label: `任务参数`,
      component: 'Input',
      colProps: { span: 8 },
      componentProps: {
        placeholder: '请输入任务参数',
      },
    },
    {
      field: `executeTime`,
      label: `执行时间`,
      component: 'RangePicker',
      colProps: { span: 8 },
      componentProps: {
        placeholder: ['开始日期', '结束日期'],
        style: { width: '100%' },
        showTime: true,
        format: 'YYYY-MM-DD HH:mm:ss',
      },
    },
    {
      field: `startTime`,
      label: `执行开始时间`,
      component: 'RangePicker',
      colProps: { span: 8 },
      defaultValue: [dayjs().startOf('date'), dayjs().endOf('date')],
      componentProps: {
        placeholder: ['开始日期', '结束日期'],
        style: { width: '100%' },
        showTime: true,
        format: 'YYYY-MM-DD HH:mm:ss',
      },
    },
    {
      field: `endTime`,
      label: `执行结束时间`,
      component: 'RangePicker',
      colProps: { span: 8 },
      componentProps: {
        placeholder: ['开始日期', '结束日期'],
        style: { width: '100%' },
        showTime: true,
        format: 'YYYY-MM-DD HH:mm:ss',
      },
    },
    {
      field: `consumeStart`,
      label: `请求间隔起始`,
      component: 'InputNumber',
      colProps: { span: 4 },
      componentProps: {
        min: 0,
        addonAfter: 'ms',
      },
    },
    {
      field: `consumeEnd`,
      label: `请求间隔结束`,
      component: 'InputNumber',
      colProps: { span: 4 },
      componentProps: {
        min: 0,
        addonAfter: 'ms',
      },
    },
    {
      field: `traceId`,
      label: `链路id`,
      component: 'Input',
      colProps: { span: 8 },
      componentProps: {
        placeholder: '请输入链路id',
      },
    },
    {
      field: `status`,
      label: `任务状态`,
      component: 'Select',
      componentProps: {
        options: getDictByCode(taskLogStatus),
        placeholder: '请选择任务状态',
      },
      colProps: { span: 8 },
    },
    {
      field: `operateBy`,
      label: `操作人`,
      component: 'Input',
      colProps: { span: 8 },
      componentProps: {
        placeholder: '请输入操作人',
      },
    },
    {
      field: `isErr`,
      label: `是否异常`,
      component: 'Select',
      colProps: { span: 8 },
      componentProps: {
        options: getDictByCode(yesOrNo),
      },
    },
    {
      field: `errMessage`,
      label: `异常信息`,
      component: 'Input',
      colProps: { span: 8 },
      componentProps: {
        placeholder: '请输入异常信息',
      },
    },
  ];
}
