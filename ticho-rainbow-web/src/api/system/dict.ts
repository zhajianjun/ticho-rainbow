import { defHttp } from '@/utils/http/axios';
import { DictDTO, DictQuery } from './model/dictModel';
import { PageResult } from '@/api/system/model/baseModel';
import { RetryRequest } from '#/axios';

enum Api {
  Dict = '/dict',
  DictPage = '/dict/page',
  list = '/dict/list',
  flush = '/dict/flush',
  Export = '/dict/expExcel',
}

export function saveDict(params: DictDTO) {
  return defHttp.post<any>({ url: Api.Dict, params }, { errorMessageMode: 'message' });
}

export function delDict(id: string) {
  const params = { id: id };
  return defHttp.delete<any>(
    { url: Api.Dict, params },
    { errorMessageMode: 'message', joinParamsToUrl: true },
  );
}

export function modifyDict(params: DictDTO) {
  return defHttp.put<any>({ url: Api.Dict, params }, { errorMessageMode: 'message' });
}

export function dictPage(params?: DictQuery) {
  return defHttp.post<PageResult<DictDTO>>(
    { url: Api.DictPage, params },
    { errorMessageMode: 'none' },
  );
}

export function list() {
  return defHttp.get<DictDTO[]>({ url: Api.list }, { errorMessageMode: 'none' });
}

export function flush() {
  return defHttp.get<DictDTO[]>({ url: Api.flush }, { errorMessageMode: 'message' });
}

export function expExcel(params?: DictQuery) {
  return defHttp.post<any>(
    { url: Api.Export, params, responseType: 'blob' },
    {
      errorMessageMode: 'message',
      isReturnNativeResponse: true,
      retryRequest: { isOpenRetry: false } as RetryRequest,
    },
  );
}
