package top.ticho.rainbow.interfaces.facade;

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
import top.ticho.rainbow.application.service.ClientService;
import top.ticho.rainbow.interfaces.dto.ClientDTO;
import top.ticho.rainbow.interfaces.query.ClientQuery;

import java.util.List;

/**
 * 客户端信息
 *
 * @author zhajianjun
 * @date 2023-12-17 20:12
 */
@RestController
@RequestMapping("client")
@Api(tags = "客户端信息")
@ApiSort(90)
public class ClientController {

    @Autowired
    private ClientService clientService;

    @PreAuthorize("@perm.hasPerms('intranet:client:save')")
    @ApiOperation(value = "保存客户端信息")
    @ApiOperationSupport(order = 10)
    @PostMapping
    public Result<Void> save(@RequestBody ClientDTO clientDTO) {
        clientService.save(clientDTO);
        return Result.ok();
    }

    @PreAuthorize("@perm.hasPerms('intranet:client:remove')")
    @ApiOperation(value = "删除客户端信息")
    @ApiOperationSupport(order = 20)
    @ApiImplicitParam(value = "编号", name = "id", required = true)
    @DeleteMapping
    public Result<Void> remove(Long id) {
        clientService.removeById(id);
        return Result.ok();
    }

    @PreAuthorize("@perm.hasPerms('intranet:client:update')")
    @ApiOperation(value = "修改客户端信息")
    @ApiOperationSupport(order = 30)
    @PutMapping
    public Result<Void> update(@RequestBody ClientDTO clientDTO) {
        clientService.updateById(clientDTO);
        return Result.ok();
    }

    @PreAuthorize("@perm.hasPerms('intranet:client:getById')")
    @ApiOperation(value = "主键查询客户端信息")
    @ApiOperationSupport(order = 40)
    @ApiImplicitParam(value = "编号", name = "id", required = true)
    @GetMapping
    public Result<ClientDTO> getById(Long id) {
        return Result.ok(clientService.getById(id));
    }

    @PreAuthorize("@perm.hasPerms('intranet:client:page')")
    @ApiOperation(value = "分页查询客户端信息")
    @ApiOperationSupport(order = 50)
    @GetMapping("page")
    public Result<PageResult<ClientDTO>> page(ClientQuery query) {
        return Result.ok(clientService.page(query));
    }

    @PreAuthorize("@perm.hasPerms('intranet:client:list')")
    @ApiOperation(value = "查询客户端信息")
    @ApiOperationSupport(order = 60)
    @GetMapping("list")
    public Result<List<ClientDTO>> list(ClientQuery query) {
        return Result.ok(clientService.list(query));
    }

}
