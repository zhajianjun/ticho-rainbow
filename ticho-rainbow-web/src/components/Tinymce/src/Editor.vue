<template>
  <div :class="prefixCls" :style="{ width: containerWidth }">
    <ImgUpload
      :fullscreen="fullscreen"
      @uploading="handleImageUploading"
      @done="handleDone"
      v-if="showImageUpload"
      v-show="editorRef"
      :disabled="disabled"
    />
    <textarea
      :id="tinymceId"
      ref="elRef"
      :style="{ visibility: 'hidden' }"
      v-if="!initOptions.inline"
    ></textarea>
    <slot v-else></slot>
  </div>
</template>

<script lang="ts" setup>
  import type { Editor, RawEditorSettings } from 'tinymce';
  import tinymce from 'tinymce/tinymce';
  import 'tinymce/themes/silver';
  import 'tinymce/icons/default/icons';
  import 'tinymce/plugins/advlist';
  import 'tinymce/plugins/anchor';
  import 'tinymce/plugins/autolink';
  import 'tinymce/plugins/autosave';
  import 'tinymce/plugins/code';
  import 'tinymce/plugins/codesample';
  import 'tinymce/plugins/directionality';
  import 'tinymce/plugins/fullscreen';
  import 'tinymce/plugins/hr';
  import 'tinymce/plugins/insertdatetime';
  import 'tinymce/plugins/link';
  import 'tinymce/plugins/lists';
  import 'tinymce/plugins/media';
  import 'tinymce/plugins/nonbreaking';
  import 'tinymce/plugins/noneditable';
  import 'tinymce/plugins/pagebreak';
  import 'tinymce/plugins/paste';
  import 'tinymce/plugins/preview';
  import 'tinymce/plugins/print';
  import 'tinymce/plugins/save';
  import 'tinymce/plugins/searchreplace';
  import 'tinymce/plugins/spellchecker';
  import 'tinymce/plugins/tabfocus';
  // import 'tinymce/plugins/table';
  import 'tinymce/plugins/template';
  import 'tinymce/plugins/textpattern';
  import 'tinymce/plugins/visualblocks';
  import 'tinymce/plugins/visualchars';
  import 'tinymce/plugins/wordcount';

  import {
    computed,
    nextTick,
    onBeforeUnmount,
    onDeactivated,
    PropType,
    ref,
    unref,
    useAttrs,
    watch,
  } from 'vue';
  import ImgUpload from './ImgUpload.vue';
  import { plugins as defaultPlugins, toolbar as defaultToolbar } from './tinymce';
  import { buildShortUUID } from '@/utils/uuid';
  import { bindHandlers } from './helper';
  import { onMountedOrActivated } from '@vben/hooks';
  import { useDesign } from '@/hooks/web/useDesign';
  import { isNumber } from '@/utils/is';
  import { useLocale } from '@/locales/useLocale';
  import { useAppStore } from '@/store/modules/app';

  defineOptions({ name: 'Tinymce', inheritAttrs: false });

  const props = defineProps({
    options: {
      type: Object as PropType<Partial<RawEditorSettings>>,
      default: () => ({}),
    },
    value: {
      type: String,
    },

    toolbar: {
      type: Array as PropType<string[]>,
      default: defaultToolbar,
    },
    plugins: {
      type: Array as PropType<string[]>,
      default: defaultPlugins,
    },
    modelValue: {
      type: String,
    },
    height: {
      type: [Number, String] as PropType<string | number>,
      required: false,
      default: 400,
    },
    width: {
      type: [Number, String] as PropType<string | number>,
      required: false,
      default: 'auto',
    },
    showImageUpload: {
      type: Boolean,
      default: true,
    },
  });

  const emit = defineEmits(['change', 'update:modelValue', 'inited', 'init-error']);

  const attrs = useAttrs();
  const editorRef = ref<Editor | null>(null);
  const fullscreen = ref(false);
  const tinymceId = ref<string>(buildShortUUID('tiny-vue'));
  const elRef = ref<HTMLElement | null>(null);

  const { prefixCls } = useDesign('tinymce-container');

  const appStore = useAppStore();

  const containerWidth = computed(() => {
    const width = props.width;
    if (isNumber(width)) {
      return `${width}px`;
    }
    return width;
  });

  const skinName = computed(() => {
    return appStore.getDarkMode === 'light' ? 'oxide' : 'oxide-dark';
  });

  const langName = computed(() => {
    const lang = useLocale().getLocale.value;
    return ['zh_CN', 'en'].includes(lang) ? lang : 'zh_CN';
  });

  const initOptions = computed((): RawEditorSettings => {
    const { height, options, toolbar, plugins } = props;
    const publicPath = import.meta.env.VITE_PUBLIC_PATH || '/';
    return {
      selector: `#${unref(tinymceId)}`,
      height,
      toolbar,
      menubar: 'file edit insert view format table',
      plugins,
      language_url: publicPath + 'resource/tinymce/langs/' + langName.value + '.js',
      language: langName.value,
      branding: false,
      default_link_target: '_blank',
      link_title: false,
      object_resizing: false,
      auto_focus: true,
      skin: skinName.value,
      skin_url: publicPath + 'resource/tinymce/skins/ui/' + skinName.value,
      content_css: publicPath + 'resource/tinymce/skins/ui/' + skinName.value + '/content.min.css',
      ...options,
      setup: (editor: Editor) => {
        editorRef.value = editor;
        editor.on('init', (e) => initSetup(e));
      },
    };
  });

  const disabled = computed(() => {
    const { options } = props;
    const getdDisabled = options && Reflect.get(options, 'readonly');
    const editor = unref(editorRef);
    if (editor) {
      editor.setMode(getdDisabled ? 'readonly' : 'design');
    }
    return getdDisabled ?? false;
  });

  watch(
    () => attrs.disabled,
    () => {
      const editor = unref(editorRef);
      if (!editor) {
        return;
      }
      editor.setMode(attrs.disabled ? 'readonly' : 'design');
    },
  );

  onMountedOrActivated(() => {
    if (!initOptions.value.inline) {
      tinymceId.value = buildShortUUID('tiny-vue');
    }
    nextTick(() => {
      setTimeout(() => {
        initEditor();
      }, 30);
    });
  });

  onBeforeUnmount(() => {
    destory();
  });

  onDeactivated(() => {
    destory();
  });

  function destory() {
    if (tinymce !== null) {
      tinymce?.remove?.(unref(initOptions).selector!);
    }
  }

  function initEditor() {
    const el = unref(elRef);
    if (el) {
      el.style.visibility = '';
    }
    tinymce
      .init(unref(initOptions))
      .then((editor) => {
        emit('inited', editor);
      })
      .catch((err) => {
        emit('init-error', err);
      });
  }

  function initSetup(e) {
    const editor = unref(editorRef);
    if (!editor) {
      return;
    }
    const value = props.modelValue || '';

    editor.setContent(value);
    bindModelHandlers(editor);
    bindHandlers(e, attrs, unref(editorRef));
  }

  function setValue(editor: Record<string, any>, val?: string, prevVal?: string) {
    if (
      editor &&
      typeof val === 'string' &&
      val !== prevVal &&
      val !== editor.getContent({ format: attrs.outputFormat })
    ) {
      editor.setContent(val);
    }
  }

  function bindModelHandlers(editor: any) {
    const modelEvents = attrs.modelEvents ? attrs.modelEvents : null;
    const normalizedEvents = Array.isArray(modelEvents) ? modelEvents.join(' ') : modelEvents;

    watch(
      () => props.modelValue,
      (val, prevVal) => {
        setValue(editor, val, prevVal);
      },
    );

    watch(
      () => props.value,
      (val, prevVal) => {
        setValue(editor, val, prevVal);
      },
      {
        immediate: true,
      },
    );

    editor.on(normalizedEvents ? normalizedEvents : 'change keyup undo redo', () => {
      const content = editor.getContent({ format: attrs.outputFormat });
      emit('update:modelValue', content);
      emit('change', content);
    });

    editor.on('FullscreenStateChanged', (e) => {
      fullscreen.value = e.state;
    });
  }

  function handleImageUploading(name: string) {
    const editor = unref(editorRef);
    if (!editor) {
      return;
    }
    editor.execCommand('mceInsertContent', false, getUploadingImgName(name));
    const content = editor?.getContent() ?? '';
    setValue(editor, content);
  }

  function handleDone(name: string, url: string) {
    const editor = unref(editorRef);
    if (!editor) {
      return;
    }
    const content = editor?.getContent() ?? '';
    const val = content?.replace(getUploadingImgName(name), `<img src="${url}"/>`) ?? '';
    setValue(editor, val);
  }

  function getUploadingImgName(name: string) {
    return `[uploading:${name}]`;
  }
</script>
<style lang="less">
  @prefix-cls: ~'@{namespace}-tinymce-container';

  .@{prefix-cls} {
    position: relative;
    line-height: normal;

    textarea {
      visibility: hidden;
      z-index: -1;
    }
  }
</style>
