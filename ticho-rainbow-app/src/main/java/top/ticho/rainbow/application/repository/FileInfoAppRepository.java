package top.ticho.rainbow.application.repository;

import top.ticho.rainbow.interfaces.dto.FileInfoDTO;
import top.ticho.rainbow.interfaces.query.FileInfoQuery;
import top.ticho.starter.view.core.TiPageResult;

/**
 * @author zhajianjun
 * @date 2025-04-06 15:30
 */
public interface FileInfoAppRepository {

    /**
     * 分页查询文件信息列表
     *
     * @param query 查询条件
     */
    TiPageResult<FileInfoDTO> page(FileInfoQuery query);

}
