import type { AppRouteModule } from '/@/router/types';

import { LAYOUT } from '/@/router/constant';
import { t } from '/@/hooks/web/useI18n';

const user: AppRouteModule = {
  path: '/user',
  name: 'User',
  component: LAYOUT,
  redirect: '/user/index',
  meta: {
    hideChildrenInMenu: true,
    orderNo: 1000,
    icon: 'ion:settings-outline',
    title: t('routes.system.user.title'),
  },
  children: [
    {
      path: 'index',
      name: 'UserPage',
      meta: {
        title: t('routes.system.user.title'),
        ignoreKeepAlive: false,
      },
      component: () => import('/@/views/system/user/index.vue'),
    },
  ],
};

export default user;
