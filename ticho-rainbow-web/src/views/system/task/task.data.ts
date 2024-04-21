import { BasicColumn, FormSchema } from '@/components/Table';
import { getDictByCode, getDictByCodeAndValue } from '@/store/modules/dict';
import { h } from 'vue';
import { Tag } from 'ant-design-vue';

const commonStatus = 'commonStatus';
const planTask = 'planTask';

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
      title: '任务类型',
      dataIndex: 'content',
      resizable: true,
      width: 100,
      customRender: ({ text }) => {
        return h(Tag, { color: 'green' }, () => getDictByCodeAndValue(planTask, text));
      },
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
      customRender: ({ text }) => {
        const isNormal = ~~text === 1;
        const color = isNormal ? 'green' : 'red';
        return h(Tag, { color: color }, () => getDictByCodeAndValue(commonStatus, text));
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
      label: `任务类型`,
      component: 'Select',
      colProps: { span: 8 },
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
      label: `任务类型`,
      component: 'Select',
      componentProps: {
        placeholder: '请选择任务类型',
        options: getDictByCode(planTask, false),
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

export function getRunOnceModalFormColumns(): FormSchema[] {
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
  ];
}
