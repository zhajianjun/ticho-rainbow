import { BasicColumn, FormSchema } from '@/components/Table';
import { h } from 'vue';
import { Tag } from 'ant-design-vue';
import { clientAll } from '@/api/intranet/client';
import { isNull } from '@/utils/is';
import { formatToDateTime } from '@/utils/dateUtil';
import { getDictByCode, getDictByCodeAndValue } from '@/store/modules/dict';
import { isUndefined } from 'lodash-es';
import dayjs from 'dayjs';

const commonStatus = 'commonStatus';
const protocolType = 'protocolType';

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
      title: '过期时间',
      dataIndex: 'expireAt',
      resizable: true,
      width: 100,
      customRender: ({ record }) => {
        if (record.expireAt === undefined || isNull(record.expireAt)) {
          return record.expireAt;
        }
        const isEffect = formatToDateTime(new Date()) < formatToDateTime(record.expireAt);
        const color = isEffect ? '#108ee9' : '#f50';
        return h(Tag, { color: color }, () => record.expireAt);
      },
    },
    {
      title: '协议类型',
      dataIndex: 'type',
      resizable: true,
      width: 50,
      customRender({ text }) {
        const dict = getDictByCodeAndValue(protocolType, text);
        if (text === undefined || isNull(text) || isNull(dict)) {
          return text;
        }
        return h(Tag, { color: dict.color }, () => dict.label);
      },
    },
    {
      title: '状态',
      dataIndex: 'status',
      resizable: true,
      width: 50,
    },
    {
      title: '客户端通道',
      dataIndex: 'clientChannelStatus',
      resizable: true,
      width: 50,
      customRender: ({ record }) => {
        const isActive = ~~record.clientChannelStatus === 1;
        const color = isActive ? '#108ee9' : '#f50';
        const text = isActive ? '已激活' : '未激活';
        return h(Tag, { color: color }, () => text);
      },
    },
    {
      title: '应用通道',
      dataIndex: 'appChannelStatus',
      resizable: true,
      width: 50,
      customRender: ({ record }) => {
        const isActive = ~~record.appChannelStatus === 1;
        const color = isActive ? '#108ee9' : '#f50';
        const text = isActive ? '已激活' : '未激活';
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
      colProps: { span: 8 },
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
      colProps: { span: 8 },
      componentProps: {
        placeholder: '请输入主机端口',
      },
    },
    {
      field: `endpoint`,
      label: `客户端地址`,
      component: 'Input',
      colProps: { span: 8 },
      componentProps: {
        placeholder: '请输入客户端地址',
      },
    },
    {
      field: `status`,
      label: `状态`,
      component: 'Select',
      colProps: { span: 8 },
      componentProps: {
        options: getDictByCode(commonStatus),
        placeholder: '请选择状态',
      },
    },
    {
      field: `domain`,
      label: `域名`,
      component: 'Input',
      colProps: { span: 8 },
      componentProps: {
        placeholder: '请输入域名',
      },
    },
    {
      field: `type`,
      label: `协议类型`,
      component: 'Select',
      colProps: { span: 8 },
      componentProps: {
        options: getDictByCode(protocolType),
        placeholder: '请输入协议类型',
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
      field: `status`,
      component: 'Input',
      label: `状态`,
      ifShow: false,
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
            trigger: 'change',
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
        options: getDictByCode(protocolType),
        placeholder: '请输入协议类型',
      },
      colProps: {
        span: 24,
      },
      rules: [
        {
          trigger: 'change',
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
            trigger: 'change',
            required: values.type && values.type === 2,
            validator: (_, value) => {
              const domainIsNull = isNull(value) || isUndefined(value);
              if (domainIsNull || value === '') {
                if (values.type === 2) {
                  return Promise.reject('请输入域名');
                }
                return Promise.resolve();
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
      field: `expireAt`,
      label: `过期时间`,
      component: 'DatePicker',
      componentProps: {
        placeholder: '请输入过期时间',
        showTime: true,
        valueFormat: 'YYYY-MM-DD HH:mm:ss',
        disabledDate: (current) => {
          return current && current <= dayjs().subtract(1, 'days').endOf('day');
        },
      },
      rules: [
        {
          trigger: 'change',
          required: true,
          message: '请输入过期时间',
        },
      ],
      colProps: {
        span: 24,
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
