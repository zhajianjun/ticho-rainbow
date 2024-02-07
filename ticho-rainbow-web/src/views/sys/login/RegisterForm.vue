<template>
  <div v-if="getShow">
    <LoginFormTitle class="enter-x" />
    <Form class="p-4 enter-x" :model="formData" :rules="getFormRules" ref="formRef">
      <FormItem name="username" class="enter-x">
        <Input
          class="fix-auto-fill"
          size="large"
          v-model:value="formData.username"
          :placeholder="t('sys.login.userName')"
        />
      </FormItem>
      <FormItem name="email" class="enter-x">
        <Input
          size="large"
          v-model:value="formData.email"
          :placeholder="t('sys.login.email')"
          class="fix-auto-fill"
        />
      </FormItem>
      <FormItem name="emailCode" class="enter-x">
        <CountdownInput
          size="large"
          class="fix-auto-fill"
          :sendCodeApi="signUpEmailSendHandle"
          v-model:value="formData.emailCode"
          :placeholder="t('sys.login.emailCode')"
        />
      </FormItem>
      <FormItem name="password" class="enter-x">
        <StrengthMeter
          size="large"
          v-model:value="formData.password"
          :placeholder="t('sys.login.password')"
        />
      </FormItem>
      <FormItem name="confirmPassword" class="enter-x">
        <InputPassword
          size="large"
          visibilityToggle
          v-model:value="formData.confirmPassword"
          :placeholder="t('sys.login.confirmPassword')"
        />
      </FormItem>

      <FormItem class="enter-x" name="policy">
        <!-- No logic, you need to deal with it yourself -->
        <Checkbox v-model:checked="formData.policy" size="small">
          {{ t('sys.login.policy') }}
        </Checkbox>
      </FormItem>

      <Button
        type="primary"
        class="enter-x"
        size="large"
        block
        @click="handleRegister"
        :loading="loading"
      >
        {{ t('sys.login.registerButton') }}
      </Button>
      <Button size="large" block class="mt-4 enter-x" @click="handleBackLogin">
        {{ t('sys.login.backSignIn') }}
      </Button>
    </Form>
  </div>
</template>
<script lang="ts" setup>
  import { reactive, ref, unref, computed, toRef } from 'vue';
  import LoginFormTitle from './LoginFormTitle.vue';
  import { Form, Input, Button, Checkbox } from 'ant-design-vue';
  import { StrengthMeter } from '@/components/StrengthMeter';
  import { CountdownInput } from '@/components/CountDown';
  import { useI18n } from '@/hooks/web/useI18n';
  import { useLoginState, useFormRules, useFormValid, LoginStateEnum } from './useLogin';
  import { signUp, signUpEmailSend } from '@/api/system/user';
  import { UserLoginDTO, UserSignUpDTO } from '@/api/system/model/userModel';
  import { useMessage } from '@/hooks/web/useMessage';
  import { useDesign } from '@/hooks/web/useDesign';
  import { useUserStore } from '@/store/modules/user';

  const { notification, createErrorModal } = useMessage();
  const { prefixCls } = useDesign('login');
  const userStore = useUserStore();

  const FormItem = Form.Item;
  const InputPassword = Input.Password;
  const { t } = useI18n();
  const { handleBackLogin, getLoginState } = useLoginState();

  const formRef = ref();
  const loading = ref(false);

  const defaultFormData = {
    username: '',
    password: '',
    confirmPassword: '',
    email: '',
    emailCode: '',
    policy: false,
  };

  const formData = reactive(defaultFormData);

  const { getFormRules } = useFormRules(formData);
  const { validForm } = useFormValid(formRef);

  const getShow = computed(() => unref(getLoginState) === LoginStateEnum.REGISTER);

  async function handleRegister() {
    const data = await validForm();
    if (!data) return;
    try {
      // 1.注册
      const signData = data as UserSignUpDTO;
      const res = await signUp(signData).then((res) => {
        notification.success({
          message: t('sys.api.successTip'),
          description:
            `账号${formData.username}${t('sys.login.signUpFormTitle')}` +
            `${t('sys.common.success')}`,
          duration: 3,
        });
        // 重置
        Object.assign(formData, defaultFormData);
        return res;
      });
      // 2.登录
      const userLoginDTO = res as UserLoginDTO;
      userLoginDTO.password = formData.password;
      const userInfo = await userStore.login(userLoginDTO, true);
      if (userInfo) {
        notification.success({
          message: t('sys.login.loginSuccessTitle'),
          description: `${t('sys.login.loginSuccessDesc')}: ${userInfo.username}`,
          duration: 3,
        });
      }
    } catch (error) {
      createErrorModal({
        title: t('sys.api.errorTip'),
        content: (error as unknown as Error).message || t('sys.api.networkExceptionMsg'),
        getContainer: () => document.body.querySelector(`.${prefixCls}`) || document.body,
      });
    } finally {
      handleBackLogin();
    }
  }

  async function signUpEmailSendHandle() {
    const data = await validForm(['email']);
    if (!data) {
      return Promise.resolve(false);
    }
    return await signUpEmailSend(formData.email)
      .then(() => {
        notification.success({
          message: t('sys.api.successTip'),
          description: `${t('sys.login.sendSuccess')}`,
          duration: 3,
        });
        return Promise.resolve(true);
      })
      .catch((error) => {
        createErrorModal({
          title: t('sys.api.errorTip'),
          content: (error as unknown as Error).message || t('sys.api.networkExceptionMsg'),
          getContainer: () => document.body.querySelector(`.${prefixCls}`) || document.body,
        });
        return Promise.resolve(false);
      });
  }
</script>
