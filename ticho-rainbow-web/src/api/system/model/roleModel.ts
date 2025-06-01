import { MenuDtlDTO } from '@/api/system/model/menuModel';

export interface RoleDTO {
  /** 主键编号 */
  id: string;
  /** 角色编码 */
  code: string;
  /** 角色名称 */
  name: string;
  /** 备注信息 */
  remark: string;
}

export interface RoleQuery {
  /** 当前页码 */
  pageNum: number;
  /** 页面大小 */
  pageSize: number;
  /** 主键编号列表 */
  ids: string[];
  /** 主键编号 */
  id: string;
  /** 角色编码 */
  code: string;
  /** 角色名称 */
  name: string;
  /** 备注信息 */
  remark: string;
}

export interface RoleDtlQuery {
  /** 角色编号列表 */
  roleIds: string[];
  /** 角色编码列表 */
  roleCodes: string[];
  /** 是否显示所有 */
  showAll: boolean;
  /** 是否树化 */
  treeHandle: boolean;
}

export interface RoleMenuDtlDTO {
  /** 角色编号列表 */
  roleIds: string[];
  /** 角色编码列表 */
  roleCodes: string[];
  /** 菜单编号 */
  menuIds: string[];
  /** 权限标识 */
  perms: string[];
  /** 菜单权限标识信息 */
  roles: RoleDTO[];
  /** 菜单信息 */
  menus: MenuDtlDTO[];
}

export interface RoleSaveCommand {
  code: string;
  name: string;
  status: number;
  remark: string;
  createTime: string;
  menuIds: number[];
}

export interface RoleModifyCommand {
  id: number;
  name: string;
  status: number;
  remark: string;
  version: number;
  menuIds: number[];
}

export interface RoleStatusModifyCommand {
  id: number;
  status: number;
  version: number;
}
