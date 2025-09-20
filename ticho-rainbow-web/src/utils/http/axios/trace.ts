export function traceId() {
  return crypto.randomUUID().replace(/-/g, '');
}
