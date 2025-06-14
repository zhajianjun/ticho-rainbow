export interface PortDTO {
  /** 主键标识 */
  id: number | null;
  /** 客户端秘钥 */
  accessKey: string;
  /** 主机端口 */
  port: number;
  /** 客户端地址 */
  endpoint: string;
  /** 域名 */
  domain: string;
  /** 状态;1-启用,0-停用 */
  enabled: number;
  /** 是否永久 */
  forever: number;
  /** 过期时间 */
  expireAt: string;
  /** 协议类型 */
  type: number;
  /** 客户端通道状态;1-激活,0-未激活 */
  clientChannelStatus: number;
  /** 应用通道状态;1-激活,0-未激活 */
  appChannelStatus: number;
  /** 排序 */
  sort: number;
  /** 备注信息 */
  remark: string;
}

export interface PortQuery {
  /** 当前页码 */
  pageNum: number;
  /** 页面大小 */
  pageSize: number;
  /** 主键编号列表 */
  ids: string[];
  /** 主键编号 */
  id: string;
  /** 客户端秘钥 */
  accessKey: string;
  /** 主机端口 */
  port: number;
  /** 客户端地址 */
  endpoint: string;
  /** 域名 */
  domain: string;
  /** 协议类型 */
  type: number;
  /** 备注信息 */
  remark: string;
}

export interface PortSaveCommand {
  accessKey: string;
  port: number;
  endpoint: string;
  domain: string;
  expireAt: string;
  type: number;
  sort: number;
  remark: string;
}

export interface PortModifyfCommand {
  id: number;
  accessKey: string;
  port: number;
  endpoint: string;
  domain: string;
  expireAt: string;
  type: number;
  sort: number;
  remark: string;
  version: number;
}
