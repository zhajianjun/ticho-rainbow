import { FormSchema } from '@/components/Form';

export const formSchema: FormSchema[] = [
  {
    field: `upload`,
    slot: 'uploadSlot',
    colProps: {
      span: 24,
    },
  },
  {
    field: 'downloadModel',
    slot: 'downloadModelSlot',
    colProps: {
      span: 24,
    },
  },
];
