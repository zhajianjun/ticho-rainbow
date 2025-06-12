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
      <template #menuSlot="{ model, field }">
        <BasicTree
          v-if="model['code'] !== 'admin'"
          v-model:value="model[field]"
          :treeData="treeData"
          :fieldNames="{ title: 'name', key: 'id' }"
          :checkedKeys="checkedKeys"
          @change="check"
          checkStrictly
          checkable
          toolbar
          title="菜单分配"
        />
        <span v-else> admin具有所有权限 </span>
      </template>
    </BasicForm>
  </BasicDrawer>
</template>
<script lang="ts">
  import { computed, defineComponent, ref, unref } from 'vue';
  import { BasicForm, useForm } from '@/components/Form/index';
  import { formSchema } from './role.data';
  import { BasicDrawer, useDrawerInner } from '@/components/Drawer';
  import { BasicTree, TreeItem } from '@/components/Tree';

  import { listRoleMenu, modifyRole, saveRole } from '@/api/system/role';
  import { RoleDtlQuery, RoleModifyCommand, RoleSaveCommand } from '@/api/system/model/roleModel';

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
        await resetFields();
        setDrawerProps({ confirmLoading: false });
        isUpdate.value = !!data?.isUpdate;
        const roleIds = data?.record?.id ? [data.record.id] : [];
        // 需要在setFieldsValue之前先填充treeData，否则Tree组件可能会报key not exist警告
        const query = {
          roleIds: roleIds,
          showAll: true,
          treeHandle: true,
        } as unknown as RoleDtlQuery;
        const { menuIds, menus } = await listRoleMenu(query);
        treeData.value = menus as any as TreeItem[];
        checkedKeys.value = menuIds;

        if (unref(isUpdate)) {
          await setFieldsValue({
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
            const modifys = values as RoleModifyCommand;
            await modifyRole(modifys);
          } else {
            const saves = values as RoleSaveCommand;
            await saveRole(saves);
          }
          closeDrawer();
          emit('success');
        } finally {
          setDrawerProps({ confirmLoading: false });
        }
      }

      function check(rawVal: any) {
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
