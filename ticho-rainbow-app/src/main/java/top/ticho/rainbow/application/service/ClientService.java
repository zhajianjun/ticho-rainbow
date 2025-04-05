package top.ticho.rainbow.application.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.ticho.rainbow.application.assembler.ClientAssembler;
import top.ticho.rainbow.application.assembler.PortAssembler;
import top.ticho.rainbow.application.dto.command.ClientModifyCommand;
import top.ticho.rainbow.application.dto.command.ClientSaveCommand;
import top.ticho.rainbow.application.dto.excel.ClientExcelExport;
import top.ticho.rainbow.application.dto.query.ClientQuery;
import top.ticho.rainbow.application.dto.response.ClientDTO;
import top.ticho.rainbow.application.executor.DictExecutor;
import top.ticho.rainbow.domain.entity.Client;
import top.ticho.rainbow.domain.entity.Port;
import top.ticho.rainbow.domain.entity.vo.ClientModifyVO;
import top.ticho.rainbow.domain.repository.ClientRepository;
import top.ticho.rainbow.domain.repository.PortRepository;
import top.ticho.rainbow.infrastructure.core.component.excel.ExcelHandle;
import top.ticho.rainbow.infrastructure.core.constant.DictConst;
import top.ticho.starter.datasource.util.TiPageUtil;
import top.ticho.starter.view.core.TiPageResult;
import top.ticho.starter.view.util.TiAssert;
import top.ticho.tool.intranet.server.entity.ClientInfo;
import top.ticho.tool.intranet.server.entity.PortInfo;
import top.ticho.tool.intranet.server.handler.ServerHandler;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 客户端信息 服务接口
 *
 * @author zhajianjun
 * @date 2023-12-17 20:12
 */
@Service
@RequiredArgsConstructor
public class ClientService {
    private final ClientRepository clientRepository;
    private final PortRepository portRepository;
    private final ClientAssembler clientAssembler;
    private final PortAssembler portAssembler;
    private final ServerHandler serverHandler;
    private final DictExecutor dictExecutor;
    private final HttpServletResponse response;

    @Transactional(rollbackFor = Exception.class)
    public void save(ClientSaveCommand clientSaveCommand) {
        Client clientFromDb = clientRepository.findByAccessKey(clientSaveCommand.getAccessKey());
        TiAssert.isNull(clientFromDb, "保存失败，客户端已存在");
        Client client = clientAssembler.toEntity(clientSaveCommand);
        TiAssert.isTrue(clientRepository.save(client), "保存失败");
        saveClientInfo(client);
    }

    public void saveClientInfo(Client client) {
        boolean enabled = Objects.equals(client.getStatus(), 1) && LocalDateTime.now().isBefore(client.getExpireAt());
        ClientInfo clientInfo = clientAssembler.toInfo(client);
        // 新增时是开启状态，则加入服务端
        if (enabled) {
            serverHandler.saveClient(clientInfo);
        }
    }

    /**
     * 删除客户端信息
     *
     * @param id 编号
     */
    @Transactional(rollbackFor = Exception.class)
    public void remove(Long id) {
        Client dbClient = clientRepository.find(id);
        TiAssert.isNotNull(dbClient, "删除失败，数据不存在");
        TiAssert.isTrue(clientRepository.remove(id), "删除失败");
        String accessKey = dbClient.getAccessKey();
        clientRepository.removeByAccessKey(accessKey);
        serverHandler.deleteClient(accessKey);
    }

    /**
     * 修改客户端信息
     */
    @Transactional(rollbackFor = Exception.class)
    public void modify(ClientModifyCommand clientModifyCommand) {
        Client client = clientRepository.find(clientModifyCommand.getId());
        TiAssert.isNotNull(client, "修改失败，数据不存在");
        ClientModifyVO vo = clientAssembler.toVO(clientModifyCommand);
        client.modify(vo);
        TiAssert.isTrue(clientRepository.modify(client), "修改失败");
        modifyClientInfo(client);
    }

    public void modifyClientInfo(Client client) {
        boolean enabled = Objects.equals(client.getStatus(), 1) && LocalDateTime.now().isBefore(client.getExpireAt());
        ClientInfo clientInfo = clientAssembler.toInfo(client);
        String accessKey = client.getAccessKey();
        if (enabled) {
            Predicate<Port> filter = x -> Objects.equals(x.getStatus(), 1) && LocalDateTime.now().isBefore(x.getExpireAt());
            Map<String, List<PortInfo>> protMap = portRepository.listAndGroupByAccessKey(Collections.singletonList(accessKey), portAssembler::toInfo, filter);
            List<PortInfo> portInfos = protMap.getOrDefault(accessKey, Collections.emptyList());
            Map<Integer, PortInfo> portInfoMap = portInfos
                .stream()
                .collect(Collectors.toMap(PortInfo::getPort, Function.identity(), (v1, v2) -> v1, LinkedHashMap::new));
            clientInfo.setPortMap(portInfoMap);
            serverHandler.saveClient(clientInfo);
        } else {
            serverHandler.deleteClient(accessKey);
        }
    }

    /**
     * 根据id查询客户端信息
     *
     * @param id 编号
     * @return {@link ClientDTO}
     */
    public ClientDTO find(Long id) {
        Client clientPO = clientRepository.find(id);
        ClientDTO clientDTO = clientAssembler.toDTO(clientPO);
        fillChannelStatus(clientDTO);
        return clientDTO;
    }

    /**
     * 分页查询客户端信息列表
     *
     * @param query 查询
     * @return {@link TiPageResult}<{@link ClientDTO}>
     */
    public TiPageResult<ClientDTO> page(ClientQuery query) {
        TiPageResult<Client> page = clientRepository.page(query);
        return TiPageUtil.of(page, (s) -> {
            ClientDTO clientDTO = clientAssembler.toDTO(s);
            fillChannelStatus(clientDTO);
            return clientDTO;
        });
    }

    /**
     * 查询客户端信息列表
     *
     * @param query 查询条件
     * @return {@link List}<{@link ClientDTO}>
     */
    public List<ClientDTO> list(ClientQuery query) {
        return clientRepository.list(query)
            .stream()
            .map(clientAssembler::toDTO)
            .peek(this::fillChannelStatus)
            .collect(Collectors.toList());
    }

    /**
     * 查询所有有效客户端通道信息
     *
     * @return {@link List}<{@link ClientInfo}>
     */
    public List<ClientInfo> listEffectClientInfo() {
        ClientQuery clientQuery = new ClientQuery();
        clientQuery.setStatus(1);
        clientQuery.setExpireAt(LocalDateTime.now());
        List<Client> clientPOS = clientRepository.list(clientQuery);
        List<String> accessKeys = clientPOS.stream().map(Client::getAccessKey).collect(Collectors.toList());
        Predicate<Port> filter = x -> Objects.equals(x.getStatus(), 1) && LocalDateTime.now().isBefore(x.getExpireAt());
        Map<String, List<PortInfo>> protMap = portRepository.listAndGroupByAccessKey(accessKeys, portAssembler::toInfo, filter);
        return clientPOS
            .stream()
            .map(clientAssembler::toInfo)
            .peek(x -> setPortMap(x, protMap))
            .collect(Collectors.toList());
    }

    private void setPortMap(ClientInfo clientInfo, Map<String, List<PortInfo>> protMap) {
        String accessKey = clientInfo.getAccessKey();
        List<PortInfo> portInfos = protMap.get(accessKey);
        if (CollUtil.isEmpty(portInfos)) {
            return;
        }
        Map<Integer, PortInfo> portInfoMap = portInfos
            .stream()
            .collect(Collectors.toMap(PortInfo::getPort, Function.identity(), (v1, v2) -> v1, LinkedHashMap::new));
        clientInfo.setPortMap(portInfoMap);
    }

    /**
     * 导出客户端信息
     *
     * @param query 查询条件
     */
    public void exportExcel(ClientQuery query) throws IOException {
        String sheetName = "客户端信息";
        String fileName = "客户端信息导出-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern(DatePattern.PURE_DATETIME_PATTERN));
        Map<String, String> labelMap = dictExecutor.getLabelMapBatch(DictConst.COMMON_STATUS, DictConst.CHANNEL_STATUS);
        query.setCount(false);
        ExcelHandle.writeToResponseBatch(x -> this.excelExpHandle(x, labelMap), query, fileName, sheetName, ClientExcelExport.class, response);
    }

    private Collection<ClientExcelExport> excelExpHandle(ClientQuery query, Map<String, String> labelMap) {
        query.checkPage();
        TiPageResult<Client> page = clientRepository.page(query);
        List<Client> result = page.getRows();
        return result
            .stream()
            .map(item -> {
                ClientExcelExport clientExcelExport = clientAssembler.toExp(item);
                clientExcelExport.setStatusName(labelMap.get(DictConst.COMMON_STATUS + item.getStatus()));
                ClientInfo clientInfo = serverHandler.getClientByAccessKey(item.getAccessKey());
                int channelStatus = Optional.ofNullable(clientInfo).map(ClientInfo::getChannel).isPresent() ? 1 : 0;
                clientExcelExport.setChannelStatusName(labelMap.get(DictConst.CHANNEL_STATUS + channelStatus));
                return clientExcelExport;
            })
            .collect(Collectors.toList());
    }

    private void fillChannelStatus(ClientDTO clientDTO) {
        if (Objects.isNull(clientDTO)) {
            return;
        }
        ClientInfo clientInfo = serverHandler.getClientByAccessKey(clientDTO.getAccessKey());
        if (Objects.isNull(clientInfo)) {
            return;
        }
        Integer channelStatus = Objects.nonNull(clientInfo.getChannel()) ? 1 : 0;
        clientDTO.setChannelStatus(channelStatus);
    }
}

