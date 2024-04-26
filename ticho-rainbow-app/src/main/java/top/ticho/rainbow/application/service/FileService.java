package top.ticho.rainbow.application.service;


import top.ticho.rainbow.interfaces.dto.ChunkCacheDTO;
import top.ticho.rainbow.interfaces.dto.ChunkFileDTO;
import top.ticho.rainbow.interfaces.dto.FileInfoDTO;
import top.ticho.rainbow.interfaces.dto.FileInfoReqDTO;

/**
 * 文件 服务接口
 *
 * @author zhajianjun
 * @date 2024-02-18 12:08
 */
public interface FileService {

    /**
     * 文件上传
     *
     * @param fileInfoReqDTO 文件上传信息
     * @return FileInfoDTO
     */
    FileInfoDTO upload(FileInfoReqDTO fileInfoReqDTO);


    /**
     * 根据资源id删除
     *
     * @param storageId 资源id
     */
    void delete(String storageId);

    /**
     * 根据资源id下载
     *
     * @param storageId 资源id
     */
    void download(String storageId);

    /**
     * 根据资源id获取下载链接
     *
     * @param storageId 资源id
     * @param expires   过期时间 <=7天，默认30分钟，单位：秒
     * @return 资源链接
     */
    String getUrl(String storageId, Integer expires);

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
     * @param md5 文件md5
     */
    void composeChunk(String md5);

}

