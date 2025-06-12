package top.ticho.rainbow.application.executor;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.MultipartFile;
import top.ticho.rainbow.application.assembler.FileInfoAssembler;
import top.ticho.rainbow.domain.entity.FileInfo;
import top.ticho.rainbow.domain.repository.FileInfoRepository;
import top.ticho.rainbow.infrastructure.common.constant.CacheConst;
import top.ticho.rainbow.infrastructure.common.dto.FileCacheDTO;
import top.ticho.rainbow.infrastructure.common.enums.FileErrorCode;
import top.ticho.rainbow.infrastructure.common.enums.FileInfoStatus;
import top.ticho.rainbow.infrastructure.common.prop.FileProperty;
import top.ticho.rainbow.infrastructure.common.util.CommonUtil;
import top.ticho.rainbow.interfaces.dto.command.FileUploadCommand;
import top.ticho.rainbow.interfaces.dto.response.FileInfoDTO;
import top.ticho.starter.cache.component.TiCacheTemplate;
import top.ticho.starter.view.enums.TiBizErrorCode;
import top.ticho.starter.view.enums.TiHttpErrorCode;
import top.ticho.starter.view.exception.TiBizException;
import top.ticho.starter.view.util.TiAssert;
import top.ticho.starter.web.util.TiIdUtil;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @author zhajianjun
 * @date 2025-06-01 12:44
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class FileInfoExecutor {
    private final FileProperty fileProperty;
    private final FileInfoAssembler fileInfoAssembler;
    private final FileInfoRepository fileInfoRepository;
    private final TiCacheTemplate tiCacheTemplate;


    public FileInfoDTO upload(FileUploadCommand fileUploadCommand) {
        String remark = fileUploadCommand.getRemark();
        Integer type = fileUploadCommand.getType();
        MultipartFile file = fileUploadCommand.getFile();
        // 原始文件名，logo.svg
        String originalFileName = file.getOriginalFilename();
        DataSize fileSize = fileProperty.getMaxFileSize();
        TiAssert.isTrue(file.getSize() <= fileSize.toBytes(), FileErrorCode.FILE_SIZE_TO_LARGER, "文件大小不能超出" + fileSize.toMegabytes() + "MB");
        // 主文件名 logo.svg -> logo
        String mainName = FileNameUtil.mainName(originalFileName);
        // 后缀名 svg
        String extName = FileNameUtil.extName(originalFileName);
        // 存储文件名 logo.svg -> logo-wKpdqhmC.svg
        String fileName = mainName + StrUtil.DASHED + CommonUtil.fastShortUUID() + StrUtil.DOT + extName;
        // 相对路径
        String relativePath = Optional.ofNullable(fileUploadCommand.getRelativePath())
            .filter(StrUtil::isNotBlank)
            // 去除两边的斜杠
            .map(x -> StrUtil.strip(x, "/"))
            .map(s -> s + "/" + fileName)
            .orElse(fileName);
        FileInfo fileInfo = FileInfo.builder()
            .id(TiIdUtil.getId())
            .type(type)
            .fileName(fileName)
            .originalFileName(originalFileName)
            .path(relativePath)
            .size(file.getSize())
            .ext(extName)
            .contentType(file.getContentType())
            .remark(remark)
            .status(FileInfoStatus.ENABLE.code())
            .build();
        // 文件存储
        String absolutePath = getAbsolutePath(fileInfo);
        try {
            FileUtil.writeBytes(file.getBytes(), absolutePath);
        } catch (IOException e) {
            throw new TiBizException(TiHttpErrorCode.FAIL, "文件上传失败");
        }
        fileInfoRepository.save(fileInfo);
        return fileInfoAssembler.toDTO(fileInfo);
    }

    public String presigned(Long id, Long expire, Boolean limit) {
        long seconds = TimeUnit.DAYS.toSeconds(7);
        if (expire != null) {
            TiAssert.isTrue(expire <= seconds, TiBizErrorCode.PARAM_ERROR, "过期时间最长为7天");
        } else {
            expire = seconds;
        }
        FileInfo dbFileInfo = fileInfoRepository.find(id);
        return presigned(dbFileInfo, expire, limit);
    }

    public String presigned(FileInfo fileInfo, Long expire, Boolean limit) {
        if (fileInfo == null) {
            return null;
        }
        if (!fileInfo.isEnable()) {
            return null;
        }
        String absolutePath = getAbsolutePath(fileInfo.getType(), fileInfo.getPath());
        File file = new File(absolutePath);
        if (!file.exists()) {
            return null;
        }
        String domain = StrUtil.removeSuffix(fileProperty.getDomain(), "/");
        if (Objects.equals(fileInfo.getType(), 1)) {
            String mvcResourcePath = StrUtil.removeSuffix(fileProperty.getMvcResourcePath(), "/**");
            mvcResourcePath = StrUtil.removePrefix(mvcResourcePath, "/");
            return domain + "/" + mvcResourcePath + "/" + fileInfo.getPath();
        }
        String sign = CommonUtil.fastShortUUID();
        FileCacheDTO fileCacheDTO = new FileCacheDTO();
        fileCacheDTO.setSign(sign);
        fileCacheDTO.setFileInfo(fileInfo);
        fileCacheDTO.setExpire(expire);
        fileCacheDTO.setLimit(limit);
        tiCacheTemplate.put(CacheConst.FILE_URL_CACHE, sign, fileCacheDTO);
        return domain + "/file/download?sign=" + sign;
    }

    /**
     * 获取绝对路径
     */
    public String getAbsolutePath(FileInfo fileInfo) {
        return getAbsolutePath(fileInfo.getType(), fileInfo.getPath());
    }

    /**
     * 获取绝对路径
     *
     * @param type 存储类型
     * @param path 文件相对路径
     * @return {@link String}
     */
    public String getAbsolutePath(Integer type, String path) {
        String prefixPath;
        // 文件存储
        if (Objects.equals(type, 1)) {
            prefixPath = fileProperty.getPublicPath();
        } else {
            prefixPath = fileProperty.getPrivatePath();
        }
        return prefixPath + path;
    }

}
