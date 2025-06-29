import type { ComputedRef, Ref } from 'vue';
import { computed, h, nextTick, unref, watchEffect } from 'vue';
import type { BasicTableProps } from '../types/table';
import TableFooter from '../components/TableFooter.vue';
import { useEventListener } from '@/hooks/event/useEventListener';

export function useTableFooter(
  propsRef: ComputedRef<BasicTableProps>,
  scrollRef: ComputedRef<BasicTableProps['scroll']>,
  tableElRef: Ref<ComponentRef>,
  getDataSourceRef: ComputedRef<Recordable>,
) {
  const getIsEmptyData = computed(() => {
    return (unref(getDataSourceRef) || []).length === 0;
  });

  const getFooterProps = computed((): Recordable | undefined => {
    const { summaryFunc, showSummary, summaryData } = unref(propsRef);
    return showSummary && !unref(getIsEmptyData)
      ? () => h(TableFooter, { summaryFunc, summaryData, scroll: unref(scrollRef) })
      : undefined;
  });

  watchEffect(() => {
    handleSummary();
  });

  function handleSummary() {
    const { showSummary } = unref(propsRef);
    if (!showSummary || unref(getIsEmptyData)) return;

    nextTick(() => {
      const tableEl = unref(tableElRef);
      if (!tableEl) return;
      const bodyDom = tableEl.$el.querySelector(' .ant-table-content,  .ant-table-body');
      useEventListener({
        el: bodyDom,
        name: 'scroll',
        listener: () => {
          const footerBodyDom = tableEl.$el.querySelector(
            '.ant-table-footer .ant-table-container  [class^="ant-table-"]',
          ) as HTMLDivElement;
          if (!footerBodyDom || !bodyDom) return;
          footerBodyDom.scrollLeft = bodyDom.scrollLeft;
        },
        wait: 0,
        options: true,
      });
    });
  }

  return { getFooterProps };
}
