package top.ticho.intranet.server.domain.service;

import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ticho.boot.view.core.PageResult;
import com.ticho.boot.view.util.Assert;
import com.ticho.boot.web.util.CloudIdUtil;
import com.ticho.boot.web.util.valid.ValidGroup;
import com.ticho.boot.web.util.valid.ValidUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.ticho.intranet.server.application.service.PortService;
import top.ticho.intranet.server.domain.repository.ClientRepository;
import top.ticho.intranet.server.domain.repository.PortRepository;
import top.ticho.intranet.server.infrastructure.entity.Client;
import top.ticho.intranet.server.infrastructure.entity.Port;
import top.ticho.intranet.server.infrastructure.enums.ProtocolType;
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

    @Autowired
    private ClientRepository clientRepository;

    @Override
    public void save(PortDTO portDTO) {
        ValidUtil.valid(portDTO);
        portDTO.setId(null);
        check(portDTO);
        Port port = PortAssembler.INSTANCE.dtoToEntity(portDTO);
        port.setId(CloudIdUtil.getId());
        Assert.isTrue(portRepository.save(port), "保存失败");
    }

    /**
     * 通用检查
     */
    public void check(PortDTO portDTO) {
        Client client = clientRepository.getByAccessKey(portDTO.getAccessKey());
        Assert.isNotNull(client, "客户端信息不存在");
        Port dbPortByNum = portRepository.getByPortExcludeId(portDTO.getId(), portDTO.getPort());
        Assert.isNull(dbPortByNum, "端口已存在");
        String domain = portDTO.getDomain();
        boolean isHttps = ProtocolType.HTTPS.compareTo(ProtocolType.getByCode(portDTO.getType())) == 0;
        if (isHttps) {
            Assert.isNotBlank(domain, "Https域名不能为空");
        }
        if (StrUtil.isNotBlank(domain)) {
            Port dbPortByDomain = portRepository.getByDomainExcludeId(portDTO.getId(), domain);
            Assert.isNull(dbPortByDomain, "域名已存在");
        }
    }


    @Override
    public void removeById(Long id) {
        Assert.isNotNull(id, "编号不能为空");
        Assert.isTrue(portRepository.removeById(id), "删除失败");
    }

    @Override
    public void updateById(PortDTO portDTO) {
        ValidUtil.valid(portDTO, ValidGroup.Upd.class);
        check(portDTO);
        Port port = PortAssembler.INSTANCE.dtoToEntity(portDTO);
        Assert.isTrue(portRepository.updateById(port), "修改失败");
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
