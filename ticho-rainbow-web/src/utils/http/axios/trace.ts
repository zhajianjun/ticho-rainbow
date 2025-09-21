const ALPHABET = 'abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ';
export function traceId() {
  return crypto.randomUUID().replace(/-/g, '');
}

export function spanId() {
  // 生成 UUID 并移除 '-'
  const uuidStr = crypto.randomUUID().replace(/-/g, '');
  let result = '';
  // 分成8组，每组4个字符
  for (let i = 0; i < 8; i++) {
    // 取出每组的4个字符
    const str = uuidStr.substring(i * 4, i * 4 + 4);
    // 将这4个字符作为十六进制数解析，并对62取模
    const x = parseInt(str, 16);
    const index = x % 62;
    // 根据索引从字符集中选择字符
    result += ALPHABET[index];
  }

  return result;
}
