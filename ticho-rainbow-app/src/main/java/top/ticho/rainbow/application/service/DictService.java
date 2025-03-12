package top.ticho.rainbow.application.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.extra.spring.SpringUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import top.ticho.rainbow.application.assembler.DictAssembler;
import top.ticho.rainbow.application.assembler.DictLabelAssembler;
import top.ticho.rainbow.application.dto.command.DictModifyCommand;
import top.ticho.rainbow.application.dto.command.DictSaveCommand;
import top.ticho.rainbow.application.dto.excel.DictExp;
import top.ticho.rainbow.application.dto.query.DictLabelQuery;
import top.ticho.rainbow.application.dto.query.DictQuery;
import top.ticho.rainbow.application.dto.response.DictDTO;
import top.ticho.rainbow.application.dto.response.DictLabelDTO;
import top.ticho.rainbow.application.executor.DictExecutor;
import top.ticho.rainbow.domain.entity.Dict;
import top.ticho.rainbow.domain.entity.DictLabel;
import top.ticho.rainbow.domain.repository.DictLabelRepository;
import top.ticho.rainbow.domain.repository.DictRepository;
import top.ticho.rainbow.domain.vo.DictModifyVO;
import top.ticho.rainbow.infrastructure.core.component.excel.ExcelHandle;
import top.ticho.rainbow.infrastructure.core.constant.CacheConst;
import top.ticho.rainbow.infrastructure.core.constant.DictConst;
import top.ticho.starter.cache.component.TiCacheTemplate;
import top.ticho.starter.datasource.util.TiPageUtil;
import top.ticho.starter.view.core.TiPageResult;
import top.ticho.starter.view.enums.TiBizErrCode;
import top.ticho.starter.view.util.TiAssert;
import top.ticho.starter.web.util.TiSpringUtil;

import javax.servlet.http.HttpServletResponse;
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
    private final DictAssembler dictAssembler;    private final DictLabelAssembler dictLabelAssembler;    private final DictRepository dictRepository;    private final DictLabelRepository dictLabelRepository;    private final TiCacheTemplate tiCacheTemplate;    private final HttpServletResponse response;
    public void save(DictSaveCommand dictSaveCommand) {
        Dict dict = dictAssembler.toEntity(dictSaveCommand);
        Dict dbDict = dictRepository.getByCodeExcludeId(dictSaveCommand.getCode(), null);
        TiAssert.isNull(dbDict, TiBizErrCode.FAIL, "保存失败，字典已存在");
        TiAssert.isTrue(dictRepository.save(dict), TiBizErrCode.FAIL, "保存失败");
    }

    public void remove(Long id) {
        TiAssert.isNotEmpty(id, TiBizErrCode.PARAM_ERROR, "编号不能为空");
        Dict dbDict = dictRepository.find(id);
        TiAssert.isNotNull(dbDict, TiBizErrCode.FAIL, "删除失败，字典不存在");
        TiAssert.isTrue(!Objects.equals(dbDict.getIsSys(), 1), TiBizErrCode.PARAM_ERROR, "系统字典无法删除");
        boolean existsDict = dictLabelRepository.existsByCode(dbDict.getCode());
        TiAssert.isTrue(!existsDict, TiBizErrCode.PARAM_ERROR, "删除失败，请先删除所有字典标签");
        TiAssert.isTrue(dictRepository.remove(id), TiBizErrCode.FAIL, "删除失败");
    }

    public void modify(Long id, DictModifyCommand dictModifyCommand) {
        Dict dict = dictRepository.find(id);
        TiAssert.isNotNull(dict, TiBizErrCode.FAIL, "修改失败，字典不存在");
        DictModifyVO dictModifyVO = dictAssembler.toVO(dictModifyCommand);
        dict.modify(dictModifyVO);
        TiAssert.isTrue(dictRepository.modify(dict), TiBizErrCode.FAIL, "修改失败");
    }

    public DictDTO getById(Long id) {
        Dict dict = dictRepository.find(id);
        return dictAssembler.toDTO(dict);
    }

    public TiPageResult<DictDTO> page(DictQuery query) {
        TiPageResult<Dict> page = dictRepository.page(query);
        return TiPageUtil.of(page, dictAssembler::toDTO);
    }

    @Cacheable(value = CacheConst.COMMON, key = "'ticho-rainbow:dict:list'")
    public List<DictDTO> list() {
        DictQuery dictQuery = new DictQuery();
        dictQuery.setStatus(1);
        List<Dict> dicts = dictRepository.list(dictQuery);
        if (CollUtil.isEmpty(dicts)) {
            return Collections.emptyList();
        }
        DictLabelQuery dictLabelQuery = new DictLabelQuery();
        dictLabelQuery.setStatus(1);
        List<DictLabel> dictLabels = dictLabelRepository.list(dictLabelQuery);
        if (CollUtil.isEmpty(dictLabels)) {
            return Collections.emptyList();
        }
        Map<String, List<DictLabelDTO>> dictDtoMap = dictLabels
            .stream()
            .sorted(Comparator.comparing(DictLabel::getSort))
            .map(dictLabelAssembler::toDTO)
            .collect(Collectors.groupingBy(DictLabelDTO::getCode, LinkedHashMap::new, Collectors.toList()));
        return dicts
            .stream()
            .map(dictAssembler::toDTO)
            .peek(x -> {
                String code = x.getCode();
                List<DictLabelDTO> dictLabelDTOS = dictDtoMap.get(code);
                x.setDetails(dictLabelDTOS);
            })
            .collect(Collectors.toList());
    }

    public List<DictDTO> flush() {
        tiCacheTemplate.evict(CacheConst.COMMON, "ticho-rainbow:dict:list");
        return SpringUtil.getBean(this.getClass()).list();
    }

    public void expExcel(DictQuery query) throws IOException {
        String sheetName = "字典信息";
        String fileName = "字典信息导出-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern(DatePattern.PURE_DATETIME_PATTERN));
        DictExecutor dictExecutor = TiSpringUtil.getBean(DictExecutor.class);
        Map<Integer, String> labelMap = dictExecutor.getLabelMap(DictConst.COMMON_STATUS, NumberUtil::parseInt);
        query.setCount(false);
        ExcelHandle.writeToResponseBatch(x -> this.excelExpHandle(x, labelMap), query, fileName, sheetName, DictExp.class, response);
    }

    private Collection<DictExp> excelExpHandle(DictQuery query, Map<Integer, String> labelMap) {
        TiPageResult<Dict> page = dictRepository.page(query);
        return page.getRows()
            .stream()
            .map(x -> {
                String statusName = labelMap.get(x.getStatus());
                List<DictLabel> labels = dictLabelRepository.getByCode(x.getCode());
                return labels
                    .stream()
                    .map(dictLabelAssembler::toExport)
                    .peek(y -> y.setName(x.getName()))
                    .peek(y -> y.setStatusName(statusName))
                    .collect(Collectors.toList());
            })
            .flatMap(Collection::stream)
            .collect(Collectors.toList());
    }

}

