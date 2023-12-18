import { defHttp } from '/@/utils/http/axios';
import { GetUserInfoModel } from './model/userModel';
import { LoginRequest, Oauth2AccessToken } from '/@/api/sys/model/loginModel';

import { ErrorMessageMode } from '/#/axios';

enum Api {
  Login = '/user/token',
  Logout = '/logout',
  GetUserInfo = '/user/principal',
  TestRetry = '/testRetry',
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

/**
 * @description: getUserInfo
 */
export function getUserInfo() {
  return defHttp.get<GetUserInfoModel>({ url: Api.GetUserInfo }, { errorMessageMode: 'none' });
}

export function doLogout() {
  return defHttp.get({ url: Api.Logout });
}

export function testRetry() {
  return defHttp.get(
    { url: Api.TestRetry },
    {
      retryRequest: {
        isOpenRetry: true,
        count: 5,
        waitTime: 1000,
      },
    },
  );
}
