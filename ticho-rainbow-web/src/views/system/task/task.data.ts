import { BasicColumn, FormSchema } from '@/components/Table';
import { getDictByCode, getDictByCodeAndValue } from '@/store/modules/dict';
import dayjs from 'dayjs';

const commonStatus = 'commonStatus';

export function getTableColumns(): BasicColumn[] {
  return [
    {
      title: '任务ID',
      dataIndex: 'id',
      resizable: true,
      width: 100,
      ifShow: false,
    },
    {
      title: '任务名称',
      dataIndex: 'name',
      resizable: true,
      width: 100,
    },
    {
      title: '任务内容',
      dataIndex: 'content',
      resizable: true,
      width: 100,
    },
    {
      title: '任务参数',
      dataIndex: 'param',
      resizable: true,
      width: 100,
    },
    {
      title: 'Cron表达式',
      dataIndex: 'cronExpression',
      resizable: true,
      width: 100,
    },
    {
      title: '备注信息',
      dataIndex: 'remark',
      resizable: true,
      width: 100,
    },
    {
      title: '任务状态',
      dataIndex: 'status',
      resizable: true,
      width: 100,
      customRender({ text }) {
        return getDictByCodeAndValue(commonStatus, text);
      },
    },
    {
      title: '创建人',
      dataIndex: 'createBy',
      resizable: true,
      width: 100,
    },
    {
      title: '创建时间',
      dataIndex: 'createTime',
      resizable: true,
      width: 100,
    },
    {
      title: '更新人',
      dataIndex: 'updateBy',
      resizable: true,
      width: 100,
    },
    {
      title: '更新时间',
      dataIndex: 'updateTime',
      resizable: true,
      width: 100,
    },
  ];
}

export function getSearchColumns(): FormSchema[] {
  return [
    {
      field: `name`,
      label: `任务名称`,
      component: 'Input',
      colProps: { span: 8 },
      componentProps: {
        placeholder: '请输入任务名称',
      },
    },
    {
      field: `content`,
      label: `任务内容`,
      component: 'Input',
      colProps: { span: 8 },
      componentProps: {
        placeholder: '请输入任务内容',
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
      field: `remark`,
      label: `备注信息`,
      component: 'Input',
      colProps: { span: 8 },
      componentProps: {
        placeholder: '请输入备注信息',
      },
    },
    {
      field: 'status',
      label: '任务状态',
      component: 'Select',
      componentProps: {
        options: getDictByCode(commonStatus),
        placeholder: '请选择任务状态',
      },
      colProps: { span: 8 },
    },
    {
      field: `createTime`,
      label: `创建时间`,
      component: 'RangePicker',
      colProps: { span: 8 },
      defaultValue: [dayjs().startOf('date'), dayjs()],
      // fieldMapToTime: [['createTime', ['startTime', 'endTime'], 'YYYY-MM-DD HH:mm:ss']],
      componentProps: {
        placeholder: ['开始日期', '结束日期'],
        style: { width: '100%' },
        inputReadOnly: true,
        showTime: true,
        format: 'YYYY-MM-DD HH:mm:ss',
        presets: [
          { label: '近半小时', value: [dayjs().add(-0.5, 'hour'), dayjs()] },
          { label: '近一小时', value: [dayjs().add(-1, 'hour'), dayjs()] },
          { label: '近一天', value: [dayjs().add(-1, 'day'), dayjs()] },
          { label: '近一周', value: [dayjs().add(-1, 'week'), dayjs()] },
          { label: '近一月', value: [dayjs().add(-1, 'month'), dayjs()] },
          { label: '近一年', value: [dayjs().add(-1, 'year'), dayjs()] },
        ],
      },
    },
  ];
}

export function getModalFormColumns(): FormSchema[] {
  return [
    {
      field: `id`,
      label: `任务ID`,
      component: 'Input',
      componentProps: {
        placeholder: '请输入任务ID',
      },
      ifShow: false,
      colProps: {
        span: 24,
      },
    },
    {
      field: `name`,
      label: `任务名称`,
      component: 'Input',
      componentProps: {
        placeholder: '请输入任务名称',
      },
      colProps: {
        span: 24,
      },
    },
    {
      field: `content`,
      label: `任务内容`,
      component: 'Input',
      componentProps: {
        placeholder: '请输入任务内容',
      },
      colProps: {
        span: 24,
      },
    },
    {
      field: `cronExpression`,
      label: `Cron表达式`,
      slot: 'cronExpressionSlot',
      componentProps: {
        placeholder: '请输入Cron表达式',
      },
      colProps: {
        span: 24,
      },
    },
    {
      field: `status`,
      label: `任务状态`,
      component: 'RadioButtonGroup',
      componentProps: {
        options: getDictByCode(commonStatus),
      },
      colProps: {
        span: 24,
      },
    },
    {
      field: `param`,
      label: `任务参数`,
      component: 'InputTextArea',
      componentProps: {
        placeholder: '请输入任务参数',
        maxlength: 120,
        showCount: true,
        rows: 4,
      },
      colProps: {
        span: 24,
      },
    },
    {
      field: `remark`,
      label: `备注信息`,
      component: 'InputTextArea',
      defaultValue: '',
      componentProps: {
        placeholder: '请输入备注信息',
        maxlength: 120,
        showCount: true,
        rows: 4,
      },
      colProps: {
        span: 24,
      },
    },
  ];
}
