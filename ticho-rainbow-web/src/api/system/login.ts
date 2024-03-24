import { defHttp } from '@/utils/http/axios';
import {
  ImgCodeEmailDTO,
  UserLoginDTO,
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
  UserDtlForSelf = '/user/detailForSelf',
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

export function userDtlForSelf() {
  return defHttp.get<UserRoleMenuDtlDTO>({ url: Api.UserDtlForSelf }, { errorMessageMode: 'none' });
}
