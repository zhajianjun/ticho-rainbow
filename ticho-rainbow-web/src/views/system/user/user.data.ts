import { BasicColumn, FormSchema } from '@/components/Table';
import { listRoles } from '@/api/system/role';
import { getDictByCode, getDictByCodeAndValue } from '@/store/modules/dict';
import { h } from 'vue';
import { Tag } from 'ant-design-vue';

const userStatus = 'userStatus';

export const columns: BasicColumn[] = [
  {
    title: '用户名',
    dataIndex: 'username',
    width: 100,
  },
  {
    title: '手机号',
    dataIndex: 'mobile',
    width: 120,
  },
  {
    title: '邮箱',
    dataIndex: 'email',
    width: 120,
  },
  {
    title: '状态',
    dataIndex: 'status',
    width: 80,
    customRender: ({ text }) => {
      const isNormal = ~~text === 1;
      const color = isNormal ? 'green' : 'red';
      return h(Tag, { color: color }, () => getDictByCodeAndValue(userStatus, text));
    },
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    width: 100,
  },
  {
    title: '角色',
    dataIndex: 'roles',
    width: 200,
  },
  {
    title: '备注',
    dataIndex: 'remark',
    width: 200,
  },
];

export const searchFormSchema: FormSchema[] = [
  {
    field: 'username',
    label: '用户名',
    component: 'Input',
    colProps: { span: 8 },
  },
  {
    field: 'mobile',
    label: '手机号',
    component: 'Input',
    colProps: { span: 8 },
  },
  {
    field: 'email',
    label: '邮箱',
    component: 'Input',
    colProps: { span: 8 },
  },
  {
    field: 'status',
    label: '状态',
    component: 'Select',
    colProps: { span: 8 },
    componentProps: {
      options: getDictByCode(userStatus),
    },
  },
];

export const userFormSchema: FormSchema[] = [
  {
    field: `id`,
    component: 'Input',
    label: `编号`,
    show: false,
  },
  {
    field: 'username',
    label: '用户名',
    component: 'Input',
    helpMessage: ['本字段演示异步验证', '不能输入带有admin的用户名'],
    rules: [
      {
        required: true,
        message: '请输入用户名',
      },
    ],
  },
  {
    field: 'password',
    label: '密码',
    component: 'InputPassword',
    required: true,
    ifShow: ({ values }) => !values.id,
  },
  {
    label: '角色',
    field: 'roleIds',
    component: 'ApiSelect',
    componentProps: {
      api: listRoles,
      mode: 'multiple',
      labelField: 'name',
      valueField: 'id',
    },
    required: true,
    colProps: {
      span: 24,
    },
  },
  {
    field: 'dept',
    label: '所属部门',
    component: 'TreeSelect',
    componentProps: {
      fieldNames: {
        label: 'deptName',
        key: 'id',
        value: 'id',
      },
      getPopupContainer: () => document.body,
    },
    required: false,
  },
  {
    field: 'nickname',
    label: '昵称',
    component: 'Input',
    required: true,
  },
  {
    field: 'mobile',
    label: '手机号',
    component: 'Input',
    required: true,
  },
  {
    label: '邮箱',
    field: 'email',
    component: 'Input',
    required: true,
  },
  {
    label: '状态',
    field: 'status',
    component: 'Select',
    required: true,
    componentProps: {
      options: getDictByCode(userStatus),
    },
  },
  {
    label: '备注',
    field: 'remark',
    component: 'InputTextArea',
  },
];

export function getPasswordModalFormColumns(): FormSchema[] {
  return [
    {
      field: `username`,
      label: `用户名`,
      component: 'Input',
      componentProps: {
        placeholder: '请输入用户名',
        disabled: true,
      },
      colProps: {
        span: 24,
      },
    },
    {
      field: 'password',
      label: '密码',
      component: 'InputPassword',
      colProps: {
        span: 24,
      },
      componentProps: {
        placeholder: '请输入密码',
      },
      required: true,
      ifShow: ({ values }) => !values.id,
      rules: [
        {
          trigger: 'change',
          required: true,
          message: '密码不能为空',
        },
      ],
    },
    {
      field: `newPassword`,
      label: `新密码`,
      component: 'StrengthMeter',
      componentProps: {
        placeholder: '请输入新密码',
      },
      colProps: {
        span: 24,
      },
      dynamicRules: ({ values }) => {
        return [
          {
            trigger: 'change',
            required: true,
            validator: (_, value) => {
              if (!value) {
                return Promise.reject('新密码不能为空');
              }
              const reg = new RegExp(
                '^(?![0-9]+$)(?![a-zA-Z]+$)(?![0-9a-zA-Z]+$)(?![0-9\\W]+$)(?![a-zA-Z\\W]+$)[0-9A-Za-z\\W]{6,18}$',
              );
              if (!value.match(reg)) {
                return Promise.reject('新密码必须包含字母、数字和特殊字符，且在6~16位之间');
              }
              if (value === values.password) {
                return Promise.reject('新密码不能和旧密码一致');
              }
              return Promise.resolve();
            },
          },
        ];
      },
    },
    {
      field: 'confirmPassword',
      label: '确认密码',
      component: 'InputPassword',
      componentProps: {
        placeholder: '请再次输入新密码',
      },
      dynamicRules: ({ values }) => {
        return [
          {
            trigger: 'change',
            required: true,
            validator: (_, value) => {
              if (!value) {
                return Promise.reject('确认密码不能为空');
              }
              if (value !== values.newPassword) {
                return Promise.reject('两次输入的密码不一致!');
              }
              return Promise.resolve();
            },
          },
        ];
      },
    },
  ];
}
