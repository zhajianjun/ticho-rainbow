package top.ticho.rainbow.infrastructure.repository;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import io.minio.GetObjectResponse;
import io.minio.Result;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Headers;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Repository;
import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.MultipartFile;
import top.ticho.boot.minio.component.MinioTemplate;
import top.ticho.boot.minio.prop.MinioProperty;
import top.ticho.boot.view.enums.BizErrCode;
import top.ticho.boot.view.exception.BizException;
import top.ticho.boot.view.util.Assert;
import top.ticho.boot.web.util.CloudIdUtil;
import top.ticho.rainbow.domain.repository.MinioRepository;
import top.ticho.rainbow.infrastructure.core.enums.MioErrCode;
import top.ticho.rainbow.interfaces.dto.BucketInfoDTO;
import top.ticho.rainbow.interfaces.dto.FileInfoDTO;
import top.ticho.rainbow.interfaces.dto.FileInfoReqDTO;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * minio repository实现
 *
 * @author zhajianjun
 * @date 2024-02-18 12:08
 */
@Repository
@Slf4j
public class MinioRepositoryImpl implements MinioRepository {
    public static final String X_AMZ_META_PREFIX = "x-amz-meta-";
    public static final String FILENAME_KEY = "fileName";
    public static final String REMARK_KEY = "remark";
    public static final String STORAGE_ID_NOT_BLANK = "资源id不能为空";

    @Resource
    private MinioProperty minioProperty;

    @Resource
    private MinioTemplate minioTemplate;

    @Resource
    private HttpServletResponse response;

    @PostConstruct
    public void init() {
        String defaultBucket = minioProperty.getDefaultBucket();
        if (StrUtil.isBlank(defaultBucket)) {
            return;
        }
        boolean bucketExists = bucketExists(defaultBucket);
        if (!bucketExists) {
            minioTemplate.createBucket(defaultBucket);
        }
    }

    @Override
    public boolean bucketExists(String bucketName) {
        if (StrUtil.isBlank(bucketName)) {
            return false;
        }
        return minioTemplate.bucketExists(bucketName);
    }

    @Override
    public void createBucket(String bucketName) {
        Assert.isTrue(!minioTemplate.bucketExists(bucketName), MioErrCode.BUCKET_IS_ALREAD_EXISITS);
        minioTemplate.createBucket(bucketName);
    }

    @Override
    public void removeBucket(String bucketName, boolean delAllFile) {
        Assert.isTrue(bucketExists(bucketName), MioErrCode.BUCKET_IS_ALREAD_EXISITS);

        // 获取当前桶内所有文件
        List<Result<Item>> allObjects = minioTemplate.listObjects(bucketName, "", true);
        if (!delAllFile) {
            Assert.isTrue(allObjects.isEmpty(), MioErrCode.BUCKET_IS_NOT_EMPTY);
        }
        try {
            for (Result<Item> itemResult : allObjects) {
                String objectName = itemResult.get().objectName();
                minioTemplate.removeObject(bucketName, objectName);
            }
        } catch (Exception e) {
            log.error("删除文件异常，{}", e.getMessage(), e);
            throw new BizException(MioErrCode.DELETE_OBJECT_ERROR);
        }
        minioTemplate.removeBucket(bucketName);
    }

    @Override
    public List<BucketInfoDTO> listBuckets() {
        return minioTemplate.listBuckets().stream().map(this::getBucketInfoDTO).collect(Collectors.toList());
    }

    private BucketInfoDTO getBucketInfoDTO(Bucket bucketName) {
        if (bucketName == null) {
            return null;
        }
        BucketInfoDTO bucketDto = new BucketInfoDTO();
        bucketDto.setBucket(bucketName.name());
        bucketDto.setCreationDate(bucketName.creationDate());
        return bucketDto;
    }

    @Override
    public FileInfoDTO upload(FileInfoReqDTO fileInfoReqDTO) {
        // @formatter:off
        String bucketName = minioProperty.getDefaultBucket();
        String fileName = fileInfoReqDTO.getFileName();
        String remark = fileInfoReqDTO.getRemark();
        MultipartFile file = fileInfoReqDTO.getFile();
        String originalFilename = file.getOriginalFilename();
        DataSize fileSize = minioProperty.getMaxFileSize();
        Assert.isTrue(file.getSize() <= fileSize.toBytes(), MioErrCode.FILE_SIZE_TO_LARGER, "文件大小不能超出" + fileSize.toMegabytes() + "MB");
        // 后缀名 .png
        String extName = StrUtil.DOT + FileNameUtil.extName(originalFilename);
        String objectName = CloudIdUtil.getId() + extName;
        if (StrUtil.isNotBlank(fileName)) {
            Assert.isTrue(fileName.endsWith(extName), "文件名与上传的文件后缀格式不统一！");
        } else {
            fileName = originalFilename;
        }
        // 构建自定义 header
        Map<String, String> userMetadata = new HashMap<>(1);
        userMetadata.put(FILENAME_KEY, fileName);

        if (StrUtil.isNotBlank(remark)) {
            userMetadata.put(REMARK_KEY, remark);
        }
        minioTemplate.putObject(bucketName, objectName, userMetadata, file);
        FileInfoDTO fileInfoDTO = new FileInfoDTO();
        fileInfoDTO.setStorageId(objectName);
        fileInfoDTO.setFileName(fileName);
        fileInfoDTO.setContentType(file.getContentType());
        fileInfoDTO.setSize(file.getSize() + "B");
        fileInfoDTO.setRemark(remark);
        fileInfoDTO.setBucket(bucketName);
        return fileInfoDTO;
        // @formatter:on
    }

    @Override
    public void delete(String storageId) {
        Assert.isNotBlank(storageId, BizErrCode.PARAM_ERROR, STORAGE_ID_NOT_BLANK);
        String bucketName = minioProperty.getDefaultBucket();
        minioTemplate.removeObject(bucketName, storageId);
    }


    @Override
    public void download(String storageId) {
        // @formatter:off
        Assert.isNotBlank(storageId, BizErrCode.PARAM_ERROR, STORAGE_ID_NOT_BLANK);
        String bucketName = minioProperty.getDefaultBucket();
        GetObjectResponse in = minioTemplate.getObject(bucketName, storageId);
        Headers headers = in.headers();
        FileInfoDTO fileInfoDto = getFileInfoDto(headers);
        try (OutputStream outputStream = response.getOutputStream()) {
            response.setHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + URLUtil.encodeAll(fileInfoDto.getFileName()));
            response.setContentType(fileInfoDto.getContentType());
            response.setHeader(HttpHeaders.PRAGMA, "no-cache");
            response.setHeader(HttpHeaders.CACHE_CONTROL, "no-cache");
            response.setHeader(HttpHeaders.CONTENT_LENGTH, headers.get(HttpHeaders.CONTENT_LENGTH));
            response.setDateHeader(HttpHeaders.EXPIRES, 0);
            IoUtil.copy(in, outputStream, 1024);
        } catch (Exception e) {
            log.error("文件下载失败，{}", e.getMessage(), e);
            throw new BizException(MioErrCode.DOWNLOAD_ERROR);
        }
        // @formatter:on
    }

    @Override
    public String getUrl(String storageId, Integer expires) {
        Assert.isNotBlank(storageId, BizErrCode.PARAM_ERROR, STORAGE_ID_NOT_BLANK);
        if (expires != null) {
            Assert.isTrue(expires <= TimeUnit.DAYS.toSeconds(7), BizErrCode.PARAM_ERROR, "过期时间最长为7天");
        }
        return minioTemplate.getObjectUrl(minioProperty.getDefaultBucket(), storageId, expires);
    }

    /**
     * 从header中获取用户上传的自定义信息
     *
     * @param headers headers
     * @return 自定义headers信息
     */
    private FileInfoDTO getFileInfoDto(Headers headers) {
        FileInfoDTO fileInfoDTO = new FileInfoDTO();
        for (String key : headers.names()) {
            if (!key.startsWith(X_AMZ_META_PREFIX)) {
                continue;
            }
            String substring = key.substring(X_AMZ_META_PREFIX.length());
            if (substring.equalsIgnoreCase(FILENAME_KEY)) {
                fileInfoDTO.setFileName(headers.get(key));
            }
            if (substring.equalsIgnoreCase(REMARK_KEY)) {
                fileInfoDTO.setRemark(headers.get(key));
            }
        }
        long size = Optional.ofNullable(headers.get(HttpHeaders.CONTENT_LENGTH)).map(Long::valueOf).orElse(0L);
        fileInfoDTO.setSize(size + "B");
        fileInfoDTO.setContentType(headers.get(HttpHeaders.CONTENT_TYPE));
        return fileInfoDTO;
    }
}
