import { defHttp } from '@/utils/http/axios';
import { PermDTO } from '@/api/system/model/permModel';

enum Api {
  PermList = '/perm/list',
}

export const getPerms = () => defHttp.get<PermDTO>({ url: Api.PermList });
