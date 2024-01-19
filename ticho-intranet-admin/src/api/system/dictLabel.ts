import { defHttp } from '@/utils/http/axios';
import { DictLabelDTO } from './model/dictLabelModel';

enum Api {
  DictLabel = '/dictLabel',
}

export function saveDictLabel(params: DictLabelDTO) {
  return defHttp.post<any>({ url: Api.DictLabel, params }, { errorMessageMode: 'message' });
}

export function delDictLabel(params: string) {
  return defHttp.delete<any>(
    { url: Api.DictLabel + '?id=', params },
    { errorMessageMode: 'message' },
  );
}

export function modifyDictLabel(params: DictLabelDTO) {
  return defHttp.put<DictLabelDTO>({ url: Api.DictLabel, params }, { errorMessageMode: 'message' });
}

export function getByCode(code: string) {
  const params = { code: code };
  return defHttp.get<DictLabelDTO[]>(
    { url: Api.DictLabel, params },
    { errorMessageMode: 'message' },
  );
}
