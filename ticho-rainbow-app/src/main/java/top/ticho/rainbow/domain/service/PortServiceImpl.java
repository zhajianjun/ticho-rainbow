package top.ticho.rainbow.domain.service;

import cn.hutool.core.date.DatePattern;
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
import top.ticho.rainbow.application.service.PortService;
import top.ticho.rainbow.domain.handle.DictTemplate;
import top.ticho.rainbow.domain.repository.ClientRepository;
import top.ticho.rainbow.domain.repository.PortRepository;
import top.ticho.rainbow.infrastructure.core.component.excel.ExcelHandle;
import top.ticho.rainbow.infrastructure.core.constant.DictConst;
import top.ticho.rainbow.infrastructure.core.enums.ProtocolType;
import top.ticho.rainbow.infrastructure.entity.Client;
import top.ticho.rainbow.infrastructure.entity.Port;
import top.ticho.rainbow.interfaces.assembler.PortAssembler;
import top.ticho.rainbow.interfaces.dto.PortDTO;
import top.ticho.rainbow.interfaces.excel.PortExp;
import top.ticho.rainbow.interfaces.query.PortQuery;
import top.ticho.tool.intranet.server.entity.ClientInfo;
import top.ticho.tool.intranet.server.entity.PortInfo;
import top.ticho.tool.intranet.server.handler.AppHandler;
import top.ticho.tool.intranet.server.handler.ServerHandler;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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

    @Autowired
    private DictTemplate dictTemplate;

    @Resource
    private HttpServletResponse response;

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

    @Override
    public void expExcel(PortQuery query) throws IOException {
        String sheetName = "端口信息";
        String fileName = "端口信息导出-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern(DatePattern.PURE_DATETIME_PATTERN));
        Map<String, String> labelMap = dictTemplate.getLabelMapBatch(DictConst.COMMON_STATUS, DictConst.CHANNEL_STATUS, DictConst.HTTP_TYPE);
        ExcelHandle.writeToResponseBatch(x-> this.excelExpHandle(x, labelMap), query, fileName, sheetName, PortExp.class, response);
    }

    private Collection<PortExp> excelExpHandle(PortQuery query, Map<String, String> labelMap) {
        query.checkPage();
        Page<Port> page = PageHelper.startPage(query.getPageNum(), query.getPageSize(), false);
        portRepository.list(query);
        List<Port> result = page.getResult();
        List<String> accessKeys = result.stream().map(Port::getAccessKey).collect(Collectors.toList());
        List<Client> clients = clientRepository.getByAccessKeys(accessKeys);
        Map<String, String> clientNameMap = clients.stream().collect(Collectors.toMap(Client::getAccessKey, Client::getName, (v1, v2) -> v1));
        return result
            .stream()
            .map(x-> {
                PortExp portExp = PortAssembler.INSTANCE.entityToExp(x);
                ClientInfo clientInfo = serverHandler.getClientByAccessKey(x.getAccessKey());
                int clientChannelStatus = Objects.nonNull(clientInfo) && Objects.nonNull(clientInfo.getChannel()) ? 1 : 0;
                AppHandler appHandler = serverHandler.getAppHandler();
                int channelStatus = appHandler.exists(x.getPort()) ? 1 : 0;
                portExp.setClientName(clientNameMap.get(x.getAccessKey()));
                portExp.setStatusName(labelMap.get(DictConst.COMMON_STATUS + x.getStatus()));
                portExp.setTypeName(labelMap.get(DictConst.HTTP_TYPE + x.getType()));
                portExp.setClientChannelStatusName(labelMap.get(DictConst.CHANNEL_STATUS + clientChannelStatus));
                portExp.setAppChannelStatusName(labelMap.get(DictConst.CHANNEL_STATUS + channelStatus));
                return portExp;
            })
            .collect(Collectors.toList());
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
        // 端口号或者客户端地址不一致时，删除旧的app，再创建新的app
        if ((!Objects.equals(oldPort.getPort(), port.getPort()) || !Objects.equals(oldPort.getEndpoint(), port.getEndpoint())) && isEnabled(oldPort)) {
            serverHandler.deleteApp(oldPort.getAccessKey(), oldPort.getPort());
        }
        AppHandler appHandler = serverHandler.getAppHandler();
        // 端口信息有效时才重新创建
        if (isEnabled(port) && !appHandler.exists(port.getPort())) {
            PortInfo portInfo = PortAssembler.INSTANCE.entityToInfo(port);
            serverHandler.createApp(portInfo);
        }
        if (!isEnabled(port) && appHandler.exists(port.getPort())) {
            serverHandler.deleteApp(port.getAccessKey(), port.getPort());
        }

    }

    private boolean isEnabled(Port port) {
        boolean enabled = Objects.equals(port.getStatus(), 1);
        boolean isNotExpire = Objects.nonNull(port.getExpireAt()) && LocalDateTime.now().isBefore(port.getExpireAt());
        return enabled && isNotExpire;
    }

    private void fillChannelStatus(PortDTO portDTO) {
        if (Objects.isNull(portDTO)) {
            return;
        }
        ClientInfo clientInfo = serverHandler.getClientByAccessKey(portDTO.getAccessKey());
        Integer clientChannelStatus = Objects.nonNull(clientInfo) && Objects.nonNull(clientInfo.getChannel()) ? 1 : 0;
        AppHandler appHandler = serverHandler.getAppHandler();
        Integer channelStatus = appHandler.exists(portDTO.getPort()) ? 1 : 0;
        portDTO.setClientChannelStatus(clientChannelStatus);
        portDTO.setAppChannelStatus(channelStatus);
    }

}
