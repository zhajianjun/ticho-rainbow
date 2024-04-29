export interface TaskDTO {
  /** 任务ID */
  id: number;
  /** 任务名称 */
  name: string;
  /** 任务内容 */
  content: string;
  /** 任务参数 */
  param: string;
  /** cron执行表达式 */
  cronExpression: string;
  /** 备注信息 */
  remark: string;
  /** 任务状态;1-正常,0-停用 */
  status: number;
  /** 创建人 */
  createBy: string;
  /** 创建时间 */
  createTime: string;
  /** 修改人 */
  updateBy: string;
  /** 修改时间 */
  updateTime: string;
}

export interface TaskQuery {
  /** 任务ID */
  id: string;
  /** 任务名称 */
  name: string;
  /** 任务内容 */
  content: string;
  /** 任务参数 */
  param: string;
  /** cron执行表达式 */
  cronExpression: string;
  /** 备注信息 */
  remark: string;
  /** 任务状态;1-正常,0-停用 */
  status: number;
  /** 乐观锁;控制版本更改 */
  version: string;
  /** 创建人 */
  createBy: string;
  /** 创建时间 */
  createTime: string;
  /** 修改人 */
  updateBy: string;
  /** 修改时间 */
  updateTime: string;
}
