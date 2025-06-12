<template>
  <div v-if="getShow">
    <LoginFormTitle class="enter-x" />
    <Form
      class="p-4 enter-x"
      :model="formData"
      :rules="getFormRules"
      ref="formRef"
      :loading="loading"
    >
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
          ref="countdownInput"
          size="large"
          class="fix-auto-fill"
          :sendCodeApi="handleSendEmailCode"
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

      <Button type="primary" class="enter-x" size="large" block @click="handleResetPassword">
        {{ t('common.resetText') }}
      </Button>
      <Button size="large" block class="mt-4 enter-x" @click="handleBackLogin">
        {{ t('sys.login.backSignIn') }}
      </Button>
    </Form>
    <ImgCodeModal @register="registerModal" @success="handleAgainSendEmailCode" forceRender />
  </div>
</template>
<script lang="ts" setup>
  import { computed, reactive, ref, unref } from 'vue';
  import LoginFormTitle from './LoginFormTitle.vue';
  import { Button, Form, Input } from 'ant-design-vue';
  import { StrengthMeter } from '@/components/StrengthMeter';
  import { CountdownInput } from '@/components/CountDown';
  import { useI18n } from '@/hooks/web/useI18n';
  import { LoginStateEnum, useFormRules, useFormValid, useLoginState } from './useLogin';
  import { resetPassword, resetPasswordEmailSend } from '@/api/system/login';
  import {
    ImgCodeDTO,
    ImgCodeEmailDTO,
    LoginCommand,
    UserSignUpDTO,
  } from '@/api/system/model/loginModel';
  import { useMessage } from '@/hooks/web/useMessage';
  import { useDesign } from '@/hooks/web/useDesign';
  import { useUserStore } from '@/store/modules/user';
  import ImgCodeModal from './ImgCodeModal.vue';
  import { useModal } from '@/components/Modal';

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
  };

  const formData = reactive(defaultFormData);

  const imgCodeData = reactive({
    email: '',
    imgKey: '',
    imgCode: '',
  });

  const { getFormRules } = useFormRules(formData);
  const { validForm } = useFormValid(formRef);

  const getShow = computed(() => unref(getLoginState) === LoginStateEnum.RESET_PASSWORD);

  async function handleResetPassword() {
    const data = await validForm();
    if (!data) return;
    try {
      loading.value = true;
      // 1.重置密码
      const resetData = data as UserSignUpDTO;
      resetData.username = formData.username;
      const res = await resetPassword(resetData).then((res) => {
        notification.success({
          message: t('sys.api.successTip'),
          description:
            `账号${formData.username}${t('sys.login.forgetFormTitle')}` +
            `${t('sys.common.success')}`,
          duration: 3,
        });
        // 重置数据
        Object.assign(formData, defaultFormData);
        return res;
      });
      // 2.登录
      const loginCommand = res as LoginCommand;
      loginCommand.password = formData.password;
      const userInfo = await userStore.login(
        {
          username: res.username,
          password: formData.password,
          imgCode: res.imgCode,
          imgKey: res.imgKey,
        },
        true,
      );
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
      loading.value = false;
    }
  }

  // 验证码子组件组件点击触发事件
  const countdownInput = ref();
  const countdownInputOnClick = () => {
    countdownInput.value.handleStart();
  };
  // 是否可以发送邮件
  const isSendEmail = ref<boolean>(false);

  function handleAgainSendEmailCode(test: ImgCodeDTO) {
    Object.assign(imgCodeData, test);
    imgCodeData.email = formData.email;
    isSendEmail.value = true;
    countdownInputOnClick();
  }

  const [registerModal, { openModal }] = useModal();

  async function handleSendEmailCode() {
    const emailData = await validForm(['email']);
    if (!emailData) {
      return Promise.resolve(false);
    }
    if (isSendEmail.value !== true) {
      openModal();
      return Promise.resolve(false);
    }
    const data = Object.assign(imgCodeData, { email: emailData.email }) as ImgCodeEmailDTO;
    return await resetPasswordEmailSend(data)
      .then((res) => {
        notification.success({
          message: t('sys.api.successTip'),
          description: `${t('sys.login.sendSuccess')}`,
          duration: 3,
        });
        formData.username = res;
        return Promise.resolve(true);
      })
      .catch((error) => {
        createErrorModal({
          title: t('sys.api.errorTip'),
          content: (error as unknown as Error).message || t('sys.api.networkExceptionMsg'),
          getContainer: () => document.body.querySelector(`.${prefixCls}`) || document.body,
        });
        return Promise.resolve(false);
      })
      .finally(() => {
        isSendEmail.value = false;
      });
  }
</script>
