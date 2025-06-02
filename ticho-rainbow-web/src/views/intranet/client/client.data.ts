import { BasicColumn, FormSchema } from '@/components/Table';
import { h } from 'vue';
import { Tag } from 'ant-design-vue';
import { getDictByCode, getDictByCodeAndValue } from '@/store/modules/dict';
import { isNull } from '@/utils/is';
import { formatToDateTime } from '@/utils/dateUtil';
import dayjs from 'dayjs';

const commonStatus = 'commonStatus';
const channelStatus = 'channelStatus';

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
      title: '状态',
      dataIndex: 'status',
      resizable: true,
      width: 50,
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
      title: '通道状态',
      dataIndex: 'channelStatus',
      resizable: true,
      width: 50,
      customRender({ text }) {
        const dict = getDictByCodeAndValue(channelStatus, text);
        if (text === undefined || isNull(text) || isNull(dict)) {
          return text;
        }
        return h(Tag, { color: dict.color }, () => dict.label);
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
      colProps: { span: 8 },
      componentProps: {
        placeholder: '请输入客户端秘钥',
      },
    },
    {
      field: `name`,
      label: `客户端名称`,
      component: 'Input',
      colProps: { span: 8 },
      componentProps: {
        placeholder: '请输入客户端名称',
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
      label: `主键标识`,
      component: 'Input',
      show: false,
    },
    {
      field: `name`,
      label: `客户端名称`,
      component: 'Input',
      required: true,
      componentProps: {
        placeholder: '请输入客户端名称',
      },
      colProps: {
        span: 24,
      },
    },
    {
      field: `expireAt`,
      label: `过期时间`,
      component: 'DatePicker',
      required: true,
      componentProps: {
        placeholder: '请输入过期时间',
        showTime: true,
        valueFormat: 'YYYY-MM-DD HH:mm:ss',
        disabledDate: (current) => {
          return current && current <= dayjs().subtract(1, 'days').endOf('day');
        },
      },
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
      required: true,
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
