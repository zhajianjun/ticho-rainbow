package top.ticho.rainbow.infrastructure.persistence.converter;

import org.mapstruct.Mapper;
import top.ticho.rainbow.application.dto.response.FileInfoDTO;
import top.ticho.rainbow.domain.entity.FileInfo;
import top.ticho.rainbow.infrastructure.persistence.po.FileInfoPO;

import java.util.List;

/**
 * 客户端信息 转换器
 *
 * @author zhajianjun
 * @date 2025-03-02 17:17
 */
@Mapper(componentModel = "spring")
public interface FileInfoConverter {

    List<FileInfo> toEntity(List<FileInfoPO> list);

    FileInfo toEntity(FileInfoPO fileInfoPO);

    FileInfoPO toPo(FileInfo fileInfo);

    List<FileInfoPO> toPo(List<FileInfo> fileInfos);

    FileInfoDTO toDTO(FileInfoPO fileInfoPO);

}
