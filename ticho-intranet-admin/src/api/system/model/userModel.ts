export interface UserDTO {
  /** 主键标识 */
  id: number;
  /** 用户名 */
  username: string;
  /** 备注信息 */
  remark: string;
  /** 乐观锁 */
  version: number;
  /** 创建人 */
  createBy: string;
  /** 创建时间 */
  createTime: string;
  /** 更新人 */
  updateBy: string;
  /** 更新时间 */
  updateTime: string;
}

export interface UserPassworUpdDTO {
  /** 用户名 */
  username: string;
  /** 密码 */
  password: string;
  /** 新密码 */
  newPassword: number;
}
