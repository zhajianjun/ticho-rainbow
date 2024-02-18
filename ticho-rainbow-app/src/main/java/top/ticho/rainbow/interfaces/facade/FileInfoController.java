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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.ticho.boot.web.annotation.View;
import top.ticho.rainbow.domain.repository.MinioRepository;
import top.ticho.rainbow.interfaces.dto.FileInfoDTO;
import top.ticho.rainbow.interfaces.dto.FileInfoReqDTO;


/**
 * 文件表 控制器
 *
 * @author zhajianjun
 * @date 2024-02-18 12:08
 */
@RestController
@RequestMapping("file")
@Api(tags = "文件操作")
@ApiSort(130)
@View
public class FileInfoController {

    // @formatter:off

    @Autowired
    private MinioRepository minioRepository;

    @PreAuthorize("@perm.hasPerms('storage:file:upload')")
    @ApiOperation(value = "文件上传")
    @ApiOperationSupport(order = 10)
    @PostMapping("upload")
    public FileInfoDTO upload(FileInfoReqDTO fileInfoReqDTO) {
        return minioRepository.upload(fileInfoReqDTO);
    }

    @PreAuthorize("@perm.hasPerms('storage:file:download')")
    @ApiOperation(value = "文件下载", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ApiOperationSupport(order = 20)
    @ApiImplicitParam(value = "资源id", name = "storageId", required = true)
    @GetMapping("download")
    public void download(String storageId) {
        minioRepository.download(storageId);
    }

    @PreAuthorize("@perm.hasPerms('storage:file:delete')")
    @ApiOperation(value = "根据id删除文件")
    @ApiOperationSupport(order = 30)
    @ApiImplicitParam(value = "资源id", name = "storageId", required = true)
    @DeleteMapping
    public void delete(String storageId) {
        minioRepository.delete(storageId);
    }

    @PreAuthorize("@perm.hasPerms('storage:file:getUrl')")
    @ApiOperation(value = "根据资源id获取下载链接")
    @ApiOperationSupport(order = 40)
    @ApiImplicitParams({
        @ApiImplicitParam(value = "资源id", name = "storageId", required = true),
        @ApiImplicitParam(value = "过期时间 <=7天，默认30分钟，单位：秒", name = "expires")
    })
    @GetMapping("getUrl")
    public String getUrl(String storageId, Integer expires) {
        return minioRepository.getUrl(storageId, expires);
    }


}
