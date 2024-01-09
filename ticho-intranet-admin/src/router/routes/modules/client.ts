import type { AppRouteModule } from '@/router/types';

import { LAYOUT } from '@/router/constant';
import { t } from '@/hooks/web/useI18n';

const client: AppRouteModule = {
  path: '/client',
  name: 'Client',
  component: LAYOUT,
  redirect: '/client/index',
  meta: {
    hideChildrenInMenu: true,
    orderNo: 2000,
    icon: 'ant-design:account-book-outlined',
    title: t('routes.intranet.client.title'),
  },
  children: [
    {
      path: 'index',
      name: 'ClientPage',
      meta: {
        title: t('routes.intranet.client.title'),
        ignoreKeepAlive: false,
      },
      component: () => import('@/views/intranet/client/index.vue'),
    },
  ],
};

export default client;
