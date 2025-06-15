import { defHttp } from '@/utils/http/axios';
import { PortDTO, PortModifyfCommand, PortQuery, PortSaveCommand } from './model/portModel';
import { RetryRequest } from '#/axios';
import { VersionModifyCommand } from '@/api/system/model/baseModel';

enum Api {
  Port = '/port',
  PortEnable = '/port/status/enable',
  PortDisable = '/port/status/disable',
  PortPage = '/port/page',
  Export = '/port/excel/export',
}

export function savePort(params: PortSaveCommand) {
  return defHttp.post<void>(
    { url: Api.Port, params },
    { successMessageMode: 'message', errorMessageMode: 'message' },
  );
}

export function delPort(params: VersionModifyCommand) {
  return defHttp.delete<void>(
    { url: Api.Port, params },
    { successMessageMode: 'message', errorMessageMode: 'message' },
  );
}

export function modifyPort(params: PortModifyfCommand) {
  return defHttp.put<void>(
    { url: Api.Port, params },
    { successMessageMode: 'message', errorMessageMode: 'message' },
  );
}

export function enablePort(params: VersionModifyCommand[]) {
  return defHttp.patch<void>({ url: Api.PortEnable, params }, { errorMessageMode: 'message' });
}

export function disablePort(params: VersionModifyCommand[]) {
  return defHttp.patch<void>({ url: Api.PortDisable, params }, { errorMessageMode: 'message' });
}

export function portPage(params?: PortQuery) {
  return defHttp.get<PortDTO[]>({ url: Api.PortPage, params }, { errorMessageMode: 'message' });
}

export function expExcel(params?: PortQuery) {
  return defHttp.get<any>(
    { url: Api.Export, params, responseType: 'blob' },
    {
      errorMessageMode: 'message',
      isReturnNativeResponse: true,
      retryRequest: { isOpenRetry: false } as RetryRequest,
    },
  );
}
