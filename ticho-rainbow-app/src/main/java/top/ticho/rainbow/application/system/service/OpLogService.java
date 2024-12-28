package top.ticho.rainbow.application.system.service;

import top.ticho.boot.view.core.TiPageResult;
import top.ticho.rainbow.interfaces.dto.OpLogDTO;
import top.ticho.rainbow.interfaces.query.OpLogQuery;

import java.io.IOException;


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
     * @return {@link TiPageResult}<{@link OpLogDTO}>
     */
    TiPageResult<OpLogDTO> page(OpLogQuery query);

    /**
     * 导出操作日志
     *
     * @param query 查询条件
     */
    void expExcel(OpLogQuery query) throws IOException;
}

