<template>
  <SvgIcon
    :size="size"
    :name="getSvgIcon"
    v-if="isSvgIcon"
    :class="[$attrs.class, 'anticon']"
    :spin="spin"
  />
  <span
    v-else
    ref="elRef"
    :class="[$attrs.class, 'app-iconify anticon', spin && 'app-iconify-spin']"
    :style="getWrapStyle"
  ></span>
</template>
<script lang="ts" setup>
  import type { PropType } from 'vue';
  import { computed, CSSProperties, nextTick, onMounted, ref, unref, watch } from 'vue';
  import SvgIcon from './src/SvgIcon.vue';
  import Iconify from '@purge-icons/generated';
  import { isString } from '@/utils/is';
  import { propTypes } from '@/utils/propTypes';

  const SVG_END_WITH_FLAG = '|svg';

  defineOptions({ name: 'Icon' });

  const props = defineProps({
    // icon name
    icon: propTypes.string,
    // icon color
    color: propTypes.string,
    // icon size
    size: {
      type: [String, Number] as PropType<string | number>,
      default: 16,
    },
    spin: propTypes.bool.def(false),
    prefix: propTypes.string.def(''),
  });

  const elRef = ref(null);

  const isSvgIcon = computed(() => props.icon?.endsWith(SVG_END_WITH_FLAG));
  const getSvgIcon = computed(() => props.icon.replace(SVG_END_WITH_FLAG, ''));
  const getIconRef = computed(() => `${props.prefix ? props.prefix + ':' : ''}${props.icon}`);

  const update = async () => {
    if (unref(isSvgIcon)) return;

    const el: any = unref(elRef);
    if (!el) return;

    await nextTick();
    const icon = unref(getIconRef);
    if (!icon) return;

    const svg = Iconify.renderSVG(icon, {});
    if (svg) {
      el.textContent = '';
      el.appendChild(svg);
    } else {
      const span = document.createElement('span');
      span.className = 'iconify';
      span.dataset.icon = icon;
      el.textContent = '';
      el.appendChild(span);
    }
  };

  const getWrapStyle = computed((): CSSProperties => {
    const { size, color } = props;
    let fs = size;
    if (isString(size)) {
      fs = parseInt(size, 10);
    }

    return {
      fontSize: `${fs}px`,
      color: color,
      display: 'inline-flex',
    };
  });

  watch(() => props.icon, update, { flush: 'post' });

  onMounted(update);
</script>
<style lang="less">
  .app-iconify {
    display: inline-block;
    // vertical-align: middle;

    &-spin {
      svg {
        animation: loadingCircle 1s infinite linear;
      }
    }
  }

  span.iconify {
    display: block;
    min-width: 1em;
    min-height: 1em;
    border-radius: 100%;
    background-color: @iconify-bg-color;
  }
</style>
