package top.ticho.rainbow.interfaces.facade;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSort;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.ticho.boot.security.annotation.IgnoreJwtCheck;
import top.ticho.boot.view.core.PageResult;
import top.ticho.boot.view.core.Result;
import top.ticho.boot.web.annotation.View;
import top.ticho.rainbow.application.service.FileInfoService;
import top.ticho.rainbow.interfaces.dto.ChunkCacheDTO;
import top.ticho.rainbow.interfaces.dto.ChunkFileDTO;
import top.ticho.rainbow.interfaces.dto.FileInfoDTO;
import top.ticho.rainbow.interfaces.dto.FileInfoReqDTO;
import top.ticho.rainbow.interfaces.query.FileInfoQuery;


/**
 * 文件 控制器
 *
 * @author zhajianjun
 * @date 2024-02-18 12:08
 */
@RestController
@RequestMapping("file")
@Api(tags = "文件操作")
@ApiSort(130)
public class FileInfoController {

    // @formatter:of

    @Autowired
    private FileInfoService fileInfoService;

    @PreAuthorize("@perm.hasPerms('storage:file:upload')")
    @ApiOperation(value = "文件上传")
    @ApiOperationSupport(order = 10)
    @PostMapping("upload")
    public Result<FileInfoDTO> upload(FileInfoReqDTO fileInfoReqDTO) {
        return Result.ok(fileInfoService.upload(fileInfoReqDTO));
    }

    @PreAuthorize("@file_perm.hasPerms('storage:file:uploadChunk')")
    @ApiOperation(value = "分片文件上传")
    @ApiOperationSupport(order = 20)
    @PostMapping("uploadChunk")
    public Result<ChunkCacheDTO> uploadChunk(ChunkFileDTO chunkFileDTO) {
        return Result.ok(fileInfoService.uploadChunk(chunkFileDTO));
    }

    @PreAuthorize("@perm.hasPerms('storage:file:composeChunk')")
    @ApiOperation(value = "分片文件合并")
    @ApiOperationSupport(order = 30)
    @ApiImplicitParam(value = "分片id", name = "chunkId", required = true)
    @PostMapping("composeChunk")
    public Result<FileInfoDTO> composeChunk(String chunkId) {
        return Result.ok(fileInfoService.composeChunk(chunkId));
    }

    @View(ignore = true)
    @PreAuthorize("@perm.hasPerms('storage:file:downloadById')")
    @ApiOperation(value = "文件下载", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ApiOperationSupport(order = 40)
    @ApiImplicitParam(value = "文件id", name = "id", required = true)
    @GetMapping("downloadById")
    public void downloadById(Long id) {
        fileInfoService.downloadById(id);
    }

    @View(ignore = true)
    @IgnoreJwtCheck
    @ApiOperation(value = "文件下载(公共)", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ApiOperationSupport(order = 50)
    @ApiImplicitParam(value = "签名", name = "sign", required = true)
    @GetMapping("download")
    public void download(String sign) {
        fileInfoService.download(sign);
    }

    @PreAuthorize("@perm.hasPerms('storage:file:getUrl')")
    @ApiOperation(value = "根据文件id获取下载链接")
    @ApiOperationSupport(order = 60)
    @ApiImplicitParams({
        @ApiImplicitParam(value = "文件id", name = "id", required = true),
        @ApiImplicitParam(value = "过期时间 <=7天，默认30分钟，单位：秒", name = "expire"),
        @ApiImplicitParam(value = "是否限制", name = "limit"),
    })
    @GetMapping("getUrl")
    public Result<String> getUrl(Long id, Integer expire, Boolean limit) {
        return Result.ok(fileInfoService.getUrl(id, expire, limit));
    }

    @PreAuthorize("@perm.hasPerms('storage:file:enable')")
    @ApiOperation(value = "根据id启用文件")
    @ApiOperationSupport(order = 70)
    @ApiImplicitParam(value = "文件id", name = "id", required = true)
    @PutMapping("enable")
    public Result<Void> enable(Long id) {
        fileInfoService.enable(id);
        return Result.ok();
    }

    @PreAuthorize("@perm.hasPerms('storage:file:disable')")
    @ApiOperation(value = "停用文件")
    @ApiOperationSupport(order = 80)
    @ApiImplicitParam(value = "文件id", name = "id", required = true)
    @PutMapping("disable")
    public Result<Void> disable(Long id) {
        fileInfoService.disable(id);
        return Result.ok();
    }

    @PreAuthorize("@perm.hasPerms('storage:file:cancel')")
    @ApiOperation(value = "作废文件")
    @ApiOperationSupport(order = 90)
    @ApiImplicitParam(value = "文件id", name = "id", required = true)
    @PutMapping("cancel")
    public Result<Void> cancel(Long id) {
        fileInfoService.cancel(id);
        return Result.ok();
    }

    @PreAuthorize("@perm.hasPerms('storage:file:delete')")
    @ApiOperation(value = "删除文件")
    @ApiOperationSupport(order = 100)
    @ApiImplicitParam(value = "文件id", name = "id", required = true)
    @DeleteMapping
    public Result<Void> delete(Long id) {
        fileInfoService.delete(id);
        return Result.ok();
    }

    @PreAuthorize("@perm.hasPerms('storage:file:update')")
    @ApiOperation(value = "修改文件信息")
    @ApiOperationSupport(order = 110)
    @PutMapping
    public Result<Void> update(FileInfoDTO fileInfoDTO) {
        fileInfoService.update(fileInfoDTO);
        return Result.ok();
    }

    @PreAuthorize("@perm.hasPerms('storage:file:page')")
    @ApiOperation(value = "分页查询文件信息")
    @ApiOperationSupport(order = 120)
    @GetMapping("page")
    public Result<PageResult<FileInfoDTO>> page(FileInfoQuery query) {
        return Result.ok(fileInfoService.page(query));
    }

}
