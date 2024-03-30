import { defHttp } from '@/utils/http/axios';
import { OpLogDTO, OpLogQuery } from './model/opLogModel';

enum Api {
  OpLog = '/opLog',
  OpLogPage = '/opLog/page',
}

export function getOpLog(id: string) {
  const params = { id: id };
  return defHttp.get<any>({ url: Api.OpLog, params }, { errorMessageMode: 'message' });
}

export function opLogPage(params?: OpLogQuery) {
  return defHttp.get<OpLogDTO[]>({ url: Api.OpLogPage, params }, { errorMessageMode: 'none' });
}
