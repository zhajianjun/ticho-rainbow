export interface FileInfoDTO {
  /** 主键编号 */
  id: number;
  /** 存储类型;1-公共,2-私有 */
  type: number;
  /** 文件名 */
  fileName: string;
  /** 文件扩展名 */
  ext: string;
  /** 存储路径 */
  path: string;
  /** 文件大小;单位字节 */
  size: number;
  /** MIME类型 */
  contentType: string;
  /** 原始文件名 */
  originalFilename: string;
  /** 分片id */
  chunkId: string;
  /** 文件元数据 */
  metadata: string;
  /** 状态;1-正常,2-停用,3-分片上传,99-作废 */
  status: number;
  /** 备注信息 */
  remark: string;
  /** 乐观锁;控制版本更改 */
  version: number;
  /** 创建人 */
  createBy: string;
  /** 创建时间 */
  createTime: string;
  /** 更新人 */
  updateBy: string;
  /** 修改时间 */
  updateTime: string;
  /** 删除标识;0-未删除,1-已删除 */
  isDelete: number;
}

export interface FileInfoQuery {
  /** 主键编号 */
  id: string;
  /** 存储类型;1-公共,2-私有 */
  type: number;
  /** 文件名 */
  fileName: string;
  /** 文件扩展名 */
  ext: string;
  /** 存储路径 */
  path: string;
  /** 文件大小;单位字节 */
  size: string;
  /** MIME类型 */
  contentType: string;
  /** 原始文件名 */
  originalFilename: string;
  /** 文件元数据 */
  metadata: string;
  /** 状态;1-正常,2-停用,3-分片上传,99-作废 */
  status: number;
  /** 备注信息 */
  remark: string;
  /** 乐观锁;控制版本更改 */
  version: string;
  /** 创建人 */
  createBy: string;
  /** 创建时间 */
  createTime: string;
  /** 更新人 */
  updateBy: string;
  /** 修改时间 */
  updateTime: string;
  /** 删除标识;0-未删除,1-已删除 */
  isDelete: number;
}
