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
import top.ticho.rainbow.application.dto.command.DictLabelModifyCommand;
import top.ticho.rainbow.application.dto.command.DictLabelSaveCommand;
import top.ticho.rainbow.application.dto.response.DictLabelDTO;
import top.ticho.rainbow.application.service.DictLabelService;
import top.ticho.starter.view.core.TiResult;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * 字典标签
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("dict-label")
public class DictLabelController {
    private final DictLabelService dictLabelService;

    /**
     * 保存字典标签
     */
    @PreAuthorize("@perm.hasPerms('system:dict-label:save')")
    @PostMapping
    public TiResult<Void> save(@Validated @RequestBody DictLabelSaveCommand dictLabelSaveCommand) {
        dictLabelService.save(dictLabelSaveCommand);
        return TiResult.ok();
    }

    /**
     * 删除字典标签
     *
     * @param id 编号
     */
    @PreAuthorize("@perm.hasPerms('system:dict-label:remove')")
    @DeleteMapping
    public TiResult<Void> remove(@NotNull(message = "编号不能为空") Long id) {
        dictLabelService.remove(id);
        return TiResult.ok();
    }

    /**
     * 修改字典标签
     */
    @PreAuthorize("@perm.hasPerms('system:dict-label:modify')")
    @PutMapping
    public TiResult<Void> modify(@Validated @RequestBody DictLabelModifyCommand dictLabelModifyCommand) {
        dictLabelService.modify(dictLabelModifyCommand);
        return TiResult.ok();
    }

    /**
     * 根据编码查询字典标签
     *
     * @param code 字典编码
     */
    @PreAuthorize("@perm.hasPerms('system:dict-label:find')")
    @GetMapping
    public TiResult<List<DictLabelDTO>> find(@NotBlank(message = "字典编码不能为空") String code) {
        return TiResult.ok(dictLabelService.find(code));
    }

}
