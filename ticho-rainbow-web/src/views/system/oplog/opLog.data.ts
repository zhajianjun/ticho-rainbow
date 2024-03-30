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
      title: '名称',
      dataIndex: 'name',
      resizable: true,
      width: 100,
    },
    {
      title: '请求地址',
      dataIndex: 'url',
      resizable: true,
      width: 100,
    },
    {
      title: '请求类型',
      dataIndex: 'type',
      resizable: true,
      width: 100,
    },
    {
      title: '请求方法',
      dataIndex: 'position',
      resizable: true,
      width: 100,
    },
    {
      title: '请求参数',
      dataIndex: 'reqBody',
      resizable: true,
      width: 100,
    },
    {
      title: '请求体',
      dataIndex: 'reqParams',
      resizable: true,
      width: 100,
    },
    {
      title: '请求头',
      dataIndex: 'reqHeaders',
      resizable: true,
      width: 100,
    },
    {
      title: '响应体',
      dataIndex: 'resBody',
      resizable: true,
      width: 100,
    },
    {
      title: '响应头',
      dataIndex: 'resHeaders',
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
      title: '请求间隔（毫秒）',
      dataIndex: 'consume',
      resizable: true,
      width: 100,
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
      width: 100,
    },
    {
      title: '操作人',
      dataIndex: 'operateBy',
      resizable: true,
      width: 100,
    },
    {
      title: '操作时间',
      dataIndex: 'createTime',
      resizable: true,
      width: 100,
    },
    {
      title: '是否异常',
      dataIndex: 'isErr',
      resizable: true,
      width: 100,
    },
    {
      title: '异常信息',
      dataIndex: 'errMessage',
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
      colProps: { span: 8 },
      componentProps: {
        placeholder: '请输入主键编号',
      },
      ifShow: false,
    },
    {
      field: `name`,
      label: `名称`,
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
      component: 'Input',
      colProps: { span: 8 },
      componentProps: {
        placeholder: '请输入请求类型',
      },
    },
    {
      field: `position`,
      label: `请求方法`,
      component: 'Input',
      colProps: { span: 8 },
      componentProps: {
        placeholder: '请输入请求方法',
      },
    },
    {
      field: `reqBody`,
      label: `请求参数`,
      component: 'Input',
      colProps: { span: 8 },
      componentProps: {
        placeholder: '请输入请求参数',
      },
    },
    {
      field: `reqParams`,
      label: `请求体`,
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
      component: 'Input',
      colProps: { span: 8 },
      componentProps: {
        placeholder: '请输入请求开始时间',
      },
    },
    {
      field: `endTime`,
      label: `请求结束时间`,
      component: 'Input',
      colProps: { span: 8 },
      componentProps: {
        placeholder: '请输入请求结束时间',
      },
    },
    {
      field: `consume`,
      label: `请求间隔（毫秒）`,
      component: 'Input',
      colProps: { span: 8 },
      componentProps: {
        placeholder: '请输入请求间隔（毫秒）',
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
      component: 'Input',
      colProps: { span: 8 },
      componentProps: {
        placeholder: '请输入是否异常',
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
