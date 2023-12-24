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
    title: t('routes.system.port.title'),
  },
  children: [
    {
      path: 'index',
      name: 'PortPage',
      meta: {
        title: t('routes.system.port.title'),
        ignoreKeepAlive: false,
      },
      component: () => import('@/views/system/port/index.vue'),
    },
  ],
};

export default port;
