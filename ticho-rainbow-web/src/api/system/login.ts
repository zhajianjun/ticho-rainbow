import { defHttp } from '@/utils/http/axios';
import { ContentTypeEnum } from '@/enums/httpEnum';
import {
  LoginCommand,
  LoginDTO,
  LoginUserDetailDTO,
  LoginUserDTO,
  LoginUserModifyCommand,
  LoginUserModifyPasswordCommand,
  ResetPasswordCommand,
  ResetPassworEmailSendCommand,
  TiToken,
  UserSignUpCommand,
  UserSignUpEmailSendCommand,
} from '@/api/system/model/loginModel';

import { ErrorMessageMode, RetryRequest } from '#/axios';

enum Api {
  Login = '/oauth/token',
  ImgCode = '/oauth/img-code',
  SignUpEmailSend = '/oauth/sign-up/email/send',
  SignUp = '/oauth/sign-up',
  ResetPasswordEmailSend = '/oauth/reset-password/email/send',
  ResetPassword = '/oauth/password/reset',
  UserDetail = '/oauth/user/detail',
  User = '/oauth/user',
  ModifyPassword = '/oauth/user/password',
  UploadAvatar = '/oauth/user/avatar/upload',
  LoginMode = '/oauth/login-mode',
}

export function loginMode() {
  return defHttp.get<String>(
    {
      url: Api.LoginMode,
    },
    {
      withToken: false,
      retryRequest: { isOpenRetry: false } as RetryRequest,
    },
  );
}

export function getImgCode(imgKey: string, mode: ErrorMessageMode = 'modal') {
  const params = { imgKey: imgKey };
  return defHttp.get<Blob>(
    {
      url: Api.ImgCode,
      responseType: 'blob',
      params,
    },
    {
      isTransformResponse: false,
      withToken: false,
      errorMessageMode: mode,
      retryRequest: { isOpenRetry: false } as RetryRequest,
    },
  );
}

export function signUpEmailSend(
  params: UserSignUpEmailSendCommand,
  mode: ErrorMessageMode = 'none',
) {
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

export function signUp(params: UserSignUpCommand, mode: ErrorMessageMode = 'none') {
  return defHttp.post<LoginDTO>({ url: Api.SignUp, params }, { errorMessageMode: mode });
}

export function loginApi(params: LoginCommand, mode: ErrorMessageMode = 'modal') {
  return defHttp.post<TiToken>(
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

export function resetPasswordEmailSend(
  params: ResetPassworEmailSendCommand,
  mode: ErrorMessageMode = 'none',
) {
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

export function resetPassword(params: ResetPasswordCommand, mode: ErrorMessageMode = 'none') {
  return defHttp.post<LoginDTO>({ url: Api.ResetPassword, params }, { errorMessageMode: mode });
}

export function findUser() {
  return defHttp.get<LoginUserDTO>({ url: Api.User }, { errorMessageMode: 'none' });
}

export function findUserDetail() {
  return defHttp.get<LoginUserDetailDTO>({ url: Api.UserDetail }, { errorMessageMode: 'none' });
}

export function modifyUser(params: LoginUserModifyCommand) {
  return defHttp.put<void>({ url: Api.User, params }, { errorMessageMode: 'message' });
}

export function modifyPassword(params: LoginUserModifyPasswordCommand) {
  return defHttp.patch<void>({ url: Api.ModifyPassword, params }, { errorMessageMode: 'message' });
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
