import type { ErrorMessageMode } from '#/axios';
import { defineStore } from 'pinia';
import { store } from '@/store';
import { PageEnum } from '@/enums/pageEnum';
import { ROLES_KEY, TOKEN_KEY, USER_INFO_KEY } from '@/enums/cacheEnum';
import { getAuthCache, setAuthCache } from '@/utils/auth';
import { UserRoleMenuDtlDTO, UserLoginDTO, UserProfileDTO } from '@/api/system/model/userModel';
import { getUserDtlInfo, loginApi } from '@/api/system/user';
import { useI18n } from '@/hooks/web/useI18n';
import { useMessage } from '@/hooks/web/useMessage';
import { router } from '@/router';
import { usePermissionStore } from '@/store/modules/permission';
import { RouteRecordRaw } from 'vue-router';
import { PAGE_NOT_FOUND_ROUTE } from '@/router/routes/basic';
import { h } from 'vue';
import { RoleDTO } from '@/api/system/model/roleModel';
import { useDictStore } from '@/store/modules/dict';

interface UserState {
  userInfo: Nullable<UserRoleMenuDtlDTO>;
  token?: string;
  roleList: RoleDTO[];
  sessionTimeout?: boolean;
  lastUpdateTime: number;
}

export const useUserStore = defineStore({
  id: 'app-user',
  state: (): UserState => ({
    // user info
    userInfo: null,
    // token
    token: undefined,
    // roleList
    roleList: [],
    // 登录是否过期
    sessionTimeout: false,
    // Last fetch time
    lastUpdateTime: 0,
  }),
  getters: {
    getUserInfo(state): UserRoleMenuDtlDTO {
      return state.userInfo || getAuthCache<UserRoleMenuDtlDTO>(USER_INFO_KEY) || {};
    },
    getToken(state): string {
      return state.token || getAuthCache<string>(TOKEN_KEY);
    },
    getRoleList(): RoleDTO[] {
      return this.roleList.length > 0 ? this.roleList : getAuthCache<RoleDTO[]>(ROLES_KEY);
    },
    getSessionTimeout(state): boolean {
      return !!state.sessionTimeout;
    },
    getLastUpdateTime(state): number {
      return state.lastUpdateTime;
    },
  },
  actions: {
    setToken(info: string | undefined) {
      this.token = info ? info : ''; // for null or undefined value
      setAuthCache(TOKEN_KEY, info);
    },
    setRoleList(roles: RoleDTO[]) {
      this.roleList = roles;
      setAuthCache(ROLES_KEY, roles);
    },
    setUserInfo(info: UserRoleMenuDtlDTO | null) {
      this.userInfo = info;
      this.lastUpdateTime = new Date().getTime();
      setAuthCache(USER_INFO_KEY, info);
    },
    updateUserInfo(info: UserProfileDTO | null) {
      const userInfo = getAuthCache(USER_INFO_KEY);
      if (userInfo) {
        // 对象属性拷贝
        Object.assign(userInfo, info);
        setAuthCache(USER_INFO_KEY, userInfo);
        this.userInfo = userInfo as UserRoleMenuDtlDTO;
      }
    },
    setSessionTimeout(flag: boolean) {
      this.sessionTimeout = flag;
    },
    resetState() {
      this.userInfo = null;
      this.token = '';
      this.roleList = [];
      this.sessionTimeout = false;
    },
    /**
     * @description: login
     */
    async login(
      params: UserLoginDTO,
      goHome: boolean = false,
      mode: ErrorMessageMode = 'none',
    ): Promise<UserRoleMenuDtlDTO | null> {
      try {
        const data = await loginApi(params, mode);
        const { access_token } = data;
        this.setToken(access_token);
        return this.afterLoginAction(goHome);
      } catch (error) {
        return Promise.reject(error);
      }
    },
    async afterLoginAction(goHome?: boolean): Promise<UserRoleMenuDtlDTO | null> {
      if (!this.getToken) {
        return null;
      }
      // get user info
      const userInfo = await this.getUserInfoAction();

      const sessionTimeout = this.sessionTimeout;
      if (sessionTimeout) {
        this.setSessionTimeout(false);
      } else {
        const permissionStore = usePermissionStore();
        if (!permissionStore.isDynamicAddedRoute) {
          // 菜单路由处理
          const routes = await permissionStore.buildRoutesAction();
          routes.forEach((route) => {
            router.addRoute(route as unknown as RouteRecordRaw);
          });
          router.addRoute(PAGE_NOT_FOUND_ROUTE as unknown as RouteRecordRaw);
          permissionStore.setDynamicAddedRoute(true);
        }
        goHome && (await router.replace(PageEnum.BASE_HOME));
      }
      return userInfo;
    },
    async getUserInfoAction(): Promise<UserRoleMenuDtlDTO | null> {
      if (!this.getToken) {
        return null;
      }
      const userInfo = await getUserDtlInfo();
      const { roles } = userInfo;
      this.setRoleList(roles);
      this.setUserInfo(userInfo);
      const dictStore = useDictStore();
      await dictStore.initDicts();
      return userInfo;
    },
    /**
     * @description: logout
     */
    async logout(goLogin = false) {
      // if (this.getToken) {
      //   try {
      //     await doLogout();
      //   } catch {
      //     console.log('注销Token失败');
      //   }
      // }
      const dictStore = useDictStore();
      dictStore.clearDicts();
      this.setToken(undefined);
      this.setSessionTimeout(false);
      this.setUserInfo(null);
      goLogin && (await router.push(PageEnum.BASE_LOGIN));
    },

    /**
     * @description: Confirm before logging out
     */
    confirmLoginOut() {
      const { createConfirm } = useMessage();
      const { t } = useI18n();
      createConfirm({
        iconType: 'warning',
        title: () => h('span', t('sys.app.logoutTip')),
        content: () => h('span', t('sys.app.logoutMessage')),
        onOk: async () => {
          await this.logout(true);
        },
      });
    },
  },
});

// Need to be used outside the setup
export function useUserStoreWithOut() {
  return useUserStore(store);
}
