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
import top.ticho.boot.view.core.Result;
import top.ticho.intranet.server.application.service.DictService;
import top.ticho.intranet.server.interfaces.dto.DictDTO;
import top.ticho.intranet.server.interfaces.dto.DictTypeDTO;

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
@ApiSort(80)
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
    public Result<Void> remove(Long id) {
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

    @PreAuthorize("@perm.hasPerms('system:dict:getByCode')")
    @ApiOperation(value = "根据字典编码查询字典")
    @ApiOperationSupport(order = 40)
    @ApiImplicitParam(value = "字典编码", name = "code", required = true)
    @GetMapping
    public Result<List<DictDTO>> getByCode(String code) {
        return Result.ok(dictService.getByCode(code));
    }

    @PreAuthorize("@perm.hasPerms('system:dict:getAllDict')")
    @ApiOperation(value = "查询所有有效字典")
    @ApiOperationSupport(order = 60)
    @GetMapping("getAllDict")
    public Result<List<DictTypeDTO>> getAllDict() {
        return Result.ok(dictService.getAllDict());
    }

    @PreAuthorize("@perm.hasPerms('system:dict:flushAllDict')")
    @ApiOperation(value = "刷新所有有效字典")
    @ApiOperationSupport(order = 70)
    @GetMapping("flushAllDict")
    public Result<List<DictTypeDTO>> flushAllDict() {
        return Result.ok(dictService.flushAllDict());
    }

}
