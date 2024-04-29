import SparkMD5 from 'spark-md5';
import { UploadFileParams } from '#/axios';
import { AxiosProgressEvent } from 'axios';
import { composeChunk, uploadChunk, uploadFile } from '@/api/storage/fileInfo';
import { ChunkFileDTO, FileInfoReqDTO } from '@/api/storage/model/uploadModel';
import { useMessage } from '@/hooks/web/useMessage';

const { createMessage } = useMessage();

export async function getFileMd5(file: File, chunkCount: number, chunkSize: number) {
  return new Promise((resolve, reject) => {
    const blobSlice = File.prototype.slice;
    const chunks = chunkCount;
    let currentChunk = 0;
    const spark = new SparkMD5.ArrayBuffer();
    const fileReader = new FileReader();
    fileReader.onload = (e) => {
      const result = e.target?.result as ArrayBuffer;
      spark.append(result);
      currentChunk++;
      if (currentChunk < chunks) {
        loadNext();
      } else {
        const md5 = spark.end();
        resolve(md5);
      }
    };
    fileReader.onerror = (e) => {
      reject(e);
    };
    function loadNext() {
      const start = currentChunk * chunkSize;
      let end = start + chunkSize;
      if (end > file.size) {
        end = file.size;
      }
      fileReader.readAsArrayBuffer(blobSlice.call(file, start, end));
    }
    loadNext();
  });
}

export async function uploadFileHandler(
  fileParams: UploadFileParams,
  onUploadProgress: (progressEvent: AxiosProgressEvent) => void,
) {
  const file = fileParams.file;
  const fileSize = file.size;
  if (fileSize <= chunkSize) {
    const params = {
      ...fileParams,
      ...fileParams.data,
    } as FileInfoReqDTO;
    return uploadFile(params, onUploadProgress)
      .then((res) => {
        createMessage.info(`${file.name} 上传成功`);
        return Promise.resolve(res);
      })
      .catch((e) => {
        return Promise.reject(e);
      });
  }
  return uploadBigFileHandler(fileParams, onUploadProgress);
}

const chunkSize = 5 * 1024 * 1024;

export async function uploadBigFileHandler(
  fileParams: UploadFileParams,
  onUploadProgress: (progressEvent: AxiosProgressEvent) => void,
) {
  const file = fileParams.file;
  const { uid, isContinued, type } = fileParams?.data as {
    uid: string;
    isContinued: boolean;
    type: number;
  };
  const fileSize = file.size;
  // 分片数量
  const chunkCount = Math.ceil(fileSize / chunkSize);
  const fileName = file.name;
  const fileMd5 = (await getFileMd5(file, chunkCount, chunkSize)) as string;
  const axiosProgressEvent = { loaded: 0, total: 100 } as AxiosProgressEvent;
  let total = 1;
  const proms: Promise<any>[] = [];
  // 上传分片
  for (let i = 0; i < chunkCount; i++) {
    // 分片开始
    const start = i * chunkSize;
    // 分片结束
    const end = Math.min(fileSize, start + chunkSize);
    // 分片文件
    const chunkFile = file.slice(start, end);
    // 定义分片上传接口参数，跟后端商定
    const formdata = {
      chunkId: uid,
      isContinued: isContinued,
      md5: fileMd5,
      fileName: fileName,
      fileSize: fileSize,
      chunkCount: chunkCount,
      chunkfile: chunkFile,
      index: i,
      type: type,
    } as ChunkFileDTO;
    const prom: Promise<any> = new Promise((resolve) => {
      uploadChunk(formdata)
        .then(() => {
          total = total + 1;
          if (total < chunkCount) {
            axiosProgressEvent.loaded = 99;
            onUploadProgress(axiosProgressEvent);
          } else if (total === chunkCount) {
            axiosProgressEvent.loaded = (total / chunkCount) * 100;
            onUploadProgress(axiosProgressEvent);
          }
          resolve(true);
        })
        .catch((e) => {
          return Promise.reject(e);
        });
    });
    proms.push(prom);

    // 等待所有分片上传完成
    await Promise.all(proms);
    try {
      // 调用分片上传接口
      await uploadChunk(formdata);
    } catch (e) {
      createMessage.error(`${e}`);
      return Promise.reject(e);
    }
  }
  // 合并
  return composeChunk(uid)
    .then((res) => {
      axiosProgressEvent.loaded = 100;
      onUploadProgress(axiosProgressEvent);
      createMessage.info(`${file.name} 上传成功`);
      return Promise.resolve(res);
    })
    .catch((e) => {
      return Promise.reject(e);
    });
}
