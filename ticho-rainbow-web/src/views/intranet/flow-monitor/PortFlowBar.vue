<template>
  <Card :title="title" :loading="loading">
    <div ref="chartRef" :style="{ height, width }"></div>
  </Card>
</template>
<script lang="ts" setup>
  import { PropType, ref, Ref, watch } from 'vue';
  import { useECharts } from '@/hooks/web/useECharts';
  import { basicProps } from './components/props';
  import { Card } from 'ant-design-vue';
  import { FlowMonitorDTO } from '@/api/intranet/model/flowMonitorModel';

  const props = defineProps({
    ...basicProps,
    datas: {
      type: Array as PropType<FlowMonitorDTO[]>,
      required: true,
    },
  });
  const chartRef = ref<HTMLDivElement | null>(null);
  const { setOptions } = useECharts(chartRef as Ref<HTMLDivElement>);
  watch(
    () => props.loading,
    () => {
      if (props.loading) {
        return;
      }
      const xData: string[] = [];
      const y1Data: any = [];
      const y2Data: any = [];
      let yMaxValue = 0;
      props.datas.forEach((item) => {
        xData.push(item.port + '');
        y1Data.push({ value: item.readBytes });
        y2Data.push({ value: item.writeBytes });
        if (item.readBytes > yMaxValue) {
          yMaxValue = item.readBytes;
        }
        if (item.writeBytes > yMaxValue) {
          yMaxValue = item.writeBytes;
        }
      });
      yMaxValue = yMaxValue * 1.2;
      setOptions({
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            lineStyle: {
              width: 1,
              color: '#019680',
            },
          },
        },
        legend: {
          data: ['上行', '下行'],
          bottom: '92%',
          left: '90%',
        },
        grid: { left: '1%', right: '1%', top: '2  %', bottom: 0, containLabel: true },
        xAxis: {
          type: 'category',
          data: xData,
        },
        yAxis: {
          type: 'value',
          max: yMaxValue,
          splitNumber: 5,
          axisLabel: {
            show: true, //开启显示
            formatter: '{value}kb',
          },
        },
        series: [
          {
            name: '上行',
            color: '#54C7C9',
            data: y1Data,
            type: 'bar',
            barMaxWidth: 80,
            label: {
              show: true,
              formatter: '{c}kb',
              color: '#000000',
            },
          },
          {
            name: '下行',
            color: '#5AB1EF',
            data: y2Data,
            type: 'bar',
            barMaxWidth: 80,
            label: {
              show: true,
              formatter: '{c}kb',
              color: '#000000',
            },
          },
        ],
      });
    },
    { immediate: true },
  );
</script>
