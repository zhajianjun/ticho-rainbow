package top.ticho.rainbow.application.repository;

import top.ticho.rainbow.application.dto.query.PortQuery;
import top.ticho.rainbow.application.dto.response.PortDTO;
import top.ticho.rainbow.domain.entity.Port;
import top.ticho.starter.view.core.TiPageResult;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author zhajianjun
 * @date 2025-04-06 15:30
 */
public interface PortAppRepository {

    /**
     * 根据所有端口信息列表
     */
    List<PortDTO> all();

    TiPageResult<PortDTO> page(PortQuery query);

    /**
     * 根据客户端秘钥列表查询
     *
     * @param accessKeys 客户端秘钥列表
     * @return {@link List}<{@link Port}>
     */
    <T> Map<String, List<T>> listAndGroupByAccessKey(Collection<String> accessKeys, Function<Port, T> function, Predicate<Port> filter);

}
