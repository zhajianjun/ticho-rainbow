import type { AppRouteModule } from '@/router/types';

import { LAYOUT } from '@/router/constant';
import { t } from '@/hooks/web/useI18n';

const port: AppRouteModule = {
  path: '/port',
  name: 'Port',
  component: LAYOUT,
  redirect: '/port/index',
  meta: {
    hideChildrenInMenu: true,
    orderNo: 3000,
    icon: 'ion:layers-outline',
    title: t('routes.intranet.port.title'),
  },
  children: [
    {
      path: 'index',
      name: 'PortPage',
      meta: {
        title: t('routes.intranet.port.title'),
        ignoreKeepAlive: false,
      },
      component: () => import('@/views/intranet/port/index.vue'),
    },
  ],
};

export default port;
