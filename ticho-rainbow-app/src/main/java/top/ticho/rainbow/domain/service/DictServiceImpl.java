package top.ticho.rainbow.domain.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import top.ticho.boot.view.core.PageResult;
import top.ticho.boot.view.enums.BizErrCode;
import top.ticho.boot.view.util.Assert;
import top.ticho.boot.web.util.CloudIdUtil;
import top.ticho.boot.web.util.valid.ValidGroup;
import top.ticho.boot.web.util.valid.ValidUtil;
import top.ticho.rainbow.application.service.DictService;
import top.ticho.rainbow.domain.repository.DictLabelRepository;
import top.ticho.rainbow.domain.repository.DictRepository;
import top.ticho.rainbow.infrastructure.core.constant.CacheConst;
import top.ticho.rainbow.infrastructure.entity.Dict;
import top.ticho.rainbow.infrastructure.entity.DictLabel;
import top.ticho.rainbow.interfaces.assembler.DictAssembler;
import top.ticho.rainbow.interfaces.assembler.DictLabelAssembler;
import top.ticho.rainbow.interfaces.dto.DictDTO;
import top.ticho.rainbow.interfaces.dto.DictLabelDTO;
import top.ticho.rainbow.interfaces.query.DictLabelQuery;
import top.ticho.rainbow.interfaces.query.DictQuery;

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

    @Autowired
    private DictRepository dictRepository;

    @Autowired
    private DictLabelRepository dictLabelRepository;

    @Autowired
    private CacheManager cacheManager;

    @Override
    public void save(DictDTO dictDTO) {
        ValidUtil.valid(dictDTO, ValidGroup.Add.class);
        Dict dict = DictAssembler.INSTANCE.dtoToEntity(dictDTO);
        Dict dbDict = dictRepository.getByCodeExcludeId(dict.getCode(), null);
        Assert.isNull(dbDict, BizErrCode.FAIL, "保存失败，字典已存在");
        Integer isSys = Optional.ofNullable(dict.getIsSys()).orElse(0);
        Integer status = Optional.ofNullable(dict.getStatus()).orElse(1);
        dict.setId(CloudIdUtil.getId());
        dict.setStatus(status);
        dict.setIsSys(isSys);
        // 系统字典默认为正常
        if (Objects.equals(dict.getIsSys(), 1)) {
            dict.setStatus(1);
        }
        Assert.isTrue(dictRepository.save(dict), BizErrCode.FAIL, "保存失败");
    }

    @Override
    public void removeById(Long id) {
        Assert.isNotEmpty(id, BizErrCode.PARAM_ERROR, "编号不能为空");
        Dict dbDict = dictRepository.getById(id);
        Assert.isNotNull(dbDict, BizErrCode.FAIL, "删除失败，字典不存在");
        Assert.isTrue(!Objects.equals(dbDict.getIsSys(), 1), BizErrCode.PARAM_ERROR, "系统字典无法删除");
        boolean existsDict = dictLabelRepository.existsByCode(dbDict.getCode());
        Assert.isTrue(!existsDict, BizErrCode.PARAM_ERROR, "删除失败，请先删除所有字典标签");
        Assert.isTrue(dictRepository.removeById(id), BizErrCode.FAIL, "删除失败");
    }

    @Override
    public void updateById(DictDTO dictDTO) {
        ValidUtil.valid(dictDTO, ValidGroup.Upd.class);
        Dict dbDict = dictRepository.getById(dictDTO.getId());
        Assert.isNotNull(dbDict, BizErrCode.FAIL, "修改失败，字典不存在");
        // 非系统字典修改
        if (!Objects.equals(dbDict.getIsSys(), 1)) {
            Dict repeatDict = dictRepository.getByCodeExcludeId(dictDTO.getCode(), dictDTO.getId());
            Assert.isNull(repeatDict, "修改失败，字典已存在");
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
        Assert.isTrue(dictRepository.updateById(dict), BizErrCode.FAIL, "修改失败");
    }

    @Override
    public DictDTO getById(Long id) {
        Dict dict = dictRepository.getById(id);
        return DictAssembler.INSTANCE.entityToDto(dict);
    }

    @Override
    public PageResult<DictDTO> page(DictQuery query) {
        // @formatter:off
        query.checkPage();
        Page<Dict> page = PageHelper.startPage(query.getPageNum(), query.getPageSize());
        dictRepository.list(query);
        List<DictDTO> dictDTOS = page.getResult()
            .stream()
            .map(DictAssembler.INSTANCE::entityToDto)
            .collect(Collectors.toList());
        return new PageResult<>(page.getPageNum(), page.getPageSize(), page.getTotal(), dictDTOS);
        // @formatter:on
    }

    @Override
    @Cacheable(value = CacheConst.COMMON, key = "'ticho-rainbow:dict:list'")
    public List<DictDTO> list() {
        // @formatter:off
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
            .peek(x-> {
                String code = x.getCode();
                List<DictLabelDTO> dictLabelDTOS = dictDtoMap.get(code);
                x.setDetails(dictLabelDTOS);
            })
            .collect(Collectors.toList());
        // @formatter:on
    }

    public List<DictDTO> flush() {
        Cache cache = cacheManager.getCache(CacheConst.COMMON);
        if (Objects.nonNull(cache)) {
            cache.evict("ticho-rainbow:dict:list");
        }
        DictServiceImpl bean = SpringUtil.getBean(this.getClass());
        return bean.list();
    }

}
