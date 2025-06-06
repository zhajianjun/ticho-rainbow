package top.ticho.rainbow.interfaces.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.ticho.rainbow.application.dto.command.DictModifyCommand;
import top.ticho.rainbow.application.dto.command.DictSaveCommand;
import top.ticho.rainbow.application.dto.query.DictQuery;
import top.ticho.rainbow.application.dto.response.DictDTO;
import top.ticho.rainbow.application.service.DictService;
import top.ticho.starter.view.core.TiPageResult;
import top.ticho.starter.view.core.TiResult;
import top.ticho.starter.web.annotation.TiView;

import jakarta.validation.constraints.NotNull;
import java.io.IOException;
import java.util.List;

/**
 * 字典
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("dict")
public class DictController {
    private final DictService dictService;

    /**
     * 保存字典
     */
    @PreAuthorize("@perm.hasPerms('system:dict:save')")
    @PostMapping
    public TiResult<Void> save(@Validated @RequestBody DictSaveCommand dictSaveCommand) {
        dictService.save(dictSaveCommand);
        return TiResult.ok();
    }

    /**
     * 删除字典
     *
     * @param id 编号
     */
    @PreAuthorize("@perm.hasPerms('system:dict:remove')")
    @DeleteMapping
    public TiResult<Void> remove(@NotNull(message = "编号不能为空") Long id) {
        dictService.remove(id);
        return TiResult.ok();
    }

    /**
     * 修改字典
     */
    @PreAuthorize("@perm.hasPerms('system:dict:modify')")
    @PutMapping
    public TiResult<Void> modify(@Validated @RequestBody DictModifyCommand dictModifyCommand) {
        dictService.modify(dictModifyCommand);
        return TiResult.ok();
    }

    /**
     * 查询字典
     *
     * @param id 编号
     */
    @PreAuthorize("@perm.hasPerms('system:dict:find')")
    @GetMapping
    public TiResult<DictDTO> find(@NotNull(message = "编号不能为空") Long id) {
        return TiResult.ok(dictService.find(id));
    }

    /**
     * 查询字典(分页)
     */
    @PreAuthorize("@perm.hasPerms('system:dict:page')")
    @GetMapping("page")
    public TiResult<TiPageResult<DictDTO>> page(@Validated DictQuery query) {
        return TiResult.ok(dictService.page(query));
    }

    /**
     * 查询所有有效字典
     */
    @PreAuthorize("@perm.hasPerms('system:dict:all')")
    @GetMapping("all")
    public TiResult<List<DictDTO>> list() {
        return TiResult.ok(dictService.list());
    }

    /**
     * 刷新所有有效字典
     */
    @PreAuthorize("@perm.hasPerms('system:dict:flush')")
    @GetMapping("flush")
    public TiResult<List<DictDTO>> flush() {
        return TiResult.ok(dictService.flush());
    }

    /**
     * 导出字典
     */
    @TiView(ignore = true)
    @PreAuthorize("@perm.hasPerms('system:dict:export')")
    @GetMapping("excel/export")
    public void exportExcel(@Validated DictQuery query) throws IOException {
        dictService.exportExcel(query);
    }

}
