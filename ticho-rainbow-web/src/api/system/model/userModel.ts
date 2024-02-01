import { MenuDtlDTO } from '@/api/system/model/menuModel';
import { RoleDTO } from '@/api/system/model/roleModel';

/**
 * @description: Login interface parameters
 */
export interface LoginParams {
  username: string;
  password: string;
}

export interface UserQuery {
  pageNum: number;
  pageSize: number;
  id: string;
  username: string;
  realname: string;
  email: string;
  mobile: string;
  photo: string;
  lastIp: string;
  lastTime: string;
  status: number;
  remark: string;
  systemIds: string[];
}

export interface UserRoleMenuDtlDTO {
  /** 用户id */
  id: string;
  /** 账户 */
  username: string;
  /** 昵称 */
  nickname: string;
  /** 真实姓名 */
  realname: string;
  /** 身份证号 */
  idcard: string;
  /** 性别;0-男,1-女 */
  sex: number;
  /** 年龄 */
  age: number;
  /** 出生日期 */
  birthday: string;
  /** 家庭住址 */
  address: string;
  /** 学历 */
  education: string;
  /** 邮箱 */
  email: string;
  /** QQ号码 */
  qq: number;
  /** 微信号码 */
  wechat: string;
  /** 手机号码 */
  mobile: string;
  /** 头像地址 */
  photo: string;
  /** 最后登录ip地址 */
  lastIp: string;
  /** 最后登录时间 */
  lastTime: string;
  /** 用户状态;1-正常,2-未激活,3-已锁定,4-已注销 */
  status: number;
  /** 备注信息 */
  remark: string;
  /** 角色id列表 */
  roleIds: string[];
  /** 角色code列表 */
  roleCodes: string[];
  /** 菜单id列表 */
  menuIds: string[];
  /** 权限标识 */
  perms: string[];
  /** 菜单权限标识信息 */
  roles: RoleDTO[];
  /** 菜单信息 */
  menus: MenuDtlDTO[];
}

export interface UserDTO {
  /** 用户id */
  id: string;
  /** 账户 */
  username: string;
  /** 昵称 */
  nickname: string;
  /** 真实姓名 */
  realname: string;
  /** 身份证号 */
  idcard: string;
  /** 性别;0-男,1-女 */
  sex: number;
  /** 年龄 */
  age: number;
  /** 出生日期 */
  birthday: string;
  /** 家庭住址 */
  address: string;
  /** 学历 */
  education: string;
  /** 邮箱 */
  email: string;
  /** QQ号码 */
  qq: number;
  /** 微信号码 */
  wechat: string;
  /** 手机号码 */
  mobile: string;
  /** 头像地址 */
  photo: string;
  /** 最后登录ip地址 */
  lastIp: string;
  /** 最后登录时间 */
  lastTime: string;
  /** 用户状态;1-正常,2-未激活,3-已锁定,4-已注销 */
  status: number;
  /** 备注信息 */
  remark: string;
  /** 角色id列表 */
  roleIds: string[];
  /** 菜单权限标识信息 */
  roles: RoleDTO[];
}

export interface UserRoleDTO {
  userId: string;
  roleIds: string[];
}

export interface UserPasswordDTO {
  /** 用户名 */
  username: string;
  /** 密码 */
  password: string;
  /** 新密码 */
  newPassword: number;
}
