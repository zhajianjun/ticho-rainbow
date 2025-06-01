import { defHttp } from '@/utils/http/axios';
import {
  RoleDTO,
  RoleMenuDtlDTO,
  RoleDtlQuery,
  RoleQuery,
  RoleSaveCommand,
  RoleModifyCommand, RoleStatusModifyCommand,
} from '@/api/system/model/roleModel';
import { RetryRequest } from '#/axios';

enum Api {
  Role = '/role',
  RolePage = '/role/page',
  RoleList = '/role/all',
  ListRoleMenu = '/role/menu/list',
  ModifyStatus = '/role/status',
  Export = '/role/expExcel',
}

export function rolePage(params?: RoleQuery) {
  return defHttp.get<RoleDTO>({ url: Api.RolePage, params }, { errorMessageMode: 'none' });
}

export function listRoles() {
  return defHttp.get<RoleDTO[]>({ url: Api.RoleList }, { errorMessageMode: 'none' });
}

export function saveRole(params: RoleSaveCommand) {
  return defHttp.post<any>({ url: Api.Role, params }, { errorMessageMode: 'message' });
}

export function delRole(id: string) {
  const params = { id: id };
  return defHttp.delete<any>(
    { url: Api.Role, params },
    { errorMessageMode: 'message', joinParamsToUrl: true },
  );
}

export function modifyRole(params: RoleModifyCommand) {
  return defHttp.put<any>({ url: Api.Role, params }, { errorMessageMode: 'message' });
}

export function modifyRoleStatus(params: RoleStatusModifyCommand) {
  return defHttp.patch<any>({ url: Api.ModifyStatus, params }, { errorMessageMode: 'message' });
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
