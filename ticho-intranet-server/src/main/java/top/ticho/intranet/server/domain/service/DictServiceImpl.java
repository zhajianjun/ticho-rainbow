package top.ticho.intranet.server.domain.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.extra.spring.SpringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import top.ticho.boot.view.enums.BizErrCode;
import top.ticho.boot.view.util.Assert;
import top.ticho.boot.web.util.CloudIdUtil;
import top.ticho.boot.web.util.valid.ValidGroup;
import top.ticho.boot.web.util.valid.ValidUtil;
import top.ticho.intranet.server.application.service.DictService;
import top.ticho.intranet.server.domain.repository.DictRepository;
import top.ticho.intranet.server.domain.repository.DictTypeRepository;
import top.ticho.intranet.server.infrastructure.core.constant.CacheConst;
import top.ticho.intranet.server.infrastructure.entity.Dict;
import top.ticho.intranet.server.infrastructure.entity.DictType;
import top.ticho.intranet.server.interfaces.assembler.DictAssembler;
import top.ticho.intranet.server.interfaces.assembler.DictTypeAssembler;
import top.ticho.intranet.server.interfaces.dto.DictDTO;
import top.ticho.intranet.server.interfaces.dto.DictTypeDTO;
import top.ticho.intranet.server.interfaces.query.DictQuery;
import top.ticho.intranet.server.interfaces.query.DictTypeQuery;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
    private DictTypeRepository dictTypeRepository;

    @Autowired
    private CacheManager cacheManager;

    @Override
    public void save(DictDTO dictDTO) {
        ValidUtil.valid(dictDTO, ValidGroup.Add.class);
        // 字典查询
        DictType dictType = dictTypeRepository.getByCodeExcludeId(dictDTO.getCode(), null);
        Assert.isNotNull(dictType, "保存失败,字典不存在");
        Assert.isTrue(!Objects.equals(dictType.getIsSys(), 1), BizErrCode.FAIL, "保存失败，系统字典无法保存");
        dictDTO.setId(null);
        Dict dict = DictAssembler.INSTANCE.dtoToEntity(dictDTO);
        Dict dbDict = dictRepository.getByCodeAndValueExcludeId(dict.getCode(), dict.getValue(), null);
        Assert.isNull(dbDict, "保存失败,字典值已存在");
        dict.setId(CloudIdUtil.getId());
        Assert.isTrue(dictRepository.save(dict), BizErrCode.FAIL, "保存失败");
    }

    @Override
    public void removeById(Long id) {
        Assert.isNotNull(id, BizErrCode.PARAM_ERROR, "编号不能为空");
        Dict dbDict = dictRepository.getById(id);
        Assert.isNotNull(dbDict, BizErrCode.FAIL, "删除失败，字典标签不存在");
        DictType dictType = dictTypeRepository.getByCodeExcludeId(dbDict.getCode(), null);
        Assert.isNotNull(dictType, "删除失败，字典不存在");
        Assert.isTrue(!Objects.equals(dictType.getIsSys(), 1), BizErrCode.FAIL, "删除失败，系统字典无法删除");
        Assert.isTrue(dictRepository.removeById(id), BizErrCode.FAIL, "删除失败");
    }

    @Override
    public void updateById(DictDTO dictDTO) {
        ValidUtil.valid(dictDTO, ValidGroup.Upd.class);
        Dict dbDict = dictRepository.getById(dictDTO.getId());
        Assert.isNotNull(dbDict, BizErrCode.FAIL, "修改失败，字典标签不存在");
        DictType dictType = dictTypeRepository.getByCodeExcludeId(dbDict.getCode(), null);
        Assert.isNotNull(dictType, "修改失败，字典不存在");
        if (!Objects.equals(dictType.getIsSys(), 1)) {
            Dict repeatDict = dictRepository.getByCodeAndValueExcludeId(dictDTO.getCode(), dictDTO.getValue(), dictDTO.getId());
            Assert.isNull(repeatDict, "修改失败,字典值已存在");
        } else {
            // 系统字典不可以修改编号、标签和值
            dictDTO.setCode(null);
            dictDTO.setLabel(null);
            dictDTO.setValue(null);
            dictDTO.setStatus(1);
        }
        Dict dict = DictAssembler.INSTANCE.dtoToEntity(dictDTO);
        Assert.isTrue(dictRepository.updateById(dict), BizErrCode.FAIL, "修改失败");
    }

    @Override
    public List<DictDTO> getByCode(String code) {
        // @formatter:off
        List<Dict> dicts = dictRepository.getByCode(code);
        return dicts
            .stream()
            .map(DictAssembler.INSTANCE::entityToDto)
            .collect(Collectors.toList());
        // @formatter:on
    }

    @Override
    @Cacheable(value = CacheConst.COMMON, key = "'ticho-intranet:dict:list'")
    public List<DictTypeDTO> getAllDict() {
        // @formatter:off
        DictTypeQuery dictTypeQuery = new DictTypeQuery();
        dictTypeQuery.setStatus(1);
        List<DictType> dictTypes = dictTypeRepository.list(dictTypeQuery);
        if (CollUtil.isEmpty(dictTypes)) {
            return Collections.emptyList();
        }
        DictQuery dictQuery = new DictQuery();
        dictQuery.setStatus(1);
        List<Dict> dicts = dictRepository.list(dictQuery);
        if (CollUtil.isEmpty(dicts)) {
            return Collections.emptyList();
        }
        Map<String, List<DictDTO>> dictDtoMap = dicts
            .stream()
            .sorted(Comparator.comparing(Dict::getSort))
            .map(DictAssembler.INSTANCE::entityToDto).collect(Collectors.groupingBy(DictDTO::getCode, LinkedHashMap::new, Collectors.toList()));
        return dictTypes
            .stream()
            .map(DictTypeAssembler.INSTANCE::entityToDto)
            .peek(x-> {
                String code = x.getCode();
                List<DictDTO> dictDTOS = dictDtoMap.get(code);
                x.setDetails(dictDTOS);
            })
            .collect(Collectors.toList());
        // @formatter:on
    }

    public List<DictTypeDTO> flushAllDict() {
        Cache cache = cacheManager.getCache(CacheConst.COMMON);
        if (Objects.nonNull(cache)) {
            cache.evict("ticho-intranet:dict:list");
        }
        DictServiceImpl bean = SpringUtil.getBean(this.getClass());
        return bean.getAllDict();
    }

}
