export interface UploadApiResult {
  msg: string;
  code: number;
  data: string;
}

export interface FileInfoReqDTO {
  /** 文件 */
  file: File;
  /** 文件名 */
  type: string;
  /** 相对位置，不包含文件名 */
  relativePath: number;
}

export interface ChunkFileDTO {
  /** 分片id */
  chunkId: string;
  /** 是否续传 */
  isContinued: boolean;
  /** md5 */
  md5: string;
  /** 文件名 */
  fileName: string;
  /** 文件大小 */
  fileSize: number;
  /** 分片数量 */
  chunkCount: number;
  /** 分片文件 */
  chunkfile: File;
  /** 分片索引 */
  index: number;
  /** 存储类型;1-公共,2-私有 */
  type: number;
}

export interface ChunkDTO {
  /** md5 */
  md5: string;
  /** 文件名 */
  fileName: string;
  /** 分片数量 */
  chunkCount: number;
  /** minio文件名 */
  objectName: string;
  /** 文件后缀名 */
  extName: string;
  /** 已经上传的分片索引 */
  indexs: string;
  /** 分片上传是否完成 */
  complete: boolean;
}
