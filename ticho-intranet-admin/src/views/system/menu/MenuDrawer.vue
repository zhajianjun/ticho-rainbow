<template>
  <BasicDrawer
    v-bind="$attrs"
    @register="registerDrawer"
    showFooter
    :title="getTitle"
    width="50%"
    @ok="handleSubmit"
  >
    <BasicForm @register="registerForm" />
  </BasicDrawer>
</template>
<script lang="ts">
  import { defineComponent, ref, computed, unref } from 'vue';
  import { BasicForm, useForm } from '@/components/Form/index';
  import { formSchema } from './menu.data';
  import { BasicDrawer, useDrawerInner } from '@/components/Drawer';

  import { saveMenu, modifyMenu } from '@/api/system/menu';

  export default defineComponent({
    name: 'MenuDrawer',
    components: { BasicDrawer, BasicForm },
    props: {
      treeData: Array,
    },
    emits: ['success', 'register'],
    setup(props, { emit }) {
      const isUpdate = ref(true);

      const [registerForm, { resetFields, setFieldsValue, updateSchema, validate }] = useForm({
        labelWidth: 100,
        schemas: formSchema,
        showActionButtonGroup: false,
        baseColProps: { lg: 12, md: 24 },
      });

      const [registerDrawer, { setDrawerProps, closeDrawer }] = useDrawerInner(async (data) => {
        await resetFields();
        setDrawerProps({ confirmLoading: false });
        isUpdate.value = !!data?.isUpdate;
        if (unref(isUpdate)) {
          let perms = data.record.perms.map((n: string) => n.split(':'));
          let icon = data.record.icon ?? '';
          await setFieldsValue({
            ...data.record,
            icon: icon,
            perms: perms,
          });
        } else {
          let parentId = data.record.id;
          let parentType = data.record.type;
          let type = parentType === 2 ? 3 : 1;
          let componentName = null;
          let name = null;
          let sort = null;
          if (parentType === 3) {
            parentId = data.record.parentId;
            type = 3;
            name = data.record.name;
            componentName = data.record.componentName;
            sort = data.record.sort;
          }
          // 创建菜单设置类型默认值
          await setFieldsValue({
            parentId: parentId,
            type: type,
            name: name,
            componentName: componentName,
            sort: sort,
          });
        }
        // 过滤按钮类型的菜单
        const treeData = filterData(props.treeData);
        await updateSchema({
          field: 'parentId',
          componentProps: { treeData },
        });
      });

      /**
       * 过滤按钮type=3的数据
       */
      function filterData(data) {
        return data.filter((item) => {
          if (item.type === 3) {
            return false;
          }
          if (item.children) {
            item.children = filterData(item.children);
          }
          return true;
        });
      }

      const getTitle = computed(() => (!unref(isUpdate) ? '新增菜单' : '编辑菜单'));

      async function handleSubmit() {
        const values = await validate();
        setDrawerProps({ confirmLoading: true });
        if (values.type === 3 && values.perms?.length) {
          values.perms = values.perms.map((n) => n.join(':'));
        }
        if (values.type === 1) {
          values.component = 'LAYOUT';
          values.componentName = 'LAYOUT';
        }
        let promis: Promise<any>;
        if (unref(isUpdate)) {
          promis = modifyMenu(values);
        } else {
          if (!values.parentId) {
            values.parentId = 0;
          }
          promis = saveMenu(values);
        }
        promis
          .then(() => {
            closeDrawer();
            emit('success');
          })
          .finally(() => {
            setDrawerProps({ confirmLoading: false });
          });
      }
      return { registerDrawer, registerForm, getTitle, handleSubmit };
    },
  });
</script>
