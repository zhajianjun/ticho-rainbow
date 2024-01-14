import { BasicColumn, FormSchema } from '@/components/Table';

export function getTableColumns(): BasicColumn[] {
  return [
    {
      title: '主键编号',
      dataIndex: 'id',
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
      title: '字典标签',
      dataIndex: 'label',
      resizable: true,
      width: 100,
    },
    {
      title: '字典值',
      dataIndex: 'value',
      resizable: true,
      width: 100,
    },
    {
      title: '排序',
      dataIndex: 'sort',
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
      field: `id`,
      label: `主键编号`,
      component: 'Input',
      colProps: {
        xl: 12,
        xxl: 4,
      },
      componentProps: {
        placeholder: '请输入主键编号',
      },
    },
    {
      field: `code`,
      label: `字典编码`,
      component: 'Input',
      colProps: {
        xl: 12,
        xxl: 4,
      },
      componentProps: {
        placeholder: '请输入字典编码',
      },
    },
    {
      field: `label`,
      label: `字典标签`,
      component: 'Input',
      colProps: {
        xl: 12,
        xxl: 4,
      },
      componentProps: {
        placeholder: '请输入字典标签',
      },
    },
    {
      field: `value`,
      label: `字典值`,
      component: 'Input',
      colProps: {
        xl: 12,
        xxl: 4,
      },
      componentProps: {
        placeholder: '请输入字典值',
      },
    },
    {
      field: `sort`,
      label: `排序`,
      component: 'Input',
      colProps: {
        xl: 12,
        xxl: 4,
      },
      componentProps: {
        placeholder: '请输入排序',
      },
    },
    {
      field: `status`,
      label: `状态`,
      component: 'Input',
      colProps: {
        xl: 12,
        xxl: 4,
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
        xxl: 4,
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
      field: `label`,
      label: `字典标签`,
      component: 'Input',
      componentProps: {
        placeholder: '请输入字典标签',
      },
      colProps: {
        span: 24,
      },
    },
    {
      field: `value`,
      label: `字典值`,
      component: 'Input',
      componentProps: {
        placeholder: '请输入字典值',
      },
      colProps: {
        span: 24,
      },
    },
    {
      field: `sort`,
      label: `排序`,
      component: 'Input',
      componentProps: {
        placeholder: '请输入排序',
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
      component: 'Input',
      componentProps: {
        placeholder: '请输入备注信息',
      },
      colProps: {
        span: 24,
      },
    },
  ];
}
