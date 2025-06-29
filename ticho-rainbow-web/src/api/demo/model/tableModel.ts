import { BasicFetchResult, BasicPageParams } from '@/api/model/baseModel';

/**
 * @description: Request list interface parameters
 */
export type DemoParams = Partial<BasicPageParams>;

export interface DemoListItem {
  id: string;
  beginTime: string;
  endTime: string;
  address: string;
  name: string;
  no: number;
  status: number;
}

/**
 * @description: Request list return value
 */
export type DemoListGetResultModel = BasicFetchResult<DemoListItem>;
