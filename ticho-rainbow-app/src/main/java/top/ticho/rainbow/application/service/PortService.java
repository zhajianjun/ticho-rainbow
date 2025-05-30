package top.ticho.rainbow.application.service;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.ticho.intranet.server.core.ServerHandler;
import top.ticho.intranet.server.entity.ClientInfo;
import top.ticho.rainbow.application.assembler.PortAssembler;
import top.ticho.rainbow.application.dto.command.PortModifyfCommand;
import top.ticho.rainbow.application.dto.command.PortSaveCommand;
import top.ticho.rainbow.application.dto.excel.PortExcelExport;
import top.ticho.rainbow.application.dto.query.PortQuery;
import top.ticho.rainbow.application.dto.response.PortDTO;
import top.ticho.rainbow.application.executor.DictExecutor;
import top.ticho.rainbow.application.repository.PortAppRepository;
import top.ticho.rainbow.domain.entity.Client;
import top.ticho.rainbow.domain.entity.Port;
import top.ticho.rainbow.domain.entity.vo.PortModifyfVO;
import top.ticho.rainbow.domain.repository.ClientRepository;
import top.ticho.rainbow.domain.repository.PortRepository;
import top.ticho.rainbow.infrastructure.common.component.excel.ExcelHandle;
import top.ticho.rainbow.infrastructure.common.constant.DictConst;
import top.ticho.rainbow.infrastructure.common.enums.ProtocolType;
import top.ticho.starter.view.core.TiPageResult;
import top.ticho.starter.view.util.TiAssert;
import top.ticho.starter.web.util.valid.TiValidUtil;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
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

    private final PortRepository portRepository;
    private final PortAppRepository portAppRepository;
    private final PortAssembler portAssembler;
    private final ClientRepository clientRepository;
    private final ServerHandler serverHandler;
    private final DictExecutor dictExecutor;
    private final HttpServletResponse response;

    public void save(PortSaveCommand portSaveCommand) {
        TiValidUtil.valid(portSaveCommand);
        check(null, portSaveCommand.getAccessKey(), portSaveCommand.getPort(), portSaveCommand.getDomain(), portSaveCommand.getType());
        Port port = portAssembler.toEntity(portSaveCommand);
        TiAssert.isTrue(portRepository.save(port), "保存失败");
        savePortInfo(portSaveCommand.getPort());
    }

    public void remove(Long id) {
        Port dbPort = portRepository.find(id);
        TiAssert.isNotNull(dbPort, "删除失败，数据不存在");
        TiAssert.isTrue(portRepository.remove(id), "删除失败");
        serverHandler.unbind(dbPort.getAccessKey(), dbPort.getPort());

    }

    public void modify(PortModifyfCommand portModifyfCommand) {
        TiValidUtil.valid(portModifyfCommand);
        Port port = portRepository.find(portModifyfCommand.getId());
        TiAssert.isNotNull(port, "修改失败，数据不存在");
        // accessKey不可修改
        portModifyfCommand.setAccessKey(port.getAccessKey());
        check(portModifyfCommand.getId(), portModifyfCommand.getAccessKey(), portModifyfCommand.getPort(), portModifyfCommand.getDomain(), portModifyfCommand.getType());
        PortModifyfVO portModifyfVO = portAssembler.toModifyfVO(portModifyfCommand);
        port.modify(portModifyfVO);
        TiAssert.isTrue(portRepository.modify(port), "修改失败，请刷新后重试");
        updatePortInfo(port);
    }

    public PortDTO find(Long id) {
        Port port = portRepository.find(id);
        PortDTO portDTO = portAssembler.toDTO(port);
        fillChannelStatus(portDTO);
        return portDTO;
    }

    public TiPageResult<PortDTO> page(PortQuery query) {
        TiPageResult<PortDTO> page = portAppRepository.page(query);
        page.getRows().forEach(this::fillChannelStatus);
        return page;
    }

    public void exportExcel(PortQuery query) throws IOException {
        String sheetName = "端口信息";
        String fileName = "端口信息导出-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern(DatePattern.PURE_DATETIME_PATTERN));
        Map<String, String> labelMap = dictExecutor.getLabelMapBatch(DictConst.COMMON_STATUS, DictConst.CHANNEL_STATUS, DictConst.HTTP_TYPE);
        query.setCount(false);
        ExcelHandle.writeToResponseBatch(x -> this.excelExpHandle(x, labelMap), query, fileName, sheetName, PortExcelExport.class, response);
    }

    private Collection<PortExcelExport> excelExpHandle(PortQuery query, Map<String, String> labelMap) {
        TiPageResult<PortDTO> page = portAppRepository.page(query);
        List<PortDTO> result = page.getRows();
        List<String> accessKeys = result.stream().map(PortDTO::getAccessKey).collect(Collectors.toList());
        List<Client> clients = clientRepository.listByAccessKeys(accessKeys);
        Map<String, String> clientNameMap = clients.stream().collect(Collectors.toMap(Client::getAccessKey, Client::getName, (v1, v2) -> v1));
        return result
            .stream()
            .map(item -> {
                fillChannelStatus(item);
                PortExcelExport portExcelExport = portAssembler.toExcelExport(item);
                portExcelExport.setClientName(clientNameMap.get(item.getAccessKey()));
                portExcelExport.setStatusName(labelMap.get(DictConst.COMMON_STATUS + item.getStatus()));
                portExcelExport.setTypeName(labelMap.get(DictConst.HTTP_TYPE + item.getType()));
                portExcelExport.setClientChannelStatusName(labelMap.get(DictConst.CHANNEL_STATUS + item.getClientChannelStatus()));
                portExcelExport.setAppChannelStatusName(labelMap.get(DictConst.CHANNEL_STATUS + item.getAppChannelStatus()));
                return portExcelExport;
            })
            .collect(Collectors.toList());
    }

    /**
     * 通用检查
     */
    public void check(Long id, String accessKey, Integer port, String domain, Integer type) {
        Client client = clientRepository.findByAccessKey(accessKey);
        TiAssert.isNotNull(client, "客户端信息不存在");
        Port dbPortByNum = portRepository.getByPortExcludeId(id, port);
        TiAssert.isNull(dbPortByNum, "端口已存在");
        boolean isHttps = ProtocolType.HTTPS.compareTo(ProtocolType.getByCode(type)) == 0;
        if (isHttps) {
            TiAssert.isNotBlank(domain, "Https域名不能为空");
            TiAssert.isTrue(ReUtil.isMatch("^([a-z0-9-]+\\.)+[a-z]{2,}(/\\S*)?$", domain), "域名格式不正确");
        }
        if (StrUtil.isNotBlank(domain)) {
            Port dbPortByDomain = portRepository.getByDomainExcludeId(id, domain);
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
        serverHandler.bind(port.getAccessKey(), port.getPort(), port.getEndpoint());
    }

    public void updatePortInfo(Port oldPort) {
        Port port = portRepository.find(oldPort.getId());
        // 端口号或者客户端地址不一致时，删除旧的app，再创建新的app
        if ((!Objects.equals(oldPort.getPort(), port.getPort()) || !Objects.equals(oldPort.getEndpoint(), port.getEndpoint())) && isEnabled(oldPort)) {
            serverHandler.unbind(oldPort.getAccessKey(), oldPort.getPort());
        }
        // 端口信息有效时才重新创建
        if (isEnabled(port) && !serverHandler.exists(port.getPort())) {
            serverHandler.bind(port.getAccessKey(), port.getPort(), port.getEndpoint());
        }
        if (!isEnabled(port) && serverHandler.exists(port.getPort())) {
            serverHandler.unbind(port.getAccessKey(), port.getPort());
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
        Optional<ClientInfo> clientInfoOpt = serverHandler.findByAccessKey(portDTO.getAccessKey());
        if (clientInfoOpt.isEmpty()) {
            return;
        }
        ClientInfo clientInfo = clientInfoOpt.get();
        Integer clientChannelStatus = Objects.nonNull(clientInfo.getChannel()) ? 1 : 0;
        Integer channelStatus = serverHandler.exists(portDTO.getPort()) ? 1 : 0;
        portDTO.setClientChannelStatus(clientChannelStatus);
        portDTO.setAppChannelStatus(channelStatus);
    }

}
