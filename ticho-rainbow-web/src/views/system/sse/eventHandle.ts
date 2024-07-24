import { SseEvent, SseMessage } from './typing';

export function successHandle(sseMessageStr: string) {
  if (sseMessageStr === null || sseMessageStr === '') {
    console.error('EventSource message is null: ', sseMessageStr);
    return;
  }
  const sseMessage: SseMessage<any> = JSON.parse(sseMessageStr);
  if (sseMessage === null || sseMessage.event === null) {
    console.error('EventSource message error: ', sseMessageStr);
    return;
  }
  const event = sseMessage.event;
  switch (event) {
    case SseEvent.HEATBEAT: {
      console.log('心跳检测:', sseMessage.data);
      alert('心跳检测');
      break;
    }
    default: {
      console.log('Unknown event:', event);
      break;
    }
  }
}

export function failHandle(error: any) {
  console.error('EventSource fail: ', error);
}
