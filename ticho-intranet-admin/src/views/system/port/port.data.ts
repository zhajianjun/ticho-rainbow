import { BasicColumn, FormSchema } from '@/components/Table';

export function getTableColumns(): BasicColumn[] {
  return [
    {
      title: '客户端秘钥',
      dataIndex: 'accessKey',
      resizable: true,
      width: 100,
    },
    {
      title: '主机端口',
      dataIndex: 'port',
      resizable: true,
      width: 100,
    },
    {
      title: '客户端地址',
      dataIndex: 'endpoint',
      resizable: true,
      width: 100,
    },
    {
      title: '域名',
      dataIndex: 'domain',
      resizable: true,
      width: 100,
    },
    {
      title: '是否开启',
      dataIndex: 'enabled',
      resizable: true,
      width: 100,
    },
    {
      title: '是否永久',
      dataIndex: 'forever',
      resizable: true,
      width: 100,
    },
    {
      title: '过期时间',
      dataIndex: 'expireAt',
      resizable: true,
      width: 100,
    },
    {
      title: '协议类型',
      dataIndex: 'type',
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
      title: '备注信息',
      dataIndex: 'remark',
      resizable: true,
      width: 100,
    },
    {
      title: '乐观锁;控制版本更改',
      dataIndex: 'version',
      resizable: true,
      width: 100,
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
      field: `port`,
      label: `主机端口`,
      component: 'Input',
      colProps: {
        xl: 12,
        xxl: 4,
      },
      componentProps: {
        placeholder: '请输入主机端口',
      },
    },
    {
      field: `endpoint`,
      label: `客户端地址`,
      component: 'Input',
      colProps: {
        xl: 12,
        xxl: 4,
      },
      componentProps: {
        placeholder: '请输入客户端地址',
      },
    },
    {
      field: `domain`,
      label: `域名`,
      component: 'Input',
      colProps: {
        xl: 12,
        xxl: 4,
      },
      componentProps: {
        placeholder: '请输入域名',
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
      field: `forever`,
      label: `是否永久`,
      component: 'Input',
      colProps: {
        xl: 12,
        xxl: 4,
      },
      componentProps: {
        placeholder: '请输入是否永久',
      },
    },
    {
      field: `expireAt`,
      label: `过期时间`,
      component: 'Input',
      colProps: {
        xl: 12,
        xxl: 4,
      },
      componentProps: {
        placeholder: '请输入过期时间',
      },
    },
    {
      field: `type`,
      label: `协议类型`,
      component: 'Input',
      colProps: {
        xl: 12,
        xxl: 4,
      },
      componentProps: {
        placeholder: '请输入协议类型',
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
      component: 'Input',
      label: `主键标识`,
      ifShow: false,
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
      field: `port`,
      label: `主机端口`,
      component: 'Input',
      componentProps: {
        placeholder: '请输入主机端口',
      },
      colProps: {
        span: 24,
      },
    },
    {
      field: `endpoint`,
      label: `客户端地址`,
      component: 'Input',
      componentProps: {
        placeholder: '请输入客户端地址',
      },
      colProps: {
        span: 24,
      },
    },
    {
      field: `domain`,
      label: `域名`,
      component: 'Input',
      componentProps: {
        placeholder: '请输入域名',
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
      field: `forever`,
      label: `是否永久`,
      component: 'Input',
      componentProps: {
        placeholder: '请输入是否永久',
      },
      colProps: {
        span: 24,
      },
    },
    {
      field: `expireAt`,
      label: `过期时间`,
      component: 'Input',
      componentProps: {
        placeholder: '请输入过期时间',
      },
      colProps: {
        span: 24,
      },
    },
    {
      field: `type`,
      label: `协议类型`,
      component: 'Input',
      componentProps: {
        placeholder: '请输入协议类型',
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
