import { defHttp } from '@/utils/http/axios';
import {
  UserDTO,
  UserModifyCommand,
  UserModifyPasswordCommand,
  UserQuery,
  UseSaveCommand,
} from './model/userModel';
import { VersionModifyCommand } from './model/baseModel';
import { ContentTypeEnum } from '@/enums/httpEnum';
import { RetryRequest } from '#/axios';

enum Api {
  UserInfo = '/user',
  UserPage = '/user/page',
  ModifyPassword = '/user/password',
  ResetUserPassword = '/user/password/reset',
  LockUser = '/user/status/lock',
  UnLockUser = '/user/status/un-lock',
  LogOutUser = '/user/status/log-out',
  ImpTemplate = '/user/excel-template/download',
  ImpExcel = '/user/excel/import',
  Export = '/user/excel/export',
}

export function userPage(params?: UserQuery) {
  return defHttp.get<UserDTO>({ url: Api.UserPage, params }, { errorMessageMode: 'none' });
}

export function saveUser(params: UseSaveCommand) {
  return defHttp.post<any>(
    { url: Api.UserInfo, params },
    {
      errorMessageMode: 'message',
      successMessageMode: 'message',
    },
  );
}

export function lockUser(params: VersionModifyCommand[]) {
  return defHttp.patch<any>(
    { url: Api.LockUser, params },
    { errorMessageMode: 'message', successMessageMode: 'message' },
  );
}

export function unlockUser(params: VersionModifyCommand[]) {
  return defHttp.patch<any>(
    { url: Api.UnLockUser, params },
    { errorMessageMode: 'message', successMessageMode: 'message' },
  );
}

export function logOutUser(params: VersionModifyCommand[]) {
  return defHttp.patch<any>(
    { url: Api.LogOutUser, params },
    { errorMessageMode: 'message', successMessageMode: 'message' },
  );
}

export function removeUser(params: VersionModifyCommand) {
  return defHttp.delete<any>(
    { url: Api.UserInfo, params },
    { errorMessageMode: 'message', successMessageMode: 'message' },
  );
}

export function modifyUser(params: UserModifyCommand) {
  return defHttp.put<void>(
    { url: Api.UserInfo, params },
    {
      errorMessageMode: 'message',
      successMessageMode: 'message',
    },
  );
}

export function modifyUserPassword(params: UserModifyPasswordCommand) {
  return defHttp.patch<void>(
    { url: Api.ModifyPassword, params },
    {
      errorMessageMode: 'message',
      successMessageMode: 'message',
    },
  );
}

export function resetUserPassword(params: VersionModifyCommand[]) {
  return defHttp.patch<void>(
    { url: Api.ResetUserPassword, params },
    { errorMessageMode: 'message', joinParamsToUrl: true },
  );
}

export function impTemplate() {
  return defHttp.post<any>(
    { url: Api.ImpTemplate, responseType: 'blob' },
    {
      errorMessageMode: 'message',
      isReturnNativeResponse: true,
      retryRequest: { isOpenRetry: false } as RetryRequest,
    },
  );
}

export function impExcel(file: File) {
  const params = { file: file };
  return defHttp.post<any>(
    {
      url: Api.ImpExcel,
      params,
      responseType: 'blob',
      headers: {
        'Content-type': ContentTypeEnum.FORM_DATA,
      },
    },
    {
      errorMessageMode: 'message',
      isReturnNativeResponse: true,
      retryRequest: { isOpenRetry: false } as RetryRequest,
    },
  );
}

export function expExcel(params?: UserQuery) {
  return defHttp.get<any>(
    { url: Api.Export, params, responseType: 'blob' },
    {
      errorMessageMode: 'message',
      isReturnNativeResponse: true,
      retryRequest: { isOpenRetry: false } as RetryRequest,
    },
  );
}
