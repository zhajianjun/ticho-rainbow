<template>
  <div :class="prefixCls" class="relative">
    <Input.Password
      v-if="showInput"
      v-bind="$attrs"
      allowClear
      :value="innerValueRef"
      @change="handleChange"
      :disabled="disabled"
    >
      <template #[item]="data" v-for="item in Object.keys($slots)">
        <slot :name="item" v-bind="data || {}"></slot>
      </template>
    </Input.Password>
    <div :class="`${prefixCls}-bar`">
      <div :class="`${prefixCls}-bar--fill`" :data-score="getPasswordStrength"></div>
    </div>
  </div>
</template>

<script lang="ts" setup>
  import { computed, ref, unref, watch, watchEffect } from 'vue';
  import { Input } from 'ant-design-vue';
  import { zxcvbn } from '@zxcvbn-ts/core';
  import { useDesign } from '@/hooks/web/useDesign';
  import { propTypes } from '@/utils/propTypes';

  defineOptions({ name: 'StrengthMeter' });

  const props = defineProps({
    value: propTypes.string,
    showInput: propTypes.bool.def(true),
    disabled: propTypes.bool,
  });

  const emit = defineEmits(['score-change', 'change']);

  const innerValueRef = ref('');
  const { prefixCls } = useDesign('strength-meter');

  const getPasswordStrength = computed(() => {
    const { disabled } = props;
    if (disabled) return -1;
    const innerValue = unref(innerValueRef);
    const score = innerValue ? zxcvbn(unref(innerValueRef)).score : -1;
    emit('score-change', score);
    return score;
  });

  function handleChange(e) {
    emit('change', e.target.value);
    innerValueRef.value = e.target.value;
  }

  watchEffect(() => {
    innerValueRef.value = props.value || '';
  });

  watch(
    () => unref(innerValueRef),
    (val) => {
      emit('change', val);
    },
  );
</script>
<style lang="less" scoped>
  @prefix-cls: ~'@{namespace}-strength-meter';

  .@{prefix-cls} {
    &-bar {
      position: relative;
      height: 6px;
      margin: 10px auto 6px;
      border-radius: 6px;
      background-color: @disabled-color;

      &::before,
      &::after {
        content: '';
        display: block;
        position: absolute;
        z-index: 10;
        width: 20%;
        height: inherit;
        border-width: 0 5px;
        border-style: solid;
        border-color: @white;
        background-color: transparent;
      }

      &::before {
        left: 20%;
      }

      &::after {
        right: 20%;
      }

      &--fill {
        position: absolute;
        width: 0;
        height: inherit;
        transition:
          width 0.5s ease-in-out,
          background 0.25s;
        border-radius: inherit;
        background-color: transparent;

        &[data-score='0'] {
          width: 20%;
          background-color: darken(@error-color, 10%);
        }

        &[data-score='1'] {
          width: 40%;
          background-color: @error-color;
        }

        &[data-score='2'] {
          width: 60%;
          background-color: @warning-color;
        }

        &[data-score='3'] {
          width: 80%;
          background-color: fade(@success-color, 50%);
        }

        &[data-score='4'] {
          width: 100%;
          background-color: @success-color;
        }
      }
    }
  }
</style>
