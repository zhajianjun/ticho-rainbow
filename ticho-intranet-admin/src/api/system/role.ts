import { defHttp } from '@/utils/http/axios';
import {
  RoleDTO,
  RoleMenuBindDTO,
  RoleMenuDtlDTO,
  RoleMenuQueryDTO,
} from '@/api/system/model/roleModel';

enum Api {
  Role = '/role',
  RolePage = '/role/page',
  RoleList = '/role/list',
  ListRoleMenuByIds = '/role/listRoleMenuByIds',
  BindMenu = '/role/bindMenu',
  UpdateStatus = '/role/updateStatus',
}

export function rolePage(params?: RoleDTO) {
  return defHttp.get<RoleDTO>({ url: Api.RolePage, params }, { errorMessageMode: 'none' });
}

export function listRoles(params?: RoleDTO) {
  return defHttp.get<RoleDTO[]>({ url: Api.RoleList, params }, { errorMessageMode: 'none' });
}

export function saveRole(params: any) {
  return defHttp.post<any>({ url: Api.Role, params }, { errorMessageMode: 'message' });
}

export function delRole(params: string) {
  return defHttp.delete<any>({ url: Api.Role + '?id=', params }, { errorMessageMode: 'message' });
}

export function modifyRole(params: any) {
  return defHttp.put<any>({ url: Api.Role, params }, { errorMessageMode: 'message' });
}

export function modifyRoleStatus(params: any) {
  return defHttp.put<any>({ url: Api.UpdateStatus, params }, { errorMessageMode: 'message' });
}

export function listRoleMenuByIds(params?: RoleMenuQueryDTO) {
  return defHttp.post<RoleMenuDtlDTO>(
    { url: Api.ListRoleMenuByIds, params },
    { errorMessageMode: 'message' },
  );
}

export function bindMenu(params?: RoleMenuBindDTO) {
  return defHttp.post<any>({ url: Api.BindMenu, params }, { errorMessageMode: 'message' });
}
