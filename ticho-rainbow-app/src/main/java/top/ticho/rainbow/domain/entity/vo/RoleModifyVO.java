package top.ticho.rainbow.domain.entity.vo;

/**
 * @param name    角色名称
 * @param remark  备注信息
 * @param version 版本号
 * @author zhajianjun
 * @date 2025-03-08 14:44
 */
public record RoleModifyVO(
    String name,
    String remark,
    Long version
) {

}
