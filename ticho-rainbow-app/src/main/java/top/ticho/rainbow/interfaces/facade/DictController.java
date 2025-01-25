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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.ticho.rainbow.application.system.service.DictService;
import top.ticho.rainbow.interfaces.dto.DictDTO;
import top.ticho.rainbow.interfaces.query.DictQuery;
import top.ticho.starter.view.core.TiPageResult;
import top.ticho.starter.view.core.TiResult;
import top.ticho.starter.web.annotation.TiView;

import javax.annotation.Resource;
import java.io.IOException;
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

    @Resource
    private DictService dictService;

    @PreAuthorize("@perm.hasPerms('system:dict:save')")
    @ApiOperation(value = "保存字典")
    @ApiOperationSupport(order = 10)
    @PostMapping
    public TiResult<Void> save(@RequestBody DictDTO dictDTO) {
        dictService.save(dictDTO);
        return TiResult.ok();
    }

    @PreAuthorize("@perm.hasPerms('system:dict:remove')")
    @ApiOperation(value = "删除字典")
    @ApiOperationSupport(order = 20)
    @ApiImplicitParam(value = "编号", name = "id", required = true)
    @DeleteMapping
    public TiResult<Void> removeById(@RequestParam("id") Long id) {
        dictService.removeById(id);
        return TiResult.ok();
    }

    @PreAuthorize("@perm.hasPerms('system:dict:update')")
    @ApiOperation(value = "修改字典")
    @ApiOperationSupport(order = 30)
    @PutMapping
    public TiResult<Void> update(@RequestBody DictDTO dictDTO) {
        dictService.updateById(dictDTO);
        return TiResult.ok();
    }

    @PreAuthorize("@perm.hasPerms('system:dict:getById')")
    @ApiOperation(value = "查询字典")
    @ApiOperationSupport(order = 40)
    @ApiImplicitParam(value = "编号", name = "id", required = true)
    @GetMapping
    public TiResult<DictDTO> getById(@RequestParam("id") Long id) {
        return TiResult.ok(dictService.getById(id));
    }

    @PreAuthorize("@perm.hasPerms('system:dict:page')")
    @ApiOperation(value = "查询所有字典(分页)")
    @ApiOperationSupport(order = 50)
    @PostMapping("page")
    public TiResult<TiPageResult<DictDTO>> page(@RequestBody DictQuery query) {
        return TiResult.ok(dictService.page(query));
    }

    @PreAuthorize("@perm.hasPerms('system:dict:list')")
    @ApiOperation(value = "查询所有有效字典")
    @ApiOperationSupport(order = 60)
    @GetMapping("list")
    public TiResult<List<DictDTO>> list() {
        return TiResult.ok(dictService.list());
    }

    @PreAuthorize("@perm.hasPerms('system:dict:flush')")
    @ApiOperation(value = "刷新所有有效字典")
    @ApiOperationSupport(order = 70)
    @GetMapping("flush")
    public TiResult<List<DictDTO>> flush() {
        return TiResult.ok(dictService.flush());
    }

    @TiView(ignore = true)
    @PreAuthorize("@perm.hasPerms('system:dict:expExcel')")
    @ApiOperation(value = "导出字典", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ApiOperationSupport(order = 100)
    @PostMapping("expExcel")
    public void expExcel(@RequestBody DictQuery query) throws IOException {
        dictService.expExcel(query);
    }

}
