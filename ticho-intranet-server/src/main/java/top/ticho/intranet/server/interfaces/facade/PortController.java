package top.ticho.intranet.server.interfaces.facade;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSort;
import org.springframework.security.access.prepost.PreAuthorize;
import top.ticho.boot.view.core.PageResult;
import top.ticho.boot.view.core.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.ticho.intranet.server.application.service.PortService;
import top.ticho.intranet.server.interfaces.dto.PortDTO;
import top.ticho.intranet.server.interfaces.query.PortQuery;

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

    @Autowired
    private PortService portService;

    @PreAuthorize("@perm.hasPerms('intranet:port:save')")
    @ApiOperation(value = "保存端口信息")
    @ApiOperationSupport(order = 10)
    @PostMapping
    public Result<Void> save(@RequestBody PortDTO portDTO) {
        portService.save(portDTO);
        return Result.ok();
    }

    @PreAuthorize("@perm.hasPerms('intranet:port:remove')")
    @ApiOperation(value = "删除端口信息")
    @ApiOperationSupport(order = 20)
    @ApiImplicitParam(value = "编号", name = "id", required = true)
    @DeleteMapping
    public Result<Void> remove(Long id) {
        portService.removeById(id);
        return Result.ok();
    }

    @PreAuthorize("@perm.hasPerms('intranet:port:update')")
    @ApiOperation(value = "修改端口信息")
    @ApiOperationSupport(order = 30)
    @PutMapping
    public Result<Void> update(@RequestBody PortDTO portDTO) {
        portService.updateById(portDTO);
        return Result.ok();
    }

    @PreAuthorize("@perm.hasPerms('intranet:port:getById')")
    @ApiOperation(value = "主键查询端口信息")
    @ApiOperationSupport(order = 40)
    @ApiImplicitParam(value = "编号", name = "id", required = true)
    @GetMapping
    public Result<PortDTO> getById(Long id) {
        return Result.ok(portService.getById(id));
    }

    @PreAuthorize("@perm.hasPerms('intranet:port:page')")
    @ApiOperation(value = "分页查询端口信息")
    @ApiOperationSupport(order = 50)
    @GetMapping("page")
    public Result<PageResult<PortDTO>> page(PortQuery query) {
        return Result.ok(portService.page(query));
    }

}
