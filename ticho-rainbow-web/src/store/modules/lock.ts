import type { LockInfo } from '#/store';

import { defineStore } from 'pinia';

import { LOCK_INFO_KEY } from '@/enums/cacheEnum';
import { Persistent } from '@/utils/cache/persistent';
import { useUserStore } from './user';
import { UserLoginDTO } from '@/api/system/model/userModel';

interface LockState {
  lockInfo: Nullable<LockInfo>;
}

export const useLockStore = defineStore({
  id: 'app-lock',
  state: (): LockState => ({
    lockInfo: Persistent.getLocal(LOCK_INFO_KEY),
  }),
  getters: {
    getLockInfo(state): Nullable<LockInfo> {
      return state.lockInfo;
    },
  },
  actions: {
    setLockInfo(info: LockInfo) {
      this.lockInfo = Object.assign({}, this.lockInfo, info);
      Persistent.setLocal(LOCK_INFO_KEY, this.lockInfo, true);
    },
    resetLockInfo() {
      Persistent.removeLocal(LOCK_INFO_KEY, true);
      this.lockInfo = null;
    },
    // Unlock
    async unLock(password?: string) {
      const userStore = useUserStore();
      if (this.lockInfo?.pwd === password) {
        this.resetLockInfo();
        return true;
      }
      const tryLogin = async () => {
        try {
          const username = userStore.getUserInfo?.username;
          const params = { username: username, password: password } as UserLoginDTO;
          const res = await userStore.login(params, false, 'none');
          if (res) {
            this.resetLockInfo();
          }
          return res;
        } catch (error) {
          return false;
        }
      };
      return await tryLogin();
    },
  },
});
