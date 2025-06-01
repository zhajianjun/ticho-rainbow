<template>
  <div class="h-full flex">
    <div class="w-1/4 p-4">
      <Card title="个人信息">
        <Row :gutter="[20, 20]">
          <Col :span="24" class="text-center align-center">
            <CropperAvatar
              :showBtn="false"
              :uploadApi="uploadUserAvatar"
              v-model:value="loginUsreDetail.photo"
            />
          </Col>
          <Col :span="8" class="text-right">
            <Icon icon="ant-design:user-outlined" color="black" />
            用户姓名：
          </Col>
          <Col :span="16" class="text-left">{{ loginUsreDetail.realname }}</Col>
          <Col :span="8" class="text-right">
            <Icon icon="ant-design:user-outlined" color="black" />
            用户昵称：
          </Col>
          <Col :span="16" class="text-left">{{ loginUsreDetail.nickname }}</Col>
          <Col :span="8" class="text-right">
            <Icon icon="ant-design:mobile-filled" />
            手机号码：
          </Col>
          <Col :span="16" class="text-left">{{ loginUsreDetail.mobile }}</Col>
          <Col :span="8" class="text-right">
            <Icon icon="ant-design:mail-filled" />
            邮箱地址：
          </Col>
          <Col :span="16" class="text-left">{{ loginUsreDetail.email }}</Col>
        </Row>
      </Card>
    </div>
    <div class="w-3/4 p-4 px-0 pr-4">
      <Card title="基本资料">
        <Tabs>
          <TabPane key="base" tab="基本资料">
            <BasicForm @register="baseicRegisterForm" :submitFunc="updateUserInfo" />
          </TabPane>
          <TabPane key="password" tab="修改密码">
            <BasicForm @register="passswordRegisterForm" :submitFunc="updateUserPassword" />
          </TabPane>
        </Tabs>
      </Card>
    </div>
  </div>
</template>
<script setup lang="ts">
  import { Card, Col, Row, TabPane, Tabs } from 'ant-design-vue';
  import { BasicForm, useForm } from '@/components/Form';
  import { CropperAvatar } from '@/components/Cropper';
  import Icon from '@/components/Icon/Icon.vue';
  import { useUserStore } from '@/store/modules/user';
  import { basicFormSchema, passwordFormSchema } from '@/views/system/user/profile/profile.data';
  import { findUserDetail, modifyPassword, modifyUser, uploadAvatar } from '@/api/system/login';
  import { useMessage } from '@/hooks/web/useMessage';
  import { onMounted, ref } from 'vue';
  import {
    LoginUserDetailDTO,
    LoginUserModifyCommand,
    LoginUserModifyPasswordCommand,
  } from '@/api/system/model/loginModel';
  import headerImg from '@/assets/images/header.jpg';

  const [baseicRegisterForm, { validate, setFieldsValue }] = useForm({
    labelWidth: 100,
    labelAlign: 'right',
    baseColProps: { span: 24 },
    schemas: basicFormSchema,
    actionColOptions: {
      span: 3,
    },
    showResetButton: false,
    submitButtonOptions: {
      text: '修改',
    },
  });
  const [passswordRegisterForm, { validate: passwordValidate, resetFields: passwordResetFields }] =
    useForm({
      labelWidth: 100,
      labelAlign: 'right',
      baseColProps: { span: 24 },
      schemas: passwordFormSchema,
      actionColOptions: {
        span: 3,
      },
      showResetButton: false,
      submitButtonOptions: {
        text: '修改',
      },
    });
  const userStore = useUserStore();
  const { createMessage } = useMessage();
  const loginUsreDetail = ref<LoginUserDetailDTO>({
    id: 0,
    username: '',
    nickname: '',
    realname: '',
    idcard: '',
    sex: 0,
    age: 0,
    birthday: '',
    address: '',
    education: '',
    email: '',
    qq: '',
    wechat: '',
    mobile: '',
    photo: '',
    lastIp: '',
    lastTime: '',
    remark: '',
    version: 0,
    createTime: '',
  });

  onMounted(() => {
    flush();
  });

  async function updateUserInfo() {
    const values = (await validate()) as LoginUserModifyCommand;
    await modifyUser(values);
    createMessage.success('修改成功');
    await flush();
  }

  async function updateUserPassword() {
    const values = (await passwordValidate()) as LoginUserModifyPasswordCommand;
    values.version = loginUsreDetail.value.version;
    await modifyPassword(values);
    await passwordResetFields();
    createMessage.success('修改密码成功');
    await flush();
  }

  function uploadUserAvatar({ file }) {
    const newFile = new File([file], `${loginUsreDetail.value.username}-avatar.png`, {
      type: file.type,
    });
    return new Promise((resolve, reject) => {
      uploadAvatar(newFile)
        .then((res) => {
          flush();
          resolve(res);
        })
        .catch((e) => {
          reject(e);
        });
    });
  }

  async function flush() {
    return findUserDetail().then((res) => {
      res.photo = res.photo ?? headerImg;
      loginUsreDetail.value = res;
      userStore.updateUserInfo(res);
      setFieldsValue(res);
    });
  }
</script>
<style scoped lang="less"></style>
