package top.ticho.rainbow.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.ticho.boot.view.enums.TiBizErrCode;
import top.ticho.boot.view.util.TiAssert;
import top.ticho.boot.web.util.CloudIdUtil;
import top.ticho.boot.web.util.valid.ValidGroup;
import top.ticho.boot.web.util.valid.ValidUtil;
import top.ticho.rainbow.application.system.service.DictLabelService;
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
        TiAssert.isNotNull(dict, "保存失败,字典不存在");
        TiAssert.isTrue(!Objects.equals(dict.getIsSys(), 1), TiBizErrCode.FAIL, "保存失败，系统字典无法保存");
        dictLabelDTO.setId(null);
        DictLabel dictLabel = DictLabelAssembler.INSTANCE.dtoToEntity(dictLabelDTO);
        DictLabel dbDictLabel = dictLabelRepository.getByCodeAndValueExcludeId(dictLabel.getCode(), dictLabel.getValue(), null);
        TiAssert.isNull(dbDictLabel, "保存失败,字典值已存在");
        dictLabel.setId(CloudIdUtil.getId());
        TiAssert.isTrue(dictLabelRepository.save(dictLabel), TiBizErrCode.FAIL, "保存失败");
    }

    @Override
    public void removeById(Long id) {
        TiAssert.isNotNull(id, TiBizErrCode.PARAM_ERROR, "编号不能为空");
        DictLabel dbDictLabel = dictLabelRepository.getById(id);
        TiAssert.isNotNull(dbDictLabel, TiBizErrCode.FAIL, "删除失败，字典标签不存在");
        Dict dict = dictRepository.getByCodeExcludeId(dbDictLabel.getCode(), null);
        TiAssert.isNotNull(dict, "删除失败，字典不存在");
        TiAssert.isTrue(!Objects.equals(dict.getIsSys(), 1), TiBizErrCode.FAIL, "删除失败，系统字典无法删除");
        TiAssert.isTrue(dictLabelRepository.removeById(id), TiBizErrCode.FAIL, "删除失败");
    }

    @Override
    public void updateById(DictLabelDTO dictLabelDTO) {
        ValidUtil.valid(dictLabelDTO, ValidGroup.Upd.class);
        DictLabel dbDictLabel = dictLabelRepository.getById(dictLabelDTO.getId());
        TiAssert.isNotNull(dbDictLabel, TiBizErrCode.FAIL, "修改失败，字典标签不存在");
        Dict dict = dictRepository.getByCodeExcludeId(dbDictLabel.getCode(), null);
        TiAssert.isNotNull(dict, "修改失败，字典不存在");
        if (!Objects.equals(dict.getIsSys(), 1)) {
            DictLabel repeatDictLabel = dictLabelRepository.getByCodeAndValueExcludeId(dictLabelDTO.getCode(), dictLabelDTO.getValue(), dictLabelDTO.getId());
            TiAssert.isNull(repeatDictLabel, "修改失败,字典值已存在");
        } else {
            // 系统字典不可以修改编号、标签和值
            dictLabelDTO.setCode(null);
            dictLabelDTO.setLabel(null);
            dictLabelDTO.setValue(null);
            dictLabelDTO.setStatus(1);
        }
        DictLabel dictLabel = DictLabelAssembler.INSTANCE.dtoToEntity(dictLabelDTO);
        TiAssert.isTrue(dictLabelRepository.updateById(dictLabel), TiBizErrCode.FAIL, "修改失败");
    }

    @Override
    public List<DictLabelDTO> getByCode(String code) {
        List<DictLabel> dictLabels = dictLabelRepository.getByCode(code);
        return dictLabels
            .stream()
            .map(DictLabelAssembler.INSTANCE::entityToDto)
            .collect(Collectors.toList());
    }

}
