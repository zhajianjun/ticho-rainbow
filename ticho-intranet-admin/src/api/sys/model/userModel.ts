/**
 * @description: Login interface parameters
 */
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
