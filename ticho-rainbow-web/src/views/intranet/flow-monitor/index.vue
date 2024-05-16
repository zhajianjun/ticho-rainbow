<template>
  <div class="p-4">
    <div class="md:flex enter-y">
      <div class="md:w-1/3 w-full">
        <ActiveSource :title="'客户端'" :datas="clients" :loading="loading" />
      </div>
      <div class="md:w-1/3 !md:mx-4 !md:my-0 !my-4 w-full">
        <ActiveSource :title="'端口'" :datas="ports" :loading="loading" />
      </div>
      <div class="md:w-1/3 w-full"></div>
    </div>
  </div>
</template>

<script setup lang="ts">
  import { flowMonitorInfo } from '@/api/intranet/flowMonitor';
  import { ref } from 'vue';
  import ActiveSource from './components/ActiveSource.vue';
  import { dataType } from '@/views/intranet/flow-monitor/components/type';

  const loading = ref(true);

  const clients = ref<dataType[]>([]);
  const ports = ref<dataType[]>([]);

  flowMonitorInfo().then((res) => {
    clients.value = [
      { name: '在线', value: res.activeClients },
      { name: '离线', value: res.clients - res.activeClients },
    ] as dataType[];
    ports.value = [
      { name: '在线', value: res.activePorts },
      { name: '离线', value: res.ports - res.activePorts },
    ] as dataType[];
    loading.value = false;
  });
</script>

<style scoped lang="less"></style>
