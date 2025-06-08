import { defHttp } from '@/utils/http/axios';
import { OpLogDTO, OpLogQuery } from './model/opLogModel';
import { RetryRequest } from '#/axios';

enum Api {
  OpLogPage = '/op-log/page',
  Export = '/op-log/excel/export',
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
