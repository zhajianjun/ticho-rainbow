package top.ticho.rainbow.domain.entity.vo;

/**
 * @param isSys   是否系统字典;1-是,0-否
 * @param status  状态;1-启用,0-停用
 * @param remark  备注信息
 * @param version 版本号
 * @author zhajianjun
 * @date 2025-03-02 19:59
 */
public record DictModifyVO(String name, Integer isSys, Integer status, String remark, Long version) {

}
