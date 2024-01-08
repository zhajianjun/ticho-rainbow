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
import org.springframework.web.bind.annotation.RestController;
import top.ticho.boot.view.core.PageResult;
import top.ticho.boot.view.core.Result;
import top.ticho.intranet.server.application.service.DictService;
import top.ticho.intranet.server.interfaces.dto.DictDTO;
import top.ticho.intranet.server.interfaces.query.DictQuery;

/**
 * 数据字典 控制器
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
@RestController
@RequestMapping("dict")
@Api(tags = "数据字典")
@ApiSort(80)
public class DictController {

    @Autowired
    private DictService dictService;

    @PreAuthorize("@perm.hasPerms('upms:dict:save')")
    @ApiOperation(value = "保存数据字典")
    @ApiOperationSupport(order = 10)
    @PostMapping
    public Result<Void> save(@RequestBody DictDTO dictDTO) {
        dictService.save(dictDTO);
        return Result.ok();
    }

    @PreAuthorize("@perm.hasPerms('upms:dict:remove')")
    @ApiOperation(value = "删除数据字典")
    @ApiOperationSupport(order = 20)
    @ApiImplicitParam(value = "编号", name = "id", required = true)
    @DeleteMapping
    public Result<Void> remove(Long id, Boolean isDelChilds) {
        dictService.removeById(id, isDelChilds);
        return Result.ok();
    }

    @PreAuthorize("@perm.hasPerms('upms:dict:update')")
    @ApiOperation(value = "修改数据字典")
    @ApiOperationSupport(order = 30)
    @PutMapping
    public Result<Void> update(@RequestBody DictDTO dictDTO) {
        dictService.updateById(dictDTO);
        return Result.ok();
    }

    @PreAuthorize("@perm.hasPerms('upms:dict:get')")
    @ApiOperation(value = "主键查询数据字典")
    @ApiOperationSupport(order = 40)
    @ApiImplicitParam(value = "编号", name = "id", required = true)
    @GetMapping
    public Result<DictDTO> get(Long id) {
        return Result.ok(dictService.getById(id));
    }

    @PreAuthorize("@perm.hasPerms('upms:dict:page')")
    @ApiOperation(value = "分页查询数据字典")
    @ApiOperationSupport(order = 50)
    @GetMapping("page")
    public Result<PageResult<DictDTO>> page(DictQuery query) {
        return Result.ok(dictService.page(query));
    }

}
