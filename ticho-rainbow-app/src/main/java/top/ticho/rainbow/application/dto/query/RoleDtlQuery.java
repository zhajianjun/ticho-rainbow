package top.ticho.rainbow.application.dto.query;

import lombok.Data;

import java.util.List;

/**
 * 角色菜单查询条件
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
@Data
public class RoleDtlQuery {

    /** 角色code */
    private List<Long> roleIds;
    /** 显示所有 */
    private Boolean showAll = false;
    /** 是否进行树化 */
    private Boolean treeHandle = false;

}
