<template>
  <LoginFormTitle v-show="getShow" class="enter-x" />
  <Form
    class="p-4 enter-x"
    :model="formData"
    :rules="getFormRules"
    ref="formRef"
    v-show="getShow"
    @keypress.enter="handleLogin"
  >
    <FormItem name="account" class="enter-x">
      <Input
        size="large"
        v-model:value="formData.account"
        :placeholder="t('sys.login.userName')"
        class="fix-auto-fill"
      />
    </FormItem>
    <FormItem name="password" class="enter-x">
      <InputPassword
        size="large"
        visibilityToggle
        v-model:value="formData.password"
        :placeholder="t('sys.login.password')"
      />
    </FormItem>
    <FormItem name="imgCode" class="enter-x img-fill" v-show="showImageCode">
      <Input
        class="rainbow"
        size="large"
        v-model:value="formData.imgCode"
        :placeholder="t('sys.login.imgCode')"
      >
        <template #addonAfter>
          <img :src="imgCodeShow" alt="imgCode" @click="flushImgCode" />
        </template>
      </Input>
    </FormItem>

    <ARow class="enter-x">
      <ACol :span="12">
        <FormItem>
          <!-- No logic, you need to deal with it yourself -->
          <Checkbox v-model:checked="rememberMe" size="small">
            {{ t('sys.login.rememberMe') }}
          </Checkbox>
        </FormItem>
      </ACol>
      <ACol :span="12">
        <FormItem :style="{ 'text-align': 'right' }">
          <!-- No logic, you need to deal with it yourself -->
          <Button type="link" size="small" @click="setLoginState(LoginStateEnum.RESET_PASSWORD)">
            {{ t('sys.login.forgetPassword') }}
          </Button>
        </FormItem>
      </ACol>
    </ARow>

    <FormItem class="enter-x">
      <Button type="primary" size="large" block @click="handleLogin" :loading="loading">
        {{ t('sys.login.loginButton') }}
      </Button>
    </FormItem>
    <ARow class="enter-x" :gutter="[16, 16]">
      <ACol :md="8" :xs="24">
        <Button block @click="setLoginState(LoginStateEnum.MOBILE)" disabled>
          {{ t('sys.login.mobileSignInFormTitle') }}
        </Button>
      </ACol>
      <ACol :md="8" :xs="24">
        <Button block @click="setLoginState(LoginStateEnum.QR_CODE)" disabled>
          {{ t('sys.login.qrSignInFormTitle') }}
        </Button>
      </ACol>
      <ACol :md="8" :xs="24">
        <Button block @click="setLoginState(LoginStateEnum.REGISTER)">
          {{ t('sys.login.registerButton') }}
        </Button>
      </ACol>
    </ARow>

    <Divider class="enter-x">{{ t('sys.login.otherSignIn') }}</Divider>

    <div class="flex justify-evenly enter-x" :class="`${prefixCls}-sign-in-way`">
      <GithubFilled disabled />
      <WechatFilled disabled />
      <AlipayCircleFilled disabled />
      <GoogleCircleFilled disabled />
      <TwitterCircleFilled disabled />
    </div>
  </Form>
</template>
<script lang="ts" setup>
  import { computed, onMounted, reactive, ref, unref } from 'vue';

  import { Button, Checkbox, Col, Divider, Form, Input, Row } from 'ant-design-vue';
  import {
    AlipayCircleFilled,
    GithubFilled,
    GoogleCircleFilled,
    TwitterCircleFilled,
    WechatFilled,
  } from '@ant-design/icons-vue';
  import LoginFormTitle from './LoginFormTitle.vue';

  import { useI18n } from '@/hooks/web/useI18n';
  import { useMessage } from '@/hooks/web/useMessage';

  import { LoginModeEnum, useUserStore } from '@/store/modules/user';
  import { LoginStateEnum, useFormRules, useFormValid, useLoginState } from './useLogin';
  import { useDesign } from '@/hooks/web/useDesign';
  //import { onKeyStroke } from '@vueuse/core';
  import { getImgCode, loginMode } from '@/api/system/login';
  import { buildUUID } from '@/utils/uuid';
  import { LoginCommand } from '@/api/system/model/loginModel';

  const ACol = Col;
  const ARow = Row;
  const FormItem = Form.Item;
  const InputPassword = Input.Password;
  const { t } = useI18n();
  const { notification, createErrorModal } = useMessage();
  const { prefixCls } = useDesign('login');
  const userStore = useUserStore();

  const { setLoginState, getLoginState } = useLoginState();
  const { getFormRules } = useFormRules();

  const formRef = ref();
  const loading = ref(false);
  const rememberMe = ref(false);

  const imgCodeShow = ref('');

  const formData = reactive({
    account: '',
    password: '',
    imgKey: buildUUID(),
    imgCode: '',
  });

  const { validForm } = useFormValid(formRef);

  //onKeyStroke('Enter', handleLogin);

  const getShow = computed(() => unref(getLoginState) === LoginStateEnum.LOGIN);

  const showImageCode = computed(() => userStore.getLoginMode === LoginModeEnum.IMAGE_CODE);

  onMounted(() => {
    loginMode()
      .then((res) => {
        userStore.setLoginMode(res as string);
      })
      .finally(() => {
        flushImgCode();
      });
  });

  async function flushImgCode() {
    if (userStore.getLoginMode !== LoginModeEnum.IMAGE_CODE) {
      return;
    }
    const imgKey = formData.imgKey.toString();
    getImgCode(imgKey).then((res) => {
      imgCodeShow.value = window.URL.createObjectURL(res);
    });
  }

  async function handleLogin() {
    const data = await validForm();
    if (!data) return;
    try {
      loading.value = true;
      const loginCommand = {
        username: data.account,
        password: data.password,
      } as LoginCommand;
      if (userStore.getLoginMode === LoginModeEnum.IMAGE_CODE) {
        loginCommand.imgCode = data.imgCode;
        loginCommand.imgKey = formData.imgKey;
      }
      const userInfo = await userStore.login(loginCommand, true);
      if (userInfo) {
        notification.success({
          message: t('sys.login.loginSuccessTitle'),
          description: `${t('sys.login.loginSuccessDesc')}: ${userInfo.nickname}`,
          duration: 3,
        });
      }
    } catch (error) {
      createErrorModal({
        title: t('sys.api.errorTip'),
        content: (error as unknown as Error).message || t('sys.api.networkExceptionMsg'),
        getContainer: () => document.body.querySelector(`.${prefixCls}`) || document.body,
      });
      await flushImgCode();
    } finally {
      loading.value = false;
    }
  }
</script>
<style lang="less">
  .img-fill {
    .ant-input-group-addon {
      padding-right: 0;
      border: none;
      background-color: transparent;

      img {
        margin: 0 0 0 0;
        padding: 0 0 0 0;
        border-radius: 8px;
        width: 102px;
        height: 40px;
        border: 1px solid #d4d4d4;
      }
    }
  }
</style>
