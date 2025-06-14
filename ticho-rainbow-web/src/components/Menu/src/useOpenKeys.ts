import { MenuModeEnum } from '@/enums/menuEnum';
import type { Menu as MenuType } from '@/router/types';
import type { Key, MenuState } from './types';
import { computed, Ref, toRaw, unref } from 'vue';
import { useTimeoutFn } from '@vben/hooks';
import { uniq } from 'lodash-es';
import { useMenuSetting } from '@/hooks/setting/useMenuSetting';
import { getAllParentPath } from '@/router/helper/menuHelper';

export function useOpenKeys(
  menuState: MenuState,
  menus: Ref<MenuType[]>,
  mode: Ref<MenuModeEnum>,
  accordion: Ref<boolean>,
) {
  const { getCollapsed, getIsMixSidebar } = useMenuSetting();

  async function setOpenKeys(path: string) {
    if (mode.value === MenuModeEnum.HORIZONTAL) {
      return;
    }
    const native = unref(getIsMixSidebar);
    const handle = () => {
      const menuList = toRaw(menus.value);
      if (menuList?.length === 0) {
        menuState.openKeys = [];
        return;
      }
      if (!unref(accordion)) {
        menuState.openKeys = uniq([...menuState.openKeys, ...getAllParentPath(menuList, path)]);
      } else {
        menuState.openKeys = getAllParentPath(menuList, path);
      }
    };
    if (native) {
      handle();
    } else {
      useTimeoutFn(handle, 16);
    }
  }

  const getOpenKeys = computed(() => {
    const collapse = unref(getIsMixSidebar) ? false : unref(getCollapsed);

    return collapse ? menuState.collapsedOpenKeys : menuState.openKeys;
  });

  /**
   * @description:  重置值
   */
  function resetKeys() {
    menuState.selectedKeys = [];
    menuState.openKeys = [];
  }

  function handleOpenChange(openKeys: Key[]) {
    if (unref(mode) === MenuModeEnum.HORIZONTAL || !unref(accordion) || unref(getIsMixSidebar)) {
      menuState.openKeys = openKeys;
    } else {
      // const menuList = toRaw(menus.value);
      // getAllParentPath(menuList, path);
      const rootSubMenuKeys: Key[] = [];
      for (const { children, path } of unref(menus)) {
        if (children && children.length > 0) {
          rootSubMenuKeys.push(path);
        }
      }
      if (!unref(getCollapsed)) {
        const latestOpenKey = openKeys.find((key) => menuState.openKeys.indexOf(key) === -1);
        if (rootSubMenuKeys.indexOf(latestOpenKey as string) === -1) {
          menuState.openKeys = openKeys;
        } else {
          menuState.openKeys = latestOpenKey ? [latestOpenKey] : [];
        }
      } else {
        menuState.collapsedOpenKeys = openKeys;
      }
    }
  }

  return { setOpenKeys, resetKeys, getOpenKeys, handleOpenChange };
}
