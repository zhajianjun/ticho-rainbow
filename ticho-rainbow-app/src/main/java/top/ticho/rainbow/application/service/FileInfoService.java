package top.ticho.rainbow.application.service;

import top.ticho.boot.view.core.PageResult;
import top.ticho.rainbow.interfaces.dto.ChunkCacheDTO;
import top.ticho.rainbow.interfaces.dto.ChunkFileDTO;
import top.ticho.rainbow.interfaces.dto.FileInfoDTO;
import top.ticho.rainbow.interfaces.dto.FileInfoReqDTO;
import top.ticho.rainbow.interfaces.query.FileInfoQuery;

/**
 * 文件信息 服务接口
 *
 * @author zhajianjun
 * @date 2024-04-23 17:55
 */
public interface FileInfoService {
    /**
     * 文件上传
     *
     * @param fileInfoReqDTO 文件上传信息
     * @return FileInfoDTO
     */
    FileInfoDTO upload(FileInfoReqDTO fileInfoReqDTO);

    /**
     * 修改文件信息
     *
     * @param fileInfoDTO 文件信息DTO 对象
     */
    void update(FileInfoDTO fileInfoDTO);

    /**
     * 根据资源id删除
     *
     * @param id 资源id
     */
    void delete(Long id);

    /**
     * 根据id下载
     *
     * @param id id
     */
    void downloadById(Long id);

    /**
     * 根据签名下载
     *
     * @param sign 签名
     */
    void download(String sign);

    /**
     * 根据资源id获取下载链接
     *
     * @param id      资源id
     * @param expire 过期时间 <=7天，默认30分钟，单位：秒
     * @param limit   是否限制
     * @return 资源链接
     */
    String getUrl(Long id, Integer expire, Boolean limit);

    /**
     * 分片文件上传
     *
     * @param chunkFileDTO 分片文件信息
     * @return {@link ChunkCacheDTO}
     */
    ChunkCacheDTO uploadChunk(ChunkFileDTO chunkFileDTO);

    /**
     * 分片文件合并
     *
     * @param chunkId 分片id
     */
    void composeChunk(String chunkId);

    /**
     * 分页查询文件信息列表
     *
     * @param query 查询
     * @return {@link PageResult}<{@link FileInfoDTO}>
     */
    PageResult<FileInfoDTO> page(FileInfoQuery query);

}
