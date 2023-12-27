import { BasicColumn, FormSchema } from '@/components/Table';
import { h } from 'vue';
import { Switch } from 'ant-design-vue';
import { useMessage } from '@/hooks/web/useMessage';
import { PortDTO } from '@/api/system/model/portModel';
import { modifyPort } from '@/api/system/port';

export function getTableColumns(): BasicColumn[] {
  return [
    {
      title: '主机端口',
      dataIndex: 'port',
      resizable: true,
      width: 100,
    },
    {
      title: '客户端地址',
      dataIndex: 'endpoint',
      resizable: true,
      width: 100,
    },
    {
      title: '域名',
      dataIndex: 'domain',
      resizable: true,
      width: 100,
    },
    {
      title: '是否永久',
      dataIndex: 'forever',
      resizable: true,
      width: 100,
      customRender: ({ record }) => {
        if (!Reflect.has(record, 'pendingStatus')) {
          record.pendingForever = false;
        }
        return h(Switch, {
          checked: record.forever === 1,
          checkedChildren: '已开启',
          unCheckedChildren: '已关闭',
          loading: record.pendingStatus,
          onChange(checked) {
            record.pendingForever = true;
            const newForever = checked ? 1 : 0;
            const { createMessage } = useMessage();
            const params = { id: record.id, forever: newForever } as PortDTO;
            const messagePrefix = checked ? '启动' : '关闭';
            modifyPort(params)
              .then(() => {
                record.forever = newForever;
                createMessage.success(messagePrefix + `成功`);
              })
              .catch(() => {
                createMessage.error(messagePrefix + `失败`);
              })
              .finally(() => {
                record.pendingForever = false;
              });
          },
        });
      },
    },
    {
      title: '过期时间',
      dataIndex: 'expireAt',
      resizable: true,
      width: 100,
      customRender: ({ record }) => {
        if (record.forever === 1) {
          return '无限制';
        }
        return record.expireAt;
      },
    },
    {
      title: '协议类型',
      dataIndex: 'type',
      resizable: true,
      width: 100,
    },
    {
      title: '是否开启',
      dataIndex: 'enabled',
      resizable: true,
      width: 100,
      customRender: ({ record }) => {
        if (!Reflect.has(record, 'pendingStatus')) {
          record.pendingEnabled = false;
        }
        return h(Switch, {
          checked: record.enabled === 1,
          checkedChildren: '已开启',
          unCheckedChildren: '已关闭',
          loading: record.pendingStatus,
          onChange(checked) {
            record.pendingEnabled = true;
            const newEnabled = checked ? 1 : 0;
            const { createMessage } = useMessage();
            const params = { id: record.id, enabled: newEnabled } as PortDTO;
            const messagePrefix = checked ? '启动' : '关闭';
            modifyPort(params)
              .then(() => {
                record.enabled = newEnabled;
                createMessage.success(messagePrefix + `成功`);
              })
              .catch(() => {
                createMessage.error(messagePrefix + `失败`);
              })
              .finally(() => {
                record.pendingEnabled = false;
              });
          },
        });
      },
    },
    {
      title: '排序',
      dataIndex: 'sort',
      resizable: true,
      width: 100,
    },
    {
      title: '备注信息',
      dataIndex: 'remark',
      resizable: true,
      width: 100,
    },
  ];
}

export function getSearchColumns(): FormSchema[] {
  return [
    {
      field: `accessKey`,
      label: `客户端秘钥`,
      component: 'Input',
      colProps: {
        xl: 12,
        xxl: 4,
      },
      componentProps: {
        placeholder: '请输入客户端秘钥',
      },
    },
    {
      field: `port`,
      label: `主机端口`,
      component: 'Input',
      colProps: {
        xl: 12,
        xxl: 4,
      },
      componentProps: {
        placeholder: '请输入主机端口',
      },
    },
    {
      field: `endpoint`,
      label: `客户端地址`,
      component: 'Input',
      colProps: {
        xl: 12,
        xxl: 4,
      },
      componentProps: {
        placeholder: '请输入客户端地址',
      },
    },
    {
      field: `domain`,
      label: `域名`,
      component: 'Input',
      colProps: {
        xl: 12,
        xxl: 4,
      },
      componentProps: {
        placeholder: '请输入域名',
      },
    },
    {
      field: `enabled`,
      label: `是否开启`,
      component: 'Input',
      colProps: {
        xl: 12,
        xxl: 4,
      },
      componentProps: {
        placeholder: '请输入是否开启',
      },
    },
    {
      field: `forever`,
      label: `是否永久`,
      component: 'Input',
      colProps: {
        xl: 12,
        xxl: 4,
      },
      componentProps: {
        placeholder: '请输入是否永久',
      },
    },
    {
      field: `expireAt`,
      label: `过期时间`,
      component: 'Input',
      colProps: {
        xl: 12,
        xxl: 4,
      },
      componentProps: {
        placeholder: '请输入过期时间',
      },
    },
    {
      field: `type`,
      label: `协议类型`,
      component: 'Input',
      colProps: {
        xl: 12,
        xxl: 4,
      },
      componentProps: {
        placeholder: '请输入协议类型',
      },
    },
    {
      field: `sort`,
      label: `排序`,
      component: 'Input',
      colProps: {
        xl: 12,
        xxl: 4,
      },
      componentProps: {
        placeholder: '请输入排序',
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
      component: 'Input',
      label: `主键标识`,
      ifShow: false,
    },
    {
      field: `accessKey`,
      label: `客户端秘钥`,
      component: 'Input',
      componentProps: {
        placeholder: '请输入客户端秘钥',
      },
      colProps: {
        span: 24,
      },
    },
    {
      field: `port`,
      label: `主机端口`,
      component: 'Input',
      componentProps: {
        placeholder: '请输入主机端口',
      },
      colProps: {
        span: 24,
      },
    },
    {
      field: `endpoint`,
      label: `客户端地址`,
      component: 'Input',
      componentProps: {
        placeholder: '请输入客户端地址',
      },
      colProps: {
        span: 24,
      },
    },
    {
      field: `domain`,
      label: `域名`,
      component: 'Input',
      componentProps: {
        placeholder: '请输入域名',
      },
      colProps: {
        span: 24,
      },
    },
    {
      field: `enabled`,
      label: `是否开启`,
      component: 'Switch',
      defaultValue: 0,
      componentProps: {
        checkedChildren: '开启',
        unCheckedChildren: '关闭',
        checkedValue: 1,
        unCheckedValue: 0,
      },
    },
    {
      field: `forever`,
      label: `是否永久`,
      component: 'Switch',
      defaultValue: 0,
      componentProps: {
        checkedChildren: '开启',
        unCheckedChildren: '关闭',
        checkedValue: 1,
        unCheckedValue: 0,
      },
    },
    {
      field: `expireAt`,
      label: `过期时间`,
      component: 'DatePicker',
      componentProps: {
        placeholder: '请输入过期时间',
        showTime: true,
      },
      colProps: {
        span: 24,
      },
    },
    {
      field: `type`,
      label: `协议类型`,
      component: 'Input',
      componentProps: {
        placeholder: '请输入协议类型',
      },
      colProps: {
        span: 24,
      },
    },
    {
      field: `sort`,
      label: `排序`,
      component: 'InputNumber',
      componentProps: {
        min: 0,
        max: 10000,
        defaultValue: 10,
        step: 10,
        placeholder: '请输入排序',
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
