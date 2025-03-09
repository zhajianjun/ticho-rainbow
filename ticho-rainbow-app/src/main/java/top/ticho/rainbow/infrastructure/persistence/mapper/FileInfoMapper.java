package top.ticho.rainbow.infrastructure.persistence.mapper;

import org.springframework.stereotype.Repository;
import top.ticho.rainbow.infrastructure.persistence.po.FileInfoPO;
import top.ticho.starter.datasource.mapper.TiMapper;


/**
 * 文件信息 mapper
 *
 * @author zhajianjun
 * @date 2024-04-23 17:55
 */
@Repository
public interface FileInfoMapper extends TiMapper<FileInfoPO> {

}