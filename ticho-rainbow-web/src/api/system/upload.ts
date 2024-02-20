import { ChunkDTO, ChunkFileDTO, UploadApiResult } from './model/uploadModel';
import { defHttp } from '@/utils/http/axios';
import { UploadFileParams } from '#/axios';
// import { useGlobSetting } from '@/hooks/setting';
import { AxiosProgressEvent } from 'axios';

// eslint-disable-next-line @typescript-eslint/no-unused-vars
// const { uploadUrl = '' } = useGlobSetting();

enum Api {
  Upload = '/upload',
  UploadChunk = '/file/uploadChunk',
  ComposeChunk = '/file/composeChunk',
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
