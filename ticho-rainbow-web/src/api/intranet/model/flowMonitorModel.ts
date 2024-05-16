import { PortDTO } from '@/api/intranet/model/portModel';

export interface FlowMonitorDTO {
  /** 端口 */
  port: number;
  /** 读取流量 */
  readBytes: number;
  /** 写入流量 */
  writeBytes: number;
  /** 读取消息数 */
  readMsgs: number;
  /** 写入消息数 */
  writeMsgs: number;
  /** 通道连接数 */
  channels: number;
  /** 端口信息 */
  portInfo: PortDTO;
}

export interface FlowMonitorStatsDTO {
  /** 客户端数 */
  clients: number;
  /** 激活客户端数 */
  activeClients: number;
  /** 端口总数 */
  ports: number;
  /** 激活端口总数 */
  activePorts: number;
  /** 统计时间 */
  dateTime: string;
  /** 激活端口流量明细 */
  flowDetails: FlowMonitorDTO[];
}
