import { defHttp } from '@/utils/http/axios';
import { ClientDTO, ClientQuery } from './model/clientModel';
import { RetryRequest } from '#/axios';

enum Api {
  Client = '/client',
  ClientPage = '/client/page',
  ClientAll = '/client/all',
  Export = '/client/excel/export',
}

export function saveClient(params: ClientDTO) {
  return defHttp.post<void>({ url: Api.Client, params }, { errorMessageMode: 'message' });
}

export function delClient(params: string) {
  return defHttp.delete<void>(
    { url: Api.Client + '?id=', params },
    { errorMessageMode: 'message' },
  );
}

export function modifyClient(params: ClientDTO) {
  return defHttp.put<void>({ url: Api.Client, params }, { errorMessageMode: 'message' });
}

export function modifyClientStatus(params: ClientDTO) {
  return defHttp.put<void>({ url: Api.Client, params }, { errorMessageMode: 'none' });
}

export function clientPage(params?: ClientQuery) {
  return defHttp.get<ClientDTO[]>(
    { url: Api.ClientPage, params },
    { errorMessageMode: 'message' },
  );
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
