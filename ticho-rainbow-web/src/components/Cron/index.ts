import { withInstall } from '@/utils';
import type { Plugin } from 'vue';
import cron from './src/Cron.vue';
import { cronRule } from './src/validator';

cron.validator = cronRule.validator;

export const Cron = withInstall(cron);

export default Cron as typeof Cron &
  Plugin & {
    readonly validator: typeof cronRule.validator;
  };
