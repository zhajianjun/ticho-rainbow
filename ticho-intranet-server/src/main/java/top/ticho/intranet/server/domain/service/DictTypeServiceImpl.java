package top.ticho.intranet.server.domain.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
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
import java.util.stream.Collectors;

/**
 * 数据字典类型 服务实现
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
        this.isRepeatDictTypePreCheck(dictTypeDTO, true);
        DictType dictType = DictTypeAssembler.INSTANCE.dtoToEntity(dictTypeDTO);
        dictType.setId(CloudIdUtil.getId());
        Assert.isTrue(dictTypeRepository.save(dictType), BizErrCode.FAIL, "保存失败");
    }

    @Override
    public void removeById(Long id) {
        Assert.isNotEmpty(id, BizErrCode.PARAM_ERROR, "编号不能为空");
        boolean existsDict = dictRepository.existsByTypeId(id);
        Assert.isTrue(!existsDict, BizErrCode.PARAM_ERROR, "删除失败，请先删除所有字典");
        Assert.isTrue(dictTypeRepository.removeById(id), BizErrCode.FAIL, "删除失败");
    }

    @Override
    public void updateById(DictTypeDTO dictTypeDTO) {
        Assert.isNotEmpty(dictTypeDTO.getId(), BizErrCode.PARAM_ERROR, "编号不能为空");
        this.isRepeatDictTypePreCheck(dictTypeDTO, false);
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

    /**
     * 数据字典是否重复预校验
     */
    private void isRepeatDictTypePreCheck(DictTypeDTO dictTypeDTO, boolean saveOrUpdate) {
        String selectCode = null;
        if (!saveOrUpdate) {
            LambdaQueryWrapper<DictType> queryWrapper = Wrappers.lambdaQuery();
            queryWrapper.select(DictType::getId, DictType::getCode);
            queryWrapper.eq(DictType::getId, dictTypeDTO.getId());
            DictType select = dictTypeRepository.getOne(queryWrapper);
            selectCode = select.getCode();
        }
        LambdaQueryWrapper<DictType> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(DictType::getCode, dictTypeDTO.getCode());
        queryWrapper.ne(selectCode != null, DictType::getCode, selectCode);
        Assert.isTrue(dictTypeRepository.count(queryWrapper) == 0, BizErrCode.FAIL, "数据字典类型重复");
    }
}
