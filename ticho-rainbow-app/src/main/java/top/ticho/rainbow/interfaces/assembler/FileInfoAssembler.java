package top.ticho.rainbow.interfaces.assembler;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import top.ticho.rainbow.infrastructure.entity.FileInfo;
import top.ticho.rainbow.interfaces.dto.ChunkCacheDTO;
import top.ticho.rainbow.interfaces.dto.ChunkMetadataDTO;
import top.ticho.rainbow.interfaces.dto.FileInfoDTO;
import top.ticho.rainbow.interfaces.excel.FileInfoExp;

/**
 * 文件信息 转换
 *
 * @author zhajianjun
 * @date 2024-04-23 10:41
 */
@Mapper
public interface FileInfoAssembler {
    FileInfoAssembler INSTANCE = Mappers.getMapper(FileInfoAssembler.class);

    /**
     * 将文件信息DTO转换为实体对象。
     *
     * @param dto 文件信息DTO
     * @return {@link FileInfo}
     */
    FileInfo dtoToEntity(FileInfoDTO dto);

    /**
     * 将文件信息实体对象转换为DTO。
     *
     * @param entity 文件信息
     * @return {@link FileInfoDTO}
     */
    FileInfoDTO entityToDto(FileInfo entity);

    /**
     * 将分块缓存DTO转换为文件信息实体对象。
     *
     * @param chunkCacheDTO 分块缓存DTO
     * @return {@link FileInfo}
     */
    FileInfo chunkToEntity(ChunkCacheDTO chunkCacheDTO);

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
     * @param fileInfo 文件信息
     * @return {@link FileInfoExp}
     */
    FileInfoExp entityToExp(FileInfo fileInfo);
}
