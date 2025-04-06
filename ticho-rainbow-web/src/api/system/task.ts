import { defHttp } from '@/utils/http/axios';
import { TaskDTO, TaskQuery } from './model/taskModel';
import { RetryRequest } from '#/axios';

enum Api {
  Task = '/task',
  TaskPage = '/task/page',
  AllTasks = '/task/all',
  RunOnceTask = '/task/run-once',
  PauseTask = '/task/pause',
  ResumeTask = '/task/resume',
  Export = '/task/excel/export',
}

export function saveTask(params: TaskDTO) {
  return defHttp.post<any>({ url: Api.Task, params }, { errorMessageMode: 'message' });
}

export function delTask(id: string) {
  const params = { id: id };
  return defHttp.delete<any>(
    { url: `${Api.Task}`, params },
    { errorMessageMode: 'message', joinParamsToUrl: true },
  );
}

export function modifyTask(params: TaskDTO) {
  return defHttp.put<any>({ url: Api.Task, params }, { errorMessageMode: 'message' });
}

export function taskPage(params?: TaskQuery) {
  return defHttp.get<TaskDTO[]>({ url: Api.TaskPage, params }, { errorMessageMode: 'none' });
}

export function allTasks(params?: TaskQuery) {
  return defHttp.get<TaskDTO[]>({ url: Api.AllTasks, params }, { errorMessageMode: 'none' });
}

export function runOnceTask(params) {
  return defHttp.get<any>(
    { url: Api.RunOnceTask, params },
    { errorMessageMode: 'message', successMessageMode: 'message' },
  );
}

export function pauseTask(id: string) {
  const params = { id: id };
  return defHttp.get<any>({ url: Api.PauseTask, params }, { errorMessageMode: 'message', successMessageMode: 'message' });
}

export function resumeTask(id: string) {
  const params = { id: id };
  return defHttp.get<any>({ url: Api.ResumeTask, params }, { errorMessageMode: 'message', successMessageMode: 'message' });
}

export function expExcel(params?: TaskQuery) {
  return defHttp.get<any>(
    { url: Api.Export, params, responseType: 'blob' },
    {
      errorMessageMode: 'message',
      isReturnNativeResponse: true,
      retryRequest: { isOpenRetry: false } as RetryRequest,
    },
  );
}
