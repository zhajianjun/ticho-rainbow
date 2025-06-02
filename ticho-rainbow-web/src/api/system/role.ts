import { defHttp } from '@/utils/http/axios';
import {
  RoleDtlQuery,
  RoleDTO,
  RoleMenuDtlDTO,
  RoleQuery,
  RoleSaveCommand,
} from '@/api/system/model/roleModel';
import { RetryRequest } from '#/axios';
import { VersionModifyCommand } from '@/api/system/model/baseModel';

enum Api {
  Role = '/role',
  RoleEnable = '/role/status/enable',
  RoleDisable = '/role/status/disable',
  RolePage = '/role/page',
  RoleList = '/role/all',
  ListRoleMenu = '/role/menu/list',
  Export = '/role/expExcel',
}

export function rolePage(params?: RoleQuery) {
  return defHttp.get<RoleDTO>({ url: Api.RolePage, params }, { errorMessageMode: 'none' });
}

export function listRoles() {
  return defHttp.get<RoleDTO[]>({ url: Api.RoleList }, { errorMessageMode: 'none' });
}

export function saveRole(params: RoleSaveCommand) {
  return defHttp.post<any>(
    { url: Api.Role, params },
    { successMessageMode: 'message', errorMessageMode: 'message' },
  );
}

export function delRole(params: VersionModifyCommand) {
  return defHttp.delete<any>(
    { url: Api.Role, params },
    { successMessageMode: 'message', errorMessageMode: 'message' },
  );
}

export function enableRole(params: VersionModifyCommand[]) {
  return defHttp.patch<void>({ url: Api.RoleEnable, params }, { errorMessageMode: 'message' });
}

export function disableRole(params: VersionModifyCommand[]) {
  return defHttp.patch<void>({ url: Api.RoleDisable, params }, { errorMessageMode: 'message' });
}

export function listRoleMenu(params?: RoleDtlQuery) {
  return defHttp.get<RoleMenuDtlDTO>(
    { url: Api.ListRoleMenu, params },
    { errorMessageMode: 'message' },
  );
}

export function expExcel(params?: RoleQuery) {
  return defHttp.post<any>(
    { url: Api.Export, params, responseType: 'blob' },
    {
      errorMessageMode: 'message',
      isReturnNativeResponse: true,
      retryRequest: { isOpenRetry: false } as RetryRequest,
    },
  );
}
