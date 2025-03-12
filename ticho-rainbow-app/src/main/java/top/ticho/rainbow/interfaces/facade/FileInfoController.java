package top.ticho.rainbow.interfaces.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.ticho.rainbow.application.dto.response.ChunkCacheDTO;
import top.ticho.rainbow.application.dto.response.FileInfoDTO;
import top.ticho.rainbow.application.dto.command.FileChunkUploadCommand;
import top.ticho.rainbow.application.dto.command.FileUploadCommand;
import top.ticho.rainbow.application.dto.query.FileInfoQuery;
import top.ticho.rainbow.application.service.FileInfoService;
import top.ticho.starter.security.annotation.IgnoreJwtCheck;
import top.ticho.starter.view.core.TiPageResult;
import top.ticho.starter.view.core.TiResult;
import top.ticho.starter.web.annotation.TiView;

import javax.validation.constraints.NotBlank;
import java.io.IOException;


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
     * 查询文件(分页)
     */
    @PreAuthorize("@perm.hasPerms('storage:file:page')")
    @GetMapping
    public TiResult<TiPageResult<FileInfoDTO>> page(@Validated @RequestBody FileInfoQuery query) {
        return TiResult.ok(fileInfoService.page(query));
    }

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
    @PreAuthorize("@file_perm.hasPerms('storage:file:uploadChunk')")
    @PostMapping("uploadChunk")
    public TiResult<ChunkCacheDTO> uploadChunk(@Validated FileChunkUploadCommand fileChunkUploadCommand) {
        return TiResult.ok(fileInfoService.uploadChunk(fileChunkUploadCommand));
    }

    /**
     * 合并分片文件
     *
     * @param chunkId 分片id
     */
    @PreAuthorize("@perm.hasPerms('storage:file:composeChunk')")
    @PostMapping("composeChunk")
    public TiResult<FileInfoDTO> composeChunk(@NotBlank(message = "分片id不能为空") String chunkId) {
        return TiResult.ok(fileInfoService.composeChunk(chunkId));
    }

    /**
     * 删除文件
     *
     * @param id 文件id
     */
    @PreAuthorize("@perm.hasPerms('storage:file:delete')")
    @DeleteMapping("{id}")
    public TiResult<Void> delete(@PathVariable("id") Long id) {
        fileInfoService.delete(id);
        return TiResult.ok();
    }

    /**
     * 启用文件
     *
     * @param id 文件id
     */
    @PreAuthorize("@perm.hasPerms('storage:file:enable')")
    @PatchMapping("enable/{id}")
    public TiResult<Void> enable(@PathVariable("id") Long id) {
        fileInfoService.enable(id);
        return TiResult.ok();
    }

    /**
     * 停用文件
     *
     * @param id 文件id
     */
    @PreAuthorize("@perm.hasPerms('storage:file:disable')")
    @PatchMapping("disable/{id}")
    public TiResult<Void> disable(@PathVariable("id") Long id) {
        fileInfoService.disable(id);
        return TiResult.ok();
    }

    /**
     * 作废文件
     *
     * @param id 文件id
     */
    @PreAuthorize("@perm.hasPerms('storage:file:cancel')")
    @PatchMapping("cancel/{id}")
    public TiResult<Void> cancel(@PathVariable("id") Long id) {
        fileInfoService.cancel(id);
        return TiResult.ok();
    }


    /**
     * 下载文件
     *
     * @param id 文件id
     */
    @TiView(ignore = true)
    @PreAuthorize("@perm.hasPerms('storage:file:downloadById')")
    @GetMapping("downloadById/{id}")
    public void downloadById(@PathVariable("id") Long id) {
        fileInfoService.downloadById(id);
    }

    /**
     * 下载文件(公共)
     *
     * @param sign 签名
     */
    @TiView(ignore = true)
    @IgnoreJwtCheck
    @GetMapping("download")
    public void download(String sign) {
        fileInfoService.download(sign);
    }

    /**
     * 查询下载链接
     *
     * @param id     文件id
     * @param expire 过期时间， <=7天，默认30分钟，单位：秒
     * @param limit  是否限制
     */
    @PreAuthorize("@perm.hasPerms('storage:file:getUrl')")
    @GetMapping("getUrl")
    public TiResult<String> getUrl(Long id, Long expire, Boolean limit) {
        return TiResult.ok(fileInfoService.getUrl(id, expire, limit));
    }

    /**
     * 导出文件信息
     *
     * @throws IOException io异常
     */
    @TiView(ignore = true)
    @PreAuthorize("@perm.hasPerms('storage:file:expExcel')")
    @GetMapping("expExcel")
    public void expExcel(@Validated @RequestBody FileInfoQuery query) throws IOException {
        fileInfoService.expExcel(query);
    }

}
