import type { ErrorMessageMode } from '#/axios';
import { defineStore } from 'pinia';
import { store } from '@/store';
import { PageEnum } from '@/enums/pageEnum';
import { ROLES_KEY, TOKEN_KEY, USER_INFO_KEY } from '@/enums/cacheEnum';
import { getAuthCache, setAuthCache } from '@/utils/auth';
import { findUser, loginApi } from '@/api/system/login';
import { useI18n } from '@/hooks/web/useI18n';
import { useMessage } from '@/hooks/web/useMessage';
import { router } from '@/router';
import { usePermissionStore } from '@/store/modules/permission';
import { RouteRecordRaw } from 'vue-router';
import { PAGE_NOT_FOUND_ROUTE } from '@/router/routes/basic';
import { h } from 'vue';
import { RoleDTO } from '@/api/system/model/roleModel';
import { useDictStore } from '@/store/modules/dict';
import { LoginDTO, LoginUserDetailDTO, LoginUserDTO } from '@/api/system/model/loginModel';
import headerImg from '@/assets/images/header.jpg';

interface UserState {
  userInfo: Nullable<LoginUserDTO>;
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
    getUserInfo(state): LoginUserDTO {
      return state.userInfo || getAuthCache<LoginUserDTO>(USER_INFO_KEY) || {};
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
    setUserInfo(info: LoginUserDTO | null) {
      this.userInfo = info;
      this.lastUpdateTime = new Date().getTime();
      setAuthCache(USER_INFO_KEY, info);
    },
    updateUserInfo(info: LoginUserDetailDTO | null) {
      const userInfo = getAuthCache(USER_INFO_KEY);
      if (userInfo) {
        // 对象属性拷贝
        const newUserInfo = {};
        Object.assign(newUserInfo, userInfo, info);
        setAuthCache(USER_INFO_KEY, newUserInfo);
        this.userInfo = newUserInfo as LoginUserDTO;
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
      params: LoginDTO,
      goHome: boolean = false,
      mode: ErrorMessageMode = 'none',
    ): Promise<LoginUserDTO | null> {
      try {
        const data = await loginApi(params, mode);
        const { access_token } = data;
        this.setToken(access_token);
        return this.afterLoginAction(goHome);
      } catch (error) {
        return Promise.reject(error);
      }
    },
    async afterLoginAction(goHome?: boolean): Promise<LoginUserDTO | null> {
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
    async getUserInfoAction(): Promise<LoginUserDTO | null> {
      if (!this.getToken) {
        return null;
      }
      const userInfo = await findUser();
      userInfo.photo = userInfo.photo ?? headerImg;
      this.setUserInfo(userInfo);
      const dictStore = useDictStore();
      await dictStore.initDicts();
      return userInfo;
    },
    /**
     * @description: logout
     */
    async logout(goLogin = false) {
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
