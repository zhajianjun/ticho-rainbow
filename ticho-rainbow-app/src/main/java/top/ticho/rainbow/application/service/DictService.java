package top.ticho.rainbow.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import top.ticho.rainbow.application.assembler.DictAssembler;
import top.ticho.rainbow.application.assembler.DictLabelAssembler;
import top.ticho.rainbow.application.dto.excel.DictExcelExport;
import top.ticho.rainbow.application.executor.DictExecutor;
import top.ticho.rainbow.application.repository.DictAppRepository;
import top.ticho.rainbow.domain.entity.Dict;
import top.ticho.rainbow.domain.entity.DictLabel;
import top.ticho.rainbow.domain.entity.vo.DictModifyVO;
import top.ticho.rainbow.domain.repository.DictLabelRepository;
import top.ticho.rainbow.domain.repository.DictRepository;
import top.ticho.rainbow.infrastructure.common.component.excel.ExcelHandle;
import top.ticho.rainbow.infrastructure.common.constant.CacheConst;
import top.ticho.rainbow.infrastructure.common.constant.DateConst;
import top.ticho.rainbow.infrastructure.common.constant.DictConst;
import top.ticho.rainbow.infrastructure.common.enums.YesOrNo;
import top.ticho.rainbow.interfaces.command.DictModifyCommand;
import top.ticho.rainbow.interfaces.command.DictSaveCommand;
import top.ticho.rainbow.interfaces.command.VersionModifyCommand;
import top.ticho.rainbow.interfaces.dto.DictCacheDTO;
import top.ticho.rainbow.interfaces.dto.DictDTO;
import top.ticho.rainbow.interfaces.dto.DictLabelDTO;
import top.ticho.rainbow.interfaces.query.DictQuery;
import top.ticho.starter.cache.component.TiCacheTemplate;
import top.ticho.starter.view.core.TiPageResult;
import top.ticho.starter.view.enums.TiBizErrorCode;
import top.ticho.starter.view.util.TiAssert;
import top.ticho.starter.web.util.TiSpringUtil;
import top.ticho.tool.core.TiCollUtil;
import top.ticho.tool.core.TiNumberUtil;
import top.ticho.tool.core.TiStrUtil;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 字典 服务
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
@RequiredArgsConstructor
@Service
public class DictService {
    private final DictAssembler dictAssembler;
    private final DictLabelAssembler dictLabelAssembler;
    private final DictRepository dictRepository;
    private final DictAppRepository dictAppRepository;
    private final DictLabelRepository dictLabelRepository;
    private final TiCacheTemplate tiCacheTemplate;
    private final HttpServletResponse response;

    public void save(DictSaveCommand dictSaveCommand) {
        Dict dict = dictAssembler.toEntity(dictSaveCommand);
        Dict dbDict = dictRepository.getByCodeExcludeId(dictSaveCommand.getCode(), null);
        TiAssert.isNull(dbDict, "保存失败，字典已存在");
        TiAssert.isTrue(dictRepository.save(dict), "保存失败");
    }

    public void remove(VersionModifyCommand command) {
        Dict dict = dictRepository.find(command.getId());
        TiAssert.isNotNull(dict, "删除失败，字典不存在");
        dict.checkVersion(command.getVersion(), "数据已被修改，请刷新后重试");
        TiAssert.isTrue(!Objects.equals(dict.getIsSys(), YesOrNo.YES.code()), TiBizErrorCode.PARAM_ERROR, "系统字典无法删除");
        boolean existsDict = dictLabelRepository.existsByCode(dict.getCode());
        TiAssert.isTrue(!existsDict, TiBizErrorCode.PARAM_ERROR, "删除失败，请先删除所有字典标签");
        TiAssert.isTrue(dictRepository.remove(command.getId()), "删除失败，请刷新后重试");
    }

    public void modify(DictModifyCommand dictModifyCommand) {
        Dict dict = dictRepository.find(dictModifyCommand.getId());
        TiAssert.isNotNull(dict, "修改失败，字典不存在");
        dict.checkVersion(dictModifyCommand.getVersion(), "数据已被修改，请刷新后重试");
        DictModifyVO dictModifyVO = dictAssembler.toVO(dictModifyCommand);
        dict.modify(dictModifyVO);
        TiAssert.isTrue(dictRepository.modify(dict), "修改失败，请刷新后重试");
    }

    public TiPageResult<DictDTO> page(DictQuery query) {
        return dictAppRepository.page(query);
    }

    @Cacheable(value = CacheConst.COMMON, key = "'ticho-rainbow:dict:list'")
    public List<DictCacheDTO> list() {
        List<Dict> dicts = dictRepository.listEnable();
        if (TiCollUtil.isEmpty(dicts)) {
            return Collections.emptyList();
        }
        List<DictLabel> dictLabels = dictLabelRepository.listEnable();
        if (TiCollUtil.isEmpty(dictLabels)) {
            return Collections.emptyList();
        }
        Map<String, List<DictLabelDTO>> dictDTOMap = dictLabels
            .stream()
            .sorted(Comparator.comparing(DictLabel::getSort))
            .map(dictLabelAssembler::toDTO)
            .collect(Collectors.groupingBy(DictLabelDTO::getCode, LinkedHashMap::new, Collectors.toList()));
        return dicts
            .stream()
            .map(dictAssembler::toDTO)
            .peek(x -> {
                String code = x.getCode();
                List<DictLabelDTO> dictLabelDTOS = dictDTOMap.get(code);
                x.setDetails(dictLabelDTOS);
            })
            .collect(Collectors.toList());
    }

    public List<DictCacheDTO> flush() {
        tiCacheTemplate.evict(CacheConst.COMMON, "ticho-rainbow:dict:list");
        return TiSpringUtil.getBean(this.getClass()).list();
    }

    public void exportExcel(DictQuery query) throws IOException {
        String sheetName = "字典信息";
        String fileName = "字典信息导出-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern(DateConst.PURE_DATETIME_PATTERN));
        DictExecutor dictExecutor = TiSpringUtil.getBean(DictExecutor.class);
        Map<Integer, String> labelMap = dictExecutor.getLabelMap(DictConst.COMMON_STATUS, TiNumberUtil::parseInt);
        query.setCount(false);
        ExcelHandle.writeToResponseBatch(x -> this.excelExpHandle(x, labelMap), query, fileName, sheetName, DictExcelExport.class, response);
    }

    private Collection<DictExcelExport> excelExpHandle(DictQuery query, Map<Integer, String> labelMap) {
        TiPageResult<DictDTO> page = dictAppRepository.page(query);
        return page.getRows()
            .stream()
            .map(x -> {
                String statusName = labelMap.get(x.getStatus());
                List<DictLabel> labels = dictLabelRepository.getByCode(x.getCode());
                return labels
                    .stream()
                    .map(dictLabelAssembler::toExcelExportort)
                    .peek(y -> y.setName(x.getName()))
                    .peek(y -> y.setStatusName(statusName))
                    .collect(Collectors.toList());
            })
            .flatMap(Collection::stream)
            .collect(Collectors.toList());
    }

    public void enable(List<VersionModifyCommand> datas) {
        boolean enable = modifyBatch(datas, Dict::enable);
        TiAssert.isTrue(enable, "启用失败，请刷新后重试");
    }

    public void disable(List<VersionModifyCommand> datas) {
        boolean disable = modifyBatch(datas, Dict::disable);
        TiAssert.isTrue(disable, "禁用失败，请刷新后重试");
    }

    private boolean modifyBatch(List<VersionModifyCommand> modifys, Consumer<Dict> modifyHandle) {
        List<Long> ids = modifys.stream().map(VersionModifyCommand::getId).collect(Collectors.toList());
        List<Dict> dicts = dictRepository.list(ids);
        Map<Long, Dict> dictMap = dicts.stream().collect(Collectors.toMap(Dict::getId, Function.identity(), (o, n) -> o));
        for (VersionModifyCommand modify : modifys) {
            Dict dict = dictMap.get(modify.getId());
            TiAssert.isNotNull(dict, TiStrUtil.format("操作失败, 数据不存在, id: {}", modify.getId()));
            dict.checkVersion(modify.getVersion(), TiStrUtil.format("数据已被修改，请刷新后重试, 字典: {}", dict.getName()));
            // 修改逻辑
            modifyHandle.accept(dict);
        }
        return dictRepository.modifyBatch(dicts);
    }

}

