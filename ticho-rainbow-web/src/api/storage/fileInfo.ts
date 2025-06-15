import { defHttp } from '@/utils/http/axios';
import { FileInfoDTO, FileInfoQuery } from './model/fileInfoModel';
import { RetryRequest, UploadFileParams } from '#/axios';
import { AxiosProgressEvent } from 'axios';
import { ChunkDTO, ChunkFileDTO, FileInfoReqDTO, UploadApiResult } from './model/uploadModel';
import { ContentTypeEnum } from '@/enums/httpEnum';
import { VersionModifyCommand } from '@/api/system/model/baseModel';

enum Api {
  Upload = '/file/upload',
  Presigned = '/file/presigned',
  UploadChunk = '/file/chunk/upload',
  ComposeChunk = '/file/chunk/compose',
  EnableFileInfo = '/file/status/enable',
  DisableFileInfo = '/file/status/disable',
  CancelFileInfo = '/file/status/cancel',
  FileInfo = '/file',
  FileInfoPage = '/file/page',
  Export = '/file/excel/export',
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
export function presigned(id: string, expire?: number | null, limit?: boolean | null) {
  const params = { id: id, expire: expire, limit: limit };
  return defHttp.get<string>(
    { url: Api.Presigned, params },
    {
      errorMessageMode: 'message',
      joinParamsToUrl: true,
      retryRequest: { isOpenRetry: false } as RetryRequest,
    },
  );
}

export function enableFileInfo(params: VersionModifyCommand[]) {
  return defHttp.patch<any>({ url: Api.EnableFileInfo, params }, { errorMessageMode: 'message' });
}

export function disableFileInfo(params: VersionModifyCommand[]) {
  return defHttp.patch<any>({ url: Api.DisableFileInfo, params }, { errorMessageMode: 'message' });
}

export function cancelFileInfo(params: VersionModifyCommand[]) {
  return defHttp.patch<any>({ url: Api.CancelFileInfo, params }, { errorMessageMode: 'message' });
}

export function delFileInfo(params: VersionModifyCommand) {
  return defHttp.delete<any>(
    { url: Api.FileInfo, params },
    { errorMessageMode: 'message', successMessageMode: 'message' },
  );
}

export function modifyFileInfo(params: FileInfoDTO) {
  return defHttp.put<any>({ url: Api.FileInfo, params }, { errorMessageMode: 'message' });
}

export function fileInfoPage(params?: FileInfoQuery) {
  return defHttp.get<FileInfoDTO[]>(
    { url: Api.FileInfoPage, params },
    { errorMessageMode: 'message' },
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
