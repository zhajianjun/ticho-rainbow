package top.ticho.rainbow.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.ticho.boot.view.enums.BizErrCode;
import top.ticho.boot.view.util.Assert;
import top.ticho.boot.web.util.CloudIdUtil;
import top.ticho.boot.web.util.valid.ValidGroup;
import top.ticho.boot.web.util.valid.ValidUtil;
import top.ticho.rainbow.application.service.DictLabelService;
import top.ticho.rainbow.domain.repository.DictLabelRepository;
import top.ticho.rainbow.domain.repository.DictRepository;
import top.ticho.rainbow.infrastructure.entity.Dict;
import top.ticho.rainbow.infrastructure.entity.DictLabel;
import top.ticho.rainbow.interfaces.assembler.DictLabelAssembler;
import top.ticho.rainbow.interfaces.dto.DictLabelDTO;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 字典标签 服务实现
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
@Service
public class DictLabelServiceImpl implements DictLabelService {

    @Autowired
    private DictLabelRepository dictLabelRepository;

    @Autowired
    private DictRepository dictRepository;

    @Override
    public void save(DictLabelDTO dictLabelDTO) {
        ValidUtil.valid(dictLabelDTO, ValidGroup.Add.class);
        // 字典查询
        Dict dict = dictRepository.getByCodeExcludeId(dictLabelDTO.getCode(), null);
        Assert.isNotNull(dict, "保存失败,字典不存在");
        Assert.isTrue(!Objects.equals(dict.getIsSys(), 1), BizErrCode.FAIL, "保存失败，系统字典无法保存");
        dictLabelDTO.setId(null);
        DictLabel dictLabel = DictLabelAssembler.INSTANCE.dtoToEntity(dictLabelDTO);
        DictLabel dbDictLabel = dictLabelRepository.getByCodeAndValueExcludeId(dictLabel.getCode(), dictLabel.getValue(), null);
        Assert.isNull(dbDictLabel, "保存失败,字典值已存在");
        dictLabel.setId(CloudIdUtil.getId());
        Assert.isTrue(dictLabelRepository.save(dictLabel), BizErrCode.FAIL, "保存失败");
    }

    @Override
    public void removeById(Long id) {
        Assert.isNotNull(id, BizErrCode.PARAM_ERROR, "编号不能为空");
        DictLabel dbDictLabel = dictLabelRepository.getById(id);
        Assert.isNotNull(dbDictLabel, BizErrCode.FAIL, "删除失败，字典标签不存在");
        Dict dict = dictRepository.getByCodeExcludeId(dbDictLabel.getCode(), null);
        Assert.isNotNull(dict, "删除失败，字典不存在");
        Assert.isTrue(!Objects.equals(dict.getIsSys(), 1), BizErrCode.FAIL, "删除失败，系统字典无法删除");
        Assert.isTrue(dictLabelRepository.removeById(id), BizErrCode.FAIL, "删除失败");
    }

    @Override
    public void updateById(DictLabelDTO dictLabelDTO) {
        ValidUtil.valid(dictLabelDTO, ValidGroup.Upd.class);
        DictLabel dbDictLabel = dictLabelRepository.getById(dictLabelDTO.getId());
        Assert.isNotNull(dbDictLabel, BizErrCode.FAIL, "修改失败，字典标签不存在");
        Dict dict = dictRepository.getByCodeExcludeId(dbDictLabel.getCode(), null);
        Assert.isNotNull(dict, "修改失败，字典不存在");
        if (!Objects.equals(dict.getIsSys(), 1)) {
            DictLabel repeatDictLabel = dictLabelRepository.getByCodeAndValueExcludeId(dictLabelDTO.getCode(), dictLabelDTO.getValue(), dictLabelDTO.getId());
            Assert.isNull(repeatDictLabel, "修改失败,字典值已存在");
        } else {
            // 系统字典不可以修改编号、标签和值
            dictLabelDTO.setCode(null);
            dictLabelDTO.setLabel(null);
            dictLabelDTO.setValue(null);
            dictLabelDTO.setStatus(1);
        }
        DictLabel dictLabel = DictLabelAssembler.INSTANCE.dtoToEntity(dictLabelDTO);
        Assert.isTrue(dictLabelRepository.updateById(dictLabel), BizErrCode.FAIL, "修改失败");
    }

    @Override
    public List<DictLabelDTO> getByCode(String code) {
        // @formatter:off
        List<DictLabel> dictLabels = dictLabelRepository.getByCode(code);
        return dictLabels
            .stream()
            .map(DictLabelAssembler.INSTANCE::entityToDto)
            .collect(Collectors.toList());
        // @formatter:on
    }

}
