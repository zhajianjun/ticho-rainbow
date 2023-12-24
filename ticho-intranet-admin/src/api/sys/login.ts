import { defHttp } from '@/utils/http/axios';
import { GetUserInfoModel, LoginRequest, Oauth2AccessToken } from './model/loginModel';
import { ErrorMessageMode } from '#/axios';

enum Api {
  Login = '/user/token',
  Logout = '/logout',
  GetUserInfo = '/user/principal',
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

export function doLogout() {
  return defHttp.get({ url: Api.Logout });
}
