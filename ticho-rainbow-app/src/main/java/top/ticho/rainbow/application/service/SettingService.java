package top.ticho.rainbow.application.service;

import cn.hutool.core.date.DatePattern;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.ticho.rainbow.application.assembler.SettingAssembler;
import top.ticho.rainbow.application.dto.excel.SettingExcelExport;
import top.ticho.rainbow.application.repository.SettingAppRepository;
import top.ticho.rainbow.domain.entity.Setting;
import top.ticho.rainbow.domain.entity.vo.SettingModifyVO;
import top.ticho.rainbow.domain.repository.SettingRepository;
import top.ticho.rainbow.infrastructure.common.component.excel.ExcelHandle;
import top.ticho.rainbow.interfaces.command.SettingModifyCommand;
import top.ticho.rainbow.interfaces.command.SettingSaveCommand;
import top.ticho.rainbow.interfaces.command.VersionModifyCommand;
import top.ticho.rainbow.interfaces.dto.SettingDTO;
import top.ticho.rainbow.interfaces.query.SettingQuery;
import top.ticho.starter.view.core.TiPageResult;
import top.ticho.starter.view.util.TiAssert;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * 配置信息 服务
 *
 * @author zhajianjun
 * @date 2025-06-15 16:34
 */
@RequiredArgsConstructor
@Service
public class SettingService {
    private final SettingAssembler settingAssembler;
    private final SettingRepository settingRepository;
    private final SettingAppRepository settingAppRepository;
    private final HttpServletResponse response;

    public void save(SettingSaveCommand settingSaveCommand) {
        Setting setting = settingAssembler.toEntity(settingSaveCommand);
        Setting byKey = settingRepository.findByKey(setting.getKey());
        TiAssert.isNull(byKey, "保存失败，key已存在");
        TiAssert.isTrue(settingRepository.save(setting), "保存失败");
    }

    public void remove(VersionModifyCommand command) {
        Setting setting = settingRepository.find(command.getId());
        TiAssert.isNotNull(setting, "删除失败，数据不存在");
        setting.checkVersion(command.getVersion(), "数据已被修改，请刷新后重试");
        TiAssert.isTrue(settingRepository.remove(command.getId()), "删除失败，请刷新后重试");
    }

    public void modify(SettingModifyCommand settingModifyfCommand) {
        Setting setting = settingRepository.find(settingModifyfCommand.getId());
        TiAssert.isNotNull(setting, "修改失败，数据不存在");
        setting.checkVersion(settingModifyfCommand.getVersion(), "数据已被修改，请刷新后重试");
        SettingModifyVO settingModifyfVO = settingAssembler.toModifyfVO(settingModifyfCommand);
        setting.modify(settingModifyfVO);
        TiAssert.isTrue(settingRepository.modify(setting), "修改失败，请刷新后重试");
    }

    public TiPageResult<SettingDTO> page(SettingQuery settingQuery) {
        return settingAppRepository.page(settingQuery);
    }

    public void exportExcel(SettingQuery settingQuery) throws IOException {
        String sheetName = "配置信息";
        String fileName = "配置信息导出-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern(DatePattern.PURE_DATETIME_PATTERN));
        settingQuery.setCount(false);
        ExcelHandle.writeToResponseBatch(this::excelExpHandle, settingQuery, fileName, sheetName, SettingExcelExport.class, response);
    }

    private Collection<SettingExcelExport> excelExpHandle(SettingQuery settingQuery) {
        TiPageResult<SettingDTO> page = settingAppRepository.page(settingQuery);
        return page.getRows()
            .stream()
            .map(settingAssembler::toExcelExport)
            .collect(Collectors.toList());
    }

}

