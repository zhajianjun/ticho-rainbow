package top.ticho.rainbow.application.service;

import cn.hutool.core.date.DatePattern;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.ticho.intranet.server.core.ServerHandler;
import top.ticho.intranet.server.entity.ClientInfo;
import top.ticho.rainbow.application.assembler.ClientAssembler;
import top.ticho.rainbow.application.dto.command.ClientModifyCommand;
import top.ticho.rainbow.application.dto.command.ClientSaveCommand;
import top.ticho.rainbow.application.dto.excel.ClientExcelExport;
import top.ticho.rainbow.application.dto.query.ClientQuery;
import top.ticho.rainbow.application.dto.response.ClientDTO;
import top.ticho.rainbow.application.executor.DictExecutor;
import top.ticho.rainbow.application.repository.ClientAppRepository;
import top.ticho.rainbow.application.repository.PortAppRepository;
import top.ticho.rainbow.domain.entity.Client;
import top.ticho.rainbow.domain.entity.Port;
import top.ticho.rainbow.domain.entity.vo.ClientModifyVO;
import top.ticho.rainbow.domain.repository.ClientRepository;
import top.ticho.rainbow.infrastructure.common.component.excel.ExcelHandle;
import top.ticho.rainbow.infrastructure.common.constant.DictConst;
import top.ticho.starter.view.core.TiPageResult;
import top.ticho.starter.view.util.TiAssert;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Collections;
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
    private final ClientAppRepository clientAppRepository;
    private final PortAppRepository portAppRepository;
    private final ClientAssembler clientAssembler;
    private final ServerHandler serverHandler;
    private final DictExecutor dictExecutor;
    private final HttpServletResponse response;

    @Transactional(rollbackFor = Exception.class)
    public void save(ClientSaveCommand clientSaveCommand) {
        Client client = clientAssembler.toEntity(clientSaveCommand);
        TiAssert.isTrue(clientRepository.save(client), "保存失败");
        createClient(client);
    }

    public void createClient(Client client) {
        boolean enabled = Objects.equals(client.getStatus(), 1) && LocalDateTime.now().isBefore(client.getExpireAt());
        // 新增时是开启状态，则加入服务端
        if (enabled) {
            serverHandler.create(client.getAccessKey(), client.getName());
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
        serverHandler.remove(accessKey);
    }

    /**
     * 修改客户端信息
     */
    @Transactional(rollbackFor = Exception.class)
    public void modify(ClientModifyCommand clientModifyCommand) {
        Client client = clientRepository.find(clientModifyCommand.getId());
        TiAssert.isNotNull(client, "修改失败，数据不存在");
        boolean statusChanged = client.equalStatus(clientModifyCommand.getStatus());
        ClientModifyVO vo = clientAssembler.toVO(clientModifyCommand);
        client.modify(vo);
        TiAssert.isTrue(clientRepository.modify(client), "修改失败，请刷新后重试");
        if (statusChanged) {
            modifyClientInfo(client);
        }
    }

    public void modifyClientInfo(Client client) {
        boolean enabled = Objects.equals(client.getStatus(), 1) && LocalDateTime.now().isBefore(client.getExpireAt());
        String accessKey = client.getAccessKey();
        if (enabled) {
            Predicate<Port> filter = x -> Objects.equals(x.getStatus(), 1) && LocalDateTime.now().isBefore(x.getExpireAt());
            Map<String, List<Port>> protMap = portAppRepository.listAndGroupByAccessKey(Collections.singletonList(accessKey), Function.identity(), filter);
            List<Port> ports = protMap.getOrDefault(accessKey, Collections.emptyList());
            serverHandler.create(client.getAccessKey(), client.getName());
            ports.forEach(port -> serverHandler.bind(accessKey, port.getPort(), port.getEndpoint()));

        } else {
            serverHandler.remove(accessKey);
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
        TiPageResult<ClientDTO> page = clientAppRepository.page(query);
        page.getRows().forEach(this::fillChannelStatus);
        return page;
    }

    /**
     * 查询客户端信息列表
     *
     * @return {@link List}<{@link ClientDTO}>
     */
    public List<ClientDTO> all() {
        List<ClientDTO> clientDTOS = clientAppRepository.all();
        clientDTOS.forEach(this::fillChannelStatus);
        return clientDTOS;
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
        TiPageResult<ClientDTO> page = clientAppRepository.page(query);
        List<ClientDTO> result = page.getRows();
        return result
            .stream()
            .map(item -> {
                fillChannelStatus(item);
                ClientExcelExport clientExcelExport = clientAssembler.toExcelExport(item);
                clientExcelExport.setStatusName(labelMap.get(DictConst.COMMON_STATUS + item.getStatus()));
                int channelStatus = item.getChannelStatus();
                clientExcelExport.setChannelStatusName(labelMap.get(DictConst.CHANNEL_STATUS + channelStatus));
                return clientExcelExport;
            })
            .collect(Collectors.toList());
    }

    private void fillChannelStatus(ClientDTO clientDTO) {
        if (Objects.isNull(clientDTO)) {
            return;
        }
        Optional<ClientInfo> clientInfoOpt = serverHandler.findByAccessKey(clientDTO.getAccessKey());
        if (clientInfoOpt.isEmpty()) {
            return;
        }
        ClientInfo clientInfo = clientInfoOpt.get();
        Integer channelStatus = Objects.nonNull(clientInfo.getChannel()) ? 1 : 0;
        clientDTO.setChannelStatus(channelStatus);
    }
}

