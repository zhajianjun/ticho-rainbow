import { defHttp } from '@/utils/http/axios';
import { PasswordDTO, UserDTO, UserPasswordDTO, UserQuery, UserRoleDTO } from './model/userModel';
import { ContentTypeEnum } from '@/enums/httpEnum';

enum Api {
  UserInfo = '/user',
  UserPage = '/user/page',
  BindRole = '/user/bindRole',
  UpdateForSelf = '/user/updateForSelf',
  UpdatePassword = '/user/updatePassword',
  UpdatePasswordForSelf = '/user/updatePasswordForSelf',
  ResetUserPassword = '/user/resetPassword',
  UploadAvatar = '/user/uploadAvatar',
  LockUser = '/user/lock',
  UnLockUser = '/user/unLock',
  LogOutUser = '/user/logOut',
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

export function lockUser(params: string[]) {
  return defHttp.post<any>(
    { url: Api.LockUser, params },
    { errorMessageMode: 'message', successMessageMode: 'message' },
  );
}

export function unlockUser(params: string[]) {
  return defHttp.post<any>(
    { url: Api.UnLockUser, params },
    { errorMessageMode: 'message', successMessageMode: 'message' },
  );
}

export function logOutUser(params: string[]) {
  return defHttp.post<any>(
    { url: Api.LogOutUser, params },
    { errorMessageMode: 'message', successMessageMode: 'message' },
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
  const params = { username: username };
  return defHttp.put<void>(
    { url: Api.ResetUserPassword, params },
    { errorMessageMode: 'message', joinParamsToUrl: true },
  );
}

export function bindRole(params: UserRoleDTO) {
  return defHttp.post<void>({ url: Api.BindRole, params }, { errorMessageMode: 'message' });
}

export function uploadAvatar(file: File) {
  const params = { file: file };
  return defHttp.post<string>(
    {
      url: Api.UploadAvatar,
      headers: {
        'Content-type': ContentTypeEnum.FORM_DATA,
      },
      params,
    },
    { errorMessageMode: 'message' },
  );
}
