package top.ticho.intranet.server.domain.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ticho.boot.view.core.PageResult;
import com.ticho.boot.view.enums.BizErrCode;
import com.ticho.boot.view.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.ticho.intranet.server.application.service.PortService;
import top.ticho.intranet.server.domain.repository.PortRepository;
import top.ticho.intranet.server.infrastructure.entity.Port;
import top.ticho.intranet.server.interfaces.assembler.PortAssembler;
import top.ticho.intranet.server.interfaces.dto.PortDTO;
import top.ticho.intranet.server.interfaces.query.PortQuery;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 端口信息 服务实现
 *
 * @author zhajianjun
 * @date 2023-12-17 20:12
 */
@Service
public class PortServiceImpl implements PortService {

    @Autowired
    private PortRepository portRepository;

    @Override
    public void save(PortDTO portDTO) {
        Port port = PortAssembler.INSTANCE.dtoToEntity(portDTO);
        Assert.isTrue(portRepository.save(port), BizErrCode.FAIL, "保存失败");
    }

    @Override
    public void removeById(Long id) {
        Assert.isNotNull(id, "编号不能为空");
        Assert.isTrue(portRepository.removeById(id), BizErrCode.FAIL, "删除失败");
    }

    @Override
    public void updateById(PortDTO portDTO) {
        Port port = PortAssembler.INSTANCE.dtoToEntity(portDTO);
        Assert.isTrue(portRepository.updateById(port), BizErrCode.FAIL, "修改失败");
    }

    @Override
    public PortDTO getById(Long id) {
        Assert.isNotNull(id, "编号不能为空");
        Port port = portRepository.getById(id);
        return PortAssembler.INSTANCE.entityToDto(port);
    }

    @Override
    public PageResult<PortDTO> page(PortQuery query) {
        // @formatter:off
        query.checkPage();
        Page<Port> page = PageHelper.startPage(query.getPageNum(), query.getPageSize());
        portRepository.list(query);
        List<PortDTO> portDTOs = page.getResult()
            .stream()
            .map(PortAssembler.INSTANCE::entityToDto)
            .collect(Collectors.toList());
        return new PageResult<>(page.getPageNum(), page.getPageSize(), page.getTotal(), portDTOs);
        // @formatter:on
    }
}
