export interface DictLabelDTO {
  /** 主键编号 */
  id: number;
  /** 字典编码 */
  code: string;
  /** 字典标签 */
  label: string;
  /** 字典值 */
  value: string;
  /** 图标 */
  icon: string;
  /** 颜色 */
  color: string;
  /** 排序 */
  sort: number;
  /** 状态;1-启用,0-停用 */
  status: number;
  /** 备注信息 */
  remark: string;
}

export interface DictLabel {
  /** 字典标签 */
  label: string;
  /** 字典值 */
  value: string | number;
}
