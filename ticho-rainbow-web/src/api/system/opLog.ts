import { defHttp } from '@/utils/http/axios';
import { OpLogDTO, OpLogQuery } from './model/opLogModel';
import { RetryRequest } from '#/axios';

enum Api {
  OpLog = '/op-log',
  OpLogPage = '/op-log/page',
  Export = '/op-log/excel/export',
}

export function getOpLog(id: string) {
  const params = { id: id };
  return defHttp.get<any>({ url: Api.OpLog, params }, { errorMessageMode: 'message' });
}

export function opLogPage(params?: OpLogQuery) {
  return defHttp.get<OpLogDTO[]>({ url: Api.OpLogPage, params }, { errorMessageMode: 'none' });
}

export function expExcel(params?: OpLogQuery) {
  return defHttp.get<any>(
    { url: Api.Export, params, responseType: 'blob' },
    {
      errorMessageMode: 'message',
      isReturnNativeResponse: true,
      retryRequest: { isOpenRetry: false } as RetryRequest,
    },
  );
}
