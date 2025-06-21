import { defHttp } from '@/utils/http/axios';
import { RetryRequest } from '#/axios';
import { VersionModifyCommand } from '@/api/system/model/baseModel';
import {
  SettingDTO,
  SettingModifyCommand,
  SettingQuery,
  SettingSaveCommand,
} from '@/api/system/model/settingModel';

enum Api {
  Setting = '/setting',
  SettingPage = '/setting/page',
  SettingAll = '/setting/all',
  SettingExport = '/setting/excel/export',
}

export function settingPage(params?: SettingQuery) {
  return defHttp.get<SettingDTO>({ url: Api.SettingPage, params }, { errorMessageMode: 'none' });
}

export function allSettings() {
  return defHttp.get<SettingDTO[]>(
    { url: Api.SettingAll },
    { errorMessageMode: 'none', withToken: false },
  );
}

export function saveSetting(params: SettingSaveCommand) {
  return defHttp.post<any>(
    { url: Api.Setting, params },
    { successMessageMode: 'message', errorMessageMode: 'message' },
  );
}

export function modifySetting(params: SettingModifyCommand) {
  return defHttp.put<any>(
    { url: Api.Setting, params },
    { successMessageMode: 'message', errorMessageMode: 'message' },
  );
}

export function delSetting(params: VersionModifyCommand) {
  return defHttp.delete<any>(
    { url: Api.Setting, params },
    { successMessageMode: 'message', errorMessageMode: 'message' },
  );
}

export function exportSetting(params?: SettingQuery) {
  return defHttp.get<any>(
    { url: Api.SettingExport, params, responseType: 'blob' },
    {
      errorMessageMode: 'message',
      isReturnNativeResponse: true,
      retryRequest: { isOpenRetry: false } as RetryRequest,
    },
  );
}
