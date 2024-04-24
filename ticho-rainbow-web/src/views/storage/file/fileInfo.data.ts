import { BasicColumn, FormSchema } from '@/components/Table';
import { isNumber } from '@/utils/is';

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
      title: '文件名',
      dataIndex: 'originalFilename',
      resizable: true,
      width: 100,
    },
    {
      title: '存储文件名',
      dataIndex: 'fileName',
      resizable: true,
      width: 100,
    },
    {
      title: '存储类型',
      dataIndex: 'type',
      resizable: true,
      width: 100,
    },
    {
      title: '文件扩展名',
      dataIndex: 'ext',
      resizable: true,
      width: 100,
      ifShow: false,
    },
    {
      title: '存储路径',
      dataIndex: 'path',
      resizable: true,
      width: 100,
    },
    {
      title: '文件大小',
      dataIndex: 'size',
      resizable: true,
      width: 100,
      customRender: ({ record }) => {
        if (!isNumber(record)) {
          return record;
        }
        return parseInt(record) / 1024 + 'kb';
      },
    },
    {
      title: 'MIME类型',
      dataIndex: 'contentType',
      resizable: true,
      width: 100,
    },
    {
      title: '文件元数据',
      dataIndex: 'metadata',
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
      title: '修改时间',
      dataIndex: 'updateTime',
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
      show: false,
    },
    {
      field: `type`,
      label: `存储类型`,
      component: 'Input',
      colProps: { span: 8 },
      componentProps: {
        placeholder: '请输入存储类型',
      },
    },
    {
      field: `fileName`,
      label: `文件名`,
      component: 'Input',
      colProps: { span: 8 },
      componentProps: {
        placeholder: '请输入文件名',
      },
    },
    {
      field: `path`,
      label: `存储路径`,
      component: 'Input',
      colProps: { span: 8 },
      componentProps: {
        placeholder: '请输入存储路径',
      },
    },
    {
      field: `size`,
      label: `文件大小;单位字节`,
      component: 'Input',
      colProps: { span: 8 },
      componentProps: {
        placeholder: '请输入文件大小;单位字节',
      },
    },
    {
      field: `contentType`,
      label: `MIME类型`,
      component: 'Input',
      colProps: { span: 8 },
      componentProps: {
        placeholder: '请输入MIME类型',
      },
    },
    {
      field: `originalFilename`,
      label: `原始文件名`,
      component: 'Input',
      colProps: { span: 8 },
      componentProps: {
        placeholder: '请输入原始文件名',
      },
    },
    {
      field: `status`,
      label: `状态`,
      component: 'Input',
      colProps: { span: 8 },
      componentProps: {
        placeholder: '请输入状态',
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
    {
      field: `createBy`,
      label: `创建人`,
      component: 'Input',
      colProps: { span: 8 },
      componentProps: {
        placeholder: '请输入创建人',
      },
    },
    {
      field: `createTime`,
      label: `创建时间`,
      component: 'Input',
      colProps: { span: 8 },
      componentProps: {
        placeholder: '请输入创建时间',
      },
    },
    {
      field: `updateBy`,
      label: `更新人`,
      component: 'Input',
      colProps: { span: 8 },
      componentProps: {
        placeholder: '请输入更新人',
      },
    },
    {
      field: `updateTime`,
      label: `修改时间`,
      component: 'Input',
      colProps: { span: 8 },
      componentProps: {
        placeholder: '请输入修改时间',
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
      field: `type`,
      label: `存储类型`,
      component: 'Input',
      componentProps: {
        placeholder: '请输入存储类型',
      },
      colProps: {
        span: 24,
      },
    },
    {
      field: `fileName`,
      label: `文件名`,
      component: 'Input',
      componentProps: {
        placeholder: '请输入文件名',
      },
      colProps: {
        span: 24,
      },
    },
    {
      field: `ext`,
      label: `文件扩展名`,
      component: 'Input',
      componentProps: {
        placeholder: '请输入文件扩展名',
      },
      colProps: {
        span: 24,
      },
    },
    {
      field: `path`,
      label: `存储路径`,
      component: 'Input',
      componentProps: {
        placeholder: '请输入存储路径',
      },
      colProps: {
        span: 24,
      },
    },
    {
      field: `size`,
      label: `文件大小;单位字节`,
      component: 'Input',
      componentProps: {
        placeholder: '请输入文件大小;单位字节',
      },
      colProps: {
        span: 24,
      },
    },
    {
      field: `contentType`,
      label: `MIME类型`,
      component: 'Input',
      componentProps: {
        placeholder: '请输入MIME类型',
      },
      colProps: {
        span: 24,
      },
    },
    {
      field: `originalFilename`,
      label: `原始文件名`,
      component: 'Input',
      componentProps: {
        placeholder: '请输入原始文件名',
      },
      colProps: {
        span: 24,
      },
    },
    {
      field: `metadata`,
      label: `文件元数据`,
      component: 'Input',
      componentProps: {
        placeholder: '请输入文件元数据',
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
