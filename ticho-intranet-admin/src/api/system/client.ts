import { defHttp } from '@/utils/http/axios';
import { ClientDTO } from './model/clientModel';

enum Api {
  Client = '/client',
  ClientPage = '/client/page',
}

export function saveClient(params: ClientDTO) {
  return defHttp.post<any>({ url: Api.Client, params }, { errorMessageMode: 'message' });
}

export function delClient(params: string) {
  return defHttp.delete<any>({ url: Api.Client + '?id=', params }, { errorMessageMode: 'message' });
}

export function modifyClient(params: ClientDTO) {
  return defHttp.put<any>({ url: Api.Client, params }, { errorMessageMode: 'message' });
}

export function clientPage(params?: ClientDTO) {
  return defHttp.get<any>({ url: Api.ClientPage, params }, { errorMessageMode: 'none' });
}
