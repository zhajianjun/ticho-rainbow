export interface PortDTO {
  /** 主键标识 */
  id: string;
  /** 客户端秘钥 */
  accessKey: string;
  /** 主机端口 */
  port: number;
  /** 客户端地址 */
  endpoint: string;
  /** 域名 */
  domain: string;
  /** 是否开启;1-开启,0-关闭 */
  enabled: number;
  /** 是否永久 */
  forever: number;
  /** 过期时间 */
  expireAt: string;
  /** 协议类型 */
  type: number;
  /** 排序 */
  sort: number;
  /** 备注信息 */
  remark: string;
  /** 乐观锁;控制版本更改 */
  version: string;
  /** 创建人 */
  createBy: string;
  /** 创建时间 */
  createTime: string;
  /** 更新人 */
  updateBy: string;
  /** 更新时间 */
  updateTime: string;
  /** 删除标识;0-未删除,1-已删除 */
  isDelete: number;
}
