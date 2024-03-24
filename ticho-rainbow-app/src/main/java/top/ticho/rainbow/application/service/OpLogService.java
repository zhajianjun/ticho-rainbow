package top.ticho.rainbow.application.service;

import top.ticho.boot.view.core.PageResult;
import top.ticho.rainbow.interfaces.dto.OpLogDTO;
import top.ticho.rainbow.interfaces.query.OpLogQuery;


/**
 * 日志信息 服务接口
 *
 * @author zhajianjun
 * @date 2024-03-24 17:55
 */
public interface OpLogService {

    /**
     * 根据id查询日志信息
     *
     * @param id 主键
     * @return {@link OpLogDTO}
     */
    OpLogDTO getById(Long id);

    /**
     * 分页查询日志信息列表
     *
     * @param query 查询
     * @return {@link PageResult}<{@link OpLogDTO}>
     */
    PageResult<OpLogDTO> page(OpLogQuery query);

}

