package top.ticho.rainbow.interfaces.facade;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSort;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.ticho.rainbow.application.storage.service.FileInfoService;
import top.ticho.rainbow.interfaces.dto.ChunkCacheDTO;
import top.ticho.rainbow.interfaces.dto.ChunkFileDTO;
import top.ticho.rainbow.interfaces.dto.FileInfoDTO;
import top.ticho.rainbow.interfaces.dto.FileInfoReqDTO;
import top.ticho.rainbow.interfaces.query.FileInfoQuery;
import top.ticho.starter.security.annotation.IgnoreJwtCheck;
import top.ticho.starter.view.core.TiPageResult;
import top.ticho.starter.view.core.TiResult;
import top.ticho.starter.web.annotation.TiView;

import javax.annotation.Resource;
import java.io.IOException;


/**
 * 文件 控制器
 *
 * @author zhajianjun
 * @date 2024-02-18 12:08
 */
@RestController
@RequestMapping("file")
@Api(tags = "文件")
@ApiSort(130)
public class FileInfoController {


    @Resource
    private FileInfoService fileInfoService;

    @PreAuthorize("@perm.hasPerms('storage:file:upload')")
    @ApiOperation(value = "上传文件")
    @ApiOperationSupport(order = 10)
    @PostMapping("upload")
    public TiResult<FileInfoDTO> upload(FileInfoReqDTO fileInfoReqDTO) {
        return TiResult.ok(fileInfoService.upload(fileInfoReqDTO));
    }

    @PreAuthorize("@file_perm.hasPerms('storage:file:uploadChunk')")
    @ApiOperation(value = "上传分片文件")
    @ApiOperationSupport(order = 20)
    @PostMapping("uploadChunk")
    public TiResult<ChunkCacheDTO> uploadChunk(ChunkFileDTO chunkFileDTO) {
        return TiResult.ok(fileInfoService.uploadChunk(chunkFileDTO));
    }

    @PreAuthorize("@perm.hasPerms('storage:file:composeChunk')")
    @ApiOperation(value = "合并分片文件")
    @ApiOperationSupport(order = 30)
    @ApiImplicitParam(value = "分片id", name = "chunkId", required = true)
    @PostMapping("composeChunk")
    public TiResult<FileInfoDTO> composeChunk(String chunkId) {
        return TiResult.ok(fileInfoService.composeChunk(chunkId));
    }

    @TiView(ignore = true)
    @PreAuthorize("@perm.hasPerms('storage:file:downloadById')")
    @ApiOperation(value = "下载文件", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ApiOperationSupport(order = 40)
    @ApiImplicitParam(value = "文件id", name = "id", required = true)
    @GetMapping("downloadById")
    public void downloadById(Long id) {
        fileInfoService.downloadById(id);
    }

    @TiView(ignore = true)
    @IgnoreJwtCheck
    @ApiOperation(value = "下载文件(公共)", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ApiOperationSupport(order = 50)
    @ApiImplicitParam(value = "签名", name = "sign", required = true)
    @GetMapping("download")
    public void download(String sign) {
        fileInfoService.download(sign);
    }

    @PreAuthorize("@perm.hasPerms('storage:file:getUrl')")
    @ApiOperation(value = "查询下载链接")
    @ApiOperationSupport(order = 60)
    @ApiImplicitParams({
        @ApiImplicitParam(value = "文件id", name = "id", required = true),
        @ApiImplicitParam(value = "过期时间 <=7天，默认30分钟，单位：秒", name = "expire"),
        @ApiImplicitParam(value = "是否限制", name = "limit"),
    })
    @GetMapping("getUrl")
    public TiResult<String> getUrl(Long id, Long expire, Boolean limit) {
        return TiResult.ok(fileInfoService.getUrl(id, expire, limit));
    }

    @PreAuthorize("@perm.hasPerms('storage:file:enable')")
    @ApiOperation(value = "启用文件")
    @ApiOperationSupport(order = 70)
    @ApiImplicitParam(value = "文件id", name = "id", required = true)
    @PutMapping("enable")
    public TiResult<Void> enable(Long id) {
        fileInfoService.enable(id);
        return TiResult.ok();
    }

    @PreAuthorize("@perm.hasPerms('storage:file:disable')")
    @ApiOperation(value = "停用文件")
    @ApiOperationSupport(order = 80)
    @ApiImplicitParam(value = "文件id", name = "id", required = true)
    @PutMapping("disable")
    public TiResult<Void> disable(Long id) {
        fileInfoService.disable(id);
        return TiResult.ok();
    }

    @PreAuthorize("@perm.hasPerms('storage:file:cancel')")
    @ApiOperation(value = "作废文件")
    @ApiOperationSupport(order = 90)
    @ApiImplicitParam(value = "文件id", name = "id", required = true)
    @PutMapping("cancel")
    public TiResult<Void> cancel(Long id) {
        fileInfoService.cancel(id);
        return TiResult.ok();
    }

    @PreAuthorize("@perm.hasPerms('storage:file:delete')")
    @ApiOperation(value = "删除文件")
    @ApiOperationSupport(order = 100)
    @ApiImplicitParam(value = "文件id", name = "id", required = true)
    @DeleteMapping
    public TiResult<Void> delete(Long id) {
        fileInfoService.delete(id);
        return TiResult.ok();
    }

    @PreAuthorize("@perm.hasPerms('storage:file:update')")
    @ApiOperation(value = "修改文件")
    @ApiOperationSupport(order = 110)
    @PutMapping
    public TiResult<Void> update(FileInfoDTO fileInfoDTO) {
        fileInfoService.update(fileInfoDTO);
        return TiResult.ok();
    }

    @PreAuthorize("@perm.hasPerms('storage:file:page')")
    @ApiOperation(value = "查询所有文件(分页)")
    @ApiOperationSupport(order = 120)
    @PostMapping("page")
    public TiResult<TiPageResult<FileInfoDTO>> page(@RequestBody FileInfoQuery query) {
        return TiResult.ok(fileInfoService.page(query));
    }

    @TiView(ignore = true)
    @PreAuthorize("@perm.hasPerms('storage:file:expExcel')")
    @ApiOperation(value = "导出文件信息", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ApiOperationSupport(order = 130)
    @PostMapping("expExcel")
    public void expExcel(@RequestBody FileInfoQuery query) throws IOException {
        fileInfoService.expExcel(query);
    }

}
