export interface TaskDTO {
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
  /** 任务状态;1-启用,0-停用 */
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
  /** 当前页码 */
  pageNum: number;
  /** 页面大小 */
  pageSize: number;
  /** 主键编号列表 */
  ids: string[];
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
  /** 任务状态;1-启用,0-停用 */
  status: number;
  /** 乐观锁;控制版本更改 */
  version: string;
  /** 创建人 */
  createBy: string;
  /** 创建时间 */
  createTime: string;
}

export interface TaskSaveCommand {
  name: string;
  content: string;
  param: string;
  cronExpression: string;
  remark: string;
}

export interface TaskModifyCommand {
  id: number;
  name: string;
  content: string;
  param: string;
  cronExpression: string;
  remark: string;
  version: number;
}

export interface TaskRunOnceCommand {
  id: number;
  param: string;
}
