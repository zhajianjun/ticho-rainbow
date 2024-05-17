<template>
  <div class="p-4">
    <div class="md:flex enter-y">
      <div class="md:w-1/3 w-full">
        <ActiveSource :title="'客户端'" :datas="clients" :loading="loading" />
      </div>
      <div class="md:w-1/3 !md:mx-4 !md:my-0 !my-4 w-full">
        <ActiveSource :title="'端口'" :datas="ports" :loading="loading" />
      </div>
      <Card class="md:w-1/3 w-full !md:mt-0" :loading="loading" :title="'通道'">
        <template #extra>
          <Tag :color="'green'">数量</Tag>
        </template>
        <div class="pt-10 pb-2 px-4 flex justify-between items-center">
          <CountTo prefix="通道数: " :startVal="1" :endVal="channels" class="text-2xl" />
        </div>
        <div class="py-2 pl-4 flex justify-between items-center">
          <CountTo
            prefix="客户端总数: "
            :startVal="1"
            :endVal="flowMonitor.clients"
            class="text-2xl"
          />
          <CountTo
            prefix="有效: "
            :startVal="1"
            :endVal="flowMonitor.activeClients"
            class="text-2xl"
          />
        </div>
        <div class="py-2 pl-4 flex justify-between items-center">
          <CountTo prefix="端口总数: " :startVal="1" :endVal="flowMonitor.ports" class="text-2xl" />
          <CountTo
            prefix="有效: "
            :startVal="1"
            :endVal="flowMonitor.activePorts"
            class="text-2xl"
          />
        </div>
      </Card>
    </div>
    <PortFlowBar
      class="!my-5 enter-y"
      :title="'流量统计'"
      :datas="flowDetails"
      :loading="loading"
    />
  </div>
</template>

<script setup lang="ts">
  import { flowMonitorInfo } from '@/api/intranet/flowMonitor';
  import { ref } from 'vue';
  import ActiveSource from './components/ActiveSource.vue';
  import { dataType } from '@/views/intranet/flow-monitor/components/type';
  import PortFlowBar from './PortFlowBar.vue';
  import { FlowMonitorDTO, FlowMonitorStatsDTO } from '@/api/intranet/model/flowMonitorModel';
  import { CountTo } from '@/components/CountTo';
  import { Card, Tag } from 'ant-design-vue';

  const loading = ref(true);

  const clients = ref<dataType[]>([]);
  const ports = ref<dataType[]>([]);
  const flowDetails = ref<FlowMonitorDTO[]>([]);
  const channels = ref<number>(1);
  const flowMonitor = ref<FlowMonitorStatsDTO>({
    activeClients: 0,
    activePorts: 0,
    clients: 0,
    dateTime: '',
    flowDetails: [],
    ports: 0,
  });

  flowMonitorInfo().then((res) => {
    flowMonitor.value = res;
    clients.value = [
      { name: '在线', value: res.activeClients },
      { name: '离线', value: res.clients - res.activeClients },
    ] as dataType[];
    ports.value = [
      { name: '在线', value: res.activePorts },
      { name: '离线', value: res.ports - res.activePorts },
    ] as dataType[];
    flowDetails.value = res.flowDetails;
    channels.value = res.flowDetails.map((item) => item.channels).reduce((a, b) => a + b, 0);
    loading.value = false;
  });
</script>

<style scoped lang="less"></style>
