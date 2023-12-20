import { defHttp } from '/@/utils/http/axios';
import { GetUserInfoModel, UserDTO, UserPassworUpdDTO } from "./model/userModel";
import { LoginRequest, Oauth2AccessToken } from '/@/api/sys/model/loginModel';

import { ErrorMessageMode } from '/#/axios';

enum Api {
  Login = '/user/token',
  Logout = '/logout',
  GetUserInfo = '/user/principal',
  User = '/user',
  UserPage = '/user/page',
  UpdatePassword = '/user/updatePassword',
}

export function loginApi(params: LoginRequest, mode: ErrorMessageMode = 'modal') {
  return defHttp.post<Oauth2AccessToken>(
    {
      url: Api.Login,
      params,
    },
    {
      errorMessageMode: mode,
    },
  );
}

export function getUserInfo() {
  return defHttp.get<GetUserInfoModel>({ url: Api.GetUserInfo }, { errorMessageMode: 'none' });
}

export function userPage(params?: UserDTO) {
  return defHttp.get<UserDTO>({ url: Api.UserPage, params }, { errorMessageMode: 'none' });
}

export function saveUser(params: UserDTO) {
  return defHttp.post<any>({ url: Api.User, params }, { errorMessageMode: 'message' });
}

export function delUser(params: string) {
  return defHttp.delete<any>({ url: Api.User + '?id=', params }, { errorMessageMode: 'message' });
}

export function modifyUser(params: UserDTO) {
  return defHttp.put<UserDTO>({ url: Api.User, params }, { errorMessageMode: 'message' });
}

export function modifyUserPassword(params: UserPassworUpdDTO) {
  return defHttp.put<any>({ url: Api.UpdatePassword, params }, { errorMessageMode: 'message' });
}

export function getUser(params: any) {
  return defHttp.get<UserDTO>({ url: Api.User, params }, { errorMessageMode: 'message' });
}

export function doLogout() {
  return defHttp.get({ url: Api.Logout });
}
