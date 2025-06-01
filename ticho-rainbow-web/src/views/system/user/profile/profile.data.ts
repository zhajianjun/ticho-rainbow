import { FormSchema } from '@/components/Form';

export const basicFormSchema: FormSchema[] = [
  {
    field: `id`,
    component: 'Input',
    label: `编号`,
    show: false,
  },
  {
    field: 'username',
    label: '账号',
    component: 'Input',
    componentProps: {
      disabled: true,
    },
    show: true,
  },
  {
    field: 'realname',
    label: '用户姓名',
    component: 'Input',
    required: true,
  },
  {
    field: 'nickname',
    label: '用户昵称',
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
    label: '备注',
    field: 'remark',
    component: 'InputTextArea',
  },
  {
    label: '版本号',
    field: 'version',
    component: 'Input',
    show: false,
  },
];

export const passwordFormSchema: FormSchema[] = [
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
