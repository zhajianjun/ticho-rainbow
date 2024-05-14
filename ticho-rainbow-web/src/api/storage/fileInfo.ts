import { defHttp } from '@/utils/http/axios';
import { FileInfoDTO, FileInfoQuery } from './model/fileInfoModel';
import { RetryRequest, UploadFileParams } from '#/axios';
import { AxiosProgressEvent } from 'axios';
import { ChunkDTO, ChunkFileDTO, FileInfoReqDTO, UploadApiResult } from './model/uploadModel';
import { ContentTypeEnum } from '@/enums/httpEnum';

enum Api {
  Upload = '/file/upload',
  GetUrl = '/file/getUrl',
  Download = '/file/downloadById',
  UploadChunk = '/file/uploadChunk',
  ComposeChunk = '/file/composeChunk',
  EnableFileInfo = '/file/enable',
  DisableFileInfo = '/file/disable',
  CancelFileInfo = '/file/cancel',
  FileInfo = '/file',
  FileInfoPage = '/file/page',
  Export = '/file/expExcel',
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
  params: FileInfoReqDTO,
  onUploadProgress: (progressEvent: AxiosProgressEvent) => void,
) {
  return defHttp.post<FileInfoDTO>({
    url: Api.Upload,
    onUploadProgress,
    headers: {
      'Content-type': ContentTypeEnum.FORM_DATA,
    },
    params,
  });
}

export function uploadChunk(params: ChunkFileDTO) {
  return defHttp.post<ChunkDTO>({
    url: Api.UploadChunk,
    headers: {
      'Content-type': ContentTypeEnum.FORM_DATA,
    },
    params,
  });
}

export function composeChunk(chunkId: string) {
  const params = { chunkId: chunkId };
  return defHttp.post<FileInfoDTO>(
    {
      url: Api.ComposeChunk,
      params,
    },
    {
      joinParamsToUrl: true,
    },
  );
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

export function enableFileInfo(id: string) {
  const params = { id: id };
  return defHttp.put<any>(
    { url: Api.EnableFileInfo, params },
    { errorMessageMode: 'message', successMessageMode: 'message', joinParamsToUrl: true },
  );
}

export function disableFileInfo(id: string) {
  const params = { id: id };
  return defHttp.put<any>(
    { url: Api.DisableFileInfo, params },
    { errorMessageMode: 'message', successMessageMode: 'message', joinParamsToUrl: true },
  );
}

export function cancelFileInfo(id: string) {
  const params = { id: id };
  return defHttp.put<any>(
    { url: Api.CancelFileInfo, params },
    { errorMessageMode: 'message', successMessageMode: 'message', joinParamsToUrl: true },
  );
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
  return defHttp.post<FileInfoDTO[]>(
    { url: Api.FileInfoPage, params },
    { errorMessageMode: 'none' },
  );
}

export function expExcel(params?: FileInfoQuery) {
  return defHttp.post<any>(
    { url: Api.Export, params, responseType: 'blob' },
    {
      errorMessageMode: 'message',
      isReturnNativeResponse: true,
      retryRequest: { isOpenRetry: false } as RetryRequest,
    },
  );
}
