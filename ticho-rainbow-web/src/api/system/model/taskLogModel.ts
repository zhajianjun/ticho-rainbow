export interface TaskLogDTO {
  /** 主键编号 */
  id: string;
  /** 任务ID */
  taskId: string;
  /** 任务内容 */
  content: string;
  /** 任务参数 */
  param: string;
  /** 执行时间 */
  executeTime: string;
  /** 执行开始时间 */
  startTime: string;
  /** 执行结束时间 */
  endTime: string;
  /** 执行间隔(毫秒) */
  consume: number;
  /** mdc信息 */
  mdc: string;
  /** 链路id */
  traceId: string;
  /** 执行状态;1-执行成功,0-执行异常 */
  status: number;
  /** 操作人 */
  operateBy: string;
  /** 创建时间 */
  createTime: string;
  /** 是否异常 */
  isErr: number;
  /** 异常信息 */
  errMessage: string;
}

export interface TaskLogQuery {
  /** 当前页码 */
  pageNum: number;
  /** 页面大小 */
  pageSize: number;
  /** 主键编号列表 */
  ids: string[];
  /** 主键编号 */
  id: string;
  /** 任务ID */
  taskId: string;
  /** 任务内容 */
  content: string;
  /** 任务参数 */
  param: string;
  /** 执行时间 */
  executeTime: string[];
  /** 执行开始时间 */
  startTime: string[];
  /** 执行结束时间 */
  endTime: string[];
  /** 执行间隔开始 */
  consumeStart: number;
  /** 执行间隔结束 */
  consumeEnd: number;
  /** mdc信息 */
  mdc: string;
  /** 链路id */
  traceId: string;
  /** 执行状态;1-执行成功,0-执行异常 */
  status: number;
  /** 操作人 */
  operateBy: string;
  /** 创建时间 */
  createTime: string;
  /** 是否异常 */
  isErr: number;
  /** 异常信息 */
  errMessage: string;
}
