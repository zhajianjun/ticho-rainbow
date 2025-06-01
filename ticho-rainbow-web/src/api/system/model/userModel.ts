export interface UserQuery {
  /** 当前页码 */
  pageNum: number;
  /** 页面大小 */
  pageSize: number;
  /** 主键编号列表 */
  ids: string[];
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
}

export interface UseSaveCommand {
  username: string;
  password: string;
  nickname: string;
  email: string;
  mobile: string;
  remark: string;
  roleIds: number[];
}

export interface UserModifyCommand {
  id: number;
  nickname: string;
  realname: string;
  email: string;
  mobile: string;
  remark: string;
  version: number;
  roleIds: number[];
}

export interface UseVersionModifyCommand {
  id: number;
  version: number;
}

export interface UserModifyPasswordCommand {
  /** 用户id */
  id: number;
  /** 密码 */
  password: string;
  /** 新密码 */
  newPassword: string;
  version: number;
}

export interface UserDTO {
  /** 用户id */
  id: string;
  /** 账号 */
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
  /** 版本号 */
  version: number;
  /** 角色id列表 */
  roleIds: string[];
}
