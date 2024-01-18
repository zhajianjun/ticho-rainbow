import { defineStore } from 'pinia';
import { DictDTO } from '@/api/system/model/dictModel';
import { getAllDict } from '@/api/system/dict';
import { DictTypeDTO } from '@/api/system/model/dictTypeModel';
import { Persistent } from '@/utils/cache/persistent';
import { DICTS_KEY } from '@/enums/cacheEnum';

interface DictInfo {
  dicts: Nullable<Map<string, Map<string, DictDTO>>>;
}

export const useDictStore = defineStore({
  id: 'app-dict',
  state: (): DictInfo => ({
    dicts: Persistent.getLocal(DICTS_KEY),
  }),
  getters: {
    getDicts(state): Map<string, Map<string, DictDTO>> {
      return (
        state.dicts || Persistent.getLocal(DICTS_KEY) || new Map<string, Map<string, DictDTO>>()
      );
    },
  },
  actions: {
    setDicts(dicts: Map<string, Map<string, DictDTO>>) {
      this.dicts = dicts;
      Persistent.setLocal(DICTS_KEY, this.dicts, true);
    },
    clearDicts() {
      Persistent.removeLocal(DICTS_KEY, true);
      this.dicts = null;
    },
    async initDicts() {
      getAllDict().then((res) => {
        if (!res || res.length <= 0) {
          return;
        }
        const dicts = res as DictTypeDTO[];
        const dictMap: Map<string, Map<string, DictDTO>> = dicts.reduce((map, dict) => {
          const value = dict.details ? dict.details : [];
          const dictLabelMap: Map<string, DictDTO> = value.reduce((mmap, dictLabel) => {
            mmap.set(dictLabel.value, dictLabel);
            return mmap;
          }, new Map<string, DictDTO>());
          map.set(dict.code, dictLabelMap);
          return map;
        }, new Map<string, Map<string, DictDTO>>());
        this.setDicts(dictMap);
      });
    },
  },
});

export function getDictByCode(code: string) {
  const dictStore = useDictStore();
  const dicts = dictStore.getDicts;
  if (!dicts || dicts.size <= 0) {
    return [];
  }
  const dictLables = dicts.get(code);
  if (!dictLables) {
    return [];
  }
  return Array.from(dictLables.values()).sort((x) => x.sort);
}

export function getDictByCodeAndValue(code: string, value: string | number) {
  if (!code || !value) {
    return value;
  }
  const dictStore = useDictStore();
  const dictMap = dictStore.getDicts;
  if (!dictMap || dictMap.size <= 0) {
    return value;
  }
  const dictLableMap = dictMap.get(code);
  if (!dictLableMap || dictLableMap.size <= 0) {
    return value;
  }
  const valueStr = value + '';
  const dictLable = dictLableMap.get(valueStr);
  if (!dictLable) {
    return value;
  }
  return dictLable.label;
}
