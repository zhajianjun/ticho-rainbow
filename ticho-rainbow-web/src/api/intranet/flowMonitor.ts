import { defHttp } from '@/utils/http/axios';
import { FlowMonitorStatsDTO } from './model/flowMonitorModel';

enum Api {
  Info = '/flow-monitor/info',
}

export function flowMonitorInfo() {
  return defHttp.get<FlowMonitorStatsDTO>({ url: Api.Info }, { errorMessageMode: 'message' });
}
