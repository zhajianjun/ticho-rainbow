package top.ticho.intranet.server.domain.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.ticho.boot.view.core.PageResult;
import top.ticho.boot.view.enums.BizErrCode;
import top.ticho.boot.view.util.Assert;
import top.ticho.boot.web.util.CloudIdUtil;
import top.ticho.boot.web.util.valid.ValidGroup;
import top.ticho.boot.web.util.valid.ValidUtil;
import top.ticho.intranet.server.application.service.DictTypeService;
import top.ticho.intranet.server.domain.repository.DictRepository;
import top.ticho.intranet.server.domain.repository.DictTypeRepository;
import top.ticho.intranet.server.infrastructure.entity.DictType;
import top.ticho.intranet.server.interfaces.assembler.DictTypeAssembler;
import top.ticho.intranet.server.interfaces.dto.DictTypeDTO;
import top.ticho.intranet.server.interfaces.query.DictTypeQuery;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 字典类型 服务实现
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
@Service
public class DictTypeServiceImpl implements DictTypeService {

    @Autowired
    private DictTypeRepository dictTypeRepository;

    @Autowired
    private DictRepository dictRepository;

    @Override
    public void save(DictTypeDTO dictTypeDTO) {
        ValidUtil.valid(dictTypeDTO, ValidGroup.Add.class);
        DictType dictType = DictTypeAssembler.INSTANCE.dtoToEntity(dictTypeDTO);
        DictType dbDictType = dictTypeRepository.getByCodeExcludeId(dictType.getCode(), null);
        Assert.isNull(dbDictType, BizErrCode.FAIL, "保存失败，字典已存在");
        Integer isSys = Optional.ofNullable(dictType.getIsSys()).orElse(0);
        Integer status = Optional.ofNullable(dictType.getStatus()).orElse(1);
        dictType.setId(CloudIdUtil.getId());
        dictType.setStatus(status);
        dictType.setIsSys(isSys);
        // 系统字典默认为正常
        if (Objects.equals(dictType.getIsSys(), 1)) {
            dictType.setStatus(1);
        }
        Assert.isTrue(dictTypeRepository.save(dictType), BizErrCode.FAIL, "保存失败");
    }

    @Override
    public void removeById(Long id) {
        Assert.isNotEmpty(id, BizErrCode.PARAM_ERROR, "编号不能为空");
        DictType dbDictType = dictTypeRepository.getById(id);
        Assert.isNotNull(dbDictType, BizErrCode.FAIL, "删除失败，字典不存在");
        Assert.isTrue(!Objects.equals(dbDictType.getIsSys(), 1), BizErrCode.PARAM_ERROR, "系统字典无法删除");
        boolean existsDict = dictRepository.existsByCode(dbDictType.getCode());
        Assert.isTrue(!existsDict, BizErrCode.PARAM_ERROR, "删除失败，请先删除所有字典标签");
        Assert.isTrue(dictTypeRepository.removeById(id), BizErrCode.FAIL, "删除失败");
    }

    @Override
    public void updateById(DictTypeDTO dictTypeDTO) {
        ValidUtil.valid(dictTypeDTO, ValidGroup.Upd.class);
        DictType dbDictType = dictTypeRepository.getById(dictTypeDTO.getId());
        Assert.isNotNull(dbDictType, BizErrCode.FAIL, "修改失败，字典不存在");
        // 非系统字典修改
        if (!Objects.equals(dbDictType.getIsSys(), 1)) {
            DictType repeatDictType = dictTypeRepository.getByCodeExcludeId(dictTypeDTO.getCode(), dictTypeDTO.getId());
            Assert.isNull(repeatDictType, "修改失败，字典已存在");
            // 非系统字典修改为系统字典时
            if (Objects.equals(dictTypeDTO.getIsSys(), 1)) {
                dictTypeDTO.setStatus(1);
            }
        }
        // 系统字典修改
        else {
            // 系统字典不可修改code，并且状态默认为正常
            dictTypeDTO.setCode(null);
            dictTypeDTO.setStatus(1);
        }
        DictType dictType = DictTypeAssembler.INSTANCE.dtoToEntity(dictTypeDTO);
        Assert.isTrue(dictTypeRepository.updateById(dictType), BizErrCode.FAIL, "修改失败");
    }

    @Override
    public DictTypeDTO getById(Long id) {
        DictType dictType = dictTypeRepository.getById(id);
        return DictTypeAssembler.INSTANCE.entityToDto(dictType);
    }

    @Override
    public PageResult<DictTypeDTO> page(DictTypeQuery query) {
        // @formatter:off
        query.checkPage();
        Page<DictType> page = PageHelper.startPage(query.getPageNum(), query.getPageSize());
        dictTypeRepository.list(query);
        List<DictTypeDTO> dictTypeDTOs = page.getResult()
            .stream()
            .map(DictTypeAssembler.INSTANCE::entityToDto)
            .collect(Collectors.toList());
        return new PageResult<>(page.getPageNum(), page.getPageSize(), page.getTotal(), dictTypeDTOs);
        // @formatter:on
    }

}
