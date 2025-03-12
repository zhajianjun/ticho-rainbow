package top.ticho.rainbow.application.service;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.ticho.rainbow.application.assembler.PortAssembler;
import top.ticho.rainbow.application.dto.PortDTO;
import top.ticho.rainbow.application.dto.excel.PortExp;
import top.ticho.rainbow.application.dto.query.PortQuery;
import top.ticho.rainbow.application.executor.DictExecutor;
import top.ticho.rainbow.domain.entity.Client;
import top.ticho.rainbow.domain.entity.Port;
import top.ticho.rainbow.domain.repository.ClientRepository;
import top.ticho.rainbow.domain.repository.PortRepository;
import top.ticho.rainbow.infrastructure.core.component.excel.ExcelHandle;
import top.ticho.rainbow.infrastructure.core.constant.DictConst;
import top.ticho.rainbow.infrastructure.core.enums.ProtocolType;
import top.ticho.starter.view.core.TiPageResult;
import top.ticho.starter.view.util.TiAssert;
import top.ticho.starter.web.util.valid.TiValidGroup;
import top.ticho.starter.web.util.valid.TiValidUtil;
import top.ticho.tool.intranet.server.entity.ClientInfo;
import top.ticho.tool.intranet.server.entity.PortInfo;
import top.ticho.tool.intranet.server.handler.AppHandler;
import top.ticho.tool.intranet.server.handler.ServerHandler;

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
@RequiredArgsConstructor
@Service
public class PortService {

    private final PortRepository portRepository;    private final PortAssembler portAssembler;    private final ClientRepository clientRepository;    private final ServerHandler serverHandler;    private final DictExecutor dictExecutor;    private final HttpServletResponse response;
    public void save(PortDTO portDTO) {
        TiValidUtil.valid(portDTO);
        portDTO.setId(null);
        check(portDTO);
        Port port = portAssembler.toEntity(portDTO);
        TiAssert.isTrue(portRepository.save(port), "保存失败");
        savePortInfo(portDTO.getPort());
    }

    public void remove(Long id) {
        TiAssert.isNotNull(id, "编号不能为空");
        Port dbPort = portRepository.find(id);
        TiAssert.isNotNull(dbPort, "删除失败，数据不存在");
        TiAssert.isTrue(portRepository.remove(id), "删除失败");
        serverHandler.deleteApp(dbPort.getAccessKey(), dbPort.getPort());

    }

    public void modify(PortDTO portDTO) {
        TiValidUtil.valid(portDTO, TiValidGroup.Upd.class);
        Port dbPort = portRepository.find(portDTO.getId());
        TiAssert.isNotNull(dbPort, "修改失败，数据不存在");
        // accessKey不可修改
        portDTO.setAccessKey(dbPort.getAccessKey());
        check(portDTO);
        Port port = portAssembler.toEntity(portDTO);
        TiAssert.isTrue(portRepository.modify(port), "修改失败");
        updatePortInfo(dbPort);
    }

    public PortDTO getById(Long id) {
        TiAssert.isNotNull(id, "编号不能为空");
        Port port = portRepository.find(id);
        PortDTO portDTO = portAssembler.toDTO(port);
        fillChannelStatus(portDTO);
        return portDTO;
    }

    public TiPageResult<PortDTO> page(PortQuery query) {
        query.checkPage();
        Page<Port> page = PageHelper.startPage(query.getPageNum(), query.getPageSize());
        portRepository.list(query);
        List<PortDTO> portDTOs = page.getResult()
            .stream()
            .map(portAssembler::toDTO)
            .peek(this::fillChannelStatus)
            .collect(Collectors.toList());
        return new TiPageResult<>(page.getPageNum(), page.getPageSize(), page.getTotal(), portDTOs);
    }

    public void expExcel(PortQuery query) throws IOException {
        String sheetName = "端口信息";
        String fileName = "端口信息导出-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern(DatePattern.PURE_DATETIME_PATTERN));
        Map<String, String> labelMap = dictExecutor.getLabelMapBatch(DictConst.COMMON_STATUS, DictConst.CHANNEL_STATUS, DictConst.HTTP_TYPE);
        ExcelHandle.writeToResponseBatch(x -> this.excelExpHandle(x, labelMap), query, fileName, sheetName, PortExp.class, response);
    }

    private Collection<PortExp> excelExpHandle(PortQuery query, Map<String, String> labelMap) {
        query.checkPage();
        Page<Port> page = PageHelper.startPage(query.getPageNum(), query.getPageSize(), false);
        portRepository.list(query);
        List<Port> result = page.getResult();
        List<String> accessKeys = result.stream().map(Port::getAccessKey).collect(Collectors.toList());
        List<Client> clients = clientRepository.listByAccessKeys(accessKeys);
        Map<String, String> clientNameMap = clients.stream().collect(Collectors.toMap(Client::getAccessKey, Client::getName, (v1, v2) -> v1));
        return result
            .stream()
            .map(x -> {
                PortExp portExp = portAssembler.toExp(x);
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
        Client client = clientRepository.findByAccessKey(portDTO.getAccessKey());
        TiAssert.isNotNull(client, "客户端信息不存在");
        Port dbPortByNum = portRepository.getByPortExcludeId(portDTO.getId(), portDTO.getPort());
        TiAssert.isNull(dbPortByNum, "端口已存在");
        String domain = portDTO.getDomain();
        boolean isHttps = ProtocolType.HTTPS.compareTo(ProtocolType.getByCode(portDTO.getType())) == 0;
        if (isHttps) {
            TiAssert.isNotBlank(domain, "Https域名不能为空");
            TiAssert.isTrue(ReUtil.isMatch("^([a-z0-9-]+\\.)+[a-z]{2,}(/\\S*)?$", domain), "域名格式不正确");
        }
        if (StrUtil.isNotBlank(domain)) {
            Port dbPortByDomain = portRepository.getByDomainExcludeId(portDTO.getId(), domain);
            TiAssert.isNull(dbPortByDomain, "域名已存在");
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
        PortInfo portInfo = portAssembler.toInfo(port);
        serverHandler.createApp(portInfo);
    }

    public void updatePortInfo(Port oldPort) {
        Port port = portRepository.find(oldPort.getId());
        // 端口号或者客户端地址不一致时，删除旧的app，再创建新的app
        if ((!Objects.equals(oldPort.getPort(), port.getPort()) || !Objects.equals(oldPort.getEndpoint(), port.getEndpoint())) && isEnabled(oldPort)) {
            serverHandler.deleteApp(oldPort.getAccessKey(), oldPort.getPort());
        }
        AppHandler appHandler = serverHandler.getAppHandler();
        // 端口信息有效时才重新创建
        if (isEnabled(port) && !appHandler.exists(port.getPort())) {
            PortInfo portInfo = portAssembler.toInfo(port);
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
