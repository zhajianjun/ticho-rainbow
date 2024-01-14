package top.ticho.intranet.server.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.ticho.boot.view.enums.BizErrCode;
import top.ticho.boot.view.util.Assert;
import top.ticho.boot.web.util.CloudIdUtil;
import top.ticho.boot.web.util.valid.ValidGroup;
import top.ticho.boot.web.util.valid.ValidUtil;
import top.ticho.intranet.server.application.service.DictService;
import top.ticho.intranet.server.domain.repository.DictRepository;
import top.ticho.intranet.server.infrastructure.entity.Dict;
import top.ticho.intranet.server.interfaces.assembler.DictAssembler;
import top.ticho.intranet.server.interfaces.dto.DictDTO;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 数据字典 服务实现
 *
 * @author zhajianjun
 * @date 2024-01-08 20:30
 */
@Service
public class DictServiceImpl implements DictService {

    @Autowired
    private DictRepository dictRepository;

    @Override
    public void save(DictDTO dictDTO) {
        ValidUtil.valid(dictDTO, ValidGroup.Add.class);
        dictDTO.setId(null);
        Dict dict = DictAssembler.INSTANCE.dtoToEntity(dictDTO);
        Dict dbDict = dictRepository.getByCodeAndValueExcludeId(dict.getCode(), dict.getValue(), null);
        Assert.isNull(dbDict, "保存失败,字典值已存在");
        dict.setId(CloudIdUtil.getId());
        Assert.isTrue(dictRepository.save(dict), BizErrCode.FAIL, "保存失败");
    }

    @Override
    public void removeById(Long id, Boolean isDelChilds) {
        Assert.isNotNull(id, BizErrCode.PARAM_ERROR, "编号不能为空");
        Assert.isTrue(dictRepository.removeById(id), BizErrCode.FAIL, "删除失败");
    }

    @Override
    public void updateById(DictDTO dictDTO) {
        // @formatter:off
        ValidUtil.valid(dictDTO, ValidGroup.Upd.class);
        Dict dict = DictAssembler.INSTANCE.dtoToEntity(dictDTO);
        Dict repeatDict = dictRepository.getByCodeAndValueExcludeId(dict.getCode(), dict.getValue(), dict.getId());
        Assert.isNull(repeatDict, "修改失败,字典值已存在");
        // 获取兄弟节点，包括自己
        Assert.isTrue(dictRepository.updateById(dict), BizErrCode.FAIL, "修改失败");
        // @formatter:on
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

}
