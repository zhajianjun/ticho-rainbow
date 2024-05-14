import { BasicColumn, FormSchema } from '@/components/Table';
import { toFinite } from 'lodash-es';
import { getDictByCode, getDictByCodeAndValue } from '@/store/modules/dict';
import { isNull } from 'xe-utils';
import { Tag } from 'ant-design-vue';
import { h } from 'vue';
import dayjs from 'dayjs';

const fileStorageType = 'fileStorageType';
const fileStatus = 'fileStatus';

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
      dataIndex: 'originalFileName',
      resizable: true,
      width: 100,
    },
    {
      title: '存储文件名',
      dataIndex: 'fileName',
      resizable: true,
      width: 100,
      ifShow: false,
    },
    {
      title: '存储类型',
      dataIndex: 'type',
      resizable: true,
      width: 60,
      customRender({ text }) {
        const dict = getDictByCodeAndValue(fileStorageType, text);
        if (text === undefined || isNull(text) || isNull(dict)) {
          return text;
        }
        return h(Tag, { color: dict.color }, () => dict.label);
      },
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
      width: 120,
      ifShow: true,
    },
    {
      title: '文件大小',
      dataIndex: 'size',
      resizable: true,
      width: 60,
      customRender: ({ value }) => {
        if (!value) {
          return value;
        }
        return toFinite(value) + 'b';
      },
    },
    {
      title: 'MIME类型',
      dataIndex: 'contentType',
      resizable: true,
      width: 100,
      ifShow: false,
    },
    {
      title: '文件元数据',
      dataIndex: 'metadata',
      resizable: true,
      width: 100,
      ifShow: false,
    },
    {
      title: '状态',
      dataIndex: 'status',
      resizable: true,
      width: 40,
      customRender({ text }) {
        const dict = getDictByCodeAndValue(fileStatus, text);
        if (text === undefined || isNull(text) || isNull(dict)) {
          return text;
        }
        return h(Tag, { color: dict.color }, () => dict.label);
      },
    },
    {
      title: '备注信息',
      dataIndex: 'remark',
      resizable: true,
      width: 120,
    },
    {
      title: '创建人',
      dataIndex: 'createBy',
      resizable: true,
      width: 60,
    },
    {
      title: '创建时间',
      dataIndex: 'createTime',
      resizable: true,
      width: 100,
    },
    {
      title: '修改人',
      dataIndex: 'updateBy',
      resizable: true,
      width: 60,
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
      component: 'Select',
      colProps: { span: 8 },
      componentProps: {
        placeholder: '请选择存储类型',
        options: getDictByCode(fileStorageType),
      },
    },
    {
      field: `originalFileName`,
      label: `文件名`,
      component: 'Input',
      colProps: { span: 8 },
      componentProps: {
        placeholder: '请输入文件名',
      },
    },
    {
      field: `fileName`,
      label: `实际文件名`,
      component: 'Input',
      colProps: { span: 8 },
      componentProps: {
        placeholder: '请输入实际文件名',
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
      field: `sizeStart`,
      label: `文件大小起始`,
      component: 'Input',
      colProps: { span: 4 },
      componentProps: {
        placeholder: '请输入文件大小起始',
      },
    },
    {
      field: `sizeEnd`,
      label: `文件大小结束`,
      component: 'Input',
      colProps: { span: 4 },
      componentProps: {
        placeholder: '请输入文件大小结束',
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
      field: `status`,
      label: `状态`,
      component: 'Select',
      colProps: { span: 8 },
      componentProps: {
        placeholder: '请选择状态',
        options: getDictByCode(fileStatus),
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
      defaultValue: [dayjs().startOf('month'), dayjs().endOf('month')],
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
      field: `updateBy`,
      label: `修改人`,
      component: 'Input',
      colProps: { span: 8 },
      componentProps: {
        placeholder: '请输入修改人',
      },
    },
    {
      field: `updateTime`,
      label: `修改时间`,
      component: 'RangePicker',
      colProps: { span: 8 },
      componentProps: {
        placeholder: ['开始日期', '结束日期'],
        style: { width: '100%' },
        showTime: true,
        format: 'YYYY-MM-DD HH:mm:ss',
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
      field: `originalFileName`,
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
