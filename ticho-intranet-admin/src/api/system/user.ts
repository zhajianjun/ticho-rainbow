import { defHttp } from '@/utils/http/axios';
import { UserDTO, UserPassworUpdDTO, UserQuery } from './model/userModel';

enum Api {
  User = '/user',
  UserPage = '/user/page',
  UpdatePassword = '/user/updatePassword',
}

export function userPage(params?: UserQuery) {
  return defHttp.get<any>({ url: Api.UserPage, params }, { errorMessageMode: 'message' });
}

export function saveUser(params: UserDTO) {
  return defHttp.post<any>({ url: Api.User, params }, { errorMessageMode: 'message' });
}

export function delUser(params: string) {
  return defHttp.delete<any>({ url: Api.User + '?id=', params }, { errorMessageMode: 'message' });
}

export function modifyUser(params: UserDTO) {
  return defHttp.put<any>({ url: Api.User, params }, { errorMessageMode: 'message' });
}

export function modifyUserPassword(params: UserPassworUpdDTO) {
  return defHttp.put<any>({ url: Api.UpdatePassword, params }, { errorMessageMode: 'message' });
}
