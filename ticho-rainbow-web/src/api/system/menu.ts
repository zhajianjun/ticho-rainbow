import { defHttp } from '@/utils/http/axios';
import { MenuModule } from './model/menuModel';
import { VersionModifyCommand } from '@/api/system/model/baseModel';

enum Api {
  Menu = '/menu',
  AllMenu = '/menu/all',
  Routes = '/menu/route',
  MenuEnable = '/menu/status/enable',
  MenuDisable = '/menu/status/disable',
}

export const getMenuList = () => defHttp.get<MenuModule[]>({ url: Api.AllMenu });

export const getRoutes = () => defHttp.get<MenuModule[]>({ url: Api.Routes });

export function saveMenu(params: any) {
  return defHttp.post<any>(
    { url: Api.Menu, params },
    { successMessageMode: 'message', errorMessageMode: 'message' },
  );
}

export function delMenu(params: VersionModifyCommand) {
  return defHttp.delete<any>(
    { url: Api.Menu, params },
    { successMessageMode: 'message', errorMessageMode: 'message' },
  );
}

export function modifyMenu(params: any) {
  return defHttp.put<any>(
    { url: Api.Menu, params },
    { successMessageMode: 'message', errorMessageMode: 'message' },
  );
}

export function enableMenu(params: VersionModifyCommand[]) {
  return defHttp.patch<void>(
    { url: Api.MenuEnable, params },
    {
      errorMessageMode: 'message',
    },
  );
}

export function disableMenu(params: VersionModifyCommand[]) {
  return defHttp.patch<void>(
    { url: Api.MenuDisable, params },
    {
      errorMessageMode: 'message',
    },
  );
}
