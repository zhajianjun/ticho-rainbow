export interface ClientDTO {
  /** 主键标识 */
  id: number;
  /** 客户端秘钥 */
  accessKey: string;
  /** 客户端名称 */
  name: string;
  /** 过期时间 */
  expireAt: string;
  /** 状态;1-启用,0-停用 */
  status: number;
  /** 通道状态;1-激活,0-未激活 */
  channelStatus: number;
  /** 排序 */
  sort: number;
  /** 备注信息 */
  remark: string;
}

export interface ClientQuery {
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
  /** 客户端名称 */
  name: string;
  /** 客户端名称 */
  expireAt: string;
  /** 客户端名称 */
  status: number;
  /** 备注信息 */
  remark: string;
}

export interface ClientSaveCommand {
  accessKey: string;
  name: string;
  expireAt: string;
  status: number;
  sort: number;
  remark: string;
}

export interface ClientModifyCommand {
  id: number;
  name: string;
  expireAt: string;
  status: number;
  sort: number;
  remark: string;
  version: number;
}
