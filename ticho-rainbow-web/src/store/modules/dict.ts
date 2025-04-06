import { defineStore } from 'pinia';
import { DictLabel, DictLabelDTO } from '@/api/system/model/dictLabelModel';
import { flush, all } from '@/api/system/dict';
import { DictDTO } from '@/api/system/model/dictModel';
import { Persistent } from '@/utils/cache/persistent';
import { DICTS_KEY } from '@/enums/cacheEnum';

interface DictInfo {
  dicts: Nullable<Map<string, Map<string, DictLabelDTO>>>;
}

export const useDictStore = defineStore({
  id: 'app-dict',
  state: (): DictInfo => ({
    dicts: Persistent.getLocal(DICTS_KEY),
  }),
  getters: {
    getDicts(state): Map<string, Map<string, DictLabelDTO>> {
      return (
        state.dicts ||
        Persistent.getLocal(DICTS_KEY) ||
        new Map<string, Map<string, DictLabelDTO>>()
      );
    },
  },
  actions: {
    setDicts(dicts: Map<string, Map<string, DictLabelDTO>>) {
      this.dicts = dicts;
      Persistent.setLocal(DICTS_KEY, dicts, true);
    },
    clearDicts() {
      Persistent.removeLocal(DICTS_KEY, true);
      this.dicts = null;
    },
    async initDicts() {
      all().then((res) => {
        if (!res || res.length <= 0) {
          return;
        }
        const dicts = res as DictDTO[];
        const dictMap: Map<string, Map<string, DictLabelDTO>> = convert(dicts);
        this.setDicts(dictMap);
      });
    },
  },
});

function convert(dicts: Nullable<DictDTO[]>) {
  if (!dicts || dicts.length <= 0) {
    return new Map<string, Map<string, DictLabelDTO>>();
  }
  return dicts.reduce((map, dict) => {
    const value = dict.details ? dict.details : [];
    const dictLabelMap: Map<string, DictLabelDTO> = value.reduce((mmap, dictLabel) => {
      mmap.set(dictLabel.value, dictLabel);
      return mmap;
    }, new Map<string, DictLabelDTO>());
    map.set(dict.code, dictLabelMap);
    return map;
  }, new Map<string, Map<string, DictLabelDTO>>());
}

/**
 * 获取字典
 *
 * @param code  字典编码
 * @param toNum value值是否转int，默认为true
 */
export function getDictByCode(code: string, toNum: boolean = true): DictLabel[] {
  const dictStore = useDictStore();
  const dicts = dictStore.getDicts;
  if (!dicts || dicts.size <= 0) {
    return [];
  }
  const dictLables = dicts.get(code);
  if (!dictLables) {
    return [];
  }
  return Array.from(dictLables.values())
    .sort((x) => x.sort)
    .map((x) => {
      const converValue = toNum ? parseInt(x.value) : x.value;
      const dict: DictLabel = { label: x.label, value: converValue };
      return dict;
    });
}

/**
 * 获取字典值
 *
 * @param code  字典编码
 * @param value 字典值
 */
export function getDictByCodeAndValue(
  code: string,
  value: string | number,
): Nullable<DictLabelDTO> {
  const valueStr = value + '';
  if (!code || !valueStr) {
    return null;
  }
  const dictStore = useDictStore();
  const dictMap = dictStore.getDicts;
  if (!dictMap || dictMap.size <= 0) {
    return null;
  }
  const dictLableMap = dictMap.get(code);
  if (!dictLableMap || dictLableMap.size <= 0) {
    return null;
  }
  const dictLable = dictLableMap.get(valueStr);
  if (!dictLable) {
    return null;
  }
  return dictLable;
}

/**
 * 获取字典值
 *
 * @param code  字典编码
 * @param value 字典值
 */
export function getDictLabelByCodeAndValue(code: string, value: string | number): string {
  const valueStr = value + '';
  if (!code || !valueStr) {
    return valueStr;
  }
  const dictStore = useDictStore();
  const dictMap = dictStore.getDicts;
  if (!dictMap || dictMap.size <= 0) {
    return valueStr;
  }
  const dictLableMap = dictMap.get(code);
  if (!dictLableMap || dictLableMap.size <= 0) {
    return valueStr;
  }
  const dictLable = dictLableMap.get(valueStr);
  if (!dictLable) {
    return valueStr;
  }
  return dictLable.label;
}

export function flushDicts() {
  const dictStore = useDictStore();
  return flush().then((res) => {
    const dictMap: Map<string, Map<string, DictLabelDTO>> = convert(res);
    dictStore.setDicts(dictMap);
  });
}
