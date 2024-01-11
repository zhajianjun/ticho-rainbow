export interface ClientDTO {
  /** 主键标识 */
  id: number;
  /** 客户端秘钥 */
  accessKey: string;
  /** 客户端名称 */
  name: string;
  /** 是否开启;1-开启,0-关闭 */
  enabled: number;
  /** 通道状态;1-激活,0-未激活 */
  channelStatus: number;
  /** 排序 */
  sort: number;
  /** 备注信息 */
  remark: string;
}

export interface ClientQuery {
  /** 客户端秘钥 */
  accessKey: string;
  /** 客户端名称 */
  name: string;
  /** 备注信息 */
  remark: string;
}
