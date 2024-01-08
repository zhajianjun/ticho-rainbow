package top.ticho.intranet.server.domain.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.ticho.boot.view.core.PageResult;
import top.ticho.boot.view.enums.BizErrCode;
import top.ticho.boot.view.exception.BizException;
import top.ticho.boot.view.util.Assert;
import top.ticho.boot.web.util.CloudIdUtil;
import top.ticho.boot.web.util.valid.ValidGroup;
import top.ticho.boot.web.util.valid.ValidUtil;
import top.ticho.intranet.server.application.service.DictService;
import top.ticho.intranet.server.domain.repository.DictRepository;
import top.ticho.intranet.server.infrastructure.core.constant.CommConst;
import top.ticho.intranet.server.infrastructure.entity.Dict;
import top.ticho.intranet.server.interfaces.assembler.DictAssembler;
import top.ticho.intranet.server.interfaces.dto.DictDTO;
import top.ticho.intranet.server.interfaces.query.DictQuery;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
        Dict dict = DictAssembler.INSTANCE.dtoToEntity(dictDTO);
        // 名称和值不能为空
        Dict parent = this.getParentAndPreCheck(dict);
        dict.setId(CloudIdUtil.getId());
        this.fillDict(dict, parent);
        Assert.isTrue(dictRepository.save(dict), BizErrCode.FAIL, "保存失败");
    }

    @Override
    public void removeById(Long id, Boolean isDelChilds) {
        Assert.isNotNull(id, BizErrCode.PARAM_ERROR, "编号不能为空");
        // 查询该节点下所有的子孙节点，不包含自己
        List<Long> descendantIds = dictRepository.getDescendantIds(id);
        // isDelChilds 为空或者 isDelChilds 是false的情况下 不可删除子节点
        if (Boolean.FALSE.equals(isDelChilds)) {
            // 不删除子节点的情况下，descendantIds一定等于0，否则有子节点，则不可删除
            Assert.isTrue(descendantIds.isEmpty(), BizErrCode.FAIL, "该字典下还有子节点,不可删除");
        }
        // 删除该节点以及其所有子节点，逻辑删除
        descendantIds.add(id);
        Assert.isTrue(dictRepository.removeById(id), BizErrCode.FAIL, "删除失败");
    }

    @Override
    public void updateById(DictDTO dictDTO) {
        // @formatter:off
        ValidUtil.valid(dictDTO, ValidGroup.Upd.class);
        Dict dict = DictAssembler.INSTANCE.dtoToEntity(dictDTO);
        // 获取兄弟节点，包括自己
        List<Dict> brothers = dictRepository.getBrothers(dict.getId());
        Dict selectDict = brothers
            .stream()
            .filter(x-> Objects.equals(dict.getId(), x.getId()))
            .findFirst()
            .orElseThrow(() -> new BizException(BizErrCode.FAIL, "数据字典不存在"));
        boolean notRepeat = brothers
            .stream()
            .filter(x-> !Objects.equals(dict.getId(), x.getId()))
            .noneMatch(brother-> Objects.equals(selectDict.getCode(), brother.getCode()));
        Assert.isTrue(notRepeat, BizErrCode.FAIL, "该数据字典编码重复");
        Assert.isTrue(dictRepository.updateById(dict), BizErrCode.FAIL, "修改失败");
        // @formatter:on
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
        List<DictDTO> dictDTOs = page.getResult()
            .stream()
            .map(DictAssembler.INSTANCE::entityToDto)
            .collect(Collectors.toList());
        return new PageResult<>(page.getPageNum(), page.getPageSize(), page.getTotal(), dictDTOs);
        // @formatter:on
    }

    private void fillDict(Dict dict, Dict parent) {
        // 父节点不存在则该节点为父节点
        if (Objects.isNull(parent)) {
            dict.setLevel(CommConst.DICT_LEVEL_INIT_ID);
            dict.setStructure(String.valueOf(dict.getId()));
            dict.setPid(CommConst.PARENT_ID);
            dict.setStatus(1);
        } else {
            dict.setLevel(parent.getLevel() + CommConst.DICT_LEVEL_INIT_ID);
            dict.setStructure(parent.getStructure() + "-" + dict.getId());
            dict.setPid(parent.getId());
            // 继承父节点的状态，可能是禁用状态
            dict.setStatus(parent.getStatus());
        }
        dict.setIsDelete(0);
    }

    /**
     * 查询父节点信息，并校验重复信息
     * <p>
     *     同类型、同个级别的字典的code不能重复
     * </p>
     *
     * @return 父节点对象
     */
    private Dict getParentAndPreCheck(Dict dict) {
        // @formatter:on
        Long typeId = dict.getTypeId();
        Long pid = Optional.ofNullable(dict.getPid()).orElse(CommConst.PARENT_ID);
        String code = dict.getCode();

        LambdaQueryWrapper<Dict> query = Wrappers.lambdaQuery();
        query.eq(Dict::getTypeId, typeId);
        query.eq(Dict::getPid, pid);
        query.eq(Dict::getCode, code);
        // 同类型、同个级别的字典的code不能重复
        Assert.isTrue(dictRepository.count(query) == 0, BizErrCode.FAIL, "该数据字典重复");

        // pid=-1则表示是根节点
        if (Objects.equals(CommConst.PARENT_ID, pid)) {
            return null;
        }
        // 如果不是根节点，则需要查下pid是否存在
        Dict parent = dictRepository.getById(pid);
        // 父节点删除情况下无法添加
        Assert.isTrue(parent != null, BizErrCode.FAIL, "该字典的父节点不存在，或已经删除");
        return parent;
    }

}
