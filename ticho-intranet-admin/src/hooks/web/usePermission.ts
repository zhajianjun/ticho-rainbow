import type { RouteRecordRaw } from 'vue-router';

import { useAppStore } from '@/store/modules/app';
import { usePermissionStore } from '@/store/modules/permission';
import { useUserStore } from '@/store/modules/user';

import { useTabs } from './useTabs';

import { router, resetRouter } from '@/router';
// import { RootRoute } from '@/router/routes';

import projectSetting from '@/settings/projectSetting';
import { PermissionModeEnum } from '@/enums/appEnum';

import { intersection } from 'lodash-es';
import { isArray } from '@/utils/is';
import { useMultipleTabStore } from '@/store/modules/multipleTab';
import { RoleDTO } from '@/api/system/model/roleModel';

// User permissions related operations
export function usePermission() {
  const userStore = useUserStore();
  const appStore = useAppStore();
  const permissionStore = usePermissionStore();
  const { closeAll } = useTabs(router);

  /**
   * Change permission mode
   */
  async function togglePermissionMode() {
    appStore.setProjectConfig({
      permissionMode:
        appStore.projectConfig?.permissionMode === PermissionModeEnum.BACK
          ? PermissionModeEnum.ROUTE_MAPPING
          : PermissionModeEnum.BACK,
    });
    location.reload();
  }

  /**
   * Reset and regain authority resource information
   * 重置和重新获得权限资源信息
   * @param id
   */
  async function resume() {
    const tabStore = useMultipleTabStore();
    tabStore.clearCacheTabs();
    resetRouter();
    const routes = await permissionStore.buildRoutesAction();
    routes.forEach((route) => {
      router.addRoute(route as unknown as RouteRecordRaw);
    });
    permissionStore.setLastBuildMenuTime();
    closeAll();
  }

  /**
   * Determine whether there is permission
   */
  function hasPermission(value?: string | string[], def = true): boolean {
    // Visible by default
    if (!value) {
      return def;
    }
    const userInfo = userStore.getUserInfo;
    // 超級管理员角色，什么都有
    if (!!userInfo.roleCodes && userInfo.roleCodes.includes('admin')) {
      return true;
    }
    const allCodeList = permissionStore.getButtons as string[];
    if (!isArray(value)) {
      return allCodeList.includes(value);
    }
    return (intersection(value, allCodeList) as unknown as string[]).length > 0;
  }

  /**
   * Change roles
   * @param roles
   */
  async function changeRole(roles: RoleDTO | RoleDTO[]): Promise<void> {
    if (projectSetting.permissionMode !== PermissionModeEnum.ROUTE_MAPPING) {
      throw new Error(
        'Please switch PermissionModeEnum to ROUTE_MAPPING mode in the configuration to operate!',
      );
    }

    if (!isArray(roles)) {
      roles = [roles];
    }
    userStore.setRoleList(roles);
    await resume();
  }

  /**
   * refresh menu data
   */
  async function refreshMenu() {
    resume();
  }

  return { changeRole, hasPermission, togglePermissionMode, refreshMenu };
}
