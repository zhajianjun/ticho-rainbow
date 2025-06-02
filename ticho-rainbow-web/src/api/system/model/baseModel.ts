export interface PageResult<T> {
  /** 页码，从1开始 */
  pageNum: number;
  /** 页面大小 */
  pageSize: number;
  /** 总页数 */
  pages: number;
  /** 总数 */
  total: number;
  /** 查询数据列表 */
  rows: T[];
}

export interface VersionModifyCommand {
  id: number;
  version: number;
}
