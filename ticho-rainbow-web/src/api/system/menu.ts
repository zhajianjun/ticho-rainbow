import { defHttp } from '@/utils/http/axios';
import { MenuDtlModule } from './model/menuModel';

enum Api {
  Menu = '/menu',
  AllMenu = '/menu/all',
  Routes = '/menu/route',
}

export const getMenuList = () => defHttp.get<MenuDtlModule[]>({ url: Api.AllMenu });

export const getRoutes = () => defHttp.get<MenuDtlModule[]>({ url: Api.Routes });

export function saveMenu(params: any) {
  return defHttp.post<any>({ url: Api.Menu, params }, { errorMessageMode: 'message' });
}

export function delMenu(id: string) {
  const params = { id: id };
  return defHttp.delete<any>(
    { url: Api.Menu, params },
    { errorMessageMode: 'message', joinParamsToUrl: true },
  );
}

export function modifyMenu(params: any) {
  return defHttp.put<any>({ url: Api.Menu, params }, { errorMessageMode: 'message' });
}
