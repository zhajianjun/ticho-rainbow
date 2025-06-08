export interface UserSignUpCommand {
  /** 用户名 */
  username: string;
  /** 密码 */
  password: string;
  /** 邮箱 */
  email: string;
  /** 邮箱验证码 */
  emailCode: string;
}

export type ResetPasswordCommand = UserSignUpCommand;

export interface ImgCodeDTO {
  /** 邮箱 */
  imgKey: string;
  /** 邮箱 */
  imgCode: string;
}

export interface UserSignUpEmailSendCommand {
  /** 邮箱 */
  email: string;
  /** 邮箱 */
  imgKey: string;
  /** 邮箱 */
  imgCode: string;
}

export type ResetPassworEmailSendCommand = UserSignUpEmailSendCommand;

/**
 * 登录返回参数
 */
export interface TiToken {
  access_token: string;
  refresh_token: string;
  token_type: string;
  iat: number;
  expires_in: number;
  exp: number;
}

/**
 * 登录请求参数
 */
export interface LoginCommand {
  username: string;
  password: string;
  imgKey: string;
  imgCode: string;
}

export interface LoginDTO {
  username: string;
  imgKey: string;
  imgCode: string;
}

export interface LoginUserDTO {
  username: string;
  nickname: string;
  realname: string;
  photo: string;
  lastIp: string;
  lastTime: string;
}

export interface LoginUserDetailDTO {
  id: number;
  username: string;
  nickname: string;
  realname: string;
  idcard: string;
  sex: number;
  age: number;
  birthday: string;
  address: string;
  education: string;
  email: string;
  qq: string;
  wechat: string;
  mobile: string;
  photo: string;
  lastIp: string;
  lastTime: string;
  remark: string;
  version: number;
  createTime: string;
}

export interface LoginUserModifyCommand {
  nickname: string;
  email: string;
  mobile: string;
  remark: string;
  version: number;
}

export interface LoginUserModifyPasswordCommand {
  /** 密码 */
  password: string;
  /** 新密码 */
  newPassword: string;
  /** 版本号 */
  version: number;
}
