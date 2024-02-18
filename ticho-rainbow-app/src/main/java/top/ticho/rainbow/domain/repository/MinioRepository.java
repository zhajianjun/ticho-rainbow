package top.ticho.rainbow.domain.repository;


import top.ticho.rainbow.interfaces.dto.BucketInfoDTO;
import top.ticho.rainbow.interfaces.dto.FileInfoDTO;
import top.ticho.rainbow.interfaces.dto.FileInfoReqDTO;

import java.util.List;

/**
 * minio repository接口
 *
 * @author zhajianjun
 * @date 2024-02-18 12:08
 */
public interface MinioRepository {

    /**
     * 文件桶是否存在
     *
     * @param bucketName 文件桶名称
     * @return true-存在 false-不存在
     */
    boolean bucketExists(String bucketName);

    /**
     * 创建文件桶
     *
     * @param bucketName 文件桶名称
     */
    void createBucket(String bucketName);

    /**
     * 删除文件桶
     *
     * @param bucketName 文件桶名称
     * @param delAllFile 是否删除所有文件
     */
    void removeBucket(String bucketName, boolean delAllFile);

    /**
     * 获取所有的文件桶
     *
     * @return List<BucketInfoDTO>
     */
    List<BucketInfoDTO> listBuckets();


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

}

