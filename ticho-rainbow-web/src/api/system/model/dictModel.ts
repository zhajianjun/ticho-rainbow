import { DictLabelDTO } from '@/api/system/model/dictLabelModel';

export interface DictDTO {
  /** 主键编号 */
  id: string;
  /** 字典编码 */
  code: string;
  /** 字典名称 */
  name: string;
  /** 是否系统字典;1-是,0-否 */
  isSys: number;
  /** 状态;1-启用,0-停用 */
  status: number;
  /** 备注信息 */
  remark: string;
  /** 创建时间 */
  createTime: string;
  /** 字典标签详情 */
  details: DictLabelDTO[];
}

export interface DictQuery {
  /** 主键编号 */
  id: string;
  /** 字典编码 */
  code: string;
  /** 字典名称 */
  name: string;
  /** 是否系统字典;1-是,0-否 */
  isSys: number;
  /** 状态;1-启用,0-停用 */
  status: number;
  /** 备注信息 */
  remark: string;
}
