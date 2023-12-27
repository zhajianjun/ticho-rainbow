export interface ClientDTO {
  /** 主键标识 */
  id: string;
  /** 客户端秘钥 */
  accessKey: string;
  /** 客户端名称 */
  name: string;
  /** 是否开启;1-开启,0-关闭 */
  enabled: number;
  /** 排序 */
  sort: number;
  /** 备注信息 */
  remark: string;
  /** 乐观锁 */
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
