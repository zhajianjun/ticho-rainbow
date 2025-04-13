package top.ticho.rainbow.domain.repository;

import top.ticho.rainbow.domain.entity.FileInfo;

/**
 * 文件信息 repository接口
 *
 * @author zhajianjun
 * @date 2024-04-23 17:55
 */
public interface FileInfoRepository {

    /**
     * 保存文件信息
     *
     * @param fileInfo 文件信息
     * @return boolean
     */
    boolean save(FileInfo fileInfo);

    /**
     * 删除文件信息
     *
     * @param id 编号
     * @return boolean
     */
    boolean remove(Long id);

    /**
     * 修改文件信息
     *
     * @param fileInfo 文件信息
     * @return boolean
     */
    boolean modify(FileInfo fileInfo);

    /**
     * 根据编号查询文件信息
     *
     * @param id 身份证
     * @return {@link FileInfo }
     */
    FileInfo find(Long id);

    /**
     * 根据分片id查询文件信息
     *
     * @param chunkId 分片id
     * @return {@link FileInfo}
     */
    FileInfo getByChunkId(String chunkId);

    /**
     * 启用
     * 状态;1-启用,2-停用,3-分片上传,99-作废
     */
    boolean enable(Long id);

    /**
     * 停用
     * 状态;1-启用,2-停用,3-分片上传,99-作废
     */
    boolean disable(Long id);

    /**
     * 作废
     */
    boolean cancel(Long id);


}

