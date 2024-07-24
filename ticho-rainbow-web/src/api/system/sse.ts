import { defHttp } from '@/utils/http/axios';

enum Api {
  Sign = '/sse/sign',
}

export function sign() {
  return defHttp.get<string>(
    {
      url: Api.Sign,
    },
    { errorMessageMode: 'message' },
  );
}
