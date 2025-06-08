package top.ticho.rainbow.infrastructure.persistence.converter;

import org.mapstruct.Mapper;
import top.ticho.rainbow.domain.entity.Menu;
import top.ticho.rainbow.infrastructure.persistence.po.MenuPO;

import java.util.List;

/**
 * 客户端信息 转换器
 *
 * @author zhajianjun
 * @date 2025-03-02 17:17
 */
@Mapper(componentModel = "spring")
public interface MenuConverter {

    List<Menu> toEntity(List<MenuPO> menuPOS);

    Menu toEntity(MenuPO one);

    MenuPO toPo(Menu menu);

    List<MenuPO> toPo(List<Menu> menus);

}
