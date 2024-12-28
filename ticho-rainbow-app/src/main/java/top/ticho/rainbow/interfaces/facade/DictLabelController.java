package top.ticho.rainbow.interfaces.facade;

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
import top.ticho.boot.view.core.TiResult;
import top.ticho.rainbow.application.system.service.DictLabelService;
import top.ticho.rainbow.interfaces.dto.DictLabelDTO;

import java.util.List;

/**
 * 字典标签 控制器
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
@RestController
@RequestMapping("dictLabel")
@Api(tags = "字典标签")
@ApiSort(80)
public class DictLabelController {

    @Autowired
    private DictLabelService dictLabelService;

    @PreAuthorize("@perm.hasPerms('system:dictLabel:save')")
    @ApiOperation(value = "保存字典标签")
    @ApiOperationSupport(order = 10)
    @PostMapping
    public TiResult<Void> save(@RequestBody DictLabelDTO dictLabelDTO) {
        dictLabelService.save(dictLabelDTO);
        return TiResult.ok();
    }

    @PreAuthorize("@perm.hasPerms('system:dictLabel:remove')")
    @ApiOperation(value = "删除字典标签")
    @ApiOperationSupport(order = 20)
    @ApiImplicitParam(value = "编号", name = "id", required = true)
    @DeleteMapping
    public TiResult<Void> remove(Long id) {
        dictLabelService.removeById(id);
        return TiResult.ok();
    }

    @PreAuthorize("@perm.hasPerms('system:dictLabel:update')")
    @ApiOperation(value = "修改字典标签")
    @ApiOperationSupport(order = 30)
    @PutMapping
    public TiResult<Void> update(@RequestBody DictLabelDTO dictLabelDTO) {
        dictLabelService.updateById(dictLabelDTO);
        return TiResult.ok();
    }

    @PreAuthorize("@perm.hasPerms('system:dictLabel:getByCode')")
    @ApiOperation(value = "根据编码查询字典标签")
    @ApiOperationSupport(order = 40)
    @ApiImplicitParam(value = "字典编码", name = "code", required = true)
    @GetMapping
    public TiResult<List<DictLabelDTO>> getByCode(String code) {
        return TiResult.ok(dictLabelService.getByCode(code));
    }

}
