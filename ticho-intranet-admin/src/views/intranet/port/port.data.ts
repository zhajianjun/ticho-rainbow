import { BasicColumn, FormSchema } from '@/components/Table';
import { h } from 'vue';
import { Tag } from 'ant-design-vue';
import { clientAll } from '@/api/intranet/client';
import { isNull } from '@/utils/is';

const protocolType = [
  { code: 0, msg: 'HTTP' },
  { code: 1, msg: 'HTTPS' },
  { code: 2, msg: 'SSH' },
  { code: 3, msg: 'TELNET' },
  { code: 4, msg: 'DATABASE' },
  { code: 5, msg: 'RDESKTOP' },
  { code: 6, msg: 'TCP' },
];

const protocolTypeMap = protocolType.reduce((acc, { code, msg }) => {
  acc[code] = msg.toLowerCase();
  return acc;
}, {});

export function getTableColumns(): BasicColumn[] {
  return [
    {
      title: '主机端口',
      dataIndex: 'port',
      resizable: true,
      width: 50,
    },
    {
      title: '客户端地址',
      dataIndex: 'endpoint',
      resizable: true,
      width: 120,
    },
    {
      title: '域名',
      dataIndex: 'domain',
      resizable: true,
      width: 80,
    },
    {
      title: '是否永久',
      dataIndex: 'forever',
      resizable: true,
      width: 50,
    },
    {
      title: '过期时间',
      dataIndex: 'expireAt',
      resizable: true,
      width: 100,
      customRender: ({ record }) => {
        if (record.forever === 1) {
          return '无限制';
        }
        return record.expireAt;
      },
    },
    {
      title: '协议类型',
      dataIndex: 'type',
      resizable: true,
      width: 50,
      customRender: ({ record }) => {
        if (record.type === undefined || isNull(record.type)) {
          return '';
        }
        if (record.type === 1) {
          return h(Tag, { color: 'red' }, () => protocolTypeMap[record.type]);
        }
        return h(Tag, { color: 'blue' }, () => protocolTypeMap[record.type]);
      },
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
      width: 100,
    },
  ];
}

export function getSearchColumns(): FormSchema[] {
  return [
    {
      field: `accessKey`,
      label: `客户端信息`,
      component: 'ApiSelect',
      colProps: {
        xl: 12,
        xxl: 4,
      },
      componentProps: {
        placeholder: '请选择客户端信息',
        api: clientAll,
        labelField: 'name',
        valueField: 'accessKey',
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
      field: `type`,
      label: `协议类型`,
      component: 'Select',
      colProps: {
        xl: 12,
        xxl: 4,
      },
      componentProps: {
        placeholder: '请输入协议类型',
        options: protocolType,
        fieldNames: {
          label: 'msg',
          value: 'code',
        },
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
      label: `客户端信息`,
      component: 'ApiSelect',
      required: true,
      componentProps: {
        placeholder: '请选择客户端信息',
        api: clientAll,
        labelField: 'name',
        valueField: 'accessKey',
      },
      colProps: {
        span: 24,
      },
    },
    {
      field: `port`,
      label: `主机端口`,
      component: 'InputNumber',
      required: true,
      componentProps: {
        placeholder: '请输入主机端口',
        min: 1,
        max: 65535,
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
      helpMessage: '客户端地址格式为[ip:port]，端口范围[1-65535]',
      dynamicRules: () => {
        return [
          {
            trigger: 'blur',
            required: true,
            validator: (_, value) => {
              if (!value) {
                return Promise.reject('请输入客户端地址');
              }
              const reg = new RegExp(
                '\\b(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?):([1-9]|[1-9][0-9]{1,3}|[1-5][0-9]{4}|6[0-5][0-5][0-3][0-5])\\b',
              );
              if (!value.match(reg)) {
                return Promise.reject('客户端地址格式不正确[ip:port]');
              }
              return Promise.resolve();
            },
          },
        ];
      },
    },
    {
      field: `type`,
      label: `协议类型`,
      component: 'Select',
      componentProps: {
        placeholder: '请选择协议类型',
        options: protocolType,
        fieldNames: {
          label: 'msg',
          value: 'code',
        },
      },
      colProps: {
        span: 24,
      },
      rules: [
        {
          trigger: 'blur',
          required: true,
          message: '请选择协议类型',
        },
      ],
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
      dynamicRules: ({ values }) => {
        return [
          {
            trigger: 'blur',
            required: values.type === 1,
            validator: (_, value) => {
              const isNullFlag = isNull(value);
              if (!isNull(values.type) && values.type !== 1 && isNullFlag) {
                return Promise.resolve();
              }
              if (isNullFlag) {
                return Promise.reject('请输入域名');
              }
              const reg = new RegExp('^([a-z0-9-]+\\.)+[a-z]{2,}(/\\S*)?$');
              if (!value.match(reg)) {
                return Promise.reject('域名格式不正确');
              }
              return Promise.resolve();
            },
          },
        ];
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
      field: `forever`,
      label: `是否永久`,
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
      field: `expireAt`,
      label: `过期时间`,
      component: 'DatePicker',
      componentProps: {
        placeholder: '请输入过期时间',
        showTime: true,
      },
      ifShow: ({ values }) => {
        return values.forever !== 1;
      },
      colProps: {
        span: 24,
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
