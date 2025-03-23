package top.ticho.rainbow.application.assembler;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import top.ticho.rainbow.application.dto.MenuDTO;
import top.ticho.rainbow.application.dto.response.RouteDTO;
import top.ticho.rainbow.application.dto.response.RouteMetaDTO;
import top.ticho.rainbow.application.dto.response.MenuDtlDTO;
import top.ticho.rainbow.domain.entity.Menu;

import java.util.Objects;

/**
 * 菜单信息 转换
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
@Mapper(componentModel = "spring", imports = {StrUtil.class, CollUtil.class, Objects.class})
public interface MenuAssembler {

    /**
     * 菜单信息
     *
     * @param dto 菜单信息DTO
     * @return {@link Menu}
     */
    @Mapping(target = "perms", expression = "java(CollUtil.join(dto.getPerms(), \",\"))")
    Menu toEntity(MenuDTO dto);

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