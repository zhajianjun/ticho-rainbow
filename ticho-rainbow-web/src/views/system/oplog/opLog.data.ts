// noinspection DuplicatedCode

import { BasicColumn, FormSchema } from '@/components/Table';
import dayjs from 'dayjs';
import { getDictByCode, getDictByCodeAndValue } from '@/store/modules/dict';
import { h } from 'vue';
import { Tag, Textarea } from 'ant-design-vue';
import { DescItem } from '@/components/Description';
import { isNull } from '@/utils/is';

const yesOrNo = 'yesOrNo';
const httpType = 'httpType';

export function getTableColumns(): BasicColumn[] {
  return [
    {
      title: '日志编号',
      dataIndex: 'id',
      resizable: true,
      width: 100,
      ifShow: false,
    },
    {
      title: '接口名称',
      dataIndex: 'name',
      resizable: true,
      width: 80,
    },
    {
      title: '请求类型',
      dataIndex: 'type',
      resizable: true,
      width: 40,
      customRender({ text }) {
        const dict = getDictByCodeAndValue(httpType, text);
        if (text === undefined || isNull(text) || isNull(dict)) {
          return text;
        }
        return h(Tag, { color: dict.color }, () => dict.label);
      },
    },
    {
      title: '接口地址',
      dataIndex: 'url',
      resizable: true,
      width: 100,
    },
    {
      title: '请求方法',
      dataIndex: 'position',
      resizable: true,
      width: 100,
      ifShow: false,
    },
    {
      title: '请求体',
      dataIndex: 'reqBody',
      resizable: true,
      width: 100,
      ifShow: false,
    },
    {
      title: '请求参数',
      dataIndex: 'reqParams',
      resizable: true,
      width: 100,
      ifShow: false,
    },
    {
      title: '请求头',
      dataIndex: 'reqHeaders',
      resizable: true,
      width: 100,
      ifShow: false,
    },
    {
      title: '响应体',
      dataIndex: 'resBody',
      resizable: true,
      width: 100,
      ifShow: false,
    },
    {
      title: '响应头',
      dataIndex: 'resHeaders',
      resizable: true,
      width: 100,
      ifShow: false,
    },
    {
      title: '链路ID',
      dataIndex: 'traceId',
      resizable: true,
      width: 100,
    },
    {
      title: '请求开始时间',
      dataIndex: 'startTime',
      resizable: true,
      width: 100,
    },
    {
      title: '请求结束时间',
      dataIndex: 'endTime',
      resizable: true,
      width: 100,
    },
    {
      title: '请求间隔',
      dataIndex: 'consume',
      resizable: true,
      width: 40,
      customRender: ({ text }) => {
        return text + 'ms';
      },
    },
    {
      title: '请求IP',
      dataIndex: 'ip',
      resizable: true,
      width: 100,
    },
    {
      title: '响应状态',
      dataIndex: 'resStatus',
      resizable: true,
      width: 40,
      customRender: ({ text }) => {
        const success = ~~text === 200;
        const color = success ? 'green' : 'red';
        return h(Tag, { color: color }, () => text);
      },
    },
    {
      title: '操作人',
      dataIndex: 'operateBy',
      resizable: true,
      width: 40,
    },
    {
      title: '是否异常',
      dataIndex: 'isErr',
      resizable: true,
      width: 40,
      customRender({ text }) {
        const dict = getDictByCodeAndValue(yesOrNo, text);
        if (text === undefined || isNull(text) || isNull(dict)) {
          return text;
        }
        return h(Tag, { color: dict.color }, () => dict.label);
      },
    },
  ];
}

export function getSearchColumns(): FormSchema[] {
  return [
    {
      field: `id`,
      label: `主键编号`,
      component: 'Input',
      colProps: { span: 8 },
      componentProps: {
        placeholder: '请输入主键编号',
      },
      ifShow: false,
    },
    {
      field: `name`,
      label: `接口名称`,
      component: 'Input',
      colProps: { span: 8 },
      componentProps: {
        placeholder: '请输入名称',
      },
    },
    {
      field: `url`,
      label: `请求地址`,
      component: 'Input',
      colProps: { span: 8 },
      componentProps: {
        placeholder: '请输入请求地址',
      },
    },
    {
      field: `type`,
      label: `请求类型`,
      component: 'Select',
      colProps: { span: 8 },
      componentProps: {
        placeholder: '请选择请求类型',
        mode: 'multiple',
        options: getDictByCode(httpType, false),
      },
    },
    {
      field: `traceId`,
      label: `链路ID`,
      component: 'Input',
      colProps: { span: 8 },
      componentProps: {
        placeholder: '请输入链路ID',
      },
    },
    {
      field: `reqBody`,
      label: `请求体`,
      component: 'Input',
      colProps: { span: 8 },
      componentProps: {
        placeholder: '请输入请求参数',
      },
    },
    {
      field: `reqParams`,
      label: `请求参数`,
      component: 'Input',
      colProps: { span: 8 },
      componentProps: {
        placeholder: '请输入请求体',
      },
    },
    {
      field: `reqHeaders`,
      label: `请求头`,
      component: 'Input',
      colProps: { span: 8 },
      componentProps: {
        placeholder: '请输入请求头',
      },
    },
    {
      field: `resBody`,
      label: `响应体`,
      component: 'Input',
      colProps: { span: 8 },
      componentProps: {
        placeholder: '请输入响应体',
      },
    },
    {
      field: `resHeaders`,
      label: `响应头`,
      component: 'Input',
      colProps: { span: 8 },
      componentProps: {
        placeholder: '请输入响应头',
      },
    },
    {
      field: `startTime`,
      label: `请求开始时间`,
      component: 'RangePicker',
      colProps: { span: 8 },
      defaultValue: [dayjs().startOf('date'), dayjs().endOf('date')],
      componentProps: {
        placeholder: ['开始日期', '结束日期'],
        style: { width: '100%' },
        showTime: true,
        format: 'YYYY-MM-DD HH:mm:ss',
      },
    },
    {
      field: `endTime`,
      label: `请求结束时间`,
      component: 'RangePicker',
      colProps: { span: 8 },
      componentProps: {
        placeholder: ['开始日期', '结束日期'],
        style: { width: '100%' },
        showTime: true,
        format: 'YYYY-MM-DD HH:mm:ss',
      },
    },
    {
      field: `consumeStart`,
      label: `请求间隔起始`,
      component: 'InputNumber',
      colProps: { span: 4 },
      componentProps: {
        min: 0,
        addonAfter: 'ms',
      },
    },
    {
      field: `consumeEnd`,
      label: `请求间隔结束`,
      component: 'InputNumber',
      colProps: { span: 4 },
      componentProps: {
        min: 0,
        addonAfter: 'ms',
      },
    },
    {
      field: `ip`,
      label: `请求IP`,
      component: 'Input',
      colProps: { span: 8 },
      componentProps: {
        placeholder: '请输入请求IP',
      },
    },
    {
      field: `resStatus`,
      label: `响应状态`,
      component: 'Input',
      colProps: { span: 8 },
      componentProps: {
        placeholder: '请输入响应状态',
      },
    },
    {
      field: `operateBy`,
      label: `操作人`,
      component: 'Input',
      colProps: { span: 8 },
      componentProps: {
        placeholder: '请输入操作人',
      },
    },
    {
      field: `isErr`,
      label: `是否异常`,
      component: 'Select',
      colProps: { span: 8 },
      componentProps: {
        options: getDictByCode(yesOrNo),
      },
    },
    {
      field: `errMessage`,
      label: `异常信息`,
      component: 'Input',
      colProps: { span: 8 },
      componentProps: {
        placeholder: '请输入异常信息',
      },
    },
  ];
}

export function getDescColumns(): DescItem[] {
  return [
    {
      label: '日志编号',
      field: 'id',
      labelMinWidth: 60,
      span: 3,
    },
    {
      label: '接口名称',
      field: 'name',
      labelMinWidth: 60,
      span: 3,
    },
    {
      label: '接口地址',
      field: 'url',
      labelMinWidth: 60,
      render: (data, values) => {
        const dict = getDictByCodeAndValue(httpType, values.type);
        if (data === undefined || isNull(data) || isNull(dict)) {
          return h('span', [h(Tag, () => values.type), h('span', '  ' + data)]);
        }
        return h('span', [h(Tag, { color: dict.color }, () => dict.label), h('span', '  ' + data)]);
      },
      span: 3,
    },
    {
      label: '请求方法',
      field: 'position',
      labelMinWidth: 60,
      span: 3,
    },
    {
      label: '开始时间',
      field: 'startTime',
      labelMinWidth: 60,
      span: 1,
    },
    {
      label: '结束时间',
      field: 'endTime',
      labelMinWidth: 60,
      span: 1,
    },
    {
      label: '时间间隔',
      field: 'consume',
      labelMinWidth: 60,
      span: 1,
      render: (data) => {
        return data + 'ms';
      },
    },
    {
      label: '链路id',
      field: 'traceId',
      labelMinWidth: 60,
      span: 3,
    },
    {
      label: '请求IP',
      field: 'ip',
      labelMinWidth: 60,
      span: 3,
    },
    {
      label: '响应状态',
      field: 'resStatus',
      labelMinWidth: 60,
      span: 3,
      render: (data) => {
        const success = ~~data === 200;
        const color = success ? 'green' : 'red';
        return h(Tag, { color: color }, () => data);
      },
    },
    {
      label: '请求体',
      field: 'reqBody',
      labelMinWidth: 60,
      span: 3,
      render: (data) => {
        if (isNull(data)) {
          return h('span', data);
        }
        return h(Textarea, {
          disabled: true,
          value: data,
          rows: 4,
        });
      },
    },
    {
      label: '请求参数',
      labelMinWidth: 60,
      field: 'reqParams',
      span: 3,
      render: (data) => {
        if (isNull(data)) {
          return h('span', data);
        }
        return h(Textarea, {
          disabled: true,
          value: data,
          rows: 4,
        });
      },
    },
    {
      label: '响应体',
      field: 'resBody',
      labelMinWidth: 60,
      span: 3,
      render: (data) => {
        if (isNull(data)) {
          return h('span', data);
        }
        return h(Textarea, {
          disabled: true,
          value: data,
          rows: 4,
        });
      },
    },
    {
      label: '操作人',
      field: 'operateBy',
      labelMinWidth: 60,
      span: 3,
    },
    {
      label: '创建时间',
      labelMinWidth: 60,
      field: 'createTime',
      span: 3,
    },
    {
      label: '异常信息',
      field: 'errMessage',
      labelMinWidth: 60,
      span: 3,
      render: (data) => {
        if (isNull(data)) {
          return h('span', data);
        }
        return h(Textarea, {
          disabled: true,
          value: data,
          rows: 4,
        });
      },
    },
  ];
}
