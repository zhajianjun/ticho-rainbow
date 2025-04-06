import { defHttp } from '@/utils/http/axios';
import { TaskLogDTO, TaskLogQuery } from './model/taskLogModel';
import { RetryRequest } from '#/axios';

enum Api {
  TaskLogPage = '/task-log/page',
  Export = '/task-log/excel/export',
}

export function taskLogPage(params?: TaskLogQuery) {
  return defHttp.get<TaskLogDTO[]>({ url: Api.TaskLogPage, params }, { errorMessageMode: 'none' });
}

export function expExcel(params?: TaskLogQuery) {
  return defHttp.get<any>(
    { url: Api.Export, params, responseType: 'blob' },
    {
      errorMessageMode: 'message',
      isReturnNativeResponse: true,
      retryRequest: { isOpenRetry: false } as RetryRequest,
    },
  );
}
