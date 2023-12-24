import { BasicColumn, FormSchema } from '@/components/Table';

export function getTableColumns(): BasicColumn[] {
  return [
    {
      title: '主键标识',
      dataIndex: 'id',
      resizable: true,
      width: 150,
      ifShow: false,
    },
    {
      title: '客户端秘钥',
      dataIndex: 'accessKey',
      resizable: true,
      width: 150,
    },
    {
      title: '客户端名称',
      dataIndex: 'name',
      resizable: true,
      width: 150,
    },
    {
      title: '是否开启',
      dataIndex: 'enabled',
      resizable: true,
      width: 150,
    },
    {
      title: '排序',
      dataIndex: 'sort',
      resizable: true,
      width: 150,
    },
    {
      title: '备注信息',
      dataIndex: 'remark',
      resizable: true,
      width: 150,
    },
    {
      title: '创建人',
      dataIndex: 'createBy',
      resizable: true,
      width: 150,
    },
    {
      title: '创建时间',
      dataIndex: 'createTime',
      resizable: true,
      width: 150,
    },
    {
      title: '更新人',
      dataIndex: 'updateBy',
      resizable: true,
      width: 150,
    },
    {
      title: '更新时间',
      dataIndex: 'updateTime',
      resizable: true,
      width: 150,
    },
  ];
}

export function getSearchColumns(): FormSchema[] {
  return [
    {
      field: `accessKey`,
      label: `客户端秘钥`,
      component: 'Input',
      colProps: {
        xl: 12,
        xxl: 4,
      },
      componentProps: {
        placeholder: '请输入客户端秘钥',
      },
    },
    {
      field: `name`,
      label: `客户端名称`,
      component: 'Input',
      colProps: {
        xl: 12,
        xxl: 4,
      },
      componentProps: {
        placeholder: '请输入客户端名称',
      },
    },
    {
      field: `enabled`,
      label: `是否开启`,
      component: 'Input',
      colProps: {
        xl: 12,
        xxl: 4,
      },
      componentProps: {
        placeholder: '请输入是否开启',
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
      label: `主键标识`,
      component: 'Input',
      show: false,
    },
    {
      field: `accessKey`,
      label: `客户端秘钥`,
      component: 'Input',
      componentProps: {
        placeholder: '请输入客户端秘钥',
      },
      colProps: {
        span: 24,
      },
    },
    {
      field: `name`,
      label: `客户端名称`,
      component: 'Input',
      componentProps: {
        placeholder: '请输入客户端名称',
      },
      colProps: {
        span: 24,
      },
    },
    {
      field: `enabled`,
      label: `是否开启`,
      component: 'Input',
      componentProps: {
        placeholder: '请输入是否开启',
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
