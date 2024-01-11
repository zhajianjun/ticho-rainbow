import { BasicColumn, FormSchema } from '@/components/Table';
import { h } from 'vue';
import { Tag } from 'ant-design-vue';

export function getTableColumns(): BasicColumn[] {
  return [
    {
      title: '主键标识',
      dataIndex: 'id',
      resizable: true,
      width: 100,
      ifShow: false,
    },
    {
      title: '客户端秘钥',
      dataIndex: 'accessKey',
      resizable: true,
      width: 120,
    },
    {
      title: '客户端名称',
      dataIndex: 'name',
      resizable: true,
      width: 80,
    },
    {
      title: '是否开启',
      dataIndex: 'enabled',
      resizable: true,
      width: 50,
    },
    {
      title: '通道状态',
      dataIndex: 'channelStatus',
      resizable: true,
      width: 50,
      customRender: ({ record }) => {
        const enabled = ~~record.channelStatus === 1;
        const color = enabled ? '#108ee9' : '#f50';
        const text = enabled ? '激活' : '未激活';
        return h(Tag, { color: color }, () => text);
      },
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
      width: 200,
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
      component: 'Switch',
      defaultValue: 0,
      componentProps: {
        checkedChildren: '开启',
        unCheckedChildren: '关闭',
        checkedValue: 1,
        unCheckedValue: 0,
      },
    },
    {
      field: `sort`,
      label: `排序`,
      component: 'InputNumber',
      componentProps: {
        min: 0,
        max: 10000,
        defaultValue: 10,
        step: 10,
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
