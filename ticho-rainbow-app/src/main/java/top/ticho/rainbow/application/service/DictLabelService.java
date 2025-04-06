package top.ticho.rainbow.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.ticho.rainbow.application.assembler.DictLabelAssembler;
import top.ticho.rainbow.application.dto.command.DictLabelModifyCommand;
import top.ticho.rainbow.application.dto.command.DictLabelSaveCommand;
import top.ticho.rainbow.application.dto.response.DictLabelDTO;
import top.ticho.rainbow.domain.entity.Dict;
import top.ticho.rainbow.domain.entity.DictLabel;
import top.ticho.rainbow.domain.entity.vo.DictLabelModifyVO;
import top.ticho.rainbow.domain.repository.DictLabelRepository;
import top.ticho.rainbow.domain.repository.DictRepository;
import top.ticho.rainbow.infrastructure.core.enums.YesOrNo;
import top.ticho.starter.view.enums.TiBizErrCode;
import top.ticho.starter.view.util.TiAssert;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 字典标签 服务
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
@Service
@RequiredArgsConstructor
public class DictLabelService {
    private final DictLabelRepository dictLabelRepository;
    private final DictRepository dictRepository;
    private final DictLabelAssembler dictLabelAssembler;

    public void save(DictLabelSaveCommand dictLabelSaveCommand) {
        Dict dict = dictRepository.getByCodeExcludeId(dictLabelSaveCommand.getCode(), null);
        TiAssert.isNotNull(dict, "保存失败,字典不存在");
        TiAssert.isTrue(!Objects.equals(YesOrNo.YES.code(), dict.getIsSys()), TiBizErrCode.FAIL, "保存失败，系统字典无法保存");
        DictLabel dictLabel = dictLabelAssembler.toEntity(dictLabelSaveCommand);
        DictLabel dbDictLabel = dictLabelRepository.getByCodeAndValueExcludeId(dictLabel.getCode(), dictLabel.getValue(), null);
        TiAssert.isNull(dbDictLabel, "保存失败,字典值已存在");
        TiAssert.isTrue(dictLabelRepository.save(dictLabel), TiBizErrCode.FAIL, "保存失败");
    }

    public void remove(Long id) {
        DictLabel dbDictLabel = dictLabelRepository.find(id);
        TiAssert.isNotNull(dbDictLabel, TiBizErrCode.FAIL, "删除失败，字典标签不存在");
        Dict dict = dictRepository.getByCodeExcludeId(dbDictLabel.getCode(), null);
        TiAssert.isNotNull(dict, "删除失败，字典不存在");
        TiAssert.isTrue(!Objects.equals(YesOrNo.YES.code(), dict.getIsSys()), TiBizErrCode.FAIL, "删除失败，系统字典无法删除");
        TiAssert.isTrue(dictLabelRepository.remove(id), TiBizErrCode.FAIL, "删除失败");
    }

    public void modify(DictLabelModifyCommand dictLabelModifyCommand) {
        DictLabel dictLabel = dictLabelRepository.find(dictLabelModifyCommand.getId());
        TiAssert.isNotNull(dictLabel, TiBizErrCode.FAIL, "修改失败，字典标签不存在");
        Dict dict = dictRepository.getByCodeExcludeId(dictLabel.getCode(), null);
        TiAssert.isNotNull(dict, "修改失败，字典不存在");
        boolean isSysDict = Objects.equals(YesOrNo.YES.code(), dict.getIsSys());
        if (!isSysDict) {
            DictLabel repeatDictLabel = dictLabelRepository.getByCodeAndValueExcludeId(dictLabelModifyCommand.getCode(), dictLabelModifyCommand.getValue(), dictLabelModifyCommand.getId());
            TiAssert.isNull(repeatDictLabel, "修改失败,字典值已存在");
        }
        DictLabelModifyVO dictLabelModifyVO = dictLabelAssembler.toVO(dictLabelModifyCommand);
        dictLabel.modify(dictLabelModifyVO, isSysDict);
        TiAssert.isTrue(dictLabelRepository.modify(dictLabel), TiBizErrCode.FAIL, "修改失败");
    }

    public List<DictLabelDTO> find(String code) {
        List<DictLabel> dictLabels = dictLabelRepository.getByCode(code);
        return dictLabels
            .stream()
            .map(dictLabelAssembler::toDTO)
            .collect(Collectors.toList());
    }

}

