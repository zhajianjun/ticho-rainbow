package top.ticho.rainbow.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.unit.DataSize;
import top.ticho.rainbow.application.assembler.PortAssembler;
import top.ticho.rainbow.application.dto.response.ClientDTO;
import top.ticho.rainbow.application.dto.response.FlowMonitorDTO;
import top.ticho.rainbow.application.dto.response.FlowMonitorStatsDTO;
import top.ticho.rainbow.application.dto.response.PortDTO;
import top.ticho.rainbow.application.repository.ClientAppRepository;
import top.ticho.rainbow.application.repository.PortAppRepository;
import top.ticho.tool.intranet.server.entity.AppDataCollector;
import top.ticho.tool.intranet.server.entity.AppDataSummary;
import top.ticho.tool.intranet.server.entity.ClientInfo;
import top.ticho.tool.intranet.server.handler.AppHandler;
import top.ticho.tool.intranet.server.handler.ServerHandler;
import top.ticho.tool.intranet.util.IntranetUtil;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 流量监控 服务实现
 *
 * @author zhajianjun
 * @date 2024-05-16 09:42
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FlowMonitorService {
    private final ClientAppRepository clientAppRepository;
    private final PortAppRepository portRepository;
    private final ServerHandler serverHandler;
    private final PortAssembler portAssembler;

    public FlowMonitorStatsDTO info() {
        AppHandler appHandler = serverHandler.getAppHandler();
        Map<String, ClientInfo> clientMap = serverHandler.getClientMap();
        // 客户端数
        List<ClientDTO> clientDTOS = clientAppRepository.all();
        // 激活的客户端数
        long count = clientDTOS
            .stream()
            .map(x -> clientMap.get(x.getAccessKey()))
            .filter(Objects::nonNull)
            .map(ClientInfo::getChannel)
            .filter(IntranetUtil::isActive)
            .count();
        int activeClients = Long.valueOf(count).intValue();
        // 激活的端口号总数
        Set<Integer> portNums = appHandler.getBindPortChannelMap().keySet();
        // 激活的端口流量信息
        List<AppDataSummary> dataSummaries = AppDataCollector.getAllData();
        Map<Integer, AppDataSummary> appDataMap = dataSummaries.stream().collect(Collectors.toMap(AppDataSummary::getPort, Function.identity()));
        // 所有端口
        List<PortDTO> ports = portRepository.all();
        List<FlowMonitorDTO> flowDetails = ports
            .stream()
            .sorted(Comparator.comparing(PortDTO::getSort).thenComparing(PortDTO::getPort))
            .map(x -> convertToFlowMonitor(x, appDataMap.get(x.getPort())))
            .collect(Collectors.toList());
        FlowMonitorStatsDTO flowMonitorStatsDTO = new FlowMonitorStatsDTO();
        flowMonitorStatsDTO.setClients(clientDTOS.size());
        flowMonitorStatsDTO.setActiveClients(activeClients);
        flowMonitorStatsDTO.setPorts(ports.size());
        flowMonitorStatsDTO.setActivePorts(portNums.size());
        flowMonitorStatsDTO.setDateTime(LocalDateTime.now());
        flowMonitorStatsDTO.setFlowDetails(flowDetails);
        return flowMonitorStatsDTO;
    }

    public FlowMonitorDTO convertToFlowMonitor(PortDTO portDTO, AppDataSummary summary) {
        if (Objects.isNull(portDTO)) {
            return null;
        }
        FlowMonitorDTO flowMonitorDTO = new FlowMonitorDTO();
        flowMonitorDTO.setPort(portDTO.getPort());
        flowMonitorDTO.setPortInfo(portDTO);
        if (Objects.isNull(summary)) {
            flowMonitorDTO.setReadBytes(0);
            flowMonitorDTO.setWriteBytes(0);
            flowMonitorDTO.setReadMsgs(0);
            flowMonitorDTO.setWriteMsgs(0);
            flowMonitorDTO.setChannels(0);
            return flowMonitorDTO;
        }
        flowMonitorDTO.setReadBytes(toKb(summary.getReadBytes()));
        flowMonitorDTO.setWriteBytes(toKb(summary.getWriteBytes()));
        flowMonitorDTO.setReadMsgs(toKb(summary.getReadMsgs()));
        flowMonitorDTO.setWriteMsgs(toKb(summary.getWriteMsgs()));
        flowMonitorDTO.setChannels(summary.getChannels());
        return flowMonitorDTO;
    }

    public Integer toKb(Long size) {
        if (size == null) {
            return 0;
        }
        return Long.valueOf(DataSize.ofBytes(size).toKilobytes()).intValue();
    }

}
