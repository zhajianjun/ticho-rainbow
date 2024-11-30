package top.ticho.rainbow.domain.service;

import cn.hutool.core.date.DatePattern;
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
import top.ticho.tool.json.util.JsonUtil;
import top.ticho.boot.view.core.PageResult;
import top.ticho.boot.view.enums.BizErrCode;
import top.ticho.boot.view.enums.HttpErrCode;
import top.ticho.boot.view.exception.BizException;
import top.ticho.boot.view.util.Assert;
import top.ticho.boot.web.util.CloudIdUtil;
import top.ticho.boot.web.util.valid.ValidUtil;
import top.ticho.rainbow.application.service.FileInfoService;
import top.ticho.rainbow.domain.handle.DictHandle;
import top.ticho.rainbow.domain.repository.FileInfoRepository;
import top.ticho.rainbow.infrastructure.config.CacheConfig;
import top.ticho.rainbow.infrastructure.core.component.cache.CommonCacheTemplate;
import top.ticho.rainbow.infrastructure.core.component.cache.SpringCacheTemplate;
import top.ticho.rainbow.infrastructure.core.component.excel.ExcelHandle;
import top.ticho.rainbow.infrastructure.core.constant.CacheConst;
import top.ticho.rainbow.infrastructure.core.constant.DictConst;
import top.ticho.rainbow.infrastructure.core.enums.FileErrCode;
import top.ticho.rainbow.infrastructure.core.enums.FileInfoStatus;
import top.ticho.rainbow.infrastructure.core.prop.FileProperty;
import top.ticho.rainbow.infrastructure.core.util.CommonUtil;
import top.ticho.rainbow.infrastructure.entity.FileCache;
import top.ticho.rainbow.infrastructure.entity.FileInfo;
import top.ticho.rainbow.interfaces.assembler.FileInfoAssembler;
import top.ticho.rainbow.interfaces.dto.ChunkCacheDTO;
import top.ticho.rainbow.interfaces.dto.ChunkFileDTO;
import top.ticho.rainbow.interfaces.dto.ChunkMetadataDTO;
import top.ticho.rainbow.interfaces.dto.FileInfoDTO;
import top.ticho.rainbow.interfaces.dto.FileInfoReqDTO;
import top.ticho.rainbow.interfaces.excel.FileInfoExp;
import top.ticho.rainbow.interfaces.query.FileInfoQuery;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
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
    public static final String STORAGE_ID_NOT_BLANK = "id不能为空";

    /** 下载链接缓存 */
    private final CommonCacheTemplate<FileCache> fileUrlCacheTemplate = new CommonCacheTemplate<>(FileCache::getExpire);

    @Resource
    private HttpServletResponse response;

    @Autowired
    private SpringCacheTemplate springCacheTemplate;

    @Autowired
    private FileProperty fileProperty;

    @Autowired
    private FileInfoRepository fileInfoRepository;

    @Autowired
    private DictHandle dictHandle;

    // @formatter:off

    @Override
    public FileInfoDTO upload(FileInfoReqDTO fileInfoReqDTO) {
        String remark = fileInfoReqDTO.getRemark();
        Integer type = fileInfoReqDTO.getType();
        MultipartFile file = fileInfoReqDTO.getFile();
        // 原始文件名，logo.svg
        String originalFileName = file.getOriginalFilename();
        DataSize fileSize = fileProperty.getMaxFileSize();
        Assert.isTrue(file.getSize() <= fileSize.toBytes(), FileErrCode.FILE_SIZE_TO_LARGER, "文件大小不能超出" + fileSize.toMegabytes() + "MB");
        // 主文件名 logo.svg -> logo
        String mainName = FileNameUtil.mainName(originalFileName);
        // 后缀名 svg
        String extName = FileNameUtil.extName(originalFileName);
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
        fileInfo.setOriginalFileName(originalFileName);
        fileInfo.setPath(relativePath);
        fileInfo.setSize(file.getSize());
        fileInfo.setExt(extName);
        fileInfo.setContentType(file.getContentType());
        fileInfo.setRemark(remark);
        fileInfo.setStatus(FileInfoStatus.NORMAL.code());
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
    public void enable(Long id) {
        Assert.isNotNull(id, BizErrCode.PARAM_ERROR, STORAGE_ID_NOT_BLANK);
        boolean enable = fileInfoRepository.enable(id);
        Assert.isTrue(enable, BizErrCode.FAIL, "启用失败");
    }

    @Override
    public void disable(Long id) {
        Assert.isNotNull(id, BizErrCode.PARAM_ERROR, STORAGE_ID_NOT_BLANK);
        boolean enable = fileInfoRepository.disable(id);
        Assert.isTrue(enable, BizErrCode.FAIL, "停用失败");
    }

    @Override
    public void cancel(Long id) {
        Assert.isNotNull(id, BizErrCode.PARAM_ERROR, STORAGE_ID_NOT_BLANK);
        boolean enable = fileInfoRepository.cancel(id);
        Assert.isTrue(enable, BizErrCode.FAIL, "作废失败");
    }

    @Override
    public void delete(Long id) {
        Assert.isNotNull(id, BizErrCode.PARAM_ERROR, STORAGE_ID_NOT_BLANK);
        FileInfo dbFileInfo = fileInfoRepository.getById(id);
        Assert.isNotNull(dbFileInfo, FileErrCode.FILE_NOT_EXIST, "删除失败, 文件信息不存在");
        boolean isCancel = Objects.equals(dbFileInfo.getStatus(), FileInfoStatus.CANCE.code());
        boolean isChunk = Objects.equals(dbFileInfo.getStatus(), FileInfoStatus.CHUNK.code());
        Assert.isTrue(isCancel || isChunk, "删除失败, 分片或者作废状态文件才能删除");
        String path;
        if (Objects.equals(dbFileInfo.getStatus(), FileInfoStatus.CHUNK.code())) {
            boolean isUploadChunkIng = springCacheTemplate.contain(CacheConst.UPLOAD_CHUNK, dbFileInfo.getChunkId());
            Assert.isTrue(!isUploadChunkIng, "删除失败, 分片文件正在上传中");
            ChunkMetadataDTO metadata = JsonUtil.toJavaObject(dbFileInfo.getChunkMetadata(), ChunkMetadataDTO.class);
            path = metadata.getChunkDirPath();
        } else {
            path = dbFileInfo.getPath();
        }
        moveToTmp(dbFileInfo.getType(), path);
        fileInfoRepository.removeById(id);
    }


    @Override
    public void downloadById(Long id) {
        Assert.isNotNull(id, BizErrCode.PARAM_ERROR, STORAGE_ID_NOT_BLANK);
        FileInfo fileInfo = fileInfoRepository.getById(id);
        Assert.isNotNull(fileInfo, FileErrCode.FILE_NOT_EXIST);
        boolean normal = Objects.equals(fileInfo.getStatus(), FileInfoStatus.NORMAL.code());
        Assert.isTrue(normal, FileErrCode.FILE_STATUS_ERROR);
        download(fileInfo);
    }

    @Override
    public void download(String sign) {
        Assert.isNotBlank(sign, BizErrCode.PARAM_ERROR, "sign不能为空");
        FileCache fileCache = fileUrlCacheTemplate.get(sign);
        Assert.isNotNull(fileCache, FileErrCode.FILE_NOT_EXIST);
        FileInfo fileInfo = fileCache.getFileInfo();
        if (Boolean.TRUE.equals(fileCache.getLimit())) {
            fileCache.setLimited(true);
            fileUrlCacheTemplate.put(sign, fileCache);
        }
        download(fileInfo);
    }

    private void download(FileInfo fileInfo) {
        File file = getFile(fileInfo);
        try {
            response.setHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + URLUtil.encodeAll(fileInfo.getOriginalFileName()));
            response.setContentType(fileInfo.getContentType());
            response.setHeader(HttpHeaders.PRAGMA, "no-cache");
            response.setHeader(HttpHeaders.CACHE_CONTROL, "no-cache");
            response.setHeader(HttpHeaders.CONTENT_LENGTH, fileInfo.getSize() + "");
            response.setDateHeader(HttpHeaders.EXPIRES, 0);
            IoUtil.copy(Files.newInputStream(file.toPath()), response.getOutputStream(), 1024);
        } catch (Exception e) {
            log.error("文件下载失败，{}", e.getMessage(), e);
            throw new BizException(FileErrCode.DOWNLOAD_ERROR);
        }
    }

    private File getFile(FileInfo fileInfo) {
        String absolutePath = getAbsolutePath(fileInfo.getType(), fileInfo.getPath());
        File file = new File(absolutePath);
        Assert.isTrue(FileUtil.exist(file), FileErrCode.FILE_NOT_EXIST);
        return file;
    }

    @Override
    public String getUrl(Long id, Long expire, Boolean limit) {
        Assert.isNotNull(id, BizErrCode.PARAM_ERROR, STORAGE_ID_NOT_BLANK);
        long seconds = TimeUnit.DAYS.toSeconds(7);
        if (expire != null) {
            Assert.isTrue(expire <= seconds, BizErrCode.PARAM_ERROR, "过期时间最长为7天");
        } else {
            expire = seconds;
        }
        FileInfo dbFileInfo = fileInfoRepository.getById(id);
        return getUrl(dbFileInfo, expire, limit);
    }

    public String getUrl(FileInfo fileInfo, Long expire, Boolean limit) {
        Assert.isNotNull(fileInfo, FileErrCode.FILE_NOT_EXIST);
        boolean normal = Objects.equals(fileInfo.getStatus(), FileInfoStatus.NORMAL.code());
        if (!normal) {
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
        FileCache fileCache = new FileCache();
        fileCache.setSign(sign);
        fileCache.setFileInfo(fileInfo);
        fileCache.setExpire(expire);
        fileCache.setLimit(limit);
        fileUrlCacheTemplate.put(sign, fileCache);
        return domain + "/file/download?sign=" + sign;
    }

    @Override
    public ChunkCacheDTO uploadChunk(ChunkFileDTO chunkFileDTO) {
        ValidUtil.valid(chunkFileDTO);
        MultipartFile chunkfile = chunkFileDTO.getChunkfile();
        DataSize maxFileSize = fileProperty.getMaxPartSize();
        Integer index = chunkFileDTO.getIndex();
        Assert.isTrue(chunkfile.getSize() <= maxFileSize.toBytes(), FileErrCode.FILE_SIZE_TO_LARGER, "分片文件大小不能超出" + maxFileSize.toMegabytes() + "MB");
        // 相对路径处理
        String relativePath = Optional.ofNullable(chunkFileDTO.getRelativePath())
            .filter(StrUtil::isNotBlank)
            // 去除两边的斜杠
            .map(x-> StrUtil.strip(x, "/"))
            .orElse(null);
        chunkFileDTO.setRelativePath(relativePath);
        ChunkCacheDTO chunkCacheDTO = getChunkSafe(chunkFileDTO);
        ConcurrentSkipListSet<Integer> indexs = chunkCacheDTO.getIndexs();
        AtomicInteger uploadedChunkCount = chunkCacheDTO.getUploadedChunkCount();
        Assert.isTrue(index + 1 <= chunkFileDTO.getChunkCount(), "索引超出分片数量大小");
        if (indexs.contains(index)) {
            log.info("分片文件已上传");
            return chunkCacheDTO;
        }
        String chunkFilePath = getAbsolutePath(chunkCacheDTO.getType(), chunkCacheDTO.getChunkDirPath()) + index;
        try {
            File chunkFile = new File(chunkFilePath);
            if (!chunkFile.exists()) {
               FileUtil.writeBytes(chunkfile.getBytes(), chunkFile);
            }
        } catch (IOException e) {
            throw new BizException(HttpErrCode.FAIL, "文件上传失败");
        }
        log.info("{}分片文件{}，分片id={}，index={}上传成功", chunkFileDTO.getFileName(), chunkfile.getOriginalFilename(), chunkFileDTO.getChunkId(), index);
        indexs.add(index);
        // 分片上传数量
        int chunkUploadCount = uploadedChunkCount.incrementAndGet();
        // 分片上传是否完成
        boolean complete = Objects.equals(chunkUploadCount, chunkCacheDTO.getChunkCount());
        chunkCacheDTO.setComplete(complete);
        return chunkCacheDTO;
    }

    /**
     * 获取缓存分片文件信息，不存在则转换存入(线程安全处理)
     */
    private ChunkCacheDTO getChunkSafe(ChunkFileDTO chunkFileDTO) {
        String chunkId = chunkFileDTO.getChunkId();
        boolean isContinued = Boolean.TRUE.equals(chunkFileDTO.getIsContinued());
        // 先从缓存中获取,每次走同步锁，性能比较差
        ChunkCacheDTO chunkCacheDTO = springCacheTemplate.get(CacheConst.UPLOAD_CHUNK, chunkId, ChunkCacheDTO.class);
        if (Objects.nonNull(chunkCacheDTO)) {
            return chunkCacheDTO;
        }
        // intern是为了从常量池拿引用，防止new的字符串，锁的字符串内容一样，但是锁得不一样
        String lock = chunkId.intern();
        // 加锁的意义是，防止并发上传同一个文件，导致缓存被覆盖
        synchronized (lock) {
            chunkCacheDTO = springCacheTemplate.get(CacheConst.UPLOAD_CHUNK, chunkId, ChunkCacheDTO.class);
            boolean hasCache = Objects.nonNull(chunkCacheDTO);
            // 缓存存在则返回false
            if (hasCache) {
                return chunkCacheDTO;
            }
            FileInfo dbFileInfo = fileInfoRepository.getByChunkId(chunkId);
            // 缓存不存在，非续传则转换参数为分片信息
            if (!isContinued) {
                Assert.isNull(dbFileInfo, "上传失败, 文件已存在");
                DataSize maxBigFileSize = fileProperty.getMaxBigFileSize();
                boolean match = chunkFileDTO.getFileSize() <= maxBigFileSize.toBytes();
                Assert.isTrue(match, BizErrCode.PARAM_ERROR, "文件大小不能超出" + maxBigFileSize.toMegabytes() + "MB");
                // 分片文件信息转换缓存信息
                chunkCacheDTO = chunkFileConvertCache(chunkFileDTO);
                // 保存数据库
                saveDb(chunkCacheDTO);
                // 保存缓存
                saveChunkCache(chunkCacheDTO);
                return chunkCacheDTO;
            }
            Assert.isNotNull(dbFileInfo, "续传失败, 分片文件信息不存在");
            Assert.isTrue(Objects.equals(dbFileInfo.getStatus(), FileInfoStatus.CHUNK.code()), BizErrCode.PARAM_ERROR, "分片文件状态才可进行续传");
            Assert.isTrue(Objects.equals(dbFileInfo.getMd5(), chunkFileDTO.getMd5()), BizErrCode.PARAM_ERROR, "分片文件md5不一致");
            // 数据库存在则转换分片信息
            chunkCacheDTO = fileInfoConvertCache(dbFileInfo);
            // 保存缓存
            saveChunkCache(chunkCacheDTO);
            return chunkCacheDTO;
        }
    }

    private void saveChunkCache(ChunkCacheDTO chunkCacheDTO) {
        // 上传队列大小限制
        long size = springCacheTemplate.size(CacheConst.UPLOAD_CHUNK);
        Assert.isTrue(size + 1 <= CacheConfig.CacheEnum.UPLOAD_CHUNK.getMaxSize(), BizErrCode.PARAM_ERROR, "分片文件上传数量超过限制");
        springCacheTemplate.put(CacheConst.UPLOAD_CHUNK, chunkCacheDTO.getChunkId(), chunkCacheDTO);
    }

    /**
     * 数据库分片文件信息转换缓存信息
     */
    private ChunkCacheDTO fileInfoConvertCache(FileInfo dbFileInfo) {
        String chunkId = dbFileInfo.getChunkId();
        ChunkCacheDTO chunkCacheDTO = new ChunkCacheDTO();
        // 如果数据库存在则进行断点续传
        ChunkMetadataDTO metadata = JsonUtil.toJavaObject(dbFileInfo.getChunkMetadata(), ChunkMetadataDTO.class);
        chunkCacheDTO.setChunkId(chunkId);
        chunkCacheDTO.setMd5(dbFileInfo.getMd5());
        chunkCacheDTO.setId(dbFileInfo.getId());
        chunkCacheDTO.setChunkCount(metadata.getChunkCount());
        chunkCacheDTO.setFileName(dbFileInfo.getFileName());
        chunkCacheDTO.setOriginalFileName(dbFileInfo.getOriginalFileName());
        chunkCacheDTO.setRelativeFullPath(dbFileInfo.getPath());
        chunkCacheDTO.setChunkDirPath(metadata.getChunkDirPath());
        chunkCacheDTO.setExtName(dbFileInfo.getExt());
        chunkCacheDTO.setUploadedChunkCount(new AtomicInteger(0));
        chunkCacheDTO.setIndexs(new ConcurrentSkipListSet<>());
        String absolutePath = getAbsolutePath(chunkCacheDTO.getType(), chunkCacheDTO.getChunkDirPath());
        File chunkDirFile = new File(absolutePath);
        if (!chunkDirFile.exists()) {
            return chunkCacheDTO;
        }
        String[] list = chunkDirFile.list();
        if (ArrayUtil.isEmpty(list)) {
            return chunkCacheDTO;
        }
        ConcurrentSkipListSet<Integer> indexs = chunkCacheDTO.getIndexs();
        AtomicInteger uploadedChunkCount = chunkCacheDTO.getUploadedChunkCount();
        for (String s : list) {
            uploadedChunkCount.incrementAndGet();
            indexs.add(Integer.valueOf(s));
        }
        boolean complete = Objects.equals(uploadedChunkCount.get(), chunkCacheDTO.getChunkCount());
        chunkCacheDTO.setComplete(complete);
        return chunkCacheDTO;
    }

    /**
     * 分片文件信息转换缓存信息
     */
    private ChunkCacheDTO chunkFileConvertCache(ChunkFileDTO chunkFileDTO) {
        ChunkCacheDTO chunkCacheDTO = new ChunkCacheDTO();
        // 原文件名 logo.svg
        String originalFileName = chunkFileDTO.getFileName();
        // 后缀 svg
        String extName = FileNameUtil.extName(originalFileName);
        // 原主文件名 logo
        String originalMainName = FileNameUtil.mainName(originalFileName);
        // 主文件名 logo-wKpdqhmC
        String mainName = originalMainName + StrUtil.DASHED + CommonUtil.fastShortUUID();
        // 文件名 logo-wKpdqhmC.svg
        String fileName = mainName + StrUtil.DOT + extName;
        // 分片文件夹路径
        String chunkDirPath;
        // 相对路径
        String relativeFullPath;
        if (Objects.nonNull(chunkFileDTO.getRelativePath())) {
            String relativePath = chunkFileDTO.getRelativePath() + File.separator;
            chunkDirPath = relativePath + mainName + File.separator;
            relativeFullPath = relativePath + fileName;
        } else {
            // 分片文件夹路径
            chunkDirPath = mainName + File.separator;
            relativeFullPath = fileName;
        }
        chunkCacheDTO.setChunkId(chunkFileDTO.getChunkId());
        chunkCacheDTO.setMd5(chunkFileDTO.getMd5());
        chunkCacheDTO.setType(chunkFileDTO.getType());
        chunkCacheDTO.setId(CloudIdUtil.getId());
        chunkCacheDTO.setChunkCount(chunkFileDTO.getChunkCount());
        chunkCacheDTO.setFileName(fileName);
        chunkCacheDTO.setFileSize(chunkFileDTO.getFileSize());
        chunkCacheDTO.setOriginalFileName(originalFileName);
        chunkCacheDTO.setContentType(FileUtil.getMimeType(fileName));
        chunkCacheDTO.setRelativeFullPath(relativeFullPath);
        chunkCacheDTO.setChunkDirPath(chunkDirPath);
        chunkCacheDTO.setExtName(extName);
        chunkCacheDTO.setIndexs(new ConcurrentSkipListSet<>());
        chunkCacheDTO.setUploadedChunkCount(new AtomicInteger(0));
        return chunkCacheDTO;
    }

    private void saveDb(ChunkCacheDTO chunkCacheDTO) {
        ChunkMetadataDTO chunkMetadataDTO = FileInfoAssembler.INSTANCE.chunkToMetadata(chunkCacheDTO);
        FileInfo fileInfo = FileInfoAssembler.INSTANCE.chunkToEntity(chunkCacheDTO);
        fileInfo.setPath(chunkCacheDTO.getRelativeFullPath());
        fileInfo.setExt(chunkCacheDTO.getExtName());
        fileInfo.setSize(chunkCacheDTO.getFileSize());
        fileInfo.setChunkMetadata(JsonUtil.toJsonString(chunkMetadataDTO));
        fileInfo.setStatus(FileInfoStatus.CHUNK.code());
        fileInfoRepository.save(fileInfo);
    }

    @Override
    public FileInfoDTO composeChunk(String chunkId) {
        Assert.isNotBlank(chunkId, "分片id不能为空");
        ChunkCacheDTO chunkCacheDTO = springCacheTemplate.get(CacheConst.UPLOAD_CHUNK, chunkId, ChunkCacheDTO.class);
        Assert.isNotNull(chunkCacheDTO, "分片文件不存在");
        ConcurrentSkipListSet<Integer> indexs = Optional.ofNullable(chunkCacheDTO.getIndexs()).orElseGet(ConcurrentSkipListSet::new);
        AtomicInteger uploadedChunkCountAto = chunkCacheDTO.getUploadedChunkCount();
        Integer chunkCount = chunkCacheDTO.getChunkCount();
        int size = indexs.size();
        int uploadedChunkCount = uploadedChunkCountAto.get();
        Assert.isTrue(size == uploadedChunkCount && size == chunkCount , "分片文件上传数量与分片数量不一致");
        // 分片上传是否完成
        boolean complete = Boolean.TRUE.equals(chunkCacheDTO.getComplete());
        Assert.isTrue(complete, "分片文件未全部上传");
        String chunkFileDirPath = getAbsolutePath(chunkCacheDTO.getType(), chunkCacheDTO.getChunkDirPath());
        String filePath = getAbsolutePath(chunkCacheDTO.getType(), chunkCacheDTO.getRelativeFullPath());
        try {
            RandomAccessFile mergedFile = new RandomAccessFile(filePath, "rw");
            // 缓冲区大小
            byte[] bytes = new byte[1024];
            for (int i = 0; i < size; i++) {
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
            fileInfo.setId(chunkCacheDTO.getId());
            fileInfo.setSize(fileSize);
            fileInfo.setStatus(FileInfoStatus.NORMAL.code());
            fileInfoRepository.updateById(fileInfo);
        } catch (IOException e) {
            throw new BizException(HttpErrCode.FAIL, "文件合并失败");
        } finally{
            // 清除缓存
            springCacheTemplate.evict(CacheConst.UPLOAD_CHUNK, chunkId);
            moveToTmp(chunkCacheDTO.getType(), chunkCacheDTO.getChunkDirPath());
        }
        FileInfo fileInfo = fileInfoRepository.getById(chunkCacheDTO.getId());
        return FileInfoAssembler.INSTANCE.entityToDto(fileInfo);
    }

    public void moveToTmp(Integer type, String relativePath) {
        String absolutePath = getAbsolutePath(type, relativePath);
        File source = new File(absolutePath);
        if (!source.exists()) {
            return;
        }
        String tmpAbstractPath = fileProperty.getTmpPath() + File.separator + relativePath;
        File target = new File(tmpAbstractPath);
        FileUtil.mkdir(target);
        FileUtil.moveContent(source, target, true);
        // 如果是文件夹
        if (source.isDirectory()) {
            FileUtil.del(source);
        }
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

    @Override
    public void expExcel(FileInfoQuery query)throws IOException {
        String sheetName = "文件信息";
        String fileName = "文件信息导出-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern(DatePattern.PURE_DATETIME_PATTERN));
        Map<String, String> labelMap = dictHandle.getLabelMapBatch(DictConst.FILE_STATUS, DictConst.FILE_STORAGE_TYPE);
        ExcelHandle.writeToResponseBatch(x-> this.excelExpHandle(x, labelMap), query, fileName, sheetName, FileInfoExp.class, response);
    }

    private Collection<FileInfoExp> excelExpHandle(FileInfoQuery query, Map<String, String> labelMap) {
        query.checkPage();
        Page<FileInfo> page = PageHelper.startPage(query.getPageNum(), query.getPageSize(), false);
        fileInfoRepository.list(query);
        List<FileInfo> result = page.getResult();
        return result
            .stream()
            .map(x-> {
                FileInfoExp fileInfoExp = FileInfoAssembler.INSTANCE.entityToExp(x);
                fileInfoExp.setTypeName(labelMap.get(DictConst.FILE_STORAGE_TYPE + x.getType()));
                fileInfoExp.setStatusName(labelMap.get(DictConst.FILE_STATUS + x.getStatus()));
                return fileInfoExp;
            })
            .collect(Collectors.toList());
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
