package top.ticho.rainbow.domain.entity.vo;

/**
 * @param name   角色名称
 * @param status 状态;1-启用,0-停用
 * @param remark 备注信息
 * @author zhajianjun
 * @date 2025-03-08 14:44
 */
public record RoleModifyVO(String name, Integer status, String remark) {

}
