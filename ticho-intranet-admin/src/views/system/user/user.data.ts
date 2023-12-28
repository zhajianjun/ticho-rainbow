import { BasicColumn, FormSchema } from '@/components/Table';

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
      title: '用户名',
      dataIndex: 'username',
      resizable: true,
      width: 80,
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
      width: 80,
    },
    {
      title: '创建时间',
      dataIndex: 'createTime',
      resizable: true,
      width: 80,
    },
    {
      title: '更新人',
      dataIndex: 'updateBy',
      resizable: true,
      width: 80,
    },
    {
      title: '更新时间',
      dataIndex: 'updateTime',
      resizable: true,
      width: 80,
    },
  ];
}

export function getSearchColumns(): FormSchema[] {
  return [
    {
      field: `username`,
      label: `用户名`,
      component: 'Input',
      colProps: {
        xl: 12,
        xxl: 4,
      },
      componentProps: {
        placeholder: '请输入用户名',
      },
    },
    {
      field: `remark`,
      label: `备注信息`,
      component: 'Input',
      colProps: {
        xl: 12,
        xxl: 4,
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
      label: `主键标识`,
      component: 'Input',
      colProps: {
        span: 24,
      },
      show: false,
    },
    {
      field: `username`,
      label: `用户名`,
      component: 'Input',
      componentProps: {
        placeholder: '请输入用户名',
      },
      colProps: {
        span: 24,
      },
    },
    {
      field: 'password',
      label: '密码',
      component: 'StrengthMeter',
      required: true,
      ifShow: ({ values }) => !values.id,
      componentProps: {
        placeholder: '请输入密码',
      },
      dynamicRules: () => {
        return [
          {
            trigger: 'blur',
            required: true,
            validator: (_, value) => {
              if (!value) {
                return Promise.reject('密码不能为空');
              }
              const reg = new RegExp(
                '^(?![0-9]+$)(?![a-zA-Z]+$)(?![0-9a-zA-Z]+$)(?![0-9\\W]+$)(?![a-zA-Z\\W]+$)[0-9A-Za-z\\W]{6,18}$',
              );
              if (!value.match(reg)) {
                return Promise.reject('新密码必须包含字母、数字和特殊字符，且在6~16位之间');
              }
              return Promise.resolve();
            },
          },
        ];
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
          trigger: 'blur',
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
            trigger: 'blur',
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
            trigger: 'blur',
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
