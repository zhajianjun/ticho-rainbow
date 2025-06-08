import { defHttp } from '@/utils/http/axios';
import {
  TaskDTO,
  TaskModifyCommand,
  TaskQuery,
  TaskRunOnceCommand,
  TaskSaveCommand,
} from './model/taskModel';
import { RetryRequest } from '#/axios';
import { VersionModifyCommand } from '@/api/system/model/baseModel';

enum Api {
  Task = '/task',
  TaskPage = '/task/page',
  AllTasks = '/task/all',
  TaskRunOnce = '/task/run-once',
  TaskEnable = '/task/status/enable',
  TaskDisable = '/task/status/disable',
  Export = '/task/excel/export',
}

export function saveTask(params: TaskSaveCommand) {
  return defHttp.post<any>(
    { url: Api.Task, params },
    { successMessageMode: 'message', errorMessageMode: 'message' },
  );
}

export function delTask(params: VersionModifyCommand) {
  return defHttp.delete<any>(
    { url: `${Api.Task}`, params },
    { successMessageMode: 'message', errorMessageMode: 'message' },
  );
}

export function modifyTask(params: TaskModifyCommand) {
  return defHttp.put<any>(
    { url: Api.Task, params },
    { successMessageMode: 'message', errorMessageMode: 'message' },
  );
}

export function taskPage(params?: TaskQuery) {
  return defHttp.get<TaskDTO[]>({ url: Api.TaskPage, params }, { errorMessageMode: 'none' });
}

export function allTasks(params?: TaskQuery) {
  return defHttp.get<TaskDTO[]>({ url: Api.AllTasks, params }, { errorMessageMode: 'none' });
}

export function runOnceTask(params: TaskRunOnceCommand) {
  return defHttp.post<any>(
    { url: Api.TaskRunOnce, params },
    { errorMessageMode: 'message', successMessageMode: 'message' },
  );
}

export function enableTask(params: VersionModifyCommand[]) {
  return defHttp.patch<any>({ url: Api.TaskEnable, params }, { errorMessageMode: 'message' });
}

export function disableTask(params: VersionModifyCommand[]) {
  return defHttp.patch<any>({ url: Api.TaskDisable, params }, { errorMessageMode: 'message' });
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
