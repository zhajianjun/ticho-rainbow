import { BasicColumn, FormSchema } from '@/components/Table';
import { getDictByCode, getDictByCodeAndValue } from '@/store/modules/dict';
import { h } from 'vue';
import { Tag } from 'ant-design-vue';

const yesOrNo = 'yesOrNo';
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
      title: '字典名称',
      dataIndex: 'name',
      resizable: true,
      width: 100,
    },
    {
      title: '字典编码',
      dataIndex: 'code',
      resizable: true,
      width: 100,
    },
    {
      title: '系统字典',
      dataIndex: 'isSys',
      resizable: true,
      width: 100,
      customRender({ text }) {
        return getDictByCodeAndValue(yesOrNo, text);
      },
    },
    {
      title: '状态',
      dataIndex: 'status',
      resizable: true,
      width: 60,
      customRender: ({ text }) => {
        const isNormal = ~~text === 1;
        const color = isNormal ? 'green' : 'red';
        return h(Tag, { color: color }, () => getDictByCodeAndValue(commonStatus, text));
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
      width: 100,
    },
  ];
}

export function getSearchColumns(): FormSchema[] {
  return [
    {
      field: `code`,
      label: `字典编码`,
      component: 'Input',
      colProps: {
        xl: 12,
        xxl: 8,
      },
      componentProps: {
        placeholder: '请输入字典编码',
      },
    },
    {
      field: `name`,
      label: `字典名称`,
      component: 'Input',
      colProps: {
        xl: 12,
        xxl: 8,
      },
      componentProps: {
        placeholder: '请输入字典名称',
      },
    },
    {
      field: `isSys`,
      label: `系统字典`,
      component: 'Select',
      colProps: {
        xl: 12,
        xxl: 8,
      },
      componentProps: {
        options: getDictByCode(yesOrNo),
        placeholder: '请选择是否系统字典',
      },
    },
    {
      field: `status`,
      label: `状态`,
      component: 'Select',
      colProps: {
        xl: 12,
        xxl: 8,
      },
      componentProps: {
        options: getDictByCode(commonStatus),
        placeholder: '请选择状态',
      },
    },
    {
      field: `remark`,
      label: `备注信息`,
      component: 'Input',
      colProps: {
        xl: 12,
        xxl: 8,
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
      },
      colProps: {
        span: 24,
      },
      required: true,
    },
    {
      field: `name`,
      label: `字典名称`,
      component: 'Input',
      componentProps: {
        placeholder: '请输入字典名称',
      },
      colProps: {
        span: 24,
      },
      required: true,
    },
    {
      field: `isSys`,
      label: `系统字典`,
      component: 'Select',
      defaultValue: 0,
      componentProps: {
        options: getDictByCode(yesOrNo),
        placeholder: '是否系统字典',
      },
      colProps: {
        span: 24,
      },
      required: true,
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
