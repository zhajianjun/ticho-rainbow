package top.ticho.rainbow.interfaces.facade;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSort;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
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
import top.ticho.rainbow.application.intranet.service.PortService;
import top.ticho.rainbow.interfaces.dto.PortDTO;
import top.ticho.rainbow.interfaces.query.PortQuery;
import top.ticho.starter.view.core.TiPageResult;
import top.ticho.starter.view.core.TiResult;
import top.ticho.starter.web.annotation.TiView;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * 端口信息
 *
 * @author zhajianjun
 * @date 2023-12-17 20:12
 */
@RestController
@RequestMapping("port")
@Api(tags = "端口信息")
@ApiSort(100)
public class PortController {

    @Resource
    private PortService portService;

    @PreAuthorize("@perm.hasPerms('intranet:port:save')")
    @ApiOperation(value = "保存端口")
    @ApiOperationSupport(order = 10)
    @PostMapping
    public TiResult<Void> save(@RequestBody PortDTO portDTO) {
        portService.save(portDTO);
        return TiResult.ok();
    }

    @PreAuthorize("@perm.hasPerms('intranet:port:remove')")
    @ApiOperation(value = "删除端口")
    @ApiOperationSupport(order = 20)
    @ApiImplicitParam(value = "编号", name = "id", required = true)
    @DeleteMapping
    public TiResult<Void> remove(Long id) {
        portService.removeById(id);
        return TiResult.ok();
    }

    @PreAuthorize("@perm.hasPerms('intranet:port:update')")
    @ApiOperation(value = "修改端口")
    @ApiOperationSupport(order = 30)
    @PutMapping
    public TiResult<Void> update(@RequestBody PortDTO portDTO) {
        portService.updateById(portDTO);
        return TiResult.ok();
    }

    @PreAuthorize("@perm.hasPerms('intranet:port:getById')")
    @ApiOperation(value = "查询端口")
    @ApiOperationSupport(order = 40)
    @ApiImplicitParam(value = "编号", name = "id", required = true)
    @GetMapping
    public TiResult<PortDTO> getById(Long id) {
        return TiResult.ok(portService.getById(id));
    }

    @PreAuthorize("@perm.hasPerms('intranet:port:page')")
    @ApiOperation(value = "查询所有端口(分页)")
    @ApiOperationSupport(order = 50)
    @PostMapping("page")
    public TiResult<TiPageResult<PortDTO>> page(@RequestBody PortQuery query) {
        return TiResult.ok(portService.page(query));
    }

    @TiView(ignore = true)
    @PreAuthorize("@perm.hasPerms('intranet:port:expExcel')")
    @ApiOperation(value = "导出端口信息", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ApiOperationSupport(order = 60)
    @PostMapping("expExcel")
    public void expExcel(@RequestBody PortQuery query) throws IOException {
        portService.expExcel(query);
    }

}
