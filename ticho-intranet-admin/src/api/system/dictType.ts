import { defHttp } from '@/utils/http/axios';
import { DictTypeDTO, DictTypeQuery } from './model/dictTypeModel';

enum Api {
  DictType = '/dictType',
  DictTypePage = '/dictType/page',
}

export function saveDictType(params: DictTypeDTO) {
  return defHttp.post<any>({ url: Api.DictType, params }, { errorMessageMode: 'message' });
}

export function delDictType(params: string) {
  return defHttp.delete<any>(
    { url: Api.DictType + '?id=', params },
    { errorMessageMode: 'message' },
  );
}

export function modifyDictType(params: DictTypeDTO) {
  return defHttp.put<any>({ url: Api.DictType, params }, { errorMessageMode: 'message' });
}

export function dictTypePage(params?: DictTypeQuery) {
  return defHttp.get<DictTypeDTO[]>(
    { url: Api.DictTypePage, params },
    { errorMessageMode: 'none' },
  );
}
