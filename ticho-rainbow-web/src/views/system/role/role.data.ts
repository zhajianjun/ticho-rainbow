import { BasicColumn, FormSchema } from '@/components/Table';
import { h } from 'vue';
import { Switch } from 'ant-design-vue';
import { useMessage } from '@/hooks/web/useMessage';
import { modifyRoleStatus } from '@/api/system/role';
import { getDictByCode, getDictLabelByCodeAndValue } from '@/store/modules/dict';

const commonStatus = 'commonStatus';

export const columns: BasicColumn[] = [
  {
    title: '编号',
    dataIndex: 'id',
    width: 200,
    ifShow: false,
  },
  {
    title: '角色编码',
    dataIndex: 'code',
    width: 200,
  },
  {
    title: '角色名称',
    dataIndex: 'name',
    width: 180,
  },
  {
    title: '状态',
    dataIndex: 'status',
    width: 120,
    customRender: ({ record }) => {
      if (!Reflect.has(record, 'pendingStatus')) {
        record.pendingStatus = false;
      }
      return h(Switch, {
        checked: record.status === 1,
        checkedChildren: getDictLabelByCodeAndValue(commonStatus, 1),
        unCheckedChildren: getDictLabelByCodeAndValue(commonStatus, 0),
        loading: record.pendingStatus,
        onChange(checked) {
          record.pendingStatus = true;
          const newStatus = checked ? 1 : 0;
          const { createMessage } = useMessage();
          const params = { id: record.id, status: newStatus };
          modifyRoleStatus(params)
            .then(() => {
              record.status = newStatus;
              createMessage.success(`已成功修改角色状态`);
            })
            .catch(() => {
              createMessage.error('修改角色状态失败');
            })
            .finally(() => {
              record.pendingStatus = false;
            });
        },
      });
    },
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    width: 180,
  },
  {
    title: '备注',
    dataIndex: 'remark',
    width: 200,
  },
];

export const searchFormSchema: FormSchema[] = [
  {
    field: `id`,
    component: 'Input',
    label: `编号`,
    show: false,
  },
  {
    field: 'code',
    label: '角色编码',
    component: 'Input',
    colProps: { span: 8 },
  },
  {
    field: 'name',
    label: '角色名称',
    component: 'Input',
    colProps: { span: 8 },
  },
  {
    field: 'status',
    label: '状态',
    component: 'Select',
    componentProps: {
      options: getDictByCode(commonStatus),
      placeholder: '请选择状态',
    },
    colProps: { span: 8 },
  },
];

export const formSchema: FormSchema[] = [
  {
    field: `id`,
    component: 'Input',
    label: `编号`,
    show: false,
  },
  {
    field: 'code',
    label: '角色编码',
    required: true,
    component: 'Input',
    rules: [
      {
        required: true,
        message: '请输入角色编码',
      },
    ],
    dynamicDisabled: ({ values }) => {
      return values.id !== null;
    },
  },
  {
    field: 'name',
    label: '角色名称',
    required: true,
    component: 'Input',
    rules: [
      {
        required: true,
        message: '请输入角色名称',
      },
    ],
  },
  {
    field: 'status',
    label: '状态',
    component: 'RadioButtonGroup',
    defaultValue: 1,
    componentProps: {
      options: getDictByCode(commonStatus),
    },
    rules: [
      {
        required: true,
        message: '请选择角色状态',
      },
    ],
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
    label: '菜单分配',
    field: 'menu',
    slot: 'menuSlot',
  },
  {
    field: `version`,
    label: `版本号`,
    component: 'Input',
    show: false,
  },
];
