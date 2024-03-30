import { BasicColumn, FormSchema } from '@/components/Table';
import {getDictByCode, getDictByCodeAndValue} from '@/store/modules/dict';

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
      title: '执行目标名称',
      dataIndex: 'executeName',
      resizable: true,
      width: 100,
    },
    {
      title: '执行参数',
      dataIndex: 'param',
      resizable: true,
      width: 100,
    },
    {
      title: 'cron执行表达式',
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
      field: `executeName`,
      label: `执行目标名称`,
      component: 'Input',
      colProps: { span: 8 },
      componentProps: {
        placeholder: '请输入执行目标名称',
      },
    },
    {
      field: `param`,
      label: `执行参数`,
      component: 'Input',
      colProps: { span: 8 },
      componentProps: {
        placeholder: '请输入执行参数',
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
      field: `executeName`,
      label: `执行目标名称`,
      component: 'Input',
      componentProps: {
        placeholder: '请输入执行目标名称',
      },
      colProps: {
        span: 24,
      },
    },
    {
      field: `param`,
      label: `执行参数`,
      component: 'Input',
      componentProps: {
        placeholder: '请输入执行参数',
      },
      colProps: {
        span: 24,
      },
    },
    {
      field: `cronExpression`,
      label: `cron执行表达式`,
      component: 'Input',
      componentProps: {
        placeholder: '请输入cron执行表达式',
      },
      colProps: {
        span: 24,
      },
    },
    {
      field: `remark`,
      label: `备注信息`,
      component: 'Input',
      componentProps: {
        placeholder: '请输入备注信息',
      },
      colProps: {
        span: 24,
      },
    },
    {
      field: `status`,
      label: `任务状态`,
      component: 'RadioButtonGroup',
      defaultValue: 0,
      componentProps: {
        options: getDictByCode(commonStatus),
      },
      colProps: {
        span: 24,
      },
    },
  ];
}
