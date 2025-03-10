import { TiResult } from '../utils';

const fakeUserInfo = {
  userId: '1',
  username: 'vben',
  realName: 'Vben Admin',
  desc: 'manager',
  password: '123456',
  token: 'fakeToken1',
  roles: [
    {
      roleName: 'Super Admin',
      value: 'super',
    },
  ],
};
export default class UserService {
  async login() {
    return TiResult.success(fakeUserInfo);
  }

  async getUserInfoById() {
    return TiResult.success(fakeUserInfo);
  }
}
