import SparkMD5 from 'spark-md5';
import { UploadFileParams } from '#/axios';
import { AxiosProgressEvent } from 'axios';
import { composeChunk, uploadChunk, uploadFile } from '@/api/storage/fileInfo';
import { ChunkFileDTO } from '@/api/storage/model/uploadModel';
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
  params: UploadFileParams,
  onUploadProgress: (progressEvent: AxiosProgressEvent) => void,
) {
  const file = params.file;
  const fileSize = file.size;
  if (fileSize <= chunkSize) {
    return uploadFile(params, onUploadProgress)
      .then(() => {
        createMessage.info(`${file.name} 上传成功`);
        return Promise.resolve({
          data: { data: file.name },
        });
      })
      .catch((e) => {
        return Promise.reject(e);
      });
  }
  return uploadBigFileHandler(params, onUploadProgress);
}

const chunkSize = 5 * 1024 * 1024;

export async function uploadBigFileHandler(
  params: UploadFileParams,
  onUploadProgress: (progressEvent: AxiosProgressEvent) => void,
) {
  const file = params.file;
  const { uid, isContinued } = params?.data as { uid: string; isContinued: boolean };
  const fileSize = file.size;
  // 分片数量
  const chunkCount = Math.ceil(fileSize / chunkSize);
  const fileName = file.name;
  const fileMd5 = (await getFileMd5(file, chunkCount, chunkSize)) as string;
  const axiosProgressEvent = { loaded: 0, total: 100 } as AxiosProgressEvent;
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
    } as ChunkFileDTO;
    let response;
    try {
      // 调用分片上传接口
      response = await uploadChunk(formdata);
    } catch (e) {
      createMessage.error(`${e}`);
      return Promise.reject(e);
    }
    const { code, msg } = response.data;
    if (response.status !== 200 || code !== 0) {
      createMessage.error(`${msg}`);
      return Promise.reject(msg);
    }
    if (i === chunkCount) {
      axiosProgressEvent.loaded = 99;
    } else {
      axiosProgressEvent.loaded = ((i + 1) / chunkCount) * 100;
    }
    onUploadProgress(axiosProgressEvent);
  }
  // 合并
  return composeChunk(uid)
    .then(() => {
      createMessage.info(`${file.name} 上传成功`);
      return Promise.resolve({
        data: { data: fileName },
      });
    })
    .catch((e) => {
      return Promise.reject(e);
    });
}
