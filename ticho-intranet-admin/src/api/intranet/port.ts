import { defHttp } from '@/utils/http/axios';
import { PortDTO, PortQuery } from './model/portModel';

enum Api {
  Port = '/port',
  PortPage = '/port/page',
}

export function savePort(params: PortDTO) {
  return defHttp.post<any>({ url: Api.Port, params }, { errorMessageMode: 'message' });
}

export function delPort(params: string) {
  return defHttp.delete<any>({ url: Api.Port + '?id=', params }, { errorMessageMode: 'message' });
}

export function modifyPort(params: PortDTO) {
  return defHttp.put<any>({ url: Api.Port, params }, { errorMessageMode: 'message' });
}

export function portPage(params?: PortQuery) {
  return defHttp.get<any>({ url: Api.PortPage, params }, { errorMessageMode: 'message' });
}
