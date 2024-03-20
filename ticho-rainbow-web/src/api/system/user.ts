import { defHttp } from '@/utils/http/axios';
import {
  ImgCodeEmailDTO,
  UserDTO,
  UserLoginDTO,
  UserPasswordDTO,
  UserQuery,
  UserRoleDTO,
  UserRoleMenuDtlDTO,
  UserSignUpDTO,
} from './model/userModel';
import { ContentTypeEnum } from '@/enums/httpEnum';
import { LoginRequest, Oauth2AccessToken } from '@/api/system/model/loginModel';

import { ErrorMessageMode } from '#/axios';

enum Api {
  Login = '/oauth/token',
  ImgCode = '/oauth/imgCode',
  SignUpEmailSend = '/oauth/signUpEmailSend',
  SignUp = '/oauth/signUp',
  ResetPasswordEmailSend = '/oauth/resetPasswordEmailSend',
  ResetPassword = '/oauth/resetPassword',
  Logout = '/logout',
  UserDtlInfo = '/user/getUserDtl',
  UserInfo = '/user',
  UserPage = '/user/page',
  BindRole = '/user/bindRole',
  UpdatePassword = '/user/updatePassword',
  ResetUserPassword = '/user/resetPassword',
}

export function getImgCode(imgKey: string, mode: ErrorMessageMode = 'modal') {
  const params = { imgKey: imgKey };
  return defHttp.get<Blob>(
    {
      url: Api.ImgCode,
      responseType: 'blob',
      params,
    },
    { isTransformResponse: false, withToken: false, errorMessageMode: mode },
  );
}

export function loginApi(params: LoginRequest, mode: ErrorMessageMode = 'modal') {
  return defHttp.post<Oauth2AccessToken>(
    {
      url: Api.Login,
      headers: { 'Content-Type': ContentTypeEnum.FORM_URLENCODED },
      params,
    },
    {
      errorMessageMode: mode,
    },
  );
}

export function signUpEmailSend(params: ImgCodeEmailDTO, mode: ErrorMessageMode = 'none') {
  return defHttp.post<any>(
    {
      url: Api.SignUpEmailSend,
      params,
    },
    {
      errorMessageMode: mode,
      withToken: false,
    },
  );
}

export function signUp(params: UserSignUpDTO, mode: ErrorMessageMode = 'none') {
  return defHttp.post<UserLoginDTO>({ url: Api.SignUp, params }, { errorMessageMode: mode });
}

export function resetPasswordEmailSend(params: ImgCodeEmailDTO, mode: ErrorMessageMode = 'none') {
  return defHttp.post<any>(
    {
      url: Api.ResetPasswordEmailSend,
      params,
    },
    {
      errorMessageMode: mode,
      withToken: false,
    },
  );
}

export function resetPassword(params: UserSignUpDTO, mode: ErrorMessageMode = 'none') {
  return defHttp.post<UserLoginDTO>({ url: Api.ResetPassword, params }, { errorMessageMode: mode });
}

export function getUserDtlInfo() {
  return defHttp.get<UserRoleMenuDtlDTO>({ url: Api.UserDtlInfo }, { errorMessageMode: 'none' });
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

export function modifyUserPassword(params: UserPasswordDTO) {
  return defHttp.put<void>({ url: Api.UpdatePassword, params }, { errorMessageMode: 'message' });
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

export function doLogout() {
  return defHttp.get({ url: Api.Logout });
}
