import { defHttp } from '@/utils/http/axios';
import { PortDTO, PortQuery } from './model/portModel';
import { RetryRequest } from '#/axios';

enum Api {
  Port = '/port',
  PortPage = '/port/page',
  Export = '/port/expExcel',
}

export function savePort(params: PortDTO) {
  return defHttp.post<void>({ url: Api.Port, params }, { errorMessageMode: 'message' });
}

export function delPort(params: string) {
  return defHttp.delete<void>({ url: Api.Port + '?id=', params }, { errorMessageMode: 'message' });
}

export function modifyPort(params: PortDTO) {
  return defHttp.put<void>({ url: Api.Port, params }, { errorMessageMode: 'message' });
}

export function portPage(params?: PortQuery) {
  return defHttp.post<PortDTO[]>({ url: Api.PortPage, params }, { errorMessageMode: 'message' });
}

export function expExcel(params?: PortQuery) {
  return defHttp.post<any>(
    { url: Api.Export, params, responseType: 'blob' },
    {
      errorMessageMode: 'message',
      isReturnNativeResponse: true,
      retryRequest: { isOpenRetry: false } as RetryRequest,
    },
  );
}
