import { BasicColumn, FormSchema } from '@/components/Table';

export function getTableColumns(): BasicColumn[] {
  return [
    {
      title: '主键编号',
      dataIndex: 'id',
      resizable: true,
      width: 100,
      ifShow: false,
    },
    {
      title: '字典名称',
      dataIndex: 'name',
      resizable: true,
      width: 100,
    },
    {
      title: '字典编码',
      dataIndex: 'code',
      resizable: true,
      width: 100,
    },
    {
      title: '系统字典',
      dataIndex: 'isSys',
      resizable: true,
      width: 100,
    },
    {
      title: '状态',
      dataIndex: 'status',
      resizable: true,
      width: 100,
    },
    {
      title: '备注信息',
      dataIndex: 'remark',
      resizable: true,
      width: 100,
    },
  ];
}

export function getSearchColumns(): FormSchema[] {
  return [
    {
      field: `code`,
      label: `字典编码`,
      component: 'Input',
      colProps: {
        xl: 12,
        xxl: 8,
      },
      componentProps: {
        placeholder: '请输入字典编码',
      },
    },
    {
      field: `name`,
      label: `字典名称`,
      component: 'Input',
      colProps: {
        xl: 12,
        xxl: 8,
      },
      componentProps: {
        placeholder: '请输入字典名称',
      },
    },
    {
      field: `isSys`,
      label: `是否系统字典`,
      component: 'Input',
      colProps: {
        xl: 12,
        xxl: 8,
      },
      componentProps: {
        placeholder: '请输入是否系统字典',
      },
    },
    {
      field: `status`,
      label: `状态`,
      component: 'Input',
      colProps: {
        xl: 12,
        xxl: 8,
      },
      componentProps: {
        placeholder: '请输入状态',
      },
    },
    {
      field: `remark`,
      label: `备注信息`,
      component: 'Input',
      colProps: {
        xl: 12,
        xxl: 8,
      },
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
      label: `主键编号`,
      component: 'Input',
      componentProps: {
        placeholder: '请输入主键编号',
      },
      colProps: {
        span: 24,
      },
      ifShow: false,
    },
    {
      field: `code`,
      label: `字典编码`,
      component: 'Input',
      componentProps: {
        placeholder: '请输入字典编码',
      },
      colProps: {
        span: 24,
      },
    },
    {
      field: `name`,
      label: `字典名称`,
      component: 'Input',
      componentProps: {
        placeholder: '请输入字典名称',
      },
      colProps: {
        span: 24,
      },
    },
    {
      field: `isSys`,
      label: `是否系统字典`,
      component: 'Input',
      componentProps: {
        placeholder: '请输入是否系统字典',
      },
      colProps: {
        span: 24,
      },
    },
    {
      field: `status`,
      label: `状态`,
      component: 'Input',
      componentProps: {
        placeholder: '请输入状态',
      },
      colProps: {
        span: 24,
      },
    },
    {
      field: `remark`,
      label: `备注信息`,
      component: 'InputTextArea',
      componentProps: {
        defaultValue: '',
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
