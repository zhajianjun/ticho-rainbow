import { defHttp } from '@/utils/http/axios';
import {
  DictCacheDTO,
  DictDTO,
  DictModifyCommand,
  DictQuery,
  DictSaveCommand,
} from './model/dictModel';
import { PageResult, VersionModifyCommand } from '@/api/system/model/baseModel';
import { RetryRequest } from '#/axios';

enum Api {
  Dict = '/dict',
  DictPage = '/dict/page',
  all = '/dict/all',
  flush = '/dict/flush',
  Export = '/dict/excel/export',
  DictEnable = '/dict/status/enable',
  DictDisable = '/dict/status/disable',
}

export function saveDict(params: DictSaveCommand) {
  return defHttp.post<any>(
    { url: Api.Dict, params },
    { errorMessageMode: 'message', successMessageMode: 'message' },
  );
}

export function delDict(params: VersionModifyCommand) {
  return defHttp.delete<any>(
    { url: Api.Dict, params },
    { errorMessageMode: 'message', successMessageMode: 'message' },
  );
}

export function modifyDict(params: DictModifyCommand) {
  return defHttp.put<any>(
    { url: Api.Dict, params },
    { errorMessageMode: 'message', successMessageMode: 'message' },
  );
}

export function enableDict(params: VersionModifyCommand[]) {
  return defHttp.patch<void>(
    { url: Api.DictEnable, params },
    {
      errorMessageMode: 'message',
    },
  );
}

export function disableDict(params: VersionModifyCommand[]) {
  return defHttp.patch<void>(
    { url: Api.DictDisable, params },
    {
      errorMessageMode: 'message',
    },
  );
}

export function dictPage(params?: DictQuery) {
  return defHttp.get<PageResult<DictDTO>>(
    { url: Api.DictPage, params },
    { errorMessageMode: 'none' },
  );
}

export function all() {
  return defHttp.get<DictCacheDTO[]>({ url: Api.all }, { errorMessageMode: 'none' });
}

export function flush() {
  return defHttp.get<DictCacheDTO[]>(
    { url: Api.flush },
    { errorMessageMode: 'message', successMessageMode: 'message' },
  );
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
