export interface SettingDTO {
  id: number;
  key: string;
  value: string;
  sort: number;
  remark: string;
  version: number;
  createBy: string;
  createTime: string;
  updateBy: string;
  updateTime: string;
}

export interface SettingSaveCommand {
  key: string;
  value: string;
  sort: number;
  remark: string;
}

export interface SettingModifyComman {
  id: number;
  key: string;
  value: string;
  sort: number;
  remark: string;
  version: number;
}

export interface SettingQuery {
  ids: number[];
  key: string;
  value: string;
  remark: string;
  pageNum: number;
  pageSize: number;
  count: boolean;
}
