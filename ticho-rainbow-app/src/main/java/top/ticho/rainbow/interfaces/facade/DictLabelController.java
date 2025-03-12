package top.ticho.rainbow.interfaces.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
@RequestMapping("dictLabel")
public class DictLabelController {
    private final DictLabelService dictLabelService;
    /**
     * 保存字典标签
     */
    @PreAuthorize("@perm.hasPerms('system:dictLabel:save')")
    @PostMapping
    public TiResult<Void> save(@Validated @RequestBody DictLabelSaveCommand dictLabelSaveCommand) {
        dictLabelService.save(dictLabelSaveCommand);
        return TiResult.ok();
    }

    /**
     * 删除字典标签
     *
     * @param id 编号
     * @return {@link TiResult }<{@link Void }>
     */
    @PreAuthorize("@perm.hasPerms('system:dictLabel:remove')")
    @DeleteMapping("{id}")
    public TiResult<Void> remove(@PathVariable("id") Long id) {
        dictLabelService.remove(id);
        return TiResult.ok();
    }

    /**
     * 修改字典标签
     */
    @PreAuthorize("@perm.hasPerms('system:dictLabel:modify')")
    @PutMapping("{id}")
    public TiResult<Void> modify(@PathVariable("id") Long id, @Validated @RequestBody DictLabelModifyCommand dictLabelModifyCommand) {
        dictLabelService.modify(id, dictLabelModifyCommand);
        return TiResult.ok();
    }

    /**
     * 根据编码查询字典标签
     *
     * @param code 字典编码
     */
    @PreAuthorize("@perm.hasPerms('system:dictLabel:getByCode')")
    @GetMapping("{code}")
    public TiResult<List<DictLabelDTO>> getByCode(@PathVariable("code") String code) {
        return TiResult.ok(dictLabelService.getByCode(code));
    }

}
