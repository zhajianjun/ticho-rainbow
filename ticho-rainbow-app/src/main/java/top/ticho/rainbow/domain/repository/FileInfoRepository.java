package top.ticho.rainbow.domain.repository;

import top.ticho.boot.datasource.service.RootService;
import top.ticho.rainbow.infrastructure.entity.FileInfo;
import top.ticho.rainbow.interfaces.query.FileInfoQuery;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 文件信息 repository接口
 *
 * @author zhajianjun
 * @date 2024-04-23 17:55
 */
public interface FileInfoRepository extends RootService<FileInfo> {

    /**
     * 根据条件查询文件信息列表
     *
     * @param query 查询条件
     * @return {@link List}<{@link FileInfo}>
     */
    List<FileInfo> list(FileInfoQuery query);

    /**
     * 根据分片id查询文件信息
     *
     * @param chunkId 分片id
     * @return {@link FileInfo}
     */
    FileInfo getByChunkId(String chunkId);

    /**
     * 查询分片状态的文件
     */
    List<FileInfo> listChunkFile(LocalDateTime before, Integer size);

    /**
     * 移除分片状态的文件
     */
    boolean removeChunkFile(Long id);



}

