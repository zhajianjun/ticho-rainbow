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
import top.ticho.rainbow.application.dto.command.FileChunkUploadCommand;
import top.ticho.rainbow.application.dto.command.FileUploadCommand;
import top.ticho.rainbow.application.dto.command.VersionModifyCommand;
import top.ticho.rainbow.application.dto.query.FileInfoQuery;
import top.ticho.rainbow.application.dto.response.ChunkCacheDTO;
import top.ticho.rainbow.application.dto.response.FileInfoDTO;
import top.ticho.rainbow.application.service.FileInfoService;
import top.ticho.rainbow.infrastructure.common.constant.CommConst;
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
    @PreAuthorize("@perm.hasPerms('storage:file:upload')")
    @PostMapping("upload")
    public TiResult<FileInfoDTO> upload(@Validated FileUploadCommand fileUploadCommand) {
        return TiResult.ok(fileInfoService.upload(fileUploadCommand));
    }

    /**
     * 上传分片文件
     */
    @PreAuthorize("@file_perm.hasPerms('storage:file:upload-chunk')")
    @PostMapping("chunk/upload")
    public TiResult<ChunkCacheDTO> uploadChunk(@Validated FileChunkUploadCommand fileChunkUploadCommand) {
        return TiResult.ok(fileInfoService.uploadChunk(fileChunkUploadCommand));
    }

    /**
     * 合并分片文件
     *
     * @param chunkId 分片id
     */
    @PreAuthorize("@perm.hasPerms('storage:file:compose-chunk')")
    @PostMapping("chunk/compose")
    public TiResult<FileInfoDTO> composeChunk(@NotBlank(message = "分片id不能为空") String chunkId) {
        return TiResult.ok(fileInfoService.composeChunk(chunkId));
    }

    /**
     * 删除文件
     */
    @PreAuthorize("@perm.hasPerms('storage:file:remove')")
    @DeleteMapping
    public TiResult<Void> remove(@Validated @RequestBody VersionModifyCommand command) {
        fileInfoService.remove(command);
        return TiResult.ok();
    }

    /**
     * 启用文件
     */
    @PreAuthorize("@perm.hasPerms('storage:file:enable')")
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
    @PreAuthorize("@perm.hasPerms('storage:file:disable')")
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
    @PreAuthorize("@perm.hasPerms('storage:file:cancel')")
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
    @PreAuthorize("@perm.hasPerms('storage:file:page')")
    @GetMapping("page")
    public TiResult<TiPageResult<FileInfoDTO>> page(@Validated FileInfoQuery query) {
        return TiResult.ok(fileInfoService.page(query));
    }

    /**
     * 下载文件
     *
     * @param sign 签名
     */
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
    @PreAuthorize("@perm.hasPerms('storage:file:presigned')")
    @GetMapping("presigned")
    public TiResult<String> presigned(Long id, Long expire, Boolean limit) {
        return TiResult.ok(fileInfoService.presigned(id, expire, limit));
    }

    /**
     * 导出文件信息
     */
    @TiView(ignore = true)
    @PreAuthorize("@perm.hasPerms('storage:file:export')")
    @GetMapping("excel/export")
    public void exportExcel(@Validated FileInfoQuery query) throws IOException {
        fileInfoService.exportExcel(query);
    }

}
