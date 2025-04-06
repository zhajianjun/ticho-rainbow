import { defHttp } from '@/utils/http/axios';
import { PermissionDTO } from '@/api/system/model/permModel';

enum Api {
  PermList = '/permission/tree',
}

export const getPermissions = () => defHttp.get<PermissionDTO>({ url: Api.PermList });
