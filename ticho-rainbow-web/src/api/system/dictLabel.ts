import { defHttp } from '@/utils/http/axios';
import { DictLabelDTO } from './model/dictLabelModel';

enum Api {
  DictLabel = '/dict-label',
}

export function saveDictLabel(params: DictLabelDTO) {
  return defHttp.post<any>({ url: Api.DictLabel, params }, { errorMessageMode: 'message' });
}

export function delDictLabel(id: string) {
  const params = { id: id };
  return defHttp.delete<any>(
    { url: Api.DictLabel + '?id=', params },
    { errorMessageMode: 'message', joinParamsToUrl: true },
  );
}

export function modifyDictLabel(params: DictLabelDTO) {
  return defHttp.put<DictLabelDTO>({ url: Api.DictLabel, params }, { errorMessageMode: 'message' });
}

export function findDictLabel(code: string) {
  const params = { code: code };
  return defHttp.get<DictLabelDTO[]>(
    { url: Api.DictLabel, params },
    { errorMessageMode: 'message' },
  );
}
