import { BasicColumn, FormSchema } from '@/components/Table';
import { h } from 'vue';
import { Tag } from 'ant-design-vue';
import Icon from '@/components/Icon/Icon.vue';

export const columns: BasicColumn[] = [
  {
    title: '菜单名称',
    dataIndex: 'name',
    width: 200,
    align: 'left',
  },
  {
    title: '图标',
    dataIndex: 'icon',
    width: 50,
    customRender: ({ record }) => {
      return h(Icon, { icon: record.icon ? record.icon : '' });
    },
  },
  {
    title: '类型',
    dataIndex: 'type',
    width: 100,
    customRender: ({ record }) => {
      const textArr = ['目录', '菜单', '按钮'];
      return record.type ? textArr[record.type - 1] : null;
    },
  },
  {
    title: '组件名称',
    width: 200,
    dataIndex: 'componentName',
  },
  {
    title: '组件路由',
    width: 280,
    dataIndex: 'path',
  },
  {
    title: '组件路径',
    dataIndex: 'component',
  },
  {
    title: '权限标识',
    dataIndex: 'perms',
    width: 250,
  },
  {
    title: '排序',
    dataIndex: 'sort',
    width: 50,
  },
  {
    title: '状态',
    dataIndex: 'status',
    width: 80,
    customRender: ({ record }) => {
      const status = record.status;
      const enable = ~~status === 1;
      const color = enable ? 'green' : 'red';
      const text = enable ? '启用' : '停用';
      return h(Tag, { color: color }, () => text);
    },
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    width: 180,
  },
];

// const isDir = (type: number) => type === 1;
const isMenu = (type: number) => type === 2;
const isButton = (type: number) => type === 3;

export const formSchema: FormSchema[] = [
  {
    field: `id`,
    component: 'Input',
    label: `编号`,
    show: false,
  },
  {
    field: 'type',
    label: '菜单类型',
    component: 'RadioButtonGroup',
    componentProps: {
      options: [
        { label: '目录', value: 1 },
        { label: '菜单', value: 2 },
        { label: '按钮', value: 3 },
      ],
    },
    colProps: { lg: 24, md: 24 },
  },
  {
    field: 'name',
    label: '菜单名称',
    component: 'Input',
    required: true,
  },
  {
    field: 'parentId',
    label: '上级菜单',
    component: 'TreeSelect',
    componentProps: {
      fieldNames: {
        label: 'name',
        key: 'id',
        value: 'id',
      },
      getPopupContainer: () => document.body,
      /* 展示搜索 */
      showSearch: true,
      /* 搜索逻辑 */
      filterTreeNode: (inputValue: string, treeNode: any) => {
        return treeNode.name.indexOf(inputValue) > -1;
      },
    },
  },

  {
    field: 'sort',
    label: '排序',
    component: 'InputNumber',
    required: true,
  },
  {
    field: 'icon',
    label: '图标',
    component: 'IconPicker',
    required: true,
    ifShow: ({ values }) => !isButton(values.type),
  },

  {
    field: 'path',
    label: '路由地址',
    component: 'Input',
    required: true,
    ifShow: ({ values }) => !isButton(values.type),
  },
  {
    field: 'component',
    label: '组件路径',
    component: 'Input',
    ifShow: ({ values }) => isMenu(values.type),
  },
  {
    field: 'componentName',
    label: '组件名称',
    component: 'Input',
    required: true,
    ifShow: ({ values }) => values.extFlag !== 1,
  },
  {
    field: 'redirect',
    label: '转发地址',
    component: 'Input',
    ifShow: ({ values }) => !isButton(values.type),
  },
  {
    field: 'perms',
    label: '权限标识',
    ifShow: ({ values }) => isButton(values.type),
    slot: 'permsSlot',
  },
  {
    field: 'status',
    label: '状态',
    component: 'RadioButtonGroup',
    defaultValue: 1,
    componentProps: {
      options: [
        { label: '启用', value: 1 },
        { label: '禁用', value: 0 },
      ],
    },
  },
  {
    field: 'extFlag',
    label: '是否外链',
    component: 'RadioButtonGroup',
    defaultValue: 0,
    componentProps: {
      options: [
        { label: '否', value: 0 },
        { label: '是', value: 1 },
      ],
    },
    ifShow: ({ values }) => !isButton(values.type),
  },

  {
    field: 'keepAlive',
    label: '是否缓存',
    component: 'RadioButtonGroup',
    defaultValue: 0,
    componentProps: {
      options: [
        { label: '否', value: 0 },
        { label: '是', value: 1 },
      ],
    },
    ifShow: ({ values }) => isMenu(values.type),
  },

  {
    field: 'invisible',
    label: '是否显示',
    component: 'RadioButtonGroup',
    defaultValue: 1,
    componentProps: {
      options: [
        { label: '是', value: 1 },
        { label: '否', value: 0 },
      ],
    },
    ifShow: ({ values }) => !isButton(values.type),
  },
];
