package top.ticho.rainbow.domain.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.util.IdUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.ticho.rainbow.application.intranet.service.ClientService;
import top.ticho.rainbow.domain.handle.DictHandle;
import top.ticho.rainbow.domain.repository.ClientRepository;
import top.ticho.rainbow.domain.repository.PortRepository;
import top.ticho.rainbow.infrastructure.core.component.excel.ExcelHandle;
import top.ticho.rainbow.infrastructure.core.constant.DictConst;
import top.ticho.rainbow.infrastructure.entity.Client;
import top.ticho.rainbow.infrastructure.entity.Port;
import top.ticho.rainbow.interfaces.assembler.ClientAssembler;
import top.ticho.rainbow.interfaces.assembler.PortAssembler;
import top.ticho.rainbow.interfaces.dto.ClientDTO;
import top.ticho.rainbow.interfaces.excel.ClientExp;
import top.ticho.rainbow.interfaces.query.ClientQuery;
import top.ticho.starter.view.core.TiPageResult;
import top.ticho.starter.view.util.TiAssert;
import top.ticho.starter.web.util.TiIdUtil;
import top.ticho.starter.web.util.valid.TiValidUtil;
import top.ticho.tool.intranet.server.entity.ClientInfo;
import top.ticho.tool.intranet.server.entity.PortInfo;
import top.ticho.tool.intranet.server.handler.ServerHandler;

import javax.annotation.Resource;
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
 * 客户端信息 服务实现
 *
 * @author zhajianjun
 * @date 2023-12-17 20:12
 */
@Service
public class ClientServiceImpl implements ClientService {

    @Resource
    private ClientRepository clientRepository;
    @Resource
    private PortRepository portRepository;
    @Resource
    private ServerHandler serverHandler;
    @Resource
    private DictHandle dictHandle;
    @Resource
    private HttpServletResponse response;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(ClientDTO clientDTO) {
        TiValidUtil.valid(clientDTO);
        Client client = ClientAssembler.INSTANCE.dtoToEntity(clientDTO);
        clientDTO.setId(TiIdUtil.getId());
        clientDTO.setAccessKey(IdUtil.fastSimpleUUID());
        TiAssert.isTrue(clientRepository.save(client), "保存失败");
        saveClientInfo(client, null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeById(Long id) {
        Client dbClient = clientRepository.getById(id);
        TiAssert.isNotNull(dbClient, "删除失败，数据不存在");
        TiAssert.isTrue(clientRepository.removeById(id), "删除失败");
        String accessKey = dbClient.getAccessKey();
        clientRepository.removeByAccessKey(accessKey);
        serverHandler.deleteClient(accessKey);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateById(ClientDTO clientDTO) {
        Client dbClient = clientRepository.getById(clientDTO.getId());
        TiAssert.isNotNull(dbClient, "修改失败，数据不存在");
        Client client = ClientAssembler.INSTANCE.dtoToEntity(clientDTO);
        TiAssert.isTrue(clientRepository.updateById(client), "修改失败");
        client.setAccessKey(dbClient.getAccessKey());
        if (client.getExpireAt() == null) {
            client.setExpireAt(dbClient.getExpireAt());
        }
        saveClientInfo(client, dbClient);
    }

    @Override
    public ClientDTO getById(Long id) {
        TiAssert.isNotNull(id, "编号不能为空");
        Client client = clientRepository.getById(id);
        ClientDTO clientDTO = ClientAssembler.INSTANCE.entityToDto(client);
        fillChannelStatus(clientDTO);
        return clientDTO;
    }

    @Override
    public TiPageResult<ClientDTO> page(ClientQuery query) {
        query.checkPage();
        Page<Client> page = PageHelper.startPage(query.getPageNum(), query.getPageSize());
        clientRepository.list(query);
        List<ClientDTO> clientDTOs = page.getResult()
            .stream()
            .map(ClientAssembler.INSTANCE::entityToDto)
            .peek(this::fillChannelStatus)
            .collect(Collectors.toList());
        return new TiPageResult<>(page.getPageNum(), page.getPageSize(), page.getTotal(), clientDTOs);
    }

    @Override
    public List<ClientDTO> list(ClientQuery query) {
        return clientRepository.list(query)
            .stream()
            .map(ClientAssembler.INSTANCE::entityToDto)
            .peek(this::fillChannelStatus)
            .collect(Collectors.toList());
    }

    public List<ClientInfo> listEffectClientInfo() {
        ClientQuery clientQuery = new ClientQuery();
        clientQuery.setStatus(1);
        clientQuery.setExpireAt(LocalDateTime.now());
        List<Client> clients = clientRepository.list(clientQuery);
        List<String> accessKeys = clients.stream().map(Client::getAccessKey).collect(Collectors.toList());
        Predicate<Port> filter = x -> Objects.equals(x.getStatus(), 1) && LocalDateTime.now().isBefore(x.getExpireAt());
        Map<String, List<PortInfo>> protMap = portRepository.listAndGroupByAccessKey(accessKeys, PortAssembler.INSTANCE::entityToInfo, filter);
        return clients
            .stream()
            .map(ClientAssembler.INSTANCE::entityToInfo)
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

    @Override
    public void expExcel(ClientQuery query) throws IOException {
        String sheetName = "客户端信息";
        String fileName = "客户端信息导出-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern(DatePattern.PURE_DATETIME_PATTERN));
        Map<String, String> labelMap = dictHandle.getLabelMapBatch(DictConst.COMMON_STATUS, DictConst.CHANNEL_STATUS);
        ExcelHandle.writeToResponseBatch(x -> this.excelExpHandle(x, labelMap), query, fileName, sheetName, ClientExp.class, response);
    }

    private Collection<ClientExp> excelExpHandle(ClientQuery query, Map<String, String> labelMap) {
        query.checkPage();
        Page<Client> page = PageHelper.startPage(query.getPageNum(), query.getPageSize(), false);
        clientRepository.list(query);
        List<Client> result = page.getResult();
        return result
            .stream()
            .map(x -> {
                ClientExp clientExp = ClientAssembler.INSTANCE.entityToExp(x);
                clientExp.setStatusName(labelMap.get(DictConst.COMMON_STATUS + x.getStatus()));
                ClientInfo clientInfo = serverHandler.getClientByAccessKey(x.getAccessKey());
                int channelStatus = Optional.ofNullable(clientInfo).map(ClientInfo::getChannel).isPresent() ? 1 : 0;
                clientExp.setChannelStatusName(labelMap.get(DictConst.CHANNEL_STATUS + channelStatus));
                return clientExp;
            })
            .collect(Collectors.toList());
    }

    public void saveClientInfo(Client client, Client dbClient) {
        if (Objects.isNull(client)) {
            return;
        }
        if (Objects.isNull(client.getStatus())) {
            return;
        }
        boolean enabled = Objects.equals(client.getStatus(), 1) && LocalDateTime.now().isBefore(client.getExpireAt());
        ClientInfo clientInfo = ClientAssembler.INSTANCE.entityToInfo(client);
        // 新增时，数据库不存在
        if (Objects.isNull(dbClient)) {
            // 新增时是开启状态，则加入服务端
            if (enabled) {
                serverHandler.saveClient(clientInfo);
            }
            return;
        }
        // 数据库存在
        String accessKey = client.getAccessKey();
        if (enabled) {
            Predicate<Port> filter = x -> Objects.equals(x.getStatus(), 1) && LocalDateTime.now().isBefore(x.getExpireAt());
            Map<String, List<PortInfo>> protMap = portRepository.listAndGroupByAccessKey(Collections.singletonList(accessKey), PortAssembler.INSTANCE::entityToInfo, filter);
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
