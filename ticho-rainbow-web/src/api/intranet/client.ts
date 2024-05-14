import { defHttp } from '@/utils/http/axios';
import { ClientDTO, ClientQuery } from './model/clientModel';
import { RetryRequest } from '#/axios';

enum Api {
  Client = '/client',
  ClientPage = '/client/page',
  ClientAll = '/client/list',
  Export = '/client/expExcel',
}

export function saveClient(params: ClientDTO) {
  return defHttp.post<any>({ url: Api.Client, params }, { errorMessageMode: 'message' });
}

export function delClient(params: string) {
  return defHttp.delete<any>({ url: Api.Client + '?id=', params }, { errorMessageMode: 'message' });
}

export function modifyClient(params: ClientDTO) {
  return defHttp.put<any>({ url: Api.Client, params }, { errorMessageMode: 'message' });
}

export function modifyClientStatus(params: ClientDTO) {
  return defHttp.put<any>({ url: Api.Client, params }, { errorMessageMode: 'none' });
}

export function clientPage(params?: ClientQuery) {
  return defHttp.post<any>({ url: Api.ClientPage, params }, { errorMessageMode: 'message' });
}

export function clientAll(params?: ClientQuery) {
  return defHttp.get<any>({ url: Api.ClientAll, params }, { errorMessageMode: 'message' });
}

export function expExcel(params?: ClientQuery) {
  return defHttp.post<any>(
    { url: Api.Export, params, responseType: 'blob' },
    {
      errorMessageMode: 'message',
      isReturnNativeResponse: true,
      retryRequest: { isOpenRetry: false } as RetryRequest,
    },
  );
}
