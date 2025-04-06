import { defHttp } from '@/utils/http/axios';
import { DictDTO, DictQuery } from './model/dictModel';
import { PageResult } from '@/api/system/model/baseModel';
import { RetryRequest } from '#/axios';

enum Api {
  Dict = '/dict',
  DictPage = '/dict/page',
  all = '/dict/all',
  flush = '/dict/flush',
  Export = '/dict/excel/export',
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
  return defHttp.get<PageResult<DictDTO>>(
    { url: Api.DictPage, params },
    { errorMessageMode: 'none' },
  );
}

export function all() {
  return defHttp.get<DictDTO[]>({ url: Api.all }, { errorMessageMode: 'none' });
}

export function flush() {
  return defHttp.get<DictDTO[]>({ url: Api.flush }, { errorMessageMode: 'message' });
}

export function expExcel(params?: DictQuery) {
  return defHttp.get<any>(
    { url: Api.Export, params, responseType: 'blob' },
    {
      errorMessageMode: 'message',
      isReturnNativeResponse: true,
      retryRequest: { isOpenRetry: false } as RetryRequest,
    },
  );
}
