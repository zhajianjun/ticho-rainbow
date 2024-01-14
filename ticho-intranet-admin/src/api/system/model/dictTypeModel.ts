export interface DictTypeDTO {
  /** 主键编号 */
  id: number;
  /** 字典编码 */
  code: string;
  /** 字典名称 */
  name: string;
  /** 是否系统字典;1-是,0-否 */
  isSys: number;
  /** 状态;1-正常,0-停用 */
  status: number;
  /** 备注信息 */
  remark: string;
}

export interface DictTypeQuery {
  /** 主键编号 */
  id: string;
  /** 字典编码 */
  code: string;
  /** 字典名称 */
  name: string;
  /** 是否系统字典;1-是,0-否 */
  isSys: number;
  /** 状态;1-正常,0-停用 */
  status: number;
  /** 备注信息 */
  remark: string;
}
