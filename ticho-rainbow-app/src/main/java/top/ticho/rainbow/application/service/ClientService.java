package top.ticho.rainbow.application.service;

import cn.hutool.core.collection.CollStreamUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.ticho.intranet.server.entity.IntranetClient;
import top.ticho.rainbow.application.assembler.ClientAssembler;
import top.ticho.rainbow.application.dto.excel.ClientExcelExport;
import top.ticho.rainbow.application.executor.DictExecutor;
import top.ticho.rainbow.application.executor.IntranetExecutor;
import top.ticho.rainbow.application.repository.ClientAppRepository;
import top.ticho.rainbow.domain.entity.Client;
import top.ticho.rainbow.domain.entity.Port;
import top.ticho.rainbow.domain.entity.vo.ClientModifyVO;
import top.ticho.rainbow.domain.repository.ClientRepository;
import top.ticho.rainbow.infrastructure.common.component.excel.ExcelHandle;
import top.ticho.rainbow.infrastructure.common.constant.DictConst;
import top.ticho.rainbow.infrastructure.common.enums.YesOrNo;
import top.ticho.rainbow.interfaces.command.ClientModifyCommand;
import top.ticho.rainbow.interfaces.command.ClientSaveCommand;
import top.ticho.rainbow.interfaces.command.VersionModifyCommand;
import top.ticho.rainbow.interfaces.dto.ClientDTO;
import top.ticho.rainbow.interfaces.query.ClientQuery;
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
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * 客户端信息服务
 *
 * @author zhajianjun
 * @date 2023-12-17 20:12
 */
@Service
@RequiredArgsConstructor
public class ClientService {
    private final ClientRepository clientRepository;
    private final ClientAppRepository clientAppRepository;
    private final ClientAssembler clientAssembler;
    private final IntranetExecutor intranetExecutor;
    private final DictExecutor dictExecutor;
    private final HttpServletResponse response;

    @Transactional(rollbackFor = Exception.class)
    public void save(ClientSaveCommand clientSaveCommand) {
        Client client = clientAssembler.toEntity(clientSaveCommand);
        TiAssert.isTrue(clientRepository.save(client), "保存失败");
    }

    @Transactional(rollbackFor = Exception.class)
    public void remove(VersionModifyCommand command) {
        Client client = clientRepository.find(command.getId());
        TiAssert.isNotNull(client, "删除失败，数据不存在");
        client.checkVersion(command.getVersion(), "数据已被修改，请刷新后重试");
        TiAssert.isTrue(!client.isEnable(), "删除失败，请先禁用该客户端");
        TiAssert.isTrue(clientRepository.remove(command.getId()), "删除失败，请刷新后重试");
    }

    @Transactional(rollbackFor = Exception.class)
    public void modify(ClientModifyCommand clientModifyCommand) {
        Client client = clientRepository.find(clientModifyCommand.getId());
        TiAssert.isNotNull(client, "修改失败，数据不存在");
        client.checkVersion(clientModifyCommand.getVersion(), "数据已被修改，请刷新后重试");
        ClientModifyVO vo = clientAssembler.toVO(clientModifyCommand);
        client.modify(vo);
        TiAssert.isTrue(clientRepository.modify(client), "修改失败，请刷新后重试");
    }

    public void enable(List<VersionModifyCommand> datas) {
        boolean enable = modifyBatch(datas, Client::enable, this::createAndBind);
        TiAssert.isTrue(enable, "启用失败，请刷新后重试");
    }

    private void createAndBind(Client client) {
        String accessKey = client.getAccessKey();
        intranetExecutor.create(accessKey, client.getName());
        List<String> accessKeys = Collections.singletonList(accessKey);
        Map<String, List<Port>> protMap = intranetExecutor.getPortMap(accessKeys);
        List<Port> ports = protMap.getOrDefault(accessKey, Collections.emptyList());
        ports.forEach(port -> intranetExecutor.bind(accessKey, port.getPort(), port.getEndpoint()));
    }

    public void disable(List<VersionModifyCommand> datas) {
        boolean disable = modifyBatch(datas, Client::disable, client -> intranetExecutor.remove(client.getAccessKey()));
        TiAssert.isTrue(disable, "禁用失败，请刷新后重试");
    }

    public TiPageResult<ClientDTO> page(ClientQuery query) {
        TiPageResult<ClientDTO> page = clientAppRepository.page(query);
        page.getRows().forEach(this::fillChannelStatus);
        return page;
    }

    public List<ClientDTO> all() {
        List<ClientDTO> clientDTOS = clientAppRepository.all();
        clientDTOS.forEach(this::fillChannelStatus);
        return clientDTOS;
    }

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
        Optional<IntranetClient> clientInfoOpt = intranetExecutor.findByAccessKey(clientDTO.getAccessKey());
        if (clientInfoOpt.isEmpty()) {
            return;
        }
        IntranetClient clientInfo = clientInfoOpt.get();
        clientDTO.setChannelStatus(YesOrNo.getCode(Objects.nonNull(clientInfo.getChannel())));
    }

    private boolean modifyBatch(List<VersionModifyCommand> modifys, Consumer<Client> modifyHandle, Consumer<Client> modifyToDbAfterHandle) {
        List<Long> ids = CollStreamUtil.toList(modifys, VersionModifyCommand::getId);
        List<Client> clients = clientRepository.list(ids);
        Map<Long, Client> clientMap = CollStreamUtil.toIdentityMap(clients, Client::getId);
        for (VersionModifyCommand modify : modifys) {
            Client client = clientMap.get(modify.getId());
            TiAssert.isNotNull(client, StrUtil.format("操作失败, 数据不存在, id: {}", modify.getId()));
            client.checkVersion(modify.getVersion(), StrUtil.format("数据已被修改，请刷新后重试, 客户端: {}", client.getName()));
            // 修改逻辑
            modifyHandle.accept(client);
        }
        boolean modifyBatch = clientRepository.modifyBatch(clients);
        if (modifyBatch) {
            clients.forEach(modifyToDbAfterHandle);
        }
        return modifyBatch;
    }

}

