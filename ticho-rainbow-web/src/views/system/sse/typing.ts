export const enum SseEvent {
  /** 心跳 */
  HEATBEAT = 'HEATBEAT',
}

export interface SseMessage<T> {
  /** 事件 */
  event: SseEvent;
  /** 数据 */
  data: T;
}
