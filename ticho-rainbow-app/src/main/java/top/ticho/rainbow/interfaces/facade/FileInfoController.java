package top.ticho.rainbow.interfaces.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.ticho.rainbow.application.service.FileInfoService;
import top.ticho.rainbow.infrastructure.common.annotation.ApiLog;
import top.ticho.rainbow.infrastructure.common.constant.ApiConst;
import top.ticho.rainbow.infrastructure.common.constant.CommConst;
import top.ticho.rainbow.interfaces.command.FileChunkUploadCommand;
import top.ticho.rainbow.interfaces.command.FileUploadCommand;
import top.ticho.rainbow.interfaces.command.VersionModifyCommand;
import top.ticho.rainbow.interfaces.dto.ChunkCacheDTO;
import top.ticho.rainbow.interfaces.dto.FileInfoDTO;
import top.ticho.rainbow.interfaces.query.FileInfoQuery;
import top.ticho.starter.security.annotation.IgnoreJwtCheck;
import top.ticho.starter.view.core.TiPageResult;
import top.ticho.starter.view.core.TiResult;
import top.ticho.starter.web.annotation.TiView;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.IOException;
import java.util.List;


/**
 * 文件
 *
 * @author zhajianjun
 * @date 2024-02-18 12:08
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("file")
public class FileInfoController {
    private final FileInfoService fileInfoService;

    /**
     * 上传文件
     */
    @ApiLog("上传文件")
    @PreAuthorize("@perm.hasPerms('" + ApiConst.STORAGE_FILE_UPLOAD + "')")
    @PostMapping("upload")
    public TiResult<FileInfoDTO> upload(@Validated FileUploadCommand fileUploadCommand) {
        return TiResult.ok(fileInfoService.upload(fileUploadCommand));
    }

    /**
     * 上传分片文件
     */
    @PreAuthorize("@file_perm.hasPerms('" + ApiConst.STORAGE_FILE_UPLOAD_CHUNK + "')")
    @PostMapping("chunk/upload")
    public TiResult<ChunkCacheDTO> uploadChunk(@Validated FileChunkUploadCommand fileChunkUploadCommand) {
        return TiResult.ok(fileInfoService.uploadChunk(fileChunkUploadCommand));
    }

    /**
     * 合并分片文件
     *
     * @param chunkId 分片id
     */
    @ApiLog("合并分片文件")
    @PreAuthorize("@perm.hasPerms('" + ApiConst.STORAGE_FILE_COMPOSE_CHUNK + "')")
    @PostMapping("chunk/compose")
    public TiResult<FileInfoDTO> composeChunk(@NotBlank(message = "分片id不能为空") String chunkId) {
        return TiResult.ok(fileInfoService.composeChunk(chunkId));
    }

    /**
     * 删除文件
     */
    @ApiLog("删除文件")
    @PreAuthorize("@perm.hasPerms('" + ApiConst.STORAGE_FILE_REMOVE + "')")
    @DeleteMapping
    public TiResult<Void> remove(@Validated @RequestBody VersionModifyCommand command) {
        fileInfoService.remove(command);
        return TiResult.ok();
    }

    /**
     * 启用文件
     */
    @ApiLog("启用文件")
    @PreAuthorize("@perm.hasPerms('" + ApiConst.STORAGE_FILE_ENABLE + "')")
    @PatchMapping("status/enable")
    public TiResult<Void> enable(
        @NotNull(message = "文件信息不能为空")
        @Size(max = CommConst.MAX_OPERATION_COUNT, message = "一次性最多操{max}条数据")
        @RequestBody List<VersionModifyCommand> datas
    ) {
        fileInfoService.enable(datas);
        return TiResult.ok();
    }

    /**
     * 停用文件
     */
    @ApiLog("停用文件")
    @PreAuthorize("@perm.hasPerms('" + ApiConst.STORAGE_FILE_DISABLE + "')")
    @PatchMapping("status/disable")
    public TiResult<Void> disable(
        @NotNull(message = "文件信息不能为空")
        @Size(max = CommConst.MAX_OPERATION_COUNT, message = "一次性最多操{max}条数据")
        @RequestBody List<VersionModifyCommand> datas
    ) {
        fileInfoService.disable(datas);
        return TiResult.ok();
    }

    /**
     * 作废文件
     */
    @ApiLog("作废文件")
    @PreAuthorize("@perm.hasPerms('" + ApiConst.STORAGE_FILE_CANCEL + "')")
    @PatchMapping("status/cancel")
    public TiResult<Void> cancel(
        @NotNull(message = "文件信息不能为空")
        @Size(max = CommConst.MAX_OPERATION_COUNT, message = "一次性最多操{max}条数据")
        @RequestBody List<VersionModifyCommand> datas
    ) {
        fileInfoService.cancel(datas);
        return TiResult.ok();
    }

    /**
     * 查询文件(分页)
     */
    @ApiLog("查询文件(分页)")
    @PreAuthorize("@perm.hasPerms('" + ApiConst.STORAGE_FILE_PAGE + "')")
    @GetMapping("page")
    public TiResult<TiPageResult<FileInfoDTO>> page(@Validated FileInfoQuery query) {
        return TiResult.ok(fileInfoService.page(query));
    }

    /**
     * 下载文件
     *
     * @param sign 签名
     */
    @ApiLog("下载文件")
    @TiView(ignore = true)
    @IgnoreJwtCheck
    @GetMapping("download")
    public void download(@NotBlank(message = "签名不能为空") String sign) {
        fileInfoService.download(sign);
    }

    /**
     * 获取下载链接
     *
     * @param id     文件id
     * @param expire 过期时间， <=7天，默认30分钟，单位：秒
     * @param limit  是否限制 true 链接只能使用一次，false 过期时间内不限制
     */
    @ApiLog("获取下载链接")
    @PreAuthorize("@perm.hasPerms('" + ApiConst.STORAGE_FILE_PRESIGNED + "')")
    @GetMapping("presigned")
    public TiResult<String> presigned(Long id, Long expire, Boolean limit) {
        return TiResult.ok(fileInfoService.presigned(id, expire, limit));
    }

    /**
     * 导出文件信息
     */
    @ApiLog("导出文件信息")
    @TiView(ignore = true)
    @PreAuthorize("@perm.hasPerms('" + ApiConst.STORAGE_FILE_EXPORT + "')")
    @GetMapping("excel/export")
    public void exportExcel(@Validated FileInfoQuery query) throws IOException {
        fileInfoService.exportExcel(query);
    }

}
