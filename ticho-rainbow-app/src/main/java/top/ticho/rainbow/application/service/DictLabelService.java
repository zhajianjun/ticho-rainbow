package top.ticho.rainbow.application.service;

import cn.hutool.core.collection.CollStreamUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.ticho.rainbow.application.assembler.DictLabelAssembler;
import top.ticho.rainbow.domain.entity.Dict;
import top.ticho.rainbow.domain.entity.DictLabel;
import top.ticho.rainbow.domain.entity.vo.DictLabelModifyVO;
import top.ticho.rainbow.domain.repository.DictLabelRepository;
import top.ticho.rainbow.domain.repository.DictRepository;
import top.ticho.rainbow.infrastructure.common.enums.YesOrNo;
import top.ticho.rainbow.interfaces.dto.command.DictLabelModifyCommand;
import top.ticho.rainbow.interfaces.dto.command.DictLabelSaveCommand;
import top.ticho.rainbow.interfaces.dto.command.VersionModifyCommand;
import top.ticho.rainbow.interfaces.dto.response.DictLabelDTO;
import top.ticho.starter.view.util.TiAssert;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
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
        TiAssert.isTrue(!Objects.equals(YesOrNo.YES.code(), dict.getIsSys()), "保存失败，系统字典无法保存");
        DictLabel dictLabel = dictLabelAssembler.toEntity(dictLabelSaveCommand);
        DictLabel dbDictLabel = dictLabelRepository.getByCodeAndValueExcludeId(dictLabel.getCode(), dictLabel.getValue(), null);
        TiAssert.isNull(dbDictLabel, "保存失败,字典值已存在");
        TiAssert.isTrue(dictLabelRepository.save(dictLabel), "保存失败");
    }

    public void remove(VersionModifyCommand command) {
        DictLabel dbDictLabel = dictLabelRepository.find(command.getId());
        TiAssert.isNotNull(dbDictLabel, "删除失败，字典标签不存在");
        dbDictLabel.checkVersion(command.getVersion(), "数据已被修改，请刷新后重试");
        Dict dict = dictRepository.getByCodeExcludeId(dbDictLabel.getCode(), null);
        TiAssert.isNotNull(dict, "删除失败，字典不存在");
        TiAssert.isTrue(!Objects.equals(YesOrNo.YES.code(), dict.getIsSys()), "删除失败，系统字典无法删除");
        TiAssert.isTrue(dictLabelRepository.remove(command.getId()), "删除失败，请刷新后重试");
    }

    public void modify(DictLabelModifyCommand dictLabelModifyCommand) {
        DictLabel dictLabel = dictLabelRepository.find(dictLabelModifyCommand.getId());
        TiAssert.isNotNull(dictLabel, "修改失败，字典标签不存在");
        dictLabel.checkVersion(dictLabelModifyCommand.getVersion(), "数据已被修改，请刷新后重试");
        Dict dict = dictRepository.getByCodeExcludeId(dictLabel.getCode(), null);
        TiAssert.isNotNull(dict, "修改失败，字典不存在");
        boolean isSysDict = Objects.equals(YesOrNo.YES.code(), dict.getIsSys());
        if (!isSysDict) {
            DictLabel repeatDictLabel = dictLabelRepository.getByCodeAndValueExcludeId(dictLabelModifyCommand.getCode(), dictLabelModifyCommand.getValue(), dictLabelModifyCommand.getId());
            TiAssert.isNull(repeatDictLabel, "修改失败,字典值已存在");
        }
        DictLabelModifyVO dictLabelModifyVO = dictLabelAssembler.toVO(dictLabelModifyCommand);
        dictLabel.modify(dictLabelModifyVO, isSysDict);
        TiAssert.isTrue(dictLabelRepository.modify(dictLabel), "修改失败，请刷新后重试");
    }

    public List<DictLabelDTO> find(String code) {
        List<DictLabel> dictLabels = dictLabelRepository.getByCode(code);
        return dictLabels
            .stream()
            .map(dictLabelAssembler::toDTO)
            .collect(Collectors.toList());
    }

    public void enable(List<VersionModifyCommand> datas) {
        boolean enable = modifyBatch(datas, DictLabel::enable);
        TiAssert.isTrue(enable, "启用失败，请刷新后重试");
    }

    public void disable(List<VersionModifyCommand> datas) {
        boolean disable = modifyBatch(datas, DictLabel::disable);
        TiAssert.isTrue(disable, "禁用失败，请刷新后重试");
    }

    private boolean modifyBatch(List<VersionModifyCommand> modifys, Consumer<DictLabel> modifyHandle) {
        List<Long> ids = CollStreamUtil.toList(modifys, VersionModifyCommand::getId);
        List<DictLabel> dictLabels = dictLabelRepository.list(ids);
        List<String> codes = dictLabels.stream().map(DictLabel::getCode).distinct().toList();
        TiAssert.isTrue(codes.size() == 1, "操作失败, 请勿操作多个字典");
        Dict dict = dictRepository.getByCodeExcludeId(codes.get(0), null);
        TiAssert.isNotNull(dict, "操作失败，字典不存在");
        TiAssert.isTrue(!Objects.equals(YesOrNo.YES.code(), dict.getIsSys()), "操作失败，系统字典无法操作");
        Map<Long, DictLabel> dictLabelMap = CollStreamUtil.toIdentityMap(dictLabels, DictLabel::getId);
        for (VersionModifyCommand modify : modifys) {
            DictLabel dictLabel = dictLabelMap.get(modify.getId());
            TiAssert.isNotNull(dictLabel, StrUtil.format("操作失败, 数据不存在, id: {}", modify.getId()));
            dictLabel.checkVersion(modify.getVersion(), StrUtil.format("数据已被修改，请刷新后重试, 字典标签: {}", dictLabel.getLabel()));
            // 修改逻辑
            modifyHandle.accept(dictLabel);
        }
        return dictLabelRepository.modifyBatch(dictLabels);
    }

}

