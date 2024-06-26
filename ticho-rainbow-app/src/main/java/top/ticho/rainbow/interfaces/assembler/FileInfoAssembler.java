package top.ticho.rainbow.interfaces.assembler;

import top.ticho.rainbow.infrastructure.entity.FileInfo;
import top.ticho.rainbow.infrastructure.entity.OpLog;
import top.ticho.rainbow.interfaces.dto.ChunkCacheDTO;
import top.ticho.rainbow.interfaces.dto.ChunkMetadataDTO;
import top.ticho.rainbow.interfaces.dto.FileInfoDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
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
     * 文件信息
     *
     * @param dto 文件信息DTO
     * @return {@link FileInfo}
     */
    FileInfo dtoToEntity(FileInfoDTO dto);

    /**
     * 文件信息DTO
     *
     * @param entity 文件信息
     * @return {@link FileInfoDTO}
     */
    FileInfoDTO entityToDto(FileInfo entity);

    FileInfo chunkToEntity(ChunkCacheDTO chunkCacheDTO);

    ChunkMetadataDTO chunkToMetadata(ChunkCacheDTO chunkCacheDTO);

    FileInfoExp entityToExp(FileInfo fileInfo);
}