package top.ticho.rainbow.infrastructure.mapper;

import top.ticho.boot.datasource.mapper.RootMapper;
import top.ticho.rainbow.infrastructure.entity.FileInfo;
import org.springframework.stereotype.Repository;


/**
 * 文件信息 mapper
 *
 * @author zhajianjun
 * @date 2024-04-23 17:55
 */
@Repository
public interface FileInfoMapper extends RootMapper<FileInfo> {

}