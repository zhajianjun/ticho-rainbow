package top.ticho.rainbow.domain.service;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.file.FileNameUtil;
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
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.TimeUnit;
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
        String relativePath = Optional.ofNullable(fileInfoReqDTO.getRelativePath())
            // 去除两边的斜杠
            .map(x-> StrUtil.strip(x, "/"))
            .map(x-> StrUtil.replace(x, "/", File.separator))
            .orElse(StrUtil.EMPTY);
        MultipartFile file = fileInfoReqDTO.getFile();
        // 原始文件名称，logo.svg
        String originalFilename = file.getOriginalFilename();
        DataSize fileSize = fileProperty.getMaxFileSize();
        Assert.isTrue(file.getSize() <= fileSize.toBytes(), FileErrCode.FILE_SIZE_TO_LARGER, "文件大小不能超出" + fileSize.toMegabytes() + "MB");
        // 主文件名 logo.svg -> logo
        String mainName = FileNameUtil.mainName(originalFilename);
        // 后缀名 svg
        String extName = FileNameUtil.extName(originalFilename);
        Long id = CloudIdUtil.getId();
        String prefixPath;
        // 存储文件名 logo.svg -> logo-wKpdqhmC.svg
        String fileName = mainName + StrUtil.DASHED + CommonUtil.fastShortUUID() + StrUtil.DOT + extName;
        // 相对全路径
        String relativeFullPath = relativePath + File.separator + fileName;
        // 文件存储
        if (Objects.equals(type, 1)) {
            prefixPath = fileProperty.getPublicPath();
        } else {
            prefixPath = fileProperty.getPrivatePath();
        }
        String absolutePath = prefixPath + relativeFullPath;
        try {
            FileUtil.writeBytes(file.getBytes(), absolutePath);
        } catch (IOException e) {
            throw new BizException(HttpErrCode.FAIL, "文件上传失败");
        }
        FileInfo fileInfo = new FileInfo();
        fileInfo.setId(id);
        fileInfo.setType(type);
        fileInfo.setFileName(fileName);
        fileInfo.setOriginalFilename(originalFilename);
        fileInfo.setPath(relativeFullPath);
        fileInfo.setSize(file.getSize());
        fileInfo.setExt(extName);
        fileInfo.setContentType(file.getContentType());
        fileInfo.setRemark(remark);
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

    public String getAbsolutePath(FileInfo fileInfo) {
        Integer type = fileInfo.getType();
        String path = fileInfo.getPath();
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
