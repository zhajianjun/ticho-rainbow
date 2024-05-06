import { defHttp } from '@/utils/http/axios';
import { TaskLogDTO, TaskLogQuery } from './model/taskLogModel';

enum Api {
  TaskLogPage = '/taskLog/page',
}

export function taskLogPage(params?: TaskLogQuery) {
  return defHttp.get<TaskLogDTO[]>({ url: Api.TaskLogPage, params }, { errorMessageMode: 'none' });
}
