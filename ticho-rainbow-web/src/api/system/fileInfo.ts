import { defHttp } from '@/utils/http/axios';
import { FileInfoDTO, FileInfoQuery } from './model/fileInfoModel';
import { UploadFileParams } from '#/axios';
import { AxiosProgressEvent } from 'axios';
import { ChunkDTO, ChunkFileDTO, UploadApiResult } from '@/api/system/model/uploadModel';

enum Api {
  FileInfo = '/file',
  FileInfoPage = '/file/page',
  Upload = '/file/upload',
  Download = '/file/download',
  Delete = '/file/delete',
  UploadChunk = '/file/uploadChunk',
  ComposeChunk = '/file/composeChunk',
}

export function delFileInfo(id: string) {
  const params = { id: id };
  return defHttp.delete<any>({ url: Api.FileInfo, params }, { errorMessageMode: 'message' });
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

export function uploadChunk(chunkFile: ChunkFileDTO) {
  const data = {
    md5: chunkFile.md5,
    fileName: chunkFile.fileName,
    chunkCount: chunkFile.chunkCount,
    index: chunkFile.index,
  };
  const params = {} as UploadFileParams;
  params.data = data;
  params.file = chunkFile.file;
  return defHttp.uploadFile<ChunkDTO>(
    {
      url: Api.UploadChunk,
    },
    params,
  );
}
export function composeChunk(md5: string) {
  const params = { md5: md5 };
  return defHttp.get<any>({
    url: Api.ComposeChunk,
    params,
  });
}
