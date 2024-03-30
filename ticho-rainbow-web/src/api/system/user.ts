import { defHttp } from '@/utils/http/axios';
import { PasswordDTO, UserDTO, UserPasswordDTO, UserQuery, UserRoleDTO } from './model/userModel';

enum Api {
  UserInfo = '/user',
  UserPage = '/user/page',
  BindRole = '/user/bindRole',
  UpdateForSelf = '/user/updateForSelf',
  UpdatePassword = '/user/updatePassword',
  UpdatePasswordForSelf = '/user/updatePasswordForSelf',
  ResetUserPassword = '/user/resetPassword',
}

export function userPage(params?: UserQuery) {
  return defHttp.get<UserDTO>({ url: Api.UserPage, params }, { errorMessageMode: 'none' });
}

export function getUserInfo(username: string) {
  const params = { username: username };
  return defHttp.get<UserDTO>({ url: Api.UserInfo, params }, { errorMessageMode: 'none' });
}

export function saveUser(params: UserDTO) {
  return defHttp.post<any>({ url: Api.UserInfo, params }, { errorMessageMode: 'message' });
}

export function delUser(params: string) {
  return defHttp.delete<any>(
    { url: Api.UserInfo + '?id=', params },
    { errorMessageMode: 'message' },
  );
}

export function modifyUser(params: UserDTO) {
  return defHttp.put<void>({ url: Api.UserInfo, params }, { errorMessageMode: 'message' });
}

export function modifyUserForSelf(params: UserDTO) {
  return defHttp.put<void>({ url: Api.UpdateForSelf, params }, { errorMessageMode: 'message' });
}

export function modifyUserPassword(params: UserPasswordDTO) {
  return defHttp.put<void>({ url: Api.UpdatePassword, params }, { errorMessageMode: 'message' });
}

export function modifyPasswordForSelf(params: PasswordDTO) {
  return defHttp.put<void>(
    { url: Api.UpdatePasswordForSelf, params },
    { errorMessageMode: 'message' },
  );
}

export function resetUserPassword(username: string) {
  return defHttp.put<void>(
    { url: `${Api.ResetUserPassword}?username=${username}` },
    { errorMessageMode: 'message' },
  );
}

export function bindRole(params: UserRoleDTO) {
  return defHttp.post<void>({ url: Api.BindRole, params }, { errorMessageMode: 'message' });
}
