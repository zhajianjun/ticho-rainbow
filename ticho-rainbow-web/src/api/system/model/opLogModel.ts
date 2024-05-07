export interface OpLogDTO {
  /** 主键编号 */
  id: string;
  /** 名称 */
  name: string;
  /** 请求地址 */
  url: string;
  /** 请求类型 */
  type: string;
  /** 请求方法 */
  position: string;
  /** 请求体 */
  reqBody: string;
  /** 请求参数 */
  reqParams: string;
  /** 请求头 */
  reqHeaders: string;
  /** 响应体 */
  resBody: string;
  /** 响应头 */
  resHeaders: string;
  /** 请求开始时间 */
  startTime: string;
  /** 请求结束时间 */
  endTime: string;
  /** 请求间隔(毫秒) */
  consume: number;
  /** mdc信息 */
  mdc: string;
  /** 链路id */
  traceId: string;
  /** 请求IP */
  ip: string;
  /** 响应状态 */
  resStatus: number;
  /** 操作人 */
  operateBy: string;
  /** 创建时间 */
  createTime: string;
  /** 是否异常 */
  isErr: number;
  /** 异常信息 */
  errMessage: string;
}

export interface OpLogQuery {
  /** 主键编号 */
  id: string;
  /** 名称 */
  name: string;
  /** 请求地址 */
  url: string;
  /** 请求类型 */
  type: string;
  /** 请求体 */
  reqBody: string;
  /** 请求参数 */
  reqParams: string;
  /** 请求头 */
  reqHeaders: string;
  /** 响应体 */
  resBody: string;
  /** 响应头 */
  resHeaders: string;
  /** 请求开始时间 */
  startTime: string[];
  /** 请求结束时间 */
  endTime: string[];
  /** 请求间隔开始 */
  consumeStart: number;
  /** 请求间隔结束 */
  consumeEnd: number;
  /** 链路id */
  traceId: string;
  /** 请求IP */
  ip: string;
  /** 响应状态 */
  resStatus: number;
  /** 操作人 */
  operateBy: string;
  /** 创建时间 */
  createTime: string;
  /** 是否异常 */
  isErr: number;
  /** 异常信息 */
  errMessage: string;
}
