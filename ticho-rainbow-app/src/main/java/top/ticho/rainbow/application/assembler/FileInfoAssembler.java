package top.ticho.rainbow.application.assembler;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import top.ticho.rainbow.application.dto.ChunkMetadataDTO;
import top.ticho.rainbow.application.dto.excel.FileInfoExcelExport;
import top.ticho.rainbow.interfaces.dto.response.ChunkCacheDTO;
import top.ticho.rainbow.interfaces.dto.response.FileInfoDTO;
import top.ticho.rainbow.domain.entity.FileInfo;
import top.ticho.rainbow.infrastructure.common.enums.FileInfoStatus;
import top.ticho.starter.web.util.TiIdUtil;
import top.ticho.tool.json.util.TiJsonUtil;

/**
 * 文件信息 转换
 *
 * @author zhajianjun
 * @date 2024-04-23 10:41
 */
@Mapper(componentModel = "spring", imports = {FileInfoStatus.class, TiJsonUtil.class, TiIdUtil.class})
public interface FileInfoAssembler {

    /**
     * 将文件信息DTO转换为实体对象。
     *
     * @param dto 文件信息DTO
     * @return {@link FileInfo}
     */
    @Mapping(target = "id", expression = "java(TiIdUtil.getId())")
    FileInfo toEntity(FileInfoDTO dto);

    /**
     * 将文件信息实体对象转换为DTO。
     *
     * @param entity 文件信息
     * @return {@link FileInfoDTO}
     */
    FileInfoDTO toDTO(FileInfo entity);

    /**
     * 将分块缓存DTO转换为文件信息实体对象。
     *
     * @param chunkCacheDTO 分块缓存DTO
     * @return {@link FileInfo}
     */
    @Mapping(target = "id", expression = "java(chunkCacheDTO.getId())")
    @Mapping(target = "chunkId", expression = "java(chunkCacheDTO.getChunkId())")
    @Mapping(target = "md5", expression = "java(chunkCacheDTO.getMd5())")
    @Mapping(target = "originalFileName", expression = "java(chunkCacheDTO.getOriginalFileName())")
    @Mapping(target = "fileName", expression = "java(chunkCacheDTO.getFileName())")
    @Mapping(target = "type", expression = "java(chunkCacheDTO.getType())")
    @Mapping(target = "contentType", expression = "java(chunkCacheDTO.getContentType())")
    @Mapping(target = "path", expression = "java(chunkCacheDTO.getRelativeFullPath())")
    @Mapping(target = "ext", expression = "java(chunkCacheDTO.getExtName())")
    @Mapping(target = "size", expression = "java(chunkCacheDTO.getFileSize())")
    @Mapping(target = "chunkMetadata", expression = "java(TiJsonUtil.toJsonString(chunkMetadataDTO))")
    @Mapping(target = "status", expression = "java(FileInfoStatus.CHUNK.code())")
    FileInfo chunkToEntity(ChunkCacheDTO chunkCacheDTO, ChunkMetadataDTO chunkMetadataDTO);

    /**
     * 将分块缓存DTO转换为分块元数据DTO。
     *
     * @param chunkCacheDTO 分块缓存DTO
     * @return {@link ChunkMetadataDTO}
     */
    ChunkMetadataDTO chunkToMetadata(ChunkCacheDTO chunkCacheDTO);

    /**
     * 将文件信息实体对象转换为导出对象。
     *
     * @param fileInfoDTO 文件信息
     * @return {@link FileInfoExcelExport}
     */
    FileInfoExcelExport toExcelExport(FileInfoDTO fileInfoDTO);

}
