import type { BasicTableProps, FetchParams, SorterResult } from '../types/table';
import type { PaginationProps } from '../types/pagination';
import {
  computed,
  ComputedRef,
  onMounted,
  reactive,
  ref,
  Ref,
  unref,
  watch,
  watchEffect,
} from 'vue';
import { useTimeoutFn } from '@vben/hooks';
import { buildUUID } from '@/utils/uuid';
import { isBoolean, isFunction, isObject } from '@/utils/is';
import { cloneDeep, get, merge } from 'lodash-es';
import { FETCH_SETTING, PAGE_SIZE, ROW_KEY } from '../const';
import { parseRowKeyValue } from '../helper';
import type { Key } from 'ant-design-vue/lib/table/interface';

interface ActionType {
  getPaginationInfo: ComputedRef<boolean | PaginationProps>;
  setPagination: (info: Partial<PaginationProps>) => void;
  setLoading: (loading: boolean) => void;
  getFieldsValue: () => Recordable;
  clearSelectedRowKeys: () => void;
  tableData: Ref<Recordable[]>;
}

interface SearchState {
  sortInfo: Recordable;
  filterInfo: Record<string, string[]>;
}

export function useDataSource(
  propsRef: ComputedRef<BasicTableProps>,
  {
    getPaginationInfo,
    setPagination,
    setLoading,
    getFieldsValue,
    clearSelectedRowKeys,
    tableData,
  }: ActionType,
  emit: EmitType,
) {
  const searchState = reactive<SearchState>({
    sortInfo: {},
    filterInfo: {},
  });
  const dataSourceRef = ref<Recordable[]>([]);
  const rawDataSourceRef = ref<Recordable>({});
  const searchInfoRef = ref<Recordable>({});

  watchEffect(() => {
    tableData.value = unref(dataSourceRef);
  });

  watch(
    () => unref(propsRef).dataSource,
    () => {
      const { dataSource, api } = unref(propsRef);
      !api && dataSource && (dataSourceRef.value = dataSource);
    },
    {
      immediate: true,
    },
  );

  function handleTableChange(
    pagination: PaginationProps,
    filters: Partial<Recordable<string[]>>,
    sorter: SorterResult,
  ) {
    const { clearSelectOnPageChange, sortFn, filterFn } = unref(propsRef);
    if (clearSelectOnPageChange) {
      clearSelectedRowKeys();
    }
    setPagination(pagination);

    const params: Recordable = {};
    if (sorter && isFunction(sortFn)) {
      const sortInfo = sortFn(sorter);
      searchState.sortInfo = sortInfo;
      params.sortInfo = sortInfo;
    }

    if (filters && isFunction(filterFn)) {
      const filterInfo = filterFn(filters);
      searchState.filterInfo = filterInfo;
      params.filterInfo = filterInfo;
    }
    fetch(params);
  }

  function setTableKey(items: any[]) {
    if (!items || !Array.isArray(items)) return;
    items.forEach((item) => {
      if (!item[ROW_KEY]) {
        item[ROW_KEY] = buildUUID();
      }
      if (item.children && item.children.length) {
        setTableKey(item.children);
      }
    });
  }

  const getAutoCreateKey = computed(() => {
    return unref(propsRef).autoCreateKey && !unref(propsRef).rowKey;
  });

  const getRowKey = computed(() => {
    const { rowKey } = unref(propsRef);
    return unref(getAutoCreateKey) ? ROW_KEY : rowKey;
  });

  const getDataSourceRef: Ref<Recordable<any>[]> = ref([]);

  watch(
    () => dataSourceRef.value,
    () => {
      const dataSource = unref(dataSourceRef);
      if (!dataSource || dataSource.length === 0) {
        getDataSourceRef.value = unref(dataSourceRef);
      }
      if (unref(getAutoCreateKey)) {
        const firstItem = dataSource[0];
        const lastItem = dataSource[dataSource.length - 1];

        if (firstItem && lastItem) {
          if (!firstItem[ROW_KEY] || !lastItem[ROW_KEY]) {
            const data = cloneDeep(unref(dataSourceRef));
            data.forEach((item) => {
              if (!item[ROW_KEY]) {
                item[ROW_KEY] = buildUUID();
              }
              if (item.children && item.children.length) {
                setTableKey(item.children);
              }
            });
            dataSourceRef.value = data;
          }
        }
      }
      getDataSourceRef.value = unref(dataSourceRef);
    },
    {
      deep: true,
    },
  );

  async function updateTableData(index: number, key: Key, value: any) {
    const record = dataSourceRef.value[index];
    if (record) {
      dataSourceRef.value[index][key] = value;
    }
    return dataSourceRef.value[index];
  }

  function updateTableDataRecord(keyValue: Key, record: Recordable): Recordable | undefined {
    const row = findTableDataRecord(keyValue);

    if (row) {
      for (const field in row) {
        if (Reflect.has(record, field)) row[field] = record[field];
      }
      return row;
    }
  }

  function deleteTableDataRecord(keyValues: Key | Key[]) {
    if (!dataSourceRef.value || dataSourceRef.value.length == 0) return;
    const delKeyValues = !Array.isArray(keyValues) ? [keyValues] : keyValues;

    function deleteRow(data, keyValue) {
      const row: { index: number; data: [] } = findRow(data, keyValue);
      if (row === null || row.index === -1) {
        return;
      }
      row.data.splice(row.index, 1);

      function findRow(data, keyValue) {
        if (data === null || data === undefined) {
          return null;
        }
        for (let i = 0; i < data.length; i++) {
          const row = data[i];
          if (parseRowKeyValue(unref(getRowKey), row) === keyValue) {
            return { index: i, data };
          }
          if (row.children?.length > 0) {
            const result = findRow(row.children, keyValue);
            if (result != null) {
              return result;
            }
          }
        }
        return null;
      }
    }

    for (const keyValue of delKeyValues) {
      deleteRow(dataSourceRef.value, keyValue);
      deleteRow(unref(propsRef).dataSource, keyValue);
    }
    setPagination({
      total: unref(propsRef).dataSource?.length,
    });
  }

  function insertTableDataRecord(
    record: Recordable | Recordable[],
    index?: number,
  ): Recordable[] | undefined {
    // if (!dataSourceRef.value || dataSourceRef.value.length == 0) return;
    index = index ?? dataSourceRef.value?.length;
    const _record = isObject(record) ? [record as Recordable] : (record as Recordable[]);
    unref(dataSourceRef).splice(index, 0, ..._record);
    return unref(dataSourceRef);
  }

  function findTableDataRecord(keyValue: Key) {
    if (!dataSourceRef.value || dataSourceRef.value.length === 0) return;
    const { childrenColumnName = 'children' } = unref(propsRef);

    const findRow = (array: any[]) => {
      let ret;
      array.some(function iter(r) {
        if (parseRowKeyValue(unref(getRowKey), r) === keyValue) {
          ret = r;
          return true;
        }
        return r[childrenColumnName] && r[childrenColumnName].some(iter);
      });
      return ret;
    };

    return findRow(dataSourceRef.value);
  }

  async function fetch(opt?: FetchParams) {
    const {
      api,
      searchInfo,
      defSort,
      fetchSetting,
      beforeFetch,
      afterFetch,
      useSearchForm,
      pagination,
    } = unref(propsRef);
    if (!api || !isFunction(api)) return;
    try {
      setLoading(true);
      const { pageField, sizeField, listField, totalField } = Object.assign(
        {},
        FETCH_SETTING,
        fetchSetting,
      );
      let pageParams: Recordable = {};

      const { current = 1, pageSize = PAGE_SIZE } = unref(getPaginationInfo) as PaginationProps;

      if ((isBoolean(pagination) && !pagination) || isBoolean(getPaginationInfo)) {
        pageParams = {};
      } else {
        pageParams[pageField] = (opt && opt.page) || current;
        pageParams[sizeField] = pageSize;
      }

      const { sortInfo = {}, filterInfo } = searchState;

      let params: Recordable = merge(
        pageParams,
        useSearchForm ? getFieldsValue() : {},
        searchInfo,
        opt?.searchInfo ?? {},
        defSort,
        sortInfo,
        filterInfo,
        opt?.sortInfo ?? {},
        opt?.filterInfo ?? {},
      );
      if (beforeFetch && isFunction(beforeFetch)) {
        params = (await beforeFetch(params)) || params;
      }
      searchInfoRef.value = params;
      const res = await api(params);
      rawDataSourceRef.value = res;

      const isArrayResult = Array.isArray(res);

      let resultItems: Recordable[] = isArrayResult ? res : get(res, listField);
      const resultTotal: number = isArrayResult ? res.length : get(res, totalField);

      // 假如数据变少，导致总页数变少并小于当前选中页码，通过getPaginationRef获取到的页码是不正确的，需获取正确的页码再次执行
      if (Number(resultTotal)) {
        const currentTotalPage = Math.ceil(resultTotal / pageSize);
        if (current > currentTotalPage) {
          setPagination({
            current: currentTotalPage,
          });
          return await fetch(opt);
        }
      }

      if (afterFetch && isFunction(afterFetch)) {
        resultItems = (await afterFetch(resultItems)) || resultItems;
      }
      dataSourceRef.value = resultItems;
      setPagination({
        total: resultTotal || 0,
      });
      if (opt && opt.page) {
        setPagination({
          current: opt.page || 1,
        });
      }
      emit('fetch-success', {
        items: unref(resultItems),
        total: resultTotal,
      });
      return resultItems;
    } catch (error) {
      emit('fetch-error', error);
      dataSourceRef.value = [];
      setPagination({
        total: 0,
      });
    } finally {
      setLoading(false);
    }
  }

  function setTableData<T = Recordable>(values: T[]) {
    dataSourceRef.value = values as Recordable[];
  }

  function getDataSource<T = Recordable>() {
    return getDataSourceRef.value as T[];
  }

  function getRawDataSource<T = Recordable>() {
    return rawDataSourceRef.value as T;
  }

  async function reload(opt?: FetchParams) {
    return await fetch(opt);
  }

  function getSearchInfo<T = Recordable>() {
    return searchInfoRef.value as T;
  }

  onMounted(() => {
    useTimeoutFn(() => {
      unref(propsRef).immediate && fetch();
    }, 16);
  });

  return {
    getDataSourceRef: computed(() => getDataSourceRef.value),
    getDataSource,
    getRawDataSource,
    searchInfoRef,
    getSearchInfo,
    getRowKey,
    setTableData,
    getAutoCreateKey,
    fetch,
    reload,
    updateTableData,
    updateTableDataRecord,
    deleteTableDataRecord,
    insertTableDataRecord,
    findTableDataRecord,
    handleTableChange,
  };
}
