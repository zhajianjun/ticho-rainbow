import { defHttp } from '@/utils/http/axios';
import {
  RoleDTO,
  RoleMenuBindDTO,
  RoleMenuDtlDTO,
  RoleMenuQueryDTO,
  RoleQuery,
} from '@/api/system/model/roleModel';
import { RetryRequest } from '#/axios';

enum Api {
  Role = '/role',
  RolePage = '/role/page',
  RoleList = '/role/list',
  ListRoleMenu = '/role/listRoleMenu',
  BindMenu = '/role/bindMenu',
  UpdateStatus = '/role/updateStatus',
  Export = '/role/expExcel',
}

export function rolePage(params?: RoleQuery) {
  return defHttp.post<RoleDTO>({ url: Api.RolePage, params }, { errorMessageMode: 'none' });
}

export function listRoles(params?: RoleDTO) {
  return defHttp.post<RoleDTO[]>({ url: Api.RoleList, params }, { errorMessageMode: 'none' });
}

export function saveRole(params: any) {
  return defHttp.post<any>({ url: Api.Role, params }, { errorMessageMode: 'message' });
}

export function delRole(id: string) {
  const params = { id: id };
  return defHttp.delete<any>(
    { url: Api.Role, params },
    { errorMessageMode: 'message', joinParamsToUrl: true },
  );
}

export function modifyRole(params: any) {
  return defHttp.put<any>({ url: Api.Role, params }, { errorMessageMode: 'message' });
}

export function modifyRoleStatus(params: any) {
  return defHttp.put<any>({ url: Api.UpdateStatus, params }, { errorMessageMode: 'message' });
}

export function listRoleMenu(params?: RoleMenuQueryDTO) {
  return defHttp.post<RoleMenuDtlDTO>(
    { url: Api.ListRoleMenu, params },
    { errorMessageMode: 'message' },
  );
}

export function bindMenu(params?: RoleMenuBindDTO) {
  return defHttp.post<any>({ url: Api.BindMenu, params }, { errorMessageMode: 'message' });
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
