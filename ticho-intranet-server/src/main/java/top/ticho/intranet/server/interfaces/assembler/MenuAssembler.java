package top.ticho.intranet.server.interfaces.assembler;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import top.ticho.intranet.server.infrastructure.entity.Menu;
import top.ticho.intranet.server.interfaces.dto.MenuDTO;
import top.ticho.intranet.server.interfaces.dto.MenuDtlDTO;
import top.ticho.intranet.server.interfaces.dto.RouteDTO;
import top.ticho.intranet.server.interfaces.dto.RouteMetaDTO;

import java.util.Objects;

/**
 * 菜单信息 转换
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
@Mapper(imports = {StrUtil.class, CollUtil.class, Objects.class})
public interface MenuAssembler {
    MenuAssembler INSTANCE = Mappers.getMapper(MenuAssembler.class);

    /**
     * 菜单信息
     *
     * @param dto 菜单信息DTO
     * @return {@link Menu}
     */
    @Mapping(target = "perms", expression = "java(CollUtil.join(dto.getPerms(), \",\"))")
    Menu dtoToEntity(MenuDTO dto);

    /**
     * 菜单信息DTO
     *
     * @param entity 菜单信息
     * @return {@link MenuDTO}
     */
    @Mapping(target = "perms", expression = "java(StrUtil.split(entity.getPerms(), ','))")
    MenuDTO entityToDto(Menu entity);

    @Mapping(target = "perms", expression = "java(StrUtil.split(Objects.equals(entity.getPerms(), \"\") ? null : entity.getPerms(), ','))")
    MenuDtlDTO entityToDtlDto(Menu entity);

    @Mapping(source = "name", target = "title")
    RouteMetaDTO entityToRouteMetaDto(Menu entity);

    @Mapping(source = "componentName", target = "name")
    RouteDTO entityToRouteDto(Menu entity);

}