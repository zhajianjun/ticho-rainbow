package top.ticho.rainbow.domain.service;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.MultipartFile;
import top.ticho.boot.view.core.PageResult;
import top.ticho.boot.view.enums.BizErrCode;
import top.ticho.boot.view.enums.HttpErrCode;
import top.ticho.boot.view.exception.BizException;
import top.ticho.boot.view.util.Assert;
import top.ticho.boot.web.util.CloudIdUtil;
import top.ticho.boot.web.util.valid.ValidUtil;
import top.ticho.rainbow.application.service.FileInfoService;
import top.ticho.rainbow.domain.repository.FileInfoRepository;
import top.ticho.rainbow.infrastructure.config.CacheConfig;
import top.ticho.rainbow.infrastructure.core.component.CacheTemplate;
import top.ticho.rainbow.infrastructure.core.constant.CacheConst;
import top.ticho.rainbow.infrastructure.core.enums.FileErrCode;
import top.ticho.rainbow.infrastructure.core.prop.FileProperty;
import top.ticho.rainbow.infrastructure.core.util.CommonUtil;
import top.ticho.rainbow.infrastructure.entity.FileInfo;
import top.ticho.rainbow.interfaces.assembler.FileInfoAssembler;
import top.ticho.rainbow.interfaces.dto.ChunkDTO;
import top.ticho.rainbow.interfaces.dto.ChunkFileDTO;
import top.ticho.rainbow.interfaces.dto.FileInfoDTO;
import top.ticho.rainbow.interfaces.dto.FileInfoReqDTO;
import top.ticho.rainbow.interfaces.query.FileInfoQuery;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;


/**
 * 文件 服务接口实现
 *
 * @author zhajianjun
 * @date 2024-02-18 12:08
 */
@Service
@Slf4j
public class FileInfoServiceImpl implements FileInfoService {
    public static final String STORAGE_ID_NOT_BLANK = "资源id不能为空";

    @Resource
    private FileProperty fileProperty;

    @Resource
    private HttpServletResponse response;

    @Autowired
    private CacheTemplate cacheTemplate;

    @Autowired
    private FileInfoRepository fileInfoRepository;

    @Override
    public FileInfoDTO upload(FileInfoReqDTO fileInfoReqDTO) {
        String remark = fileInfoReqDTO.getRemark();
        Integer type = fileInfoReqDTO.getType();
        MultipartFile file = fileInfoReqDTO.getFile();
        // 原始文件名，logo.svg
        String originalFilename = file.getOriginalFilename();
        DataSize fileSize = fileProperty.getMaxFileSize();
        Assert.isTrue(file.getSize() <= fileSize.toBytes(), FileErrCode.FILE_SIZE_TO_LARGER, "文件大小不能超出" + fileSize.toMegabytes() + "MB");
        // 主文件名 logo.svg -> logo
        String mainName = FileNameUtil.mainName(originalFilename);
        // 后缀名 svg
        String extName = FileNameUtil.extName(originalFilename);
        // 存储文件名 logo.svg -> logo-wKpdqhmC.svg
        String fileName = mainName + StrUtil.DASHED + CommonUtil.fastShortUUID() + StrUtil.DOT + extName;
        // 相对路径
        String relativePath = Optional.ofNullable(fileInfoReqDTO.getRelativePath())
            .filter(StrUtil::isNotBlank)
            // 去除两边的斜杠
            .map(x-> StrUtil.strip(x, "/"))
            .map(s -> s + File.separator + fileName)
            .orElse(fileName);
        FileInfo fileInfo = new FileInfo();
        fileInfo.setId(CloudIdUtil.getId());
        fileInfo.setType(type);
        fileInfo.setFileName(fileName);
        fileInfo.setOriginalFilename(originalFilename);
        fileInfo.setPath(relativePath);
        fileInfo.setSize(file.getSize());
        fileInfo.setExt(extName);
        fileInfo.setContentType(file.getContentType());
        fileInfo.setRemark(remark);
        fileInfo.setStatus(1);
        // 文件存储
        String absolutePath = getAbsolutePath(fileInfo);
        try {
            FileUtil.writeBytes(file.getBytes(), absolutePath);
        } catch (IOException e) {
            throw new BizException(HttpErrCode.FAIL, "文件上传失败");
        }
        fileInfoRepository.save(fileInfo);
        return FileInfoAssembler.INSTANCE.entityToDto(fileInfo);
    }

    @Override
    public void update(FileInfoDTO fileInfoDTO) {
        FileInfo fileInfo = FileInfoAssembler.INSTANCE.dtoToEntity(fileInfoDTO);
        Assert.isNotNull(fileInfo, FileErrCode.FILE_NOT_EXIST, "文件不存在");
        Assert.isTrue(fileInfoRepository.updateById(fileInfo), BizErrCode.FAIL, "修改失败");
    }

    @Override
    public void delete(Long id) {
        Assert.isNotNull(id, BizErrCode.PARAM_ERROR, STORAGE_ID_NOT_BLANK);
        FileInfo fileInfo = fileInfoRepository.getById(id);
        Assert.isNotNull(fileInfo, FileErrCode.FILE_NOT_EXIST, "文件不存在");
        String absolutePath = getAbsolutePath(fileInfo);
        File file = new File(absolutePath);
        if (FileUtil.exist(file)) {
            FileUtil.del(file);
        }
        fileInfoRepository.removeById(id);
    }


    @Override
    public void download(Long id) {
        // @formatter:off
        Assert.isNotNull(id, BizErrCode.PARAM_ERROR, STORAGE_ID_NOT_BLANK);
        FileInfo fileInfo = fileInfoRepository.getById(id);
        Assert.isNotNull(fileInfo, FileErrCode.FILE_NOT_EXIST, "文件不存在");
        String absolutePath = getAbsolutePath(fileInfo);
        File file = new File(absolutePath);
        Assert.isTrue(FileUtil.exist(file), FileErrCode.FILE_NOT_EXIST, "文件不存在");
        try (OutputStream outputStream = response.getOutputStream()) {
            response.setHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + URLUtil.encodeAll(fileInfo.getOriginalFilename()));
            response.setContentType(fileInfo.getContentType());
            response.setHeader(HttpHeaders.PRAGMA, "no-cache");
            response.setHeader(HttpHeaders.CACHE_CONTROL, "no-cache");
            response.setHeader(HttpHeaders.CONTENT_LENGTH, fileInfo.getSize() + "");
            response.setDateHeader(HttpHeaders.EXPIRES, 0);
            IoUtil.write(outputStream, true, FileUtil.readBytes(file));
        } catch (Exception e) {
            log.error("文件下载失败，{}", e.getMessage(), e);
            throw new BizException(FileErrCode.DOWNLOAD_ERROR);
        }
        // @formatter:on
    }

    @Override
    public String getUrl(Long id, Integer expires) {
        Assert.isNotNull(id, BizErrCode.PARAM_ERROR, STORAGE_ID_NOT_BLANK);
        if (expires != null) {
            Assert.isTrue(expires <= TimeUnit.DAYS.toSeconds(7), BizErrCode.PARAM_ERROR, "过期时间最长为7天");
        }
        return null;
    }

    @Override
    public ChunkDTO uploadChunk(ChunkFileDTO chunkFileDTO) {
        ValidUtil.valid(chunkFileDTO);
        String chunkId = chunkFileDTO.getChunkId();
        MultipartFile file = chunkFileDTO.getFile();
        DataSize fileSize = fileProperty.getMaxFileSize();
        Assert.isTrue(file.getSize() <= fileSize.toBytes(), FileErrCode.FILE_SIZE_TO_LARGER, "文件大小不能超出" + fileSize.toMegabytes() + "MB");
        // 相对路径
        Optional<String> relativePathOpt = Optional.ofNullable(chunkFileDTO.getRelativePath())
            .filter(StrUtil::isNotBlank)
            // 去除两边的斜杠
            .map(x-> StrUtil.strip(x, "/"));
        Integer index = chunkFileDTO.getIndex();
        ChunkDTO chunkDTO;
        boolean isFirst;
        // 加锁的意义是，防止并发上传同一个文件，导致md5缓存被覆盖
        synchronized (chunkId) {
            chunkDTO = cacheTemplate.get(CacheConst.UPLOAD_CHUNK, chunkId, ChunkDTO.class);
            isFirst = Objects.isNull(chunkDTO);
            if (isFirst) {
                long size = cacheTemplate.size(CacheConst.UPLOAD_CHUNK);
                Assert.isTrue(size + 1 > CacheConfig.CacheEnum.UPLOAD_CHUNK.getMaxSize(), BizErrCode.PARAM_ERROR, "分片文件上传数量超过限制");
                FileInfo dbFileInfo = fileInfoRepository.getByChunkId(chunkId);
                chunkDTO = new ChunkDTO();
                if (dbFileInfo != null) {
                    isFirst = false;
                    // TODO
                    String chunkDirPath = "";
                    chunkDTO.setChunkId(chunkId);
                    chunkDTO.setMd5(chunkFileDTO.getMd5());
                    chunkDTO.setId(dbFileInfo.getId());
                    // chunkDTO.setChunkCount(dbFileInfo.get);
                    chunkDTO.setFileName(dbFileInfo.getFileName());
                    chunkDTO.setOriginalFilename(dbFileInfo.getOriginalFilename());
                    chunkDTO.setRelativeFullPath(dbFileInfo.getPath());
                    chunkDTO.setChunkDirPath(chunkDirPath);
                    chunkDTO.setExtName(dbFileInfo.getExt());
                    chunkDTO.setIndexs(new ConcurrentSkipListSet<>());
                    ConcurrentSkipListSet<Integer> indexs = chunkDTO.getIndexs();
                    String absolutePath = getAbsolutePath(chunkDTO.getType(), chunkDTO.getChunkDirPath());
                    File chunkDirFile = new File(absolutePath);
                    String[] list = chunkDirFile.list();
                    if (ArrayUtil.isNotEmpty(list)) {
                        for (String s : list) {
                            if (StrUtil.isNumeric(s)) {
                                int i = Integer.parseInt(s);
                                indexs.add(i);
                            }
                        }
                    }

                }
                cacheTemplate.put(CacheConst.UPLOAD_CHUNK, chunkId, chunkDTO);
            }
        }
        // 原文件名 logo.svg
        String originalFilename = chunkFileDTO.getFileName();
        if (isFirst) {
            Long id = CloudIdUtil.getId();
            // 后缀 svg
            String extName = FileNameUtil.extName(originalFilename);
            // 原主文件名 logo
            String originalMainName = FileNameUtil.mainName(originalFilename);
            // 主文件名 logo-wKpdqhmC
            String mainName = originalMainName + StrUtil.DASHED + CommonUtil.fastShortUUID();
            // 文件名 logo-wKpdqhmC.svg
            String fileName = mainName + StrUtil.DOT + extName;
            // 分片文件夹路径
            String chunkDirPath;
            // 相对路径
            String relativeFullPath;
            if (relativePathOpt.isPresent()) {
                String relativePath = relativePathOpt.get() + File.separator;
                chunkDirPath = relativePath + mainName + File.separator;
                relativeFullPath = relativePath + fileName;
            } else {
                // 分片文件夹路径
                chunkDirPath = mainName + File.separator;
                relativeFullPath = fileName;
            }
            chunkDTO.setChunkId(chunkId);
            chunkDTO.setMd5(chunkFileDTO.getMd5());
            chunkDTO.setId(id);
            chunkDTO.setChunkCount(chunkFileDTO.getChunkCount());
            chunkDTO.setFileName(fileName);
            chunkDTO.setOriginalFilename(originalFilename);
            chunkDTO.setRelativeFullPath(relativeFullPath);
            chunkDTO.setChunkDirPath(chunkDirPath);
            chunkDTO.setExtName(extName);
            chunkDTO.setIndexs(new ConcurrentSkipListSet<>());
            FileInfo fileInfo = new FileInfo();
            fileInfo.setId(id);
            fileInfo.setMd5(chunkFileDTO.getMd5());
            fileInfo.setChunkId(chunkId);
            fileInfo.setType(chunkFileDTO.getType());
            fileInfo.setFileName(fileName);
            fileInfo.setOriginalFilename(originalFilename);
            fileInfo.setPath(relativeFullPath);
            fileInfo.setExt(extName);
            fileInfo.setContentType(FileUtil.getMimeType(originalFilename));
            fileInfo.setStatus(3);
            fileInfoRepository.save(fileInfo);
        }
        ConcurrentSkipListSet<Integer> indexs = chunkDTO.getIndexs();
        AtomicReference<LocalDateTime> recentUpdateTime = chunkDTO.getRecentUpdateTime();
        recentUpdateTime.set(LocalDateTime.now());
        Assert.isTrue(index + 1 <= chunkFileDTO.getChunkCount(), "索引超出分片数量大小");
        Assert.isTrue(!indexs.contains(index), "分片文件已上传");
        String chunkFilePath = getAbsolutePath(chunkDTO.getType(), chunkDTO.getChunkDirPath()) + index;
        try {
            FileUtil.writeBytes(file.getBytes(), chunkFilePath);
        } catch (IOException e) {
            throw new BizException(HttpErrCode.FAIL, "文件上传失败");
        }
        log.info("{}分片文件{}，分片id={}，index={}上传成功", originalFilename, file.getOriginalFilename(), chunkId, index);
        indexs.add(index);
        // 分片上传数量
        int chunkUploadCount = Long.valueOf(indexs.stream().distinct().count()).intValue();
        // 分片上传是否完成
        boolean complete = Objects.equals(chunkUploadCount, chunkDTO.getChunkCount());
        chunkDTO.setComplete(complete);
        return chunkDTO;
    }

    @Override
    public void composeChunk(String chunkId) {
        // @formatter:off
        Assert.isNotBlank(chunkId, "分片id不能为空");
        ChunkDTO chunkDTO = cacheTemplate.get(CacheConst.UPLOAD_CHUNK, chunkId, ChunkDTO.class);
        Assert.isNotNull(chunkDTO, "分片文件不存在");
        ConcurrentSkipListSet<Integer> indexs = Optional.ofNullable(chunkDTO.getIndexs()).orElseGet(ConcurrentSkipListSet::new);
        // 分片上传是否完成
        boolean complete = Boolean.TRUE.equals(chunkDTO.getComplete());
        Assert.isTrue(complete, "分片文件未全部上传");
        String chunkFileDirPath = getAbsolutePath(chunkDTO.getType(), chunkDTO.getChunkDirPath());
        String filePath = getAbsolutePath(chunkDTO.getType(), chunkDTO.getRelativeFullPath());
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
            long fileSize = mergedFile.length();
            mergedFile.close();
            FileInfo fileInfo = new FileInfo();
            fileInfo.setId(chunkDTO.getId());
            fileInfo.setSize(fileSize);
            fileInfo.setStatus(1);
            fileInfoRepository.updateById(fileInfo);
        } catch (IOException e) {
            throw new BizException(HttpErrCode.FAIL, "文件合并失败");
        } finally{
            // 清除缓存
            cacheTemplate.evict(CacheConst.UPLOAD_CHUNK, chunkId);
            String tmpPath = fileProperty.getTmpPath() + File.separator + chunkDTO.getChunkDirPath();
            File source = new File(chunkFileDirPath);
            if (source.exists()) {
                File target = new File(tmpPath);
                FileUtil.mkdir(target);
                FileUtil.moveContent(source, target, true);
                FileUtil.del(source);
            }
        }
        // @formatter:on
    }

    @Override
    public PageResult<FileInfoDTO> page(FileInfoQuery query) {
        query.checkPage();
        Page<FileInfo> page = PageHelper.startPage(query.getPageNum(), query.getPageSize());
        fileInfoRepository.list(query);
        List<FileInfoDTO> fileInfoDTOs = page.getResult()
            .stream()
            .map(FileInfoAssembler.INSTANCE::entityToDto)
            .collect(Collectors.toList());
        return new PageResult<>(page.getPageNum(), page.getPageSize(), page.getTotal(), fileInfoDTOs);
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

    /**
     * 获取绝对路径
     */
    public String getAbsolutePath(FileInfo fileInfo) {
        return getAbsolutePath(fileInfo.getType(), fileInfo.getPath());
    }

}
