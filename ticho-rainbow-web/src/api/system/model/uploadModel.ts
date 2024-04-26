export interface UploadApiResult {
  msg: string;
  code: number;
  data: string;
}

export interface ChunkFileDTO {
  /** 分片id */
  chunkId: string;
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
