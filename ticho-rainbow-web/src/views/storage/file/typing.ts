import { UploadApiResult } from '@/api/storage/model/uploadModel';
import { UploadResultStatus } from '@/components/Upload/src/types/typing';

export interface FileItem {
  num: number;
  thumbUrl?: string;
  name: string;
  size: string | number;
  type?: string;
  percent: number;
  file: File;
  status?: UploadResultStatus;
  response?: UploadApiResult | Recordable;
  uuid: string;
}
