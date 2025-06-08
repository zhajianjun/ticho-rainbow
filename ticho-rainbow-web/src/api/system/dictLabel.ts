import { defHttp } from '@/utils/http/axios';
import { DictLabelDTO, DictLabelModifyCommand, DictLabelSaveCommand } from './model/dictLabelModel';
import { VersionModifyCommand } from '@/api/system/model/baseModel';

enum Api {
  DictLabel = '/dict-label',
  DictLabelEnable = '/dict-label/status/enable',
  DictLabelDisable = '/dict-label/status/disable',
}

export function saveDictLabel(params: DictLabelSaveCommand) {
  return defHttp.post<any>(
    { url: Api.DictLabel, params },
    {
      errorMessageMode: 'message',
      successMessageMode: 'message',
    },
  );
}

export function delDictLabel(params: VersionModifyCommand) {
  return defHttp.delete<any>(
    { url: Api.DictLabel, params },
    {
      errorMessageMode: 'message',
      successMessageMode: 'message',
    },
  );
}

export function modifyDictLabel(params: DictLabelModifyCommand) {
  return defHttp.put<DictLabelDTO>(
    { url: Api.DictLabel, params },
    {
      errorMessageMode: 'message',
      successMessageMode: 'message',
    },
  );
}

export function findDictLabel(code: string) {
  const params = { code: code };
  return defHttp.get<DictLabelDTO[]>(
    { url: Api.DictLabel, params },
    { errorMessageMode: 'message' },
  );
}

export function enableDictLabel(params: VersionModifyCommand[]) {
  return defHttp.patch<void>(
    { url: Api.DictLabelEnable, params },
    {
      errorMessageMode: 'message',
    },
  );
}

export function disableDictLabel(params: VersionModifyCommand[]) {
  return defHttp.patch<void>(
    { url: Api.DictLabelDisable, params },
    {
      errorMessageMode: 'message',
    },
  );
}
