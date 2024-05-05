import { useMessage } from '@/hooks/web/useMessage';
import { h } from 'vue';

/** 上一次获取到的script地址 */
let lastSrcs: string[];
const scriptReg: RegExp = /<script.*src=["'](?<src>[^"']+)/gm;
const DURATION: number = 60 * 1000;
const { createConfirm } = useMessage();

async function extractNewScripts() {
  const html = await fetch('/?_t=' + Date.now()).then((resp) => resp.text());
  scriptReg.lastIndex = 0;
  const result: string[] = [];
  let match: RegExpExecArray | null;
  while ((match = scriptReg.exec(html))) {
    result.push(match.groups?.src ?? '');
  }
  return result;
}

// 判断是否需要更新
async function needUpdate(): Promise<boolean> {
  // 获取当前页面的script标签src属性
  const newScripts: string[] = await extractNewScripts();
  // 如果是第一次加载，则不更新
  if (!lastSrcs) {
    lastSrcs = newScripts;
    return false;
  }
  //判断是否需要更新 长度不同-更新
  if (lastSrcs.length !== newScripts.length) {
    lastSrcs = newScripts;
    return true;
  }
  //比较两个数组是否相等 长度相同，内容不同-更新
  for (let i = 0; i < lastSrcs.length; i++) {
    if (lastSrcs[i] !== newScripts[i]) {
      lastSrcs = newScripts;
      return true;
    }
  }
  return false;
}

export const autoRefresh = (): void => {
  setTimeout(async () => {
    const willUpdate = await needUpdate();
    if (willUpdate) {
      createConfirm({
        iconType: 'warning',
        title: () => h('span', '提示'),
        content: () => h('span', '页面有更新，点击确定刷新页面?'),
        onOk: async () => {
          location.reload();
        },
        okText: '确定',
        cancelText: '取消',
      });
    } else {
      autoRefresh();
    }
  }, DURATION);
};
