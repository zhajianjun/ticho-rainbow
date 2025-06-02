package top.ticho.rainbow.application.assembler;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import top.ticho.rainbow.application.dto.command.MenuModifyCommand;
import top.ticho.rainbow.application.dto.command.MenuSaveCommand;
import top.ticho.rainbow.application.dto.response.MenuDTO;
import top.ticho.rainbow.application.dto.response.MenuDtlDTO;
import top.ticho.rainbow.application.dto.response.RouteDTO;
import top.ticho.rainbow.application.dto.response.RouteMetaDTO;
import top.ticho.rainbow.domain.entity.Menu;
import top.ticho.rainbow.domain.entity.vo.MenuModifyVO;
import top.ticho.starter.web.util.TiIdUtil;

import java.util.Objects;

/**
 * 菜单信息 转换
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
@Mapper(componentModel = "spring", imports = {StrUtil.class, CollUtil.class, Objects.class, TiIdUtil.class})
public interface MenuAssembler {

    @Mapping(target = "id", expression = "java(TiIdUtil.getId())")
    @Mapping(target = "perms", expression = "java(CollUtil.join(menuSaveCommand.getPerms(), \",\"))")
    Menu toEntity(MenuSaveCommand menuSaveCommand);

    @Mapping(target = "perms", expression = "java(CollUtil.join(menuModifyCommand.getPerms(), \",\"))")
    MenuModifyVO toModifyVO(MenuModifyCommand menuModifyCommand);

    /**
     * 菜单信息DTO
     *
     * @param entity 菜单信息
     * @return {@link MenuDTO}
     */
    @Mapping(target = "perms", expression = "java(StrUtil.split(entity.getPerms(), ','))")
    MenuDTO toDTO(Menu entity);

    @Mapping(target = "perms", expression = "java(StrUtil.split(Objects.equals(entity.getPerms(), \"\") ? null : entity.getPerms(), ','))")
    MenuDtlDTO toDtlDTO(Menu entity);

    @Mapping(source = "name", target = "title")
    RouteMetaDTO toRouteMetaDTO(Menu entity);

    @Mapping(source = "componentName", target = "name")
    RouteDTO toRouteDTO(Menu entity);


}