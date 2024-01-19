<template>
  <BasicDrawer
    v-bind="$attrs"
    @register="registerDrawer"
    showFooter
    :title="getTitle"
    width="50%"
    @ok="handleSubmit"
  >
    <BasicForm @register="registerForm">
      <template #menu="{ model, field }">
        <BasicTree
          v-model:value="model[field]"
          :treeData="treeData"
          :fieldNames="{ title: 'name', key: 'id' }"
          :checkedKeys="checkedKeys"
          @change="check"
          checkable
          toolbar
          title="菜单分配"
        />
      </template>
    </BasicForm>
  </BasicDrawer>
</template>
<script lang="ts">
  import { defineComponent, ref, computed, unref } from 'vue';
  import { BasicForm, useForm } from '@/components/Form/index';
  import { formSchema } from './role.data';
  import { BasicDrawer, useDrawerInner } from '@/components/Drawer';
  import { BasicTree, TreeItem } from '@/components/Tree';

  import { listRoleMenuByIds, modifyRole, saveRole } from '@/api/system/role';
  import { RoleMenuQueryDTO } from '@/api/system/model/roleModel';

  export default defineComponent({
    name: 'RoleDrawer',
    components: { BasicDrawer, BasicForm, BasicTree },
    emits: ['success', 'register'],
    setup(_, { emit }) {
      const isUpdate = ref(true);
      const checkedKeys = ref<string[]>([]);
      const treeData = ref<TreeItem[]>([]);

      const [registerForm, { resetFields, setFieldsValue, validate }] = useForm({
        labelWidth: 90,
        schemas: formSchema,
        showActionButtonGroup: false,
        baseColProps: { span: 24 },
      });

      const [registerDrawer, { setDrawerProps, closeDrawer }] = useDrawerInner(async (data) => {
        resetFields();
        setDrawerProps({ confirmLoading: false });
        isUpdate.value = !!data?.isUpdate;
        // 需要在setFieldsValue之前先填充treeData，否则Tree组件可能会报key not exist警告
        const query = {
          roleIds: [data.record.id],
          showAll: true,
          treeHandle: true,
        } as unknown as RoleMenuQueryDTO;
        const { menuIds, menus } = await listRoleMenuByIds(query);
        treeData.value = menus as any as TreeItem[];
        checkedKeys.value = menuIds;

        if (unref(isUpdate)) {
          setFieldsValue({
            ...data.record,
          });
        }
      });

      const getTitle = computed(() => (!unref(isUpdate) ? '新增角色' : '编辑角色'));

      async function handleSubmit() {
        try {
          const values = await validate();
          setDrawerProps({ confirmLoading: true });
          values.menuIds = checkedKeys.value;
          if (unref(isUpdate)) {
            await modifyRole(values);
          } else {
            values.tenantId = '000000';
            await saveRole(values);
          }
          closeDrawer();
          emit('success');
        } finally {
          setDrawerProps({ confirmLoading: false });
        }
      }

      function check(rawVal: any) {
        console.log(rawVal);
        if (rawVal.checked) {
          checkedKeys.value = rawVal.checked;
        } else {
          checkedKeys.value = rawVal;
        }
      }

      return {
        registerDrawer,
        registerForm,
        getTitle,
        handleSubmit,
        treeData,
        checkedKeys,
        check,
      };
    },
  });
</script>
