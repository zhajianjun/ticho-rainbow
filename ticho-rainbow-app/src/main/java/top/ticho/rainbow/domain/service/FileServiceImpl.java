package top.ticho.rainbow.domain.service;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.MultipartFile;
import top.ticho.boot.view.enums.BizErrCode;
import top.ticho.boot.view.enums.HttpErrCode;
import top.ticho.boot.view.exception.BizException;
import top.ticho.boot.view.util.Assert;
import top.ticho.boot.web.util.CloudIdUtil;
import top.ticho.boot.web.util.valid.ValidUtil;
import top.ticho.rainbow.application.service.FileService;
import top.ticho.rainbow.infrastructure.core.component.CacheTemplate;
import top.ticho.rainbow.infrastructure.core.constant.CacheConst;
import top.ticho.rainbow.infrastructure.core.enums.MioErrCode;
import top.ticho.rainbow.infrastructure.core.prop.FileProperty;
import top.ticho.rainbow.infrastructure.core.util.CommonUtil;
import top.ticho.rainbow.interfaces.dto.ChunkDTO;
import top.ticho.rainbow.interfaces.dto.ChunkFileDTO;
import top.ticho.rainbow.interfaces.dto.FileInfoDTO;
import top.ticho.rainbow.interfaces.dto.FileInfoReqDTO;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.TimeUnit;


/**
 * 文件 服务接口实现
 *
 * @author zhajianjun
 * @date 2024-02-18 12:08
 */
@Service
@Slf4j
public class FileServiceImpl implements FileService {
    public static final String STORAGE_ID_NOT_BLANK = "资源id不能为空";

    @Resource
    private FileProperty fileProperty;

    @Resource
    private HttpServletResponse response;

    @Autowired
    private CacheTemplate cacheTemplate;

    private final Map<Long, Map<String, Object>> fileMap = new LinkedHashMap<>();


    @Override
    public FileInfoDTO upload(FileInfoReqDTO fileInfoReqDTO) {
        // @formatter:of
        String remark = fileInfoReqDTO.getRemark();
        Integer type = fileInfoReqDTO.getType();
        MultipartFile file = fileInfoReqDTO.getFile();
        String fileName = file.getOriginalFilename();
        DataSize fileSize = fileProperty.getMaxFileSize();
        Assert.isTrue(file.getSize() <= fileSize.toBytes(), MioErrCode.FILE_SIZE_TO_LARGER, "文件大小不能超出" + fileSize.toMegabytes() + "MB");
        // 后缀名 .png
        String extName = FileNameUtil.extName(fileName);
        // 主文件名 test.png -> test
        String mainName = FileNameUtil.mainName(fileName);
        Long id = CloudIdUtil.getId();
        String prefixPath;
        String realFileName = mainName + StrUtil.DASHED + CommonUtil.fastShortUUID() + StrUtil.DOT + extName;
        String path = realFileName;
        Map<String, Object> fileInfo = new HashMap<>();
        // 文件存储
        if (Objects.equals(type, 1)) {
            prefixPath = fileProperty.getPublicPath();
        } else {
            prefixPath = fileProperty.getPrivatePath();
        }
        String absolutePath = prefixPath + realFileName;
        try {
            FileUtil.writeBytes(file.getBytes(), absolutePath);
        } catch (IOException e) {
            throw new BizException(HttpErrCode.FAIL, "文件上传失败");
        }
        fileInfo.put("id", id);
        fileInfo.put("type", type);
        fileInfo.put("fileName", fileName);
        fileInfo.put("path", path);
        fileInfo.put("size", file.getSize());
        fileInfo.put("ext", extName);
        fileInfo.put("contentType", file.getContentType());
        fileInfo.put("remark", remark);
        fileMap.put(id, fileInfo);
        FileInfoDTO fileInfoDTO = new FileInfoDTO();
        fileInfoDTO.setId(id);
        fileInfoDTO.setFileName(fileName);
        fileInfoDTO.setType(type);
        fileInfoDTO.setContentType(file.getContentType());
        fileInfoDTO.setSize(file.getSize());
        fileInfoDTO.setRemark(remark);
        fileInfoDTO.setPath(realFileName);
        log.info("文件上传成功，{}", fileInfo);
        return fileInfoDTO;
        // @formatter:on
    }

    @Override
    public void delete(String storageId) {
        Assert.isNotBlank(storageId, BizErrCode.PARAM_ERROR, STORAGE_ID_NOT_BLANK);
        long id = NumberUtil.parseLong(storageId);
        Map<String, Object> map = fileMap.get(id);
        FileInfoDTO fileInfoDto = getFileInfoDto(map);
        String absolutePath = getAbsolutePath(fileInfoDto);
        FileUtil.del(absolutePath);
        fileMap.remove(id);
    }


    @Override
    public void download(String storageId) {
        // @formatter:off
        Assert.isNotBlank(storageId, BizErrCode.PARAM_ERROR, STORAGE_ID_NOT_BLANK);
        Map<String, Object> in = fileMap.get(NumberUtil.parseLong(storageId));
        FileInfoDTO fileInfoDto = getFileInfoDto(in);
        try (OutputStream outputStream = response.getOutputStream()) {
            response.setHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + URLUtil.encodeAll(fileInfoDto.getFileName()));
            response.setContentType(fileInfoDto.getContentType());
            response.setHeader(HttpHeaders.PRAGMA, "no-cache");
            response.setHeader(HttpHeaders.CACHE_CONTROL, "no-cache");
            response.setHeader(HttpHeaders.CONTENT_LENGTH, fileInfoDto.getSize() + "");
            response.setDateHeader(HttpHeaders.EXPIRES, 0);
            String absolutePath = getAbsolutePath(fileInfoDto);
            IoUtil.write(outputStream, true, FileUtil.readBytes(absolutePath));
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
        return null;
    }

    @Override
    public ChunkDTO uploadChunk(ChunkFileDTO chunkFileDTO) {
        ValidUtil.valid(chunkFileDTO);
        String md5 = chunkFileDTO.getMd5();
        MultipartFile file = chunkFileDTO.getFile();
        Integer index = chunkFileDTO.getIndex();
        ChunkDTO chunkDTO = cacheTemplate.get(CacheConst.UPLOAD_CHUNK, md5, ChunkDTO.class);
        String fileName = chunkFileDTO.getFileName();
        if (Objects.isNull(chunkDTO)) {
            String extName = FileNameUtil.extName(fileName);
            String mainName = FileNameUtil.mainName(fileName);
            String realFileName = mainName + StrUtil.DASHED + CommonUtil.fastShortUUID() + StrUtil.DOT + extName;
            chunkDTO = new ChunkDTO();
            chunkDTO.setMd5(md5);
            chunkDTO.setChunkCount(chunkFileDTO.getChunkCount());
            chunkDTO.setFileName(fileName);
            chunkDTO.setPath(realFileName);
            chunkDTO.setExtName(extName);
            chunkDTO.setIndexs(new ConcurrentSkipListSet<>());
        }
        ConcurrentSkipListSet<Integer> indexs = chunkDTO.getIndexs();
        Assert.isTrue(index + 1 <= chunkFileDTO.getChunkCount(), "索引超出分片数量大小");
        Assert.isTrue(!indexs.contains(index), "分片文件已上传");
        String rootPath = fileProperty.getPrivatePath();
        String chunkFilePath = FileUtil.getPrefix(chunkDTO.getPath()) + File.separator + index;
        String absolutePath = rootPath + chunkFilePath;
        try {
            FileUtil.writeBytes(file.getBytes(), absolutePath);
        } catch (IOException e) {
            throw new BizException(HttpErrCode.FAIL, "文件上传失败");
        }
        log.info("{}分片文件{}，md5={}，index={}上传成功", fileName, file.getOriginalFilename(), md5, index);
        indexs.add(index);
        // 分片上传数量
        int chunkUploadCount = Long.valueOf(indexs.stream().distinct().count()).intValue();
        // 分片上传是否完成
        boolean complete = Objects.equals(chunkUploadCount, chunkDTO.getChunkCount());
        chunkDTO.setComplete(complete);
        cacheTemplate.put(CacheConst.UPLOAD_CHUNK, md5, chunkDTO);
        return chunkDTO;
    }

    @Override
    public void composeChunk(String md5) {
        // @formatter:off
        Assert.isNotBlank(md5, "md5不能为空");
        ChunkDTO chunkDTO = cacheTemplate.get(CacheConst.UPLOAD_CHUNK, md5, ChunkDTO.class);
        Assert.isNotNull(chunkDTO, "分片文件不存在");
        ConcurrentSkipListSet<Integer> indexs = Optional.ofNullable(chunkDTO.getIndexs()).orElseGet(ConcurrentSkipListSet::new);
        // 分片上传是否完成
        boolean complete = Boolean.TRUE.equals(chunkDTO.getComplete());
        Assert.isTrue(complete, "分片文件未全部上传");
        String rootPath = fileProperty.getPrivatePath();
        String chunkFileDirPath = rootPath + FileUtil.getPrefix(chunkDTO.getPath()) + File.separator;
        String filePath = rootPath + chunkDTO.getPath();
        try {
            RandomAccessFile mergedFile = new RandomAccessFile(filePath, "rw");
            // 缓冲区大小
            byte[] bytes = new byte[1024];
            for (int i = 0; i < indexs.size(); i++) {
                RandomAccessFile partFile = new RandomAccessFile(chunkFileDirPath + i, "r");
                int len;
                while ((len = partFile.read(bytes)) != -1) {
                    mergedFile.write(bytes, 0, len);
                }
                partFile.close();
            }
            mergedFile.close();
        } catch (IOException e) {
            throw new BizException(HttpErrCode.FAIL, "文件合并失败");
        } finally{
            // 清除缓存
            cacheTemplate.evict(CacheConst.UPLOAD_CHUNK, md5);
            FileUtil.moveContent(new File(chunkFileDirPath), new File(fileProperty.getTmpPath()), true);
        }
        // @formatter:on
    }

    /**
     * 从header中获取用户上传的自定义信息
     *
     * @param map headers
     * @return 自定义headers信息
     */
    private FileInfoDTO getFileInfoDto(Map<String, Object> map) {
        if (Objects.isNull(map)) {
            return null;
        }
        FileInfoDTO fileInfoDTO = new FileInfoDTO();
        fileInfoDTO.setFileName((String) map.get("fileName"));
        fileInfoDTO.setType(NumberUtil.parseInt(map.get("type").toString(), 2));
        fileInfoDTO.setPath((String) map.get("path"));
        fileInfoDTO.setRemark((String) map.get("remark"));
        long size = Optional.ofNullable(map.get("size")).map(Object::toString).map(Long::valueOf).orElse(0L);
        fileInfoDTO.setSize(size);
        fileInfoDTO.setContentType((String) map.get("contentType"));
        return fileInfoDTO;
    }

    public String getAbsolutePath(FileInfoDTO fileInfoDto) {
        Integer type = fileInfoDto.getType();
        String path = fileInfoDto.getPath();
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
