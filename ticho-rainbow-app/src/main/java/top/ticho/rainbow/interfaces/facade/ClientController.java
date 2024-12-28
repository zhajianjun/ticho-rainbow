package top.ticho.rainbow.interfaces.facade;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSort;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.ticho.boot.view.core.TiPageResult;
import top.ticho.boot.view.core.TiResult;
import top.ticho.boot.web.annotation.View;
import top.ticho.rainbow.application.intranet.service.ClientService;
import top.ticho.rainbow.interfaces.dto.ClientDTO;
import top.ticho.rainbow.interfaces.query.ClientQuery;

import java.io.IOException;
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
    @ApiOperation(value = "保存客户端")
    @ApiOperationSupport(order = 10)
    @PostMapping
    public TiResult<Void> save(@RequestBody ClientDTO clientDTO) {
        clientService.save(clientDTO);
        return TiResult.ok();
    }

    @PreAuthorize("@perm.hasPerms('intranet:client:remove')")
    @ApiOperation(value = "删除客户端")
    @ApiOperationSupport(order = 20)
    @ApiImplicitParam(value = "编号", name = "id", required = true)
    @DeleteMapping
    public TiResult<Void> remove(Long id) {
        clientService.removeById(id);
        return TiResult.ok();
    }

    @PreAuthorize("@perm.hasPerms('intranet:client:update')")
    @ApiOperation(value = "修改客户端")
    @ApiOperationSupport(order = 30)
    @PutMapping
    public TiResult<Void> update(@RequestBody ClientDTO clientDTO) {
        clientService.updateById(clientDTO);
        return TiResult.ok();
    }

    @PreAuthorize("@perm.hasPerms('intranet:client:getById')")
    @ApiOperation(value = "查询客户端")
    @ApiOperationSupport(order = 40)
    @ApiImplicitParam(value = "编号", name = "id", required = true)
    @GetMapping
    public TiResult<ClientDTO> getById(Long id) {
        return TiResult.ok(clientService.getById(id));
    }

    @PreAuthorize("@perm.hasPerms('intranet:client:page')")
    @ApiOperation(value = "查询所有客户端(分页)")
    @ApiOperationSupport(order = 50)
    @PostMapping("page")
    public TiResult<TiPageResult<ClientDTO>> page(@RequestBody ClientQuery query) {
        return TiResult.ok(clientService.page(query));
    }

    @PreAuthorize("@perm.hasPerms('intranet:client:list')")
    @ApiOperation(value = "查询所有客户端")
    @ApiOperationSupport(order = 60)
    @GetMapping("list")
    public TiResult<List<ClientDTO>> list(ClientQuery query) {
        return TiResult.ok(clientService.list(query));
    }

    @View(ignore = true)
    @PreAuthorize("@perm.hasPerms('intranet:client:expExcel')")
    @ApiOperation(value = "导出客户端信息", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ApiOperationSupport(order = 70)
    @PostMapping("expExcel")
    public void expExcel(@RequestBody ClientQuery query) throws IOException {
        clientService.expExcel(query);
    }

}
