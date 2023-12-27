import { BasicColumn, FormSchema } from '@/components/Table';
import { h } from 'vue';
import { Switch } from 'ant-design-vue';
import { useMessage } from '@/hooks/web/useMessage';
import { modifyClient } from '@/api/system/client';
import { ClientDTO } from '@/api/system/model/clientModel';

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
      title: '客户端秘钥',
      dataIndex: 'accessKey',
      resizable: true,
      width: 120,
    },
    {
      title: '客户端名称',
      dataIndex: 'name',
      resizable: true,
      width: 80,
    },
    {
      title: '是否开启',
      dataIndex: 'enabled',
      resizable: true,
      width: 50,
      // customRender: ({ record }) => {
      //   const enabled = ~~record.enabled === 1;
      //   const color = enabled ? '#108ee9' : '#f50';
      //   const text = enabled ? '开启' : '关闭';
      //   return h(Tag, { color: color }, () => text);
      // },
      customRender: ({ record }) => {
        if (!Reflect.has(record, 'pendingStatus')) {
          record.pendingEnabled = false;
        }
        return h(Switch, {
          checked: record.enabled === 1,
          checkedChildren: '已开启',
          unCheckedChildren: '已关闭',
          loading: record.pendingEnabled,
          onChange(checked) {
            record.pendingStatus = true;
            const newEnabled = checked ? 1 : 0;
            const { createMessage } = useMessage();
            const params = { id: record.id, enabled: newEnabled } as ClientDTO;
            const messagePrefix = checked ? '启动' : '关闭';
            modifyClient(params)
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
      width: 50,
    },
    {
      title: '备注信息',
      dataIndex: 'remark',
      resizable: true,
      width: 200,
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
      field: `name`,
      label: `客户端名称`,
      component: 'Input',
      colProps: {
        xl: 12,
        xxl: 4,
      },
      componentProps: {
        placeholder: '请输入客户端名称',
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
      show: false,
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
      field: `name`,
      label: `客户端名称`,
      component: 'Input',
      componentProps: {
        placeholder: '请输入客户端名称',
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
      component: 'InputTextArea',
      componentProps: {
        defaultValue: '',
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
