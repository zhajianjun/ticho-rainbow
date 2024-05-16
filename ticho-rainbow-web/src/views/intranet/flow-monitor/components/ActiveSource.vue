<template>
  <Card :title="title" :loading="loading">
    <div ref="chartRef" :style="{ width, height }"></div>
  </Card>
</template>
<script lang="ts" setup>
  import { PropType, Ref, ref, watch } from 'vue';
  import { Card } from 'ant-design-vue';
  import { useECharts } from '@/hooks/web/useECharts';
  import { dataType } from './type';

  const props = defineProps({
    loading: Boolean,
    title: {
      type: String as PropType<string>,
      default: '标题',
    },
    color: {
      type: Array as PropType<string[]>,
      default: () => ['#5ab1ef', '#ff0000'],
    },
    datas: {
      type: Array as PropType<dataType[]>,
      default: () => [
        { name: 'A', value: 1 },
        { name: 'B', value: 1 },
      ],
    },
    width: {
      type: String as PropType<string>,
      default: '100%',
    },
    height: {
      type: String as PropType<string>,
      default: '300px',
    },
  });
  const chartRef = ref<HTMLDivElement | null>(null);
  const { setOptions } = useECharts(chartRef as Ref<HTMLDivElement>);

  watch(
    () => props.loading,
    () => {
      console.log('props.datas', props.loading, props.datas);
      if (props.loading) {
        return;
      }
      setOptions({
        tooltip: {
          trigger: 'item',
        },
        legend: {
          bottom: '1%',
          left: 'center',
        },
        series: [
          {
            color: props.color,
            name: props.title,
            type: 'pie',
            radius: ['40%', '70%'],
            avoidLabelOverlap: false,
            itemStyle: {
              borderRadius: 10,
              borderColor: '#fff',
              borderWidth: 2,
            },
            label: {
              show: false,
              position: 'center',
            },
            emphasis: {
              label: {
                show: true,
                fontSize: '12',
                fontWeight: 'bold',
              },
            },
            labelLine: {
              show: false,
            },
            data: props.datas,
            animationType: 'scale',
            animationEasing: 'exponentialInOut',
            animationDelay: function () {
              return Math.random() * 100;
            },
          },
        ],
      });
    },
    { immediate: true },
  );
</script>
