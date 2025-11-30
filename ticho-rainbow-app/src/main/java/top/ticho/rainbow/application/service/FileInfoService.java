package top.ticho.rainbow.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.MultipartFile;
import top.ticho.rainbow.application.assembler.FileInfoAssembler;
import top.ticho.rainbow.application.dto.ChunkMetadataDTO;
import top.ticho.rainbow.application.dto.excel.FileInfoExcelExport;
import top.ticho.rainbow.application.executor.DictExecutor;
import top.ticho.rainbow.application.executor.FileInfoExecutor;
import top.ticho.rainbow.application.repository.FileInfoAppRepository;
import top.ticho.rainbow.domain.entity.FileInfo;
import top.ticho.rainbow.domain.repository.FileInfoRepository;
import top.ticho.rainbow.infrastructure.common.component.excel.ExcelHandle;
import top.ticho.rainbow.infrastructure.common.constant.CacheConst;
import top.ticho.rainbow.infrastructure.common.constant.DateConst;
import top.ticho.rainbow.infrastructure.common.constant.DictConst;
import top.ticho.rainbow.infrastructure.common.dto.FileCacheDTO;
import top.ticho.rainbow.infrastructure.common.enums.FileErrorCode;
import top.ticho.rainbow.infrastructure.common.enums.FileInfoStatus;
import top.ticho.rainbow.infrastructure.common.prop.FileProperty;
import top.ticho.rainbow.infrastructure.config.CacheConfig;
import top.ticho.rainbow.interfaces.command.FileChunkUploadCommand;
import top.ticho.rainbow.interfaces.command.FileUploadCommand;
import top.ticho.rainbow.interfaces.command.VersionModifyCommand;
import top.ticho.rainbow.interfaces.dto.ChunkCacheDTO;
import top.ticho.rainbow.interfaces.dto.FileInfoDTO;
import top.ticho.rainbow.interfaces.query.FileInfoQuery;
import top.ticho.starter.cache.component.TiCacheTemplate;
import top.ticho.starter.view.core.TiPageResult;
import top.ticho.tool.core.TiArrayUtil;
import top.ticho.tool.core.TiAssert;
import top.ticho.tool.core.TiFileUtil;
import top.ticho.tool.core.TiIdUtil;
import top.ticho.tool.core.TiIoUtil;
import top.ticho.tool.core.TiStrUtil;
import top.ticho.tool.core.TiUrlUtil;
import top.ticho.tool.core.constant.TiStrConst;
import top.ticho.tool.core.enums.TiBizErrorCode;
import top.ticho.tool.core.enums.TiHttpErrorCode;
import top.ticho.tool.core.exception.TiBizException;
import top.ticho.tool.json.util.TiJsonUtil;

import jakarta.servlet.http.HttpServletResponse;
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
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * 文件服务
 *
 * @author zhajianjun
 * @date 2024-02-18 12:08
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class FileInfoService {
    private final HttpServletResponse response;
    private final TiCacheTemplate tiCacheTemplate;
    private final FileProperty fileProperty;
    private final FileInfoRepository fileInfoRepository;
    private final FileInfoAppRepository fileInfoAppRepository;
    private final FileInfoAssembler fileInfoAssembler;
    private final DictExecutor dictExecutor;
    private final FileInfoExecutor fileInfoExecutor;

    public FileInfoDTO upload(FileUploadCommand fileUploadCommand) {
        return fileInfoExecutor.upload(fileUploadCommand);
    }

    public void enable(List<VersionModifyCommand> datas) {
        boolean enable = modifyBatch(datas, FileInfo::enable);
        TiAssert.isTrue(enable, "启用失败，请刷新后重试");
    }

    public void disable(List<VersionModifyCommand> datas) {
        boolean disable = modifyBatch(datas, FileInfo::disable);
        TiAssert.isTrue(disable, "停用失败，请刷新后重试");
    }

    public void cancel(List<VersionModifyCommand> datas) {
        boolean cancel = modifyBatch(datas, FileInfo::cancel);
        TiAssert.isTrue(cancel, "作废失败，请刷新后重试");
    }

    public void remove(VersionModifyCommand command) {
        FileInfo fileInfo = fileInfoRepository.find(command.getId());
        TiAssert.isNotNull(fileInfo, FileErrorCode.FILE_NOT_EXIST, "删除失败, 文件信息不存在");
        fileInfo.checkVersion(command.getVersion(), "数据已被修改，请刷新后重试");
        TiAssert.isTrue(fileInfo.isCancel() || fileInfo.isChunk(), "删除失败, 分片或者作废状态文件才能删除");
        String path;
        if (fileInfo.isChunk()) {
            boolean isUploadChunkIng = tiCacheTemplate.contain(CacheConst.UPLOAD_CHUNK, fileInfo.getChunkId());
            TiAssert.isTrue(!isUploadChunkIng, "删除失败, 分片文件正在上传中");
            ChunkMetadataDTO metadata = TiJsonUtil.toObject(fileInfo.getChunkMetadata(), ChunkMetadataDTO.class);
            path = metadata.getChunkDirPath();
        } else {
            path = fileInfo.getPath();
        }
        moveToTmp(fileInfo.getType(), path);
        fileInfoRepository.remove(command.getId());
    }

    public void download(String sign) {
        FileCacheDTO fileCacheDTO = tiCacheTemplate.get(CacheConst.FILE_URL_CACHE, sign, FileCacheDTO.class);
        TiAssert.isNotNull(fileCacheDTO, FileErrorCode.FILE_NOT_EXIST);
        FileInfo fileInfo = fileCacheDTO.getFileInfo();
        if (Boolean.TRUE.equals(fileCacheDTO.getLimit())) {
            fileCacheDTO.setLimited(true);
            tiCacheTemplate.put(CacheConst.FILE_URL_CACHE, sign, fileCacheDTO);
        }
        download(fileInfo);
    }

    private void download(FileInfo fileInfo) {
        TiAssert.isNotNull(fileInfo, FileErrorCode.FILE_NOT_EXIST);
        TiAssert.isTrue(fileInfo.isEnable(), FileErrorCode.FILE_STATUS_ERROR);
        String absolutePath = fileInfoExecutor.getAbsolutePath(fileInfo.getType(), fileInfo.getPath());
        File file = new File(absolutePath);
        TiAssert.isTrue(TiFileUtil.exist(file), FileErrorCode.FILE_NOT_EXIST);
        try {
            response.setHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + TiUrlUtil.encode(fileInfo.getOriginalFileName()));
            response.setContentType(fileInfo.getContentType());
            response.setHeader(HttpHeaders.PRAGMA, "no-cache");
            response.setHeader(HttpHeaders.CACHE_CONTROL, "no-cache");
            response.setHeader(HttpHeaders.CONTENT_LENGTH, fileInfo.getSize() + "");
            response.setDateHeader(HttpHeaders.EXPIRES, 0);
            TiIoUtil.copy(Files.newInputStream(file.toPath()), response.getOutputStream(), 1024);
        } catch (Exception e) {
            log.error("文件下载失败，{}", e.getMessage(), e);
            throw new TiBizException(FileErrorCode.DOWNLOAD_ERROR);
        }
    }

    public String presigned(Long id, Long expire, Boolean limit) {
        return fileInfoExecutor.presigned(id, expire, limit);
    }

    public ChunkCacheDTO uploadChunk(FileChunkUploadCommand fileChunkUploadCommand) {
        MultipartFile chunkfile = fileChunkUploadCommand.getChunkfile();
        DataSize maxFileSize = fileProperty.getMaxPartSize();
        Integer index = fileChunkUploadCommand.getIndex();
        TiAssert.isTrue(chunkfile.getSize() <= maxFileSize.toBytes(), FileErrorCode.FILE_SIZE_TO_LARGER, "分片文件大小不能超出" + maxFileSize.toMegabytes() + "MB");
        // 相对路径处理
        String relativePath = Optional.ofNullable(fileChunkUploadCommand.getRelativePath())
            .filter(TiStrUtil::isNotBlank)
            // 去除两边的斜杠
            .map(x -> TiStrUtil.strip(x, "/"))
            .orElse(null);
        fileChunkUploadCommand.setRelativePath(relativePath);
        ChunkCacheDTO chunkCacheDTO = getChunkSafe(fileChunkUploadCommand);
        ConcurrentSkipListSet<Integer> indexs = chunkCacheDTO.getIndexs();
        AtomicInteger uploadedChunkCount = chunkCacheDTO.getUploadedChunkCount();
        TiAssert.isTrue(index + 1 <= fileChunkUploadCommand.getChunkCount(), "索引超出分片数量大小");
        if (indexs.contains(index)) {
            log.info("分片文件已上传");
            return chunkCacheDTO;
        }
        String chunkFilePath = fileInfoExecutor.getAbsolutePath(chunkCacheDTO.getType(), chunkCacheDTO.getChunkDirPath()) + index;
        try {
            File chunkFile = new File(chunkFilePath);
            if (!chunkFile.exists()) {
                TiFileUtil.writeBytes(chunkfile.getBytes(), chunkFile);
            }
        } catch (IOException e) {
            throw new TiBizException(TiHttpErrorCode.FAIL, "文件上传失败");
        }
        log.info("{}分片文件{}，分片id={}，index={}上传成功", fileChunkUploadCommand.getFileName(), chunkfile.getOriginalFilename(), fileChunkUploadCommand.getChunkId(), index);
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
    private ChunkCacheDTO getChunkSafe(FileChunkUploadCommand fileChunkUploadCommand) {
        String chunkId = fileChunkUploadCommand.getChunkId();
        boolean isContinued = Boolean.TRUE.equals(fileChunkUploadCommand.getIsContinued());
        // 先从缓存中获取,每次走同步锁，性能比较差
        ChunkCacheDTO chunkCacheDTO = tiCacheTemplate.get(CacheConst.UPLOAD_CHUNK, chunkId, ChunkCacheDTO.class);
        if (Objects.nonNull(chunkCacheDTO)) {
            return chunkCacheDTO;
        }
        // intern是为了从常量池拿引用，防止new的字符串，锁的字符串内容一样，但是锁得不一样
        String lock = chunkId.intern();
        // 加锁的意义是，防止并发上传同一个文件，导致缓存被覆盖
        synchronized (lock) {
            chunkCacheDTO = tiCacheTemplate.get(CacheConst.UPLOAD_CHUNK, chunkId, ChunkCacheDTO.class);
            boolean hasCache = Objects.nonNull(chunkCacheDTO);
            // 缓存存在则返回false
            if (hasCache) {
                return chunkCacheDTO;
            }
            FileInfo dbFileInfo = fileInfoRepository.getByChunkId(chunkId);
            // 缓存不存在，非续传则转换参数为分片信息
            if (!isContinued) {
                TiAssert.isNull(dbFileInfo, "上传失败, 文件已存在");
                DataSize maxBigFileSize = fileProperty.getMaxBigFileSize();
                boolean match = fileChunkUploadCommand.getFileSize() <= maxBigFileSize.toBytes();
                TiAssert.isTrue(match, TiBizErrorCode.PARAM_ERROR, "文件大小不能超出" + maxBigFileSize.toMegabytes() + "MB");
                // 分片文件信息转换缓存信息
                chunkCacheDTO = chunkFileConvertCache(fileChunkUploadCommand);
                // 保存数据库
                saveDb(chunkCacheDTO);
                // 保存缓存
                saveChunkCache(chunkCacheDTO);
                return chunkCacheDTO;
            }
            TiAssert.isNotNull(dbFileInfo, "续传失败, 分片文件信息不存在");
            TiAssert.isTrue(Objects.equals(dbFileInfo.getStatus(), FileInfoStatus.CHUNK.code()), TiBizErrorCode.PARAM_ERROR, "分片文件状态才可进行续传");
            TiAssert.isTrue(Objects.equals(dbFileInfo.getMd5(), fileChunkUploadCommand.getMd5()), TiBizErrorCode.PARAM_ERROR, "分片文件md5不一致");
            // 数据库存在则转换分片信息
            chunkCacheDTO = fileInfoConvertCache(dbFileInfo);
            // 保存缓存
            saveChunkCache(chunkCacheDTO);
            return chunkCacheDTO;
        }
    }

    private void saveChunkCache(ChunkCacheDTO chunkCacheDTO) {
        // 上传队列大小限制
        long size = tiCacheTemplate.size(CacheConst.UPLOAD_CHUNK);
        TiAssert.isTrue(size + 1 <= CacheConfig.CacheEnum.UPLOAD_CHUNK.getMaxSize(), TiBizErrorCode.PARAM_ERROR, "分片文件上传数量超过限制");
        tiCacheTemplate.put(CacheConst.UPLOAD_CHUNK, chunkCacheDTO.getChunkId(), chunkCacheDTO);
    }

    /**
     * 数据库分片文件信息转换缓存信息
     */
    private ChunkCacheDTO fileInfoConvertCache(FileInfo dbFileInfo) {
        String chunkId = dbFileInfo.getChunkId();
        ChunkCacheDTO chunkCacheDTO = new ChunkCacheDTO();
        // 如果数据库存在则进行断点续传
        ChunkMetadataDTO metadata = TiJsonUtil.toObject(dbFileInfo.getChunkMetadata(), ChunkMetadataDTO.class);
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
        String absolutePath = fileInfoExecutor.getAbsolutePath(chunkCacheDTO.getType(), chunkCacheDTO.getChunkDirPath());
        File chunkDirFile = new File(absolutePath);
        if (!chunkDirFile.exists()) {
            return chunkCacheDTO;
        }
        String[] list = chunkDirFile.list();
        if (TiArrayUtil.isEmpty(list)) {
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
    private ChunkCacheDTO chunkFileConvertCache(FileChunkUploadCommand fileChunkUploadCommand) {
        ChunkCacheDTO chunkCacheDTO = new ChunkCacheDTO();
        // 原文件名 logo.svg
        String originalFileName = fileChunkUploadCommand.getFileName();
        // 后缀 svg
        String extName = TiFileUtil.extName(originalFileName);
        // 原主文件名 logo
        String originalMainName = TiFileUtil.mainName(originalFileName);
        // 主文件名 logo-wKpdqhmC
        String mainName = originalMainName + TiStrConst.DASHED + TiIdUtil.shortUuid();
        // 文件名 logo-wKpdqhmC.svg
        String fileName = mainName + TiStrConst.DOT + extName;
        // 分片文件夹路径
        String chunkDirPath;
        // 相对路径
        String relativeFullPath;
        if (Objects.nonNull(fileChunkUploadCommand.getRelativePath())) {
            String relativePath = fileChunkUploadCommand.getRelativePath() + File.separator;
            chunkDirPath = relativePath + mainName + File.separator;
            relativeFullPath = relativePath + fileName;
        } else {
            // 分片文件夹路径
            chunkDirPath = mainName + File.separator;
            relativeFullPath = fileName;
        }
        chunkCacheDTO.setChunkId(fileChunkUploadCommand.getChunkId());
        chunkCacheDTO.setMd5(fileChunkUploadCommand.getMd5());
        chunkCacheDTO.setType(fileChunkUploadCommand.getType());
        chunkCacheDTO.setId(TiIdUtil.snowId());
        chunkCacheDTO.setChunkCount(fileChunkUploadCommand.getChunkCount());
        chunkCacheDTO.setFileName(fileName);
        chunkCacheDTO.setFileSize(fileChunkUploadCommand.getFileSize());
        chunkCacheDTO.setOriginalFileName(originalFileName);
        chunkCacheDTO.setContentType(TiFileUtil.getMimeType(fileName));
        chunkCacheDTO.setRelativeFullPath(relativeFullPath);
        chunkCacheDTO.setChunkDirPath(chunkDirPath);
        chunkCacheDTO.setExtName(extName);
        chunkCacheDTO.setIndexs(new ConcurrentSkipListSet<>());
        chunkCacheDTO.setUploadedChunkCount(new AtomicInteger(0));
        return chunkCacheDTO;
    }

    private void saveDb(ChunkCacheDTO chunkCacheDTO) {
        ChunkMetadataDTO chunkMetadataDTO = fileInfoAssembler.chunkToMetadata(chunkCacheDTO);
        FileInfo fileInfo = fileInfoAssembler.chunkToEntity(chunkCacheDTO, chunkMetadataDTO);
        fileInfoRepository.save(fileInfo);
    }

    public FileInfoDTO composeChunk(String chunkId) {
        TiAssert.isNotBlank(chunkId, "分片id不能为空");
        ChunkCacheDTO chunkCacheDTO = tiCacheTemplate.get(CacheConst.UPLOAD_CHUNK, chunkId, ChunkCacheDTO.class);
        TiAssert.isNotNull(chunkCacheDTO, "分片文件不存在");
        ConcurrentSkipListSet<Integer> indexs = Optional.ofNullable(chunkCacheDTO.getIndexs()).orElseGet(ConcurrentSkipListSet::new);
        AtomicInteger uploadedChunkCountAto = chunkCacheDTO.getUploadedChunkCount();
        Integer chunkCount = chunkCacheDTO.getChunkCount();
        int size = indexs.size();
        int uploadedChunkCount = uploadedChunkCountAto.get();
        TiAssert.isTrue(size == uploadedChunkCount && size == chunkCount, "分片文件上传数量与分片数量不一致");
        // 分片上传是否完成
        boolean complete = Boolean.TRUE.equals(chunkCacheDTO.getComplete());
        TiAssert.isTrue(complete, "分片文件未全部上传");
        String chunkFileDirPath = fileInfoExecutor.getAbsolutePath(chunkCacheDTO.getType(), chunkCacheDTO.getChunkDirPath());
        String filePath = fileInfoExecutor.getAbsolutePath(chunkCacheDTO.getType(), chunkCacheDTO.getRelativeFullPath());
        FileInfo fileInfo = fileInfoRepository.find(chunkCacheDTO.getId());
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
            fileInfo.compose(fileSize);
            fileInfoRepository.modify(fileInfo);
        } catch (IOException e) {
            throw new TiBizException(TiHttpErrorCode.FAIL, "文件合并失败");
        } finally {
            // 清除缓存
            tiCacheTemplate.evict(CacheConst.UPLOAD_CHUNK, chunkId);
            moveToTmp(chunkCacheDTO.getType(), chunkCacheDTO.getChunkDirPath());
        }
        return fileInfoAssembler.toDTO(fileInfo);
    }

    public void moveToTmp(Integer type, String relativePath) {
        String absolutePath = fileInfoExecutor.getAbsolutePath(type, relativePath);
        File source = new File(absolutePath);
        if (!source.exists()) {
            return;
        }
        String tmpAbstractPath = fileProperty.getTmpPath() + File.separator + relativePath;
        File target = new File(tmpAbstractPath);
        TiFileUtil.mkdir(target);
        TiFileUtil.moveFile(source, target);
        // 如果是文件夹
        if (source.isDirectory()) {
            TiFileUtil.del(source);
        }
    }

    public TiPageResult<FileInfoDTO> page(FileInfoQuery query) {
        return fileInfoAppRepository.page(query);
    }

    public void exportExcel(FileInfoQuery query) throws IOException {
        String sheetName = "文件信息";
        String fileName = "文件信息导出-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern(DateConst.PURE_DATETIME_PATTERN));
        Map<String, String> labelMap = dictExecutor.getLabelMapBatch(DictConst.FILE_STATUS, DictConst.FILE_STORAGE_TYPE);
        query.setCount(false);
        ExcelHandle.writeToResponseBatch(x -> this.excelExpHandle(x, labelMap), query, fileName, sheetName, FileInfoExcelExport.class, response);
    }

    private Collection<FileInfoExcelExport> excelExpHandle(FileInfoQuery query, Map<String, String> labelMap) {
        TiPageResult<FileInfoDTO> page = fileInfoAppRepository.page(query);
        List<FileInfoDTO> result = page.getRows();
        return result
            .stream()
            .map(x -> {
                FileInfoExcelExport fileInfoExcelExport = fileInfoAssembler.toExcelExport(x);
                fileInfoExcelExport.setTypeName(labelMap.get(DictConst.FILE_STORAGE_TYPE + x.getType()));
                fileInfoExcelExport.setStatusName(labelMap.get(DictConst.FILE_STATUS + x.getStatus()));
                return fileInfoExcelExport;
            })
            .collect(Collectors.toList());
    }

    private boolean modifyBatch(List<VersionModifyCommand> modifys, Consumer<FileInfo> modifyHandle) {
        List<Long> ids = modifys.stream().map(VersionModifyCommand::getId).collect(Collectors.toList());
        List<FileInfo> fileInfos = fileInfoRepository.list(ids);
        Map<Long, FileInfo> fileInfoMap = fileInfos.stream().collect(Collectors.toMap(FileInfo::getId, Function.identity(), (o, n) -> o));
        for (VersionModifyCommand modify : modifys) {
            FileInfo fileInfo = fileInfoMap.get(modify.getId());
            TiAssert.isNotNull(fileInfo, TiStrUtil.format("操作失败, 数据不存在, id: {}", modify.getId()));
            fileInfo.checkVersion(modify.getVersion(), TiStrUtil.format("数据已被修改，请刷新后重试, 文件: {}", fileInfo.getFileName()));
            // 修改逻辑
            modifyHandle.accept(fileInfo);
        }
        return fileInfoRepository.modifyBatch(fileInfos);
    }

}
