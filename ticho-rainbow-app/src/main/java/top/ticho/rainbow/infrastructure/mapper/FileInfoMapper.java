package top.ticho.rainbow.infrastructure.mapper;

import org.springframework.stereotype.Repository;
import top.ticho.boot.datasource.mapper.RootMapper;
import top.ticho.rainbow.infrastructure.entity.FileInfo;


/**
 * 文件信息 mapper
 *
 * @author zhajianjun
 * @date 2024-04-23 17:55
 */
@Repository
public interface FileInfoMapper extends RootMapper<FileInfo> {

}