package top.ticho.rainbow.application.repository;

import top.ticho.rainbow.interfaces.dto.query.PortQuery;
import top.ticho.rainbow.interfaces.dto.response.PortDTO;
import top.ticho.starter.view.core.TiPageResult;

import java.util.List;

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

}
