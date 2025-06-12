import { PropType } from 'vue';

export interface BasicProps {
  width: string;
  height: string;
}

export const basicProps = {
  width: {
    type: String as PropType<string>,
    default: '100%',
  },
  height: {
    type: String as PropType<string>,
    default: '280px',
  },
  loading: Boolean,
  title: {
    type: String as PropType<string>,
    default: '标题',
  },
};
