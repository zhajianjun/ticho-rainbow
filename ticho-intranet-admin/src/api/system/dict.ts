import { defHttp } from '@/utils/http/axios';
import { DictDTO } from './model/dictModel';

enum Api {
  Dict = '/dict',
  GetByCode = '/dict/getByCode',
}

export function saveDict(params: DictDTO) {
  return defHttp.post<any>({ url: Api.Dict, params }, { errorMessageMode: 'message' });
}

export function delDict(params: string) {
  return defHttp.delete<any>({ url: Api.Dict + '?id=', params }, { errorMessageMode: 'message' });
}

export function modifyDict(params: DictDTO) {
  return defHttp.put<DictDTO>({ url: Api.Dict, params }, { errorMessageMode: 'message' });
}

export function getByCode(code: string) {
  const params = { code: code };
  return defHttp.get<DictDTO[]>({ url: Api.Dict, params }, { errorMessageMode: 'message' });
}
