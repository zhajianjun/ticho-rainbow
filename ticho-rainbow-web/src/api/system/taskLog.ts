import { defHttp } from '@/utils/http/axios';
import { TaskLogDTO, TaskLogQuery } from './model/taskLogModel';
import { RetryRequest } from '#/axios';

enum Api {
  TaskLogPage = '/taskLog/page',
  Export = '/taskLog/expExcel',
}

export function taskLogPage(params?: TaskLogQuery) {
  return defHttp.post<TaskLogDTO[]>({ url: Api.TaskLogPage, params }, { errorMessageMode: 'none' });
}

export function expExcel(params?: TaskLogQuery) {
  return defHttp.post<any>(
    { url: Api.Export, params, responseType: 'blob' },
    {
      errorMessageMode: 'message',
      isReturnNativeResponse: true,
      retryRequest: { isOpenRetry: false } as RetryRequest,
    },
  );
}
