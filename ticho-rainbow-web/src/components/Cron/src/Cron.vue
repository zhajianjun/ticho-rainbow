<template>
  <div :class="`${prefixCls}`">
    <div class="content">
      <a-tabs v-model:activeKey="activeKey" type="card">
        <a-tab-pane tab="秒" key="second" v-if="!hideSecond">
          <SecondUI v-model:value="second" :disabled="disabled" />
        </a-tab-pane>
        <a-tab-pane tab="分" key="minute">
          <MinuteUI v-model:value="minute" :disabled="disabled" />
        </a-tab-pane>
        <a-tab-pane tab="时" key="hour">
          <HourUI v-model:value="hour" :disabled="disabled" />
        </a-tab-pane>
        <a-tab-pane tab="日" key="day">
          <DayUI v-model:value="day" :week="week" :disabled="disabled" />
        </a-tab-pane>
        <a-tab-pane tab="月" key="month">
          <MonthUI v-model:value="month" :disabled="disabled" />
        </a-tab-pane>
        <a-tab-pane tab="周" key="week">
          <WeekUI v-model:value="week" :day="day" :disabled="disabled" />
        </a-tab-pane>
        <a-tab-pane tab="年" key="year" v-if="!hideYear && !hideSecond">
          <YearUI v-model:value="year" :disabled="disabled" />
        </a-tab-pane>
      </a-tabs>
      <a-divider v-if="inputArea" />
      <!-- 执行时间预览 -->
      <a-row :gutter="8" v-if="inputArea">
        <a-col :span="18" style="margin-top: 22px">
          <a-row :gutter="8">
            <a-col :span="8" style="margin-bottom: 12px">
              <a-input v-model:value="inputValues.second" @blur="onInputBlur">
                <template #addonBefore>
                  <span class="allow-click" @click="activeKey = 'second'">秒</span>
                </template>
              </a-input>
            </a-col>
            <a-col :span="8" style="margin-bottom: 12px">
              <a-input v-model:value="inputValues.minute" @blur="onInputBlur">
                <template #addonBefore>
                  <span class="allow-click" @click="activeKey = 'minute'">分</span>
                </template>
              </a-input>
            </a-col>
            <a-col :span="8" style="margin-bottom: 12px">
              <a-input v-model:value="inputValues.hour" @blur="onInputBlur">
                <template #addonBefore>
                  <span class="allow-click" @click="activeKey = 'hour'">时</span>
                </template>
              </a-input>
            </a-col>
            <a-col :span="8" style="margin-bottom: 12px">
              <a-input v-model:value="inputValues.day" @blur="onInputBlur">
                <template #addonBefore>
                  <span class="allow-click" @click="activeKey = 'day'">日</span>
                </template>
              </a-input>
            </a-col>
            <a-col :span="8" style="margin-bottom: 12px">
              <a-input v-model:value="inputValues.month" @blur="onInputBlur">
                <template #addonBefore>
                  <span class="allow-click" @click="activeKey = 'month'">月</span>
                </template>
              </a-input>
            </a-col>
            <a-col :span="8" style="margin-bottom: 12px">
              <a-input v-model:value="inputValues.week" @blur="onInputBlur">
                <template #addonBefore>
                  <span class="allow-click" @click="activeKey = 'week'">周</span>
                </template>
              </a-input>
            </a-col>
            <a-col :span="8">
              <a-input v-model:value="inputValues.year" @blur="onInputBlur">
                <template #addonBefore>
                  <span class="allow-click" @click="activeKey = 'year'">年</span>
                </template>
              </a-input>
            </a-col>
            <a-col :span="16">
              <a-input v-model:value="inputValues.cron" @blur="onInputCronBlur">
                <template #addonBefore>
                  <a-tooltip title="Cron表达式">式</a-tooltip>
                </template>
              </a-input>
            </a-col>
          </a-row>
        </a-col>
        <a-col :span="6">
          <div>近十次执行时间（不含年）</div>
          <a-textarea :value="preTimeList" :rows="5" />
        </a-col>
      </a-row>
    </div>
  </div>
</template>

<script lang="ts" setup>
  import { computed, provide, reactive, ref, watch } from 'vue';
  import {
    Col as ACol,
    Divider as ADivider,
    Input as AInput,
    Row as ARow,
    TabPane as ATabPane,
    Tabs as ATabs,
    Textarea as ATextarea,
    Tooltip as ATooltip,
  } from 'ant-design-vue';
  import CronParser from 'cron-parser';
  import SecondUI from './tabs/SecondUI.vue';
  import MinuteUI from './tabs/MinuteUI.vue';
  import HourUI from './tabs/HourUI.vue';
  import DayUI from './tabs/DayUI.vue';
  import MonthUI from './tabs/MonthUI.vue';
  import WeekUI from './tabs/WeekUI.vue';
  import YearUI from './tabs/YearUI.vue';
  import { cronEmits, cronProps } from './cron.data';
  import { formatToDateTime } from '@/utils/dateUtil';
  import { debounce } from 'lodash-es';

  const prefixCls = 'shiyzhang-easy-cron-inner';
  provide('prefixCls', prefixCls);
  const emit = defineEmits([...cronEmits]);
  const props = defineProps({ ...cronProps });
  const activeKey = ref(props.hideSecond ? 'minute' : 'second');
  const second = ref('*');
  const minute = ref('*');
  const hour = ref('*');
  const day = ref('*');
  const month = ref('*');
  const week = ref('?');
  const year = ref('*');
  const inputValues = reactive({
    second: '',
    minute: '',
    hour: '',
    day: '',
    month: '',
    week: '',
    year: '',
    cron: '',
  });
  const preTimeList = ref('执行预览，会忽略年份参数。');

  // cron表达式
  const cronValueInner = computed(() => {
    let result: string[] = [];
    if (!props.hideSecond) {
      result.push(second.value ? second.value : '*');
    }
    result.push(minute.value ? minute.value : '*');
    result.push(hour.value ? hour.value : '*');
    result.push(day.value ? day.value : '*');
    result.push(month.value ? month.value : '*');
    result.push(week.value ? week.value : '?');
    if (!props.hideYear && !props.hideSecond) result.push(year.value ? year.value : '*');
    return result.join(' ');
  });
  // 不含年
  const cronValueNoYear = computed(() => {
    const v = cronValueInner.value;
    if (props.hideYear || props.hideSecond) return v;
    const vs = v.split(' ');
    if (vs.length >= 6) {
      // 转成 Quartz 的规则
      vs[5] = convertWeekToQuartz(vs[5]);
    }
    return vs.slice(0, vs.length - 1).join(' ');
  });
  const calTriggerList = debounce(calTriggerListInner, 500);

  watch(
    () => props.value,
    (newVal) => {
      if (newVal === cronValueInner.value) {
        return;
      }
      formatValue();
    },
  );

  watch(cronValueInner, (newValue) => {
    calTriggerList();
    emitValue(newValue);
    assignInput();
  });

  assignInput();
  formatValue();
  calTriggerListInner();

  function assignInput() {
    inputValues.second = second.value;
    inputValues.minute = minute.value;
    inputValues.hour = hour.value;
    inputValues.day = day.value;
    inputValues.month = month.value;
    inputValues.week = week.value;
    inputValues.year = year.value;
    inputValues.cron = cronValueInner.value;
  }

  function formatValue() {
    if (!props.value) return;
    const values = props.value.split(' ').filter((item) => !!item);
    if (!values || values.length <= 0) return;
    let i = 0;
    if (!props.hideSecond) second.value = values[i++];
    if (values.length > i) minute.value = values[i++];
    if (values.length > i) hour.value = values[i++];
    if (values.length > i) day.value = values[i++];
    if (values.length > i) month.value = values[i++];
    if (values.length > i) week.value = values[i++];
    if (values.length > i) year.value = values[i];
    assignInput();
  }

  // Quartz 的规则：
  // 1 = 周日，2 = 周一，3 = 周二，4 = 周三，5 = 周四，6 = 周五，7 = 周六
  function convertWeekToQuartz(week: string) {
    let convert = (v: string) => {
      if (v === '0') {
        return '1';
      }
      if (v === '1') {
        return '0';
      }
      return (Number.parseInt(v) - 1).toString();
    };
    // 匹配示例 1-7 or 1/7
    let patten1 = /^([0-7])([-/])([0-7])$/;
    // 匹配示例 1,4,7
    let patten2 = /^([0-7])(,[0-7])+$/;
    if (/^[0-7]$/.test(week)) {
      return convert(week);
    } else if (patten1.test(week)) {
      return week.replace(patten1, ($0, before, separator, after) => {
        if (separator === '/') {
          return convert(before) + separator + after;
        } else {
          return convert(before) + separator + convert(after);
        }
      });
    } else if (patten2.test(week)) {
      return week
        .split(',')
        .map((v) => convert(v))
        .join(',');
    }
    return week;
  }

  function calTriggerListInner() {
    // 设置了回调函数
    if (props.remote) {
      props.remote(cronValueInner.value, +new Date(), (v) => {
        preTimeList.value = v;
      });
      return;
    }
    const format = 'YYYY-MM-DD HH:mm:ss';
    console.log(formatToDateTime(new Date(), format));
    const options = {
      currentDate: formatToDateTime(new Date(), format),
    };
    const iter = CronParser.parseExpression(cronValueNoYear.value, options);
    const result: string[] = [];
    for (let i = 1; i <= 10; i++) {
      result.push(formatToDateTime(new Date(iter.next() as any), format));
    }
    preTimeList.value = result.length > 0 ? result.join('\n') : '无执行时间';
  }

  function onInputBlur() {
    second.value = inputValues.second;
    minute.value = inputValues.minute;
    hour.value = inputValues.hour;
    day.value = inputValues.day;
    month.value = inputValues.month;
    week.value = inputValues.week;
    year.value = inputValues.year;
  }

  function onInputCronBlur(event: any) {
    emitValue(event.target.value);
  }

  function emitValue(value: any) {
    emit('change', value);
    emit('update:value', value);
  }
</script>
<style lang="less">
  @import './cron.inner';
</style>
