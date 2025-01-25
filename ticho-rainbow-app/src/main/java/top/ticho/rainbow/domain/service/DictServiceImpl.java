package top.ticho.rainbow.domain.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import top.ticho.rainbow.application.system.service.DictService;
import top.ticho.rainbow.domain.handle.DictHandle;
import top.ticho.rainbow.domain.repository.DictLabelRepository;
import top.ticho.rainbow.domain.repository.DictRepository;
import top.ticho.rainbow.infrastructure.core.component.excel.ExcelHandle;
import top.ticho.rainbow.infrastructure.core.constant.CacheConst;
import top.ticho.rainbow.infrastructure.core.constant.DictConst;
import top.ticho.rainbow.infrastructure.entity.Dict;
import top.ticho.rainbow.infrastructure.entity.DictLabel;
import top.ticho.rainbow.interfaces.assembler.DictAssembler;
import top.ticho.rainbow.interfaces.assembler.DictLabelAssembler;
import top.ticho.rainbow.interfaces.dto.DictDTO;
import top.ticho.rainbow.interfaces.dto.DictLabelDTO;
import top.ticho.rainbow.interfaces.excel.DictExp;
import top.ticho.rainbow.interfaces.query.DictLabelQuery;
import top.ticho.rainbow.interfaces.query.DictQuery;
import top.ticho.starter.cache.component.TiCacheTemplate;
import top.ticho.starter.view.core.TiPageResult;
import top.ticho.starter.view.enums.TiBizErrCode;
import top.ticho.starter.view.util.TiAssert;
import top.ticho.starter.web.util.TiIdUtil;
import top.ticho.starter.web.util.TiSpringUtil;
import top.ticho.starter.web.util.valid.TiValidGroup;
import top.ticho.starter.web.util.valid.TiValidUtil;

import javax.annotation.Resource;
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
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 字典 服务实现
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
@Service
public class DictServiceImpl implements DictService {

    @Resource
    private DictRepository dictRepository;

    @Resource
    private DictLabelRepository dictLabelRepository;

    @Resource
    private TiCacheTemplate tiCacheTemplate;

    @Resource
    private HttpServletResponse response;


    @Override
    public void save(DictDTO dictDTO) {
        TiValidUtil.valid(dictDTO, TiValidGroup.Add.class);
        Dict dict = DictAssembler.INSTANCE.dtoToEntity(dictDTO);
        Dict dbDict = dictRepository.getByCodeExcludeId(dict.getCode(), null);
        TiAssert.isNull(dbDict, TiBizErrCode.FAIL, "保存失败，字典已存在");
        Integer isSys = Optional.ofNullable(dict.getIsSys()).orElse(0);
        Integer status = Optional.ofNullable(dict.getStatus()).orElse(1);
        dict.setId(TiIdUtil.getId());
        dict.setStatus(status);
        dict.setIsSys(isSys);
        // 系统字典默认为正常
        if (Objects.equals(dict.getIsSys(), 1)) {
            dict.setStatus(1);
        }
        TiAssert.isTrue(dictRepository.save(dict), TiBizErrCode.FAIL, "保存失败");
    }

    @Override
    public void removeById(Long id) {
        TiAssert.isNotEmpty(id, TiBizErrCode.PARAM_ERROR, "编号不能为空");
        Dict dbDict = dictRepository.getById(id);
        TiAssert.isNotNull(dbDict, TiBizErrCode.FAIL, "删除失败，字典不存在");
        TiAssert.isTrue(!Objects.equals(dbDict.getIsSys(), 1), TiBizErrCode.PARAM_ERROR, "系统字典无法删除");
        boolean existsDict = dictLabelRepository.existsByCode(dbDict.getCode());
        TiAssert.isTrue(!existsDict, TiBizErrCode.PARAM_ERROR, "删除失败，请先删除所有字典标签");
        TiAssert.isTrue(dictRepository.removeById(id), TiBizErrCode.FAIL, "删除失败");
    }

    @Override
    public void updateById(DictDTO dictDTO) {
        TiValidUtil.valid(dictDTO, TiValidGroup.Upd.class);
        Dict dbDict = dictRepository.getById(dictDTO.getId());
        TiAssert.isNotNull(dbDict, TiBizErrCode.FAIL, "修改失败，字典不存在");
        // 非系统字典修改
        if (!Objects.equals(dbDict.getIsSys(), 1)) {
            Dict repeatDict = dictRepository.getByCodeExcludeId(dictDTO.getCode(), dictDTO.getId());
            TiAssert.isNull(repeatDict, "修改失败，字典已存在");
            // 非系统字典修改为系统字典时
            if (Objects.equals(dictDTO.getIsSys(), 1)) {
                dictDTO.setStatus(1);
            }
        }
        // 系统字典状态默认为正常
        else {
            dictDTO.setStatus(1);
        }
        // 不可修改code
        dictDTO.setCode(null);
        Dict dict = DictAssembler.INSTANCE.dtoToEntity(dictDTO);
        TiAssert.isTrue(dictRepository.updateById(dict), TiBizErrCode.FAIL, "修改失败");
    }

    @Override
    public DictDTO getById(Long id) {
        Dict dict = dictRepository.getById(id);
        return DictAssembler.INSTANCE.entityToDto(dict);
    }

    @Override
    public TiPageResult<DictDTO> page(DictQuery query) {
        query.checkPage();
        Page<Dict> page = PageHelper.startPage(query.getPageNum(), query.getPageSize());
        dictRepository.list(query);
        List<DictDTO> dictDTOS = page.getResult()
            .stream()
            .map(DictAssembler.INSTANCE::entityToDto)
            .collect(Collectors.toList());
        return new TiPageResult<>(page.getPageNum(), page.getPageSize(), page.getTotal(), dictDTOS);
    }

    @Override
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
            .map(DictLabelAssembler.INSTANCE::entityToDto)
            .collect(Collectors.groupingBy(DictLabelDTO::getCode, LinkedHashMap::new, Collectors.toList()));
        return dicts
            .stream()
            .map(DictAssembler.INSTANCE::entityToDto)
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

    @Override
    public void expExcel(DictQuery query) throws IOException {
        String sheetName = "字典信息";
        String fileName = "字典信息导出-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern(DatePattern.PURE_DATETIME_PATTERN));
        DictHandle dictHandle = TiSpringUtil.getBean(DictHandle.class);
        Map<Integer, String> labelMap = dictHandle.getLabelMap(DictConst.COMMON_STATUS, NumberUtil::parseInt);
        ExcelHandle.writeToResponseBatch(x -> this.excelExpHandle(x, labelMap), query, fileName, sheetName, DictExp.class, response);
    }

    private Collection<DictExp> excelExpHandle(DictQuery query, Map<Integer, String> labelMap) {
        query.checkPage();
        Page<Dict> page = PageHelper.startPage(query.getPageNum(), query.getPageSize(), false);
        dictRepository.list(query);
        return page.getResult()
            .stream()
            .map(x -> {
                String statusName = labelMap.get(x.getStatus());
                List<DictLabel> labels = dictLabelRepository.getByCode(x.getCode());
                return labels
                    .stream()
                    .map(DictLabelAssembler.INSTANCE::entityToExp)
                    .peek(y -> y.setName(x.getName()))
                    .peek(y -> y.setStatusName(statusName))
                    .collect(Collectors.toList());
            })
            .flatMap(Collection::stream)
            .collect(Collectors.toList());
    }

}
