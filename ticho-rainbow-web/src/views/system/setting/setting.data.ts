import { BasicColumn, FormSchema } from '@/components/Table';

export function getTableColumns(): BasicColumn[] {
  return [
    {
      title: 'key',
      dataIndex: 'key',
      resizable: true,
      width: 80,
    },
    {
      title: 'value',
      dataIndex: 'value',
      resizable: true,
      width: 80,
    },
    {
      title: '排序',
      dataIndex: 'sort',
      resizable: true,
      width: 80,
    },
    {
      title: '备注信息',
      dataIndex: 'remark',
      resizable: true,
      width: 80,
    },
    {
      title: '创建人',
      dataIndex: 'createBy',
      resizable: true,
      width: 80,
    },
    {
      title: '创建时间',
      dataIndex: 'createTime',
      resizable: true,
      width: 80,
    },
    {
      title: '修改人',
      dataIndex: 'updateBy',
      resizable: true,
      width: 80,
    },
    {
      title: '修改时间',
      dataIndex: 'updateTime',
      resizable: true,
      width: 80,
    },
  ];
}

export function getSearchColumns(): FormSchema[] {
  return [
    {
      label: `key`,
      field: `key`,
      component: 'Input',
      colProps: { span: 8 },
      componentProps: {
        placeholder: '请输入key',
      },
    },
    {
      label: `value`,
      field: `value`,
      component: 'Input',
      colProps: { span: 8 },
      componentProps: {
        placeholder: '请输入value',
      },
    },
    {
      label: `备注信息`,
      field: `remark`,
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
      label: `主键标识`,
      field: `id`,
      component: 'Input',
      show: false,
    },
    {
      label: `key`,
      field: `key`,
      component: 'Input',
      colProps: { span: 24 },
      componentProps: {
        placeholder: '请输入key',
      },
    },
    {
      label: `value`,
      field: `value`,
      component: 'Input',
      colProps: { span: 24 },
      componentProps: {
        placeholder: '请输入value',
      },
    },
    {
      field: 'sort',
      label: '排序',
      component: 'InputNumber',
      defaultValue: 10,
      componentProps: {
        min: 0,
        step: 10,
        max: 65535,
        placeholder: '请输入排序',
      },
      required: true,
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
      label: `版本号`,
      field: `version`,
      component: 'Input',
      show: false,
    },
  ];
}
