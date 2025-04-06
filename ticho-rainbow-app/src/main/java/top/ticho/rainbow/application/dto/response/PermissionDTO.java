package top.ticho.rainbow.application.dto.response;

import lombok.Data;

import java.util.List;

/**
 * 权限标识信息DTO
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
@Data
public class PermissionDTO {

    /** 编码 */
    private String code;
    /** 名称 */
    private String name;
    /** 排序 */
    private Integer sort;
    /** 子对象 */
    private List<PermissionDTO> children;

}
