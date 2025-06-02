package top.ticho.rainbow.domain.repository;

import top.ticho.rainbow.domain.entity.FileInfo;

import java.util.List;

/**
 * 文件信息 repository接口
 *
 * @author zhajianjun
 * @date 2024-04-23 17:55
 */
public interface FileInfoRepository {

    boolean save(FileInfo fileInfo);

    boolean remove(Long id);

    boolean modify(FileInfo fileInfo);

    boolean modifyBatch(List<FileInfo> fileInfos);

    List<FileInfo> list(List<Long> ids);

    FileInfo find(Long id);

    FileInfo getByChunkId(String chunkId);

}

