import { BasicColumn, FormSchema } from '@/components/Table';

export function getTableColumns(): BasicColumn[] {
  return [
    {
      title: 'key',
      dataIndex: 'key',
      resizable: true,
      width: 50,
    },
    {
      title: 'value',
      dataIndex: 'value',
      resizable: true,
      width: 120,
    },
    {
      title: '排序',
      dataIndex: 'sort',
      resizable: true,
      width: 50,
    },
    {
      title: '备注信息',
      dataIndex: 'remark',
      resizable: true,
      width: 100,
    },
    {
      title: '创建时间',
      dataIndex: 'createTime',
      resizable: true,
      width: 100,
    },
  ];
}

export function getSearchColumns(): FormSchema[] {
  return [
    {
      field: `key`,
      label: `key`,
      component: 'Input',
      colProps: { span: 8 },
      componentProps: {
        placeholder: '请输入key',
      },
    },
    {
      field: `value`,
      label: `value`,
      component: 'Input',
      colProps: { span: 8 },
      componentProps: {
        placeholder: '请输入value',
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
  ];
}

export function getModalFormColumns(): FormSchema[] {
  return [
    {
      field: `id`,
      component: 'Input',
      label: `主键标识`,
      ifShow: false,
    },
    {
      field: `key`,
      label: `key`,
      component: 'Input',
      colProps: { span: 24 },
      componentProps: {
        placeholder: '请输入key',
      },
    },
    {
      field: `value`,
      label: `value`,
      component: 'Input',
      colProps: { span: 24 },
      componentProps: {
        placeholder: '请输入value',
      },
    },
    {
      field: `sort`,
      label: `排序`,
      component: 'InputNumber',
      defaultValue: 10,
      componentProps: {
        min: 0,
        step: 10,
        max: 65535,
        placeholder: '请输入排序',
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
    {
      field: `version`,
      label: `版本号`,
      component: 'Input',
      show: false,
    },
  ];
}
