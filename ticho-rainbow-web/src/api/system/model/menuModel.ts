import { defineComponent } from 'vue';

export type Component<T = any> =
  | ReturnType<typeof defineComponent>
  | (() => Promise<typeof import('*.vue')>)
  | (() => Promise<T>);

export interface MenuDTO {
  /** 菜单id */
  id: string;
  /** 父级id */
  parentId: string;
  /** 类型;1-目录,2-菜单,3-权限 */
  type: number;
  /** 权限标识 */
  perms: string;
  /** 标题;目录名称、菜单名称 */
  name: string;
  /** 路由地址 */
  path: string;
  /** 组件 */
  component: Component | string;
  /** 组件名称 */
  componentName: string;
  /** 转发地址 */
  redirect: string;
  /** 是否外链菜单;1-是,0-否 */
  extFlag: number;
  /** 是否缓存;1-是,0-否 */
  keepAlive: number;
  /** 菜单和目录是否可见;1-是,0-否 */
  invisible: number;
  /** 当前激活的菜单 */
  currentActiveMenu: string;
  /** 菜单是否可关闭;1-是,0-否 */
  closable: number;
  /** 图标 */
  icon: string;
  /** 排序 */
  sort: number;
  /** 状态;1-启用,0-停用 */
  status: number;
  /** 备注信息 */
  remark: string;
  /** 是否选中;true-选中,false-未选中 */
  checkbox: number;
  /** 子级 */
  children: MenuDTO[];
}

export type MenuModule = MenuDTO[];
