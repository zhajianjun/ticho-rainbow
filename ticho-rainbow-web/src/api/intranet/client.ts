import { defHttp } from '@/utils/http/axios';
import {
  ClientDTO,
  ClientModifyCommand,
  ClientQuery,
  ClientSaveCommand,
} from './model/clientModel';
import { RetryRequest } from '#/axios';
import { VersionModifyCommand } from '@/api/system/model/baseModel';

enum Api {
  Client = '/client',
  ClientEnable = '/client/status/enable',
  ClientDisable = '/client/status/disable',
  ClientPage = '/client/page',
  ClientAll = '/client/all',
  Export = '/client/excel/export',
}

export function saveClient(params: ClientSaveCommand) {
  return defHttp.post<void>({ url: Api.Client, params }, { errorMessageMode: 'message' });
}

export function delClient(params: VersionModifyCommand) {
  return defHttp.delete<void>({ url: Api.Client, params }, { errorMessageMode: 'message' });
}

export function modifyClient(params: ClientModifyCommand) {
  return defHttp.put<void>(
    { url: Api.Client, params },
    { successMessageMode: 'message', errorMessageMode: 'message' },
  );
}

export function enableClient(params: VersionModifyCommand[]) {
  return defHttp.patch<void>({ url: Api.ClientEnable, params }, { errorMessageMode: 'message' });
}

export function disableClient(params: VersionModifyCommand[]) {
  return defHttp.patch<void>({ url: Api.ClientDisable, params }, { errorMessageMode: 'message' });
}

export function clientPage(params?: ClientQuery) {
  return defHttp.get<ClientDTO[]>({ url: Api.ClientPage, params }, { errorMessageMode: 'message' });
}

export function clientAll(params?: ClientQuery) {
  return defHttp.get<ClientDTO[]>({ url: Api.ClientAll, params }, { errorMessageMode: 'message' });
}

export function expExcel(params?: ClientQuery) {
  return defHttp.get<any>(
    { url: Api.Export, params, responseType: 'blob' },
    {
      errorMessageMode: 'message',
      isReturnNativeResponse: true,
      retryRequest: { isOpenRetry: false } as RetryRequest,
    },
  );
}
