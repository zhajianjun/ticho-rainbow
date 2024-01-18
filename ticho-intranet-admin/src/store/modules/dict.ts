import { defineStore } from 'pinia';
import { DictDTO } from '@/api/system/model/dictModel';
import { getAllDict } from '@/api/system/dict';
import { DictTypeDTO } from '@/api/system/model/dictTypeModel';

export const useDictStore = defineStore({
  id: 'app-dict',
  state: (): { dicts: Map<string, DictDTO[]> } => ({
    dicts: new Map<string, DictDTO[]>(),
  }),
  getters: {
    getDicts(state): Map<string, DictDTO[]> {
      return state.dicts;
    },
  },
  actions: {
    setDicts(key: string, value: DictDTO[]) {
      this.dicts.set(key, value);
    },
    clearDicts() {
      this.dicts.clear();
    },
    async initDicts() {
      getAllDict().then((res) => {
        if (!res || res.length <= 0) {
          return;
        }
        const dicts = res as DictTypeDTO[];
        dicts.forEach((x) => {
          this.setDicts(x.code, x.details);
        });
      });
    },
  },
});
