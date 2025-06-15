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
import top.ticho.rainbow.application.service.SettingService;
import top.ticho.rainbow.infrastructure.common.annotation.ApiLog;
import top.ticho.rainbow.infrastructure.common.constant.ApiConst;
import top.ticho.rainbow.interfaces.command.SettingModifyCommand;
import top.ticho.rainbow.interfaces.command.SettingSaveCommand;
import top.ticho.rainbow.interfaces.command.VersionModifyCommand;
import top.ticho.rainbow.interfaces.dto.SettingDTO;
import top.ticho.rainbow.interfaces.query.SettingQuery;
import top.ticho.starter.view.core.TiPageResult;
import top.ticho.starter.view.core.TiResult;
import top.ticho.starter.web.annotation.TiView;

import java.io.IOException;

/**
 * 配置信息
 *
 * @author zhajianjun
 * @date 2025-06-15 16:34
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("setting")
public class SettingController {
    private final SettingService settingService;

    /**
     * 保存配置信息
     */
    @ApiLog("保存配置信息")
    @PreAuthorize("@perm.hasPerms('" + ApiConst.SYSTEM_SETTING_SAVE + "')")
    @PostMapping
    public TiResult<Void> save(@Validated @RequestBody SettingSaveCommand settingSaveCommand) {
        settingService.save(settingSaveCommand);
        return TiResult.ok();
    }

    /**
     * 删除配置信息
     */
    @ApiLog("删除配置信息")
    @PreAuthorize("@perm.hasPerms('" + ApiConst.SYSTEM_SETTING_REMOVE + "')")
    @DeleteMapping
    public TiResult<Void> remove(@Validated @RequestBody VersionModifyCommand command) {
        settingService.remove(command);
        return TiResult.ok();
    }

    /**
     * 修改配置信息
     */
    @ApiLog("修改配置信息")
    @PreAuthorize("@perm.hasPerms('" + ApiConst.SYSTEM_SETTING_MODIFY + "')")
    @PutMapping
    public TiResult<Void> modify(@Validated @RequestBody SettingModifyCommand settingModifyfCommand) {
        settingService.modify(settingModifyfCommand);
        return TiResult.ok();
    }

    /**
     * 查询配置信息(分页)
     */
    @PreAuthorize("@perm.hasPerms('" + ApiConst.SYSTEM_SETTING_PAGE + "')")
    @GetMapping("page")
    public TiResult<TiPageResult<SettingDTO>> page(@Validated SettingQuery settingQuery) {
        return TiResult.ok(settingService.page(settingQuery));
    }

    /**
     * 导出配置信息信息
     */
    @ApiLog("导出配置信息信息")
    @TiView(ignore = true)
    @PreAuthorize("@perm.hasPerms('" + ApiConst.SYSTEM_SETTING_EXPORT + "')")
    @GetMapping("excel/export")
    public void exportExcel(@Validated SettingQuery settingQuery) throws IOException {
        settingService.exportExcel(settingQuery);
    }

}
