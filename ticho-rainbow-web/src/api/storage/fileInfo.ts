import { defHttp } from '@/utils/http/axios';
import { FileInfoDTO, FileInfoQuery } from './model/fileInfoModel';
import { RetryRequest, UploadFileParams } from '#/axios';
import { AxiosProgressEvent } from 'axios';
import { ChunkDTO, ChunkFileDTO, UploadApiResult } from './model/uploadModel';
import { ContentTypeEnum } from '@/enums/httpEnum';

enum Api {
  FileInfo = '/file',
  FileInfoPage = '/file/page',
  Upload = '/file/upload',
  GetUrl = '/file/getUrl',
  Download = '/file/downloadById',
  UploadChunk = '/file/uploadChunk',
  ComposeChunk = '/file/composeChunk',
}

export function delFileInfo(id: string) {
  const params = { id: id };
  return defHttp.delete<any>(
    { url: Api.FileInfo, params },
    { errorMessageMode: 'message', successMessageMode: 'message', joinParamsToUrl: true },
  );
}

export function modifyFileInfo(params: FileInfoDTO) {
  return defHttp.put<any>({ url: Api.FileInfo, params }, { errorMessageMode: 'message' });
}

export function fileInfoPage(params?: FileInfoQuery) {
  return defHttp.get<FileInfoDTO[]>(
    { url: Api.FileInfoPage, params },
    { errorMessageMode: 'none' },
  );
}

export function upload(
  params: UploadFileParams,
  onUploadProgress: (progressEvent: AxiosProgressEvent) => void,
) {
  return defHttp.uploadFile<UploadApiResult>(
    {
      url: Api.Upload,
      onUploadProgress,
    },
    params,
  );
}

export function uploadFile(
  params: UploadFileParams,
  onUploadProgress: (progressEvent: AxiosProgressEvent) => void,
) {
  return defHttp.post<UploadApiResult>({
    url: Api.Upload,
    onUploadProgress,
    headers: {
      'Content-type': ContentTypeEnum.FORM_DATA,
    },
    params,
  });
}

export function uploadChunk(chunkFile: ChunkFileDTO) {
  const params = {} as UploadFileParams;
  params.data = chunkFile;
  params.file = chunkFile.chunkfile;
  return defHttp.uploadFile<ChunkDTO>(
    {
      url: Api.UploadChunk,
    },
    params,
  );
}
export function composeChunk(chunkId: string) {
  const params = { chunkId: chunkId };
  return defHttp.get<any>({
    url: Api.ComposeChunk,
    params,
  });
}

/**
 * 获取下载链接
 *
 * @param id
 * @param expire
 * @param limit
 */
export function getUrl(id: string, expire?: number | null, limit?: boolean | null) {
  const params = { id: id, expire: expire, limit: limit };
  return defHttp.get<string>(
    { url: Api.GetUrl, params },
    {
      errorMessageMode: 'message',
      joinParamsToUrl: true,
      retryRequest: { isOpenRetry: false } as RetryRequest,
    },
  );
}

export function downloadFile(id: string) {
  const params = { id: id };
  return defHttp.get<any>(
    { url: Api.Download, params, responseType: 'blob' },
    {
      errorMessageMode: 'message',
      isTransformResponse: false,
      joinParamsToUrl: true,
      retryRequest: { isOpenRetry: false } as RetryRequest,
    },
  );
}
