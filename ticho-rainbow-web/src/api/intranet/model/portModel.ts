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
  /** 通道状态;1-激活,0-未激活 */
  channelStatus: number;
  /** 排序 */
  sort: number;
  /** 备注信息 */
  remark: string;
}

export interface PortQuery {
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
