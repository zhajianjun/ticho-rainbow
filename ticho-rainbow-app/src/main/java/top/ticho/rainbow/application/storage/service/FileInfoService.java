package top.ticho.rainbow.application.storage.service;

import top.ticho.rainbow.infrastructure.entity.FileInfo;
import top.ticho.rainbow.interfaces.dto.ChunkCacheDTO;
import top.ticho.rainbow.interfaces.dto.ChunkFileDTO;
import top.ticho.rainbow.interfaces.dto.FileInfoDTO;
import top.ticho.rainbow.interfaces.dto.FileInfoReqDTO;
import top.ticho.rainbow.interfaces.query.FileInfoQuery;
import top.ticho.starter.view.core.TiPageResult;

import java.io.IOException;

/**
 * 文件信息 服务接口
 *
 * @author zhajianjun
 * @date 2024-04-23 17:55
 */
public interface FileInfoService {

    /**
     * 文件上传
     */
    FileInfoDTO upload(FileInfoReqDTO fileInfoReqDTO);

    /**
     * 修改文件信息
     */
    void update(FileInfoDTO fileInfoDTO);

    /**
     * 启用
     */
    void enable(Long id);

    /**
     * 停用
     */
    void disable(Long id);

    /**
     * 作废
     */
    void cancel(Long id);

    /**
     * 删除
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
     * 根据id获取下载链接
     *
     * @param id     id
     * @param expire 过期时间 <=7天，默认30分钟，单位：秒
     * @param limit  是否限制
     * @return 资源链接
     */
    String getUrl(Long id, Long expire, Boolean limit);

    /**
     * 根据文件信息获取下载链接
     *
     * @param fileInfo 文件信息
     * @param expire   过期时间 <=7天，默认30分钟，单位：秒
     * @param limit    是否限制
     * @return {@link String }
     */
    String getUrl(FileInfo fileInfo, Long expire, Boolean limit);

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
    FileInfoDTO composeChunk(String chunkId);

    /**
     * 分页查询文件信息列表
     *
     * @param query 查询
     * @return {@link TiPageResult}<{@link FileInfoDTO}>
     */
    TiPageResult<FileInfoDTO> page(FileInfoQuery query);

    /**
     * 导出文件信息
     *
     * @param query 查询条件
     */
    void expExcel(FileInfoQuery query) throws IOException;

}

