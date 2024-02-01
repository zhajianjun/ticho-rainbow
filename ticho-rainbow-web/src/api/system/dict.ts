import { defHttp } from '@/utils/http/axios';
import { DictDTO, DictQuery } from './model/dictModel';
import { PageResult } from '@/api/system/model/baseModel';

enum Api {
  Dict = '/dict',
  DictPage = '/dict/page',
  list = '/dict/list',
  flush = '/dict/flush',
}

export function saveDict(params: DictDTO) {
  return defHttp.post<any>({ url: Api.Dict, params }, { errorMessageMode: 'message' });
}

export function delDict(params: string) {
  return defHttp.delete<any>({ url: Api.Dict + '?id=', params }, { errorMessageMode: 'message' });
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

export function list() {
  return defHttp.get<DictDTO[]>({ url: Api.list }, { errorMessageMode: 'none' });
}

export function flush() {
  return defHttp.get<DictDTO[]>({ url: Api.flush }, { errorMessageMode: 'message' });
}
