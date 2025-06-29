import { BasicColumn } from '@/components/Table';
import { UploadApiResult } from '@/api/storage/model/uploadModel';

export enum UploadResultStatus {
  DONE = 'done',
  SUCCESS = 'success',
  ERROR = 'error',
  UPLOADING = 'uploading',
}

export interface FileItem {
  thumbUrl?: string;
  name: string;
  size: string | number;
  type?: string;
  percent: number;
  file: File;
  status?: UploadResultStatus;
  response?: UploadApiResult | Recordable<any>;
  uuid: string;
}

export interface BaseFileItem {
  uid: string | number;
  url: string;
  name?: string;
}

export interface PreviewFileItem {
  url: string;
  name: string;
  type: string;
}

export interface FileBasicColumn extends Omit<BasicColumn, 'customRender'> {
  /**
   * Renderer of the table cell. The return value should be a VNode, or an object for colSpan/rowSpan config
   * @type Function | ScopedSlot
   */
  customRender?: Function;
  /**
   * Title of this column
   * @type any (string | slot)
   */
  title: string;

  /**
   * Display field of the data record, could be set like a.b.c
   * @type string
   */
  dataIndex: string;
}
