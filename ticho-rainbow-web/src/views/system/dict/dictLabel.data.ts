import { BasicColumn, FormSchema } from '@/components/Table';
import { getDictByCode, getDictByCodeAndValue } from '@/store/modules/dict';

const commonStatus = 'commonStatus';

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
      title: '字典编码',
      dataIndex: 'code',
      resizable: true,
      width: 100,
      ifShow: false,
    },
    {
      title: '字典标签',
      dataIndex: 'label',
      resizable: true,
      width: 100,
    },
    {
      title: '字典值',
      dataIndex: 'value',
      resizable: true,
      width: 50,
    },
    {
      title: '排序',
      dataIndex: 'sort',
      resizable: true,
      width: 50,
    },
    {
      title: '状态',
      dataIndex: 'status',
      resizable: true,
      width: 50,
      customRender({ text }) {
        return getDictByCodeAndValue(commonStatus, text);
      },
    },
    {
      title: '备注信息',
      dataIndex: 'remark',
      resizable: true,
      width: 100,
    },
    {
      title: '创建时间',
      dataIndex: 'createTime',
      resizable: true,
      width: 80,
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
      field: `code`,
      label: `字典编码`,
      component: 'Input',
      componentProps: {
        placeholder: '请输入字典编码',
        disabled: true,
      },
      colProps: {
        span: 24,
      },
      required: true,
    },
    {
      field: `label`,
      label: `字典标签`,
      component: 'Input',
      componentProps: {
        placeholder: '请输入字典标签',
      },
      colProps: {
        span: 24,
      },
      required: true,
    },
    {
      field: `value`,
      label: `字典值`,
      component: 'Input',
      componentProps: {
        placeholder: '请输入字典值',
      },
      colProps: {
        span: 24,
      },
      required: true,
    },
    {
      field: `sort`,
      label: `排序`,
      component: 'InputNumber',
      defaultValue: 10,
      componentProps: {
        min: 0,
        step: 10,
        placeholder: '请输入排序',
      },
      colProps: {
        span: 24,
      },
    },
    {
      field: `status`,
      label: `状态`,
      component: 'RadioButtonGroup',
      defaultValue: 1,
      componentProps: {
        options: getDictByCode(commonStatus),
      },
      colProps: {
        span: 24,
      },
      required: true,
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
  ];
}
