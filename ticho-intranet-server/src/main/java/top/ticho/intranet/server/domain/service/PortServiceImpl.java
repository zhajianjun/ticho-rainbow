package top.ticho.intranet.server.domain.service;

import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.ticho.boot.view.core.PageResult;
import top.ticho.boot.view.util.Assert;
import top.ticho.boot.web.util.CloudIdUtil;
import top.ticho.boot.web.util.valid.ValidGroup;
import top.ticho.boot.web.util.valid.ValidUtil;
import top.ticho.intranet.core.server.entity.ClientInfo;
import top.ticho.intranet.core.server.entity.PortInfo;
import top.ticho.intranet.core.server.handler.AppHandler;
import top.ticho.intranet.core.server.handler.ServerHandler;
import top.ticho.intranet.server.application.service.PortService;
import top.ticho.intranet.server.domain.repository.ClientRepository;
import top.ticho.intranet.server.domain.repository.PortRepository;
import top.ticho.intranet.server.infrastructure.entity.Client;
import top.ticho.intranet.server.infrastructure.entity.Port;
import top.ticho.intranet.server.infrastructure.enums.ProtocolType;
import top.ticho.intranet.server.interfaces.assembler.PortAssembler;
import top.ticho.intranet.server.interfaces.dto.PortDTO;
import top.ticho.intranet.server.interfaces.query.PortQuery;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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

    @Autowired
    private ServerHandler serverHandler;

    @Override
    public void save(PortDTO portDTO) {
        ValidUtil.valid(portDTO);
        portDTO.setId(null);
        check(portDTO);
        Port port = PortAssembler.INSTANCE.dtoToEntity(portDTO);
        port.setId(CloudIdUtil.getId());
        Assert.isTrue(portRepository.save(port), "保存失败");
        savePortInfo(portDTO.getPort());
    }


    @Override
    public void removeById(Long id) {
        Assert.isNotNull(id, "编号不能为空");
        Port dbPort = portRepository.getById(id);
        Assert.isNotNull(dbPort, "删除失败，数据不存在");
        Assert.isTrue(portRepository.removeById(id), "删除失败");
        serverHandler.deleteApp(dbPort.getAccessKey(), dbPort.getPort());

    }

    @Override
    public void updateById(PortDTO portDTO) {
        ValidUtil.valid(portDTO, ValidGroup.Upd.class);
        Port dbPort = portRepository.getById(portDTO.getId());
        Assert.isNotNull(dbPort, "修改失败，数据不存在");
        // accessKey不可修改
        portDTO.setAccessKey(dbPort.getAccessKey());
        check(portDTO);
        Port port = PortAssembler.INSTANCE.dtoToEntity(portDTO);
        Assert.isTrue(portRepository.updateById(port), "修改失败");
        updatePortInfo(dbPort);
    }

    @Override
    public PortDTO getById(Long id) {
        Assert.isNotNull(id, "编号不能为空");
        Port port = portRepository.getById(id);
        PortDTO portDTO = PortAssembler.INSTANCE.entityToDto(port);
        fillChannelStatus(portDTO);
        return portDTO;
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
            .peek(this::fillChannelStatus)
            .collect(Collectors.toList());
        return new PageResult<>(page.getPageNum(), page.getPageSize(), page.getTotal(), portDTOs);
        // @formatter:on
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

    public void savePortInfo(Integer portNum) {
        if (Objects.isNull(portNum)) {
            return;
        }
        Port port = portRepository.getByPortExcludeId(null, portNum);
        if (Objects.isNull(port) || !isEnabled(port)) {
            return;
        }
        PortInfo portInfo = PortAssembler.INSTANCE.entityToInfo(port);
        serverHandler.createApp(portInfo);
    }

    public void updatePortInfo(Port oldPort) {
        Port port = portRepository.getById(oldPort.getId());
        // 端口号不一致时，删除旧的app
        if (!Objects.equals(oldPort.getPort(), port.getPort())) {
            serverHandler.deleteApp(oldPort.getAccessKey(), oldPort.getPort());
        }
        AppHandler appHandler = serverHandler.getAppHandler();
        // 端口信息有效时才重新创建
        if (isEnabled(port) || !appHandler.exists(port.getPort())) {
            PortInfo portInfo = PortAssembler.INSTANCE.entityToInfo(port);
            serverHandler.createApp(portInfo);
        }

    }

    private boolean isEnabled(Port port) {
        boolean enabled = Objects.equals(port.getEnabled(), 1);
        boolean isForeaver = Objects.equals(port.getForever(), 1);
        boolean isNotExpire = Objects.nonNull(port.getExpireAt()) && LocalDateTime.now().isBefore(port.getExpireAt());
        return enabled && (isForeaver || isNotExpire);
    }

    private void fillChannelStatus(PortDTO portDTO) {
        if (Objects.isNull(portDTO)) {
            return;
        }
        ClientInfo clientInfo = serverHandler.getClientByAccessKey(portDTO.getAccessKey());
        boolean clientActived = Optional.ofNullable(clientInfo).map(x-> Objects.nonNull(x.getChannel())).orElse(false);
        AppHandler appHandler = serverHandler.getAppHandler();
        Integer channelStatus = clientActived && appHandler.exists(portDTO.getPort()) ? 1 : 0;
        portDTO.setChannelStatus(channelStatus);
    }

}
