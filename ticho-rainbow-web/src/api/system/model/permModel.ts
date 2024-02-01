export interface PermDTO {
  /** 标签 */
  label: string;
  /** 标签值 */
  value: string;
  /** 子对象 */
  children: PermDTO[];
}
