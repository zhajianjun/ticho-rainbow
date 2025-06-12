import type { FormInstance } from 'ant-design-vue/lib/form/Form';
import type {
  NamePath,
  Rule as ValidationRule,
  RuleObject,
  ValidateOptions,
} from 'ant-design-vue/lib/form/interface';
import { computed, ref, Ref, unref } from 'vue';
import { useI18n } from '@/hooks/web/useI18n';

export enum LoginStateEnum {
  LOGIN,
  REGISTER,
  RESET_PASSWORD,
  MOBILE,
  QR_CODE,
}

const currentState = ref(LoginStateEnum.LOGIN);

// 这里也可以优化
// import { createGlobalState } from '@vueuse/core'

export function useLoginState() {
  function setLoginState(state: LoginStateEnum) {
    currentState.value = state;
  }

  const getLoginState = computed(() => currentState.value);

  function handleBackLogin() {
    setLoginState(LoginStateEnum.LOGIN);
  }

  return { setLoginState, getLoginState, handleBackLogin };
}

export function useFormValid<T extends Object = any>(formRef: Ref<FormInstance>) {
  const validate = computed(() => {
    const form = unref(formRef);
    return form?.validate ?? ((_nameList?: NamePath) => Promise.resolve());
  });

  async function validForm(nameList?: NamePath[] | string, options?: ValidateOptions) {
    const form = unref(formRef);
    if (!form) return;
    const data = await form.validate(nameList, options);
    return data as T;
  }

  return { validate, validForm };
}

export function useFormRules(formData?: Recordable) {
  const { t } = useI18n();

  const getAccountFormRule = computed(() => createRule(t('sys.login.accountPlaceholder')));
  const getPasswordFormRule = computed(() => createRule(t('sys.login.passwordPlaceholder')));
  const getSmsFormRule = computed(() => createRule(t('sys.login.smsPlaceholder')));
  const getEmailFormRule = computed(() => createRule(t('sys.login.emailPlaceholder')));
  const getMobileFormRule = computed(() => createRule(t('sys.login.mobilePlaceholder')));

  const validatePolicy = async (_: RuleObject, value: boolean) => {
    return !value ? Promise.reject(t('sys.login.policyPlaceholder')) : Promise.resolve();
  };

  const validateConfirmPassword = (password: string) => {
    return async (_: RuleObject, value: string) => {
      if (!value) {
        return Promise.reject(t('sys.login.passwordPlaceholder'));
      }
      if (value !== password) {
        return Promise.reject(t('sys.login.diffPwd'));
      }
      return Promise.resolve();
    };
  };

  const getFormRules = computed((): { [k: string]: ValidationRule | ValidationRule[] } => {
    const accountFormRule = unref(getAccountFormRule);
    const passwordFormRule = unref(getPasswordFormRule);
    const emailCodeFormRule = unref(getSmsFormRule);
    const smsFormRule = unref(getSmsFormRule);
    const emailFormRule = unref(getEmailFormRule);
    const mobileFormRule = unref(getMobileFormRule);

    const mobileRule = {
      sms: smsFormRule,
      mobile: mobileFormRule,
    };

    const emailRule = {
      emailCode: emailCodeFormRule,
      email: emailFormRule,
    };
    switch (unref(currentState)) {
      // register form rules
      case LoginStateEnum.REGISTER:
        return {
          username: accountFormRule,
          password: [
            {
              validator: (_, value) => {
                if (!value) {
                  return Promise.reject(t('sys.login.passwordPlaceholder'));
                }
                const reg = new RegExp(
                  '^(?![0-9]+$)(?![a-zA-Z]+$)(?![0-9a-zA-Z]+$)(?![0-9\\W]+$)(?![a-zA-Z\\W]+$)[0-9A-Za-z\\W]{6,18}$',
                );
                if (!value.match(reg)) {
                  return Promise.reject(t('sys.login.passwordValidPlaceholder'));
                }
                return Promise.resolve();
              },
              trigger: 'change',
            },
          ],
          confirmPassword: [
            { validator: validateConfirmPassword(formData?.password), trigger: 'change' },
          ],
          policy: [{ validator: validatePolicy, trigger: 'change' }],
          ...emailRule,
        };

      // reset password form rules
      case LoginStateEnum.RESET_PASSWORD:
        return {
          password: [
            {
              validator: (_, value) => {
                if (!value) {
                  return Promise.reject(t('sys.login.passwordPlaceholder'));
                }
                const reg = new RegExp(
                  '^(?![0-9]+$)(?![a-zA-Z]+$)(?![0-9a-zA-Z]+$)(?![0-9\\W]+$)(?![a-zA-Z\\W]+$)[0-9A-Za-z\\W]{6,18}$',
                );
                if (!value.match(reg)) {
                  return Promise.reject(t('sys.login.passwordValidPlaceholder'));
                }
                return Promise.resolve();
              },
              trigger: 'change',
            },
          ],
          confirmPassword: [
            { validator: validateConfirmPassword(formData?.password), trigger: 'change' },
          ],
          ...emailRule,
        };

      // mobile form rules
      case LoginStateEnum.MOBILE:
        return mobileRule;

      // login form rules
      default:
        return {
          account: accountFormRule,
          password: passwordFormRule,
          imgCode: emailCodeFormRule,
        };
    }
  });
  return { getFormRules };
}

function createRule(message: string): ValidationRule[] {
  return [
    {
      required: true,
      message,
      trigger: 'change',
    },
  ];
}
