/**
 * 登录请求参数
 */
export interface LoginRequest {
  username: string;
  password: string;
}

/**
 * 登录返回参数
 */
export interface Oauth2AccessToken {
  access_token: string;
  refresh_token: string;
  token_type: string;
  iat: number;
  expires_in: number;
  exp: number;
}

export interface LoginParams {
  username: string;
  password: string;
}

export interface GetUserInfoModel {
  authenticated: boolean;
  principal: UserInfo;
}

export interface UserInfo {
  // 用户名
  username: string;
  // 头像
  avatar: string;
  roles: String[];
}
