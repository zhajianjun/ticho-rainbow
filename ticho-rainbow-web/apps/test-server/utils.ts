export class TiResult {
  static success(data: any) {
    return {
      code: 0,
      success: true,
      result: data,
    };
  }
}
