package top.ticho.intranet.server.interfaces.facade;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSort;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.ticho.boot.view.core.PageResult;
import top.ticho.boot.view.core.Result;
import top.ticho.intranet.server.application.service.DictService;
import top.ticho.intranet.server.interfaces.dto.DictDTO;
import top.ticho.intranet.server.interfaces.query.DictQuery;

import java.util.List;

/**
 * 字典 控制器
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
@RestController
@RequestMapping("dict")
@Api(tags = "字典")
@ApiSort(70)
public class DictController {

    @Autowired
    private DictService dictService;

    @PreAuthorize("@perm.hasPerms('system:dict:save')")
    @ApiOperation(value = "保存字典")
    @ApiOperationSupport(order = 10)
    @PostMapping
    public Result<Void> save(@RequestBody DictDTO dictDTO) {
        dictService.save(dictDTO);
        return Result.ok();
    }

    @PreAuthorize("@perm.hasPerms('system:dict:remove')")
    @ApiOperation(value = "删除字典")
    @ApiOperationSupport(order = 20)
    @ApiImplicitParam(value = "编号", name = "id", required = true)
    @DeleteMapping
    public Result<Void> removeById(@RequestParam("id") Long id) {
        dictService.removeById(id);
        return Result.ok();
    }

    @PreAuthorize("@perm.hasPerms('system:dict:update')")
    @ApiOperation(value = "修改字典")
    @ApiOperationSupport(order = 30)
    @PutMapping
    public Result<Void> update(@RequestBody DictDTO dictDTO) {
        dictService.updateById(dictDTO);
        return Result.ok();
    }

    @PreAuthorize("@perm.hasPerms('system:dict:get')")
    @ApiOperation(value = "主键查询字典")
    @ApiOperationSupport(order = 40)
    @ApiImplicitParam(value = "编号", name = "id", required = true)
    @GetMapping
    public Result<DictDTO> getById(@RequestParam("id") Long id) {
        return Result.ok(dictService.getById(id));
    }

    @PreAuthorize("@perm.hasPerms('system:dict:page')")
    @ApiOperation(value = "分页查询字典")
    @ApiOperationSupport(order = 50)
    @GetMapping("page")
    public Result<PageResult<DictDTO>> page(DictQuery query) {
        return Result.ok(dictService.page(query));
    }

    @PreAuthorize("@perm.hasPerms('system:dict:list')")
    @ApiOperation(value = "查询所有有效字典")
    @ApiOperationSupport(order = 60)
    @GetMapping("list")
    public Result<List<DictDTO>> list() {
        return Result.ok(dictService.list());
    }

    @PreAuthorize("@perm.hasPerms('system:dict:flush')")
    @ApiOperation(value = "刷新所有有效字典")
    @ApiOperationSupport(order = 70)
    @GetMapping("flush")
    public Result<List<DictDTO>> flush() {
        return Result.ok(dictService.flush());
    }

}
