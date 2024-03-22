<template>
  <div class="h-full flex">
    <div class="w-1/4 p-4">
      <Card title="个人信息">
        <Row :gutter="[20, 20]">
          <Col :span="24" class="text-center align-center">
            <CropperAvatar :showBtn="false" :uploadApi="uploadAvatar" />
          </Col>
          <Col :span="8" class="text-right">
            <Icon icon="ant-design:user-outlined" color="black" />用户名称</Col
          >
          <Col :span="16" class="text-left">{{ userInfo.username }}</Col>
          <Col :span="8" class="text-right"> <Icon icon="ant-design:mobile-filled" />手机号码</Col>
          <Col :span="16" class="text-left">{{ userInfo.mobile }}</Col>
          <Col :span="8" class="text-right"> <Icon icon="ant-design:mail-filled" />邮箱地址</Col>
          <Col :span="16" class="text-left">{{ userInfo.email }}</Col>
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
  import { Card, Tabs, TabPane, Row, Col } from 'ant-design-vue';
  import { BasicForm, useForm } from '@/components/Form';
  import { CropperAvatar } from '@/components/Cropper';
  import Icon from '@/components/Icon/Icon.vue';
  import { useUserStore } from '@/store/modules/user';
  import { basicFormSchema, passwordFormSchema } from '@/views/system/user/profile/profile.data';
  import { UserDTO, UserPasswordDTO, UserProfileDTO } from '@/api/system/model/userModel';
  import { modifyUser, modifyUserPassword } from '@/api/system/user';
  import { useMessage } from '@/hooks/web/useMessage';
  import { onMounted } from 'vue';

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
  const [
    passswordRegisterForm,
    {
      validate: passwordValidate,
      setFieldsValue: passwordSetFieldsValue,
      resetFields: passwordResetFields,
    },
  ] = useForm({
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
  const userInfo = userStore.getUserInfo;

  onMounted(() => {
    setFieldsValue(userInfo);
  });

  const { createMessage } = useMessage();

  async function updateUserInfo() {
    const values = (await validate()) as UserDTO;
    await modifyUser(values);
    userStore.updateUserInfo(values as UserProfileDTO);
    createMessage.success('修改成功');
  }

  async function updateUserPassword() {
    await passwordSetFieldsValue(userInfo);
    const values = (await passwordValidate()) as UserPasswordDTO;
    await modifyUserPassword(values);
    await passwordResetFields();
    createMessage.success('修改密码成功');
  }

  function uploadAvatar(file) {
    console.log(file);
    return Promise.resolve({ url: '' });
  }
</script>
<style scoped lang="less"></style>
