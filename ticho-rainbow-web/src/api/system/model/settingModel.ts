export interface SettingDTO {
  /** 主键编号 */
  id: number;
  /** key */
  key: string;
  /** value */
  value: string;
  /** 排序 */
  sort: number;
  /** 备注信息 */
  remark: string;
  /** 版本号 */
  version: number;
  /** 创建人 */
  createBy: string;
  /** 创建时间 */
  createTime: string;
  /** 修改人 */
  updateBy: string;
  /** 修改时间 */
  updateTime: string;
}

export interface SettingQuery {
  ids: number[];
  /** key */
  key: string;
  /** value */
  value: string;
  /** 备注信息 */
  remark: string;
}

export interface SettingSaveCommand {
  /** key */
  key: string;
  /** value */
  value: string;
  /** 排序 */
  sort: number;
  /** 备注信息 */
  remark: string;
}

export interface SettingModifyCommand {
  /** 主键编号 */
  id: number;
  /** key */
  key: string;
  /** value */
  value: string;
  /** 排序 */
  sort: number;
  /** 备注信息 */
  remark: string;
  /** 版本号 */
  version: number;
}
