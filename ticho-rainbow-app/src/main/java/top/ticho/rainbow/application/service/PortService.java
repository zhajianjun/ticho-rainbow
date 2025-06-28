package top.ticho.rainbow.application.service;

import cn.hutool.core.collection.CollStreamUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.ticho.intranet.server.entity.IntranetClient;
import top.ticho.rainbow.application.assembler.PortAssembler;
import top.ticho.rainbow.application.dto.excel.PortExcelExport;
import top.ticho.rainbow.application.executor.DictExecutor;
import top.ticho.rainbow.application.executor.IntranetExecutor;
import top.ticho.rainbow.application.repository.PortAppRepository;
import top.ticho.rainbow.domain.entity.Client;
import top.ticho.rainbow.domain.entity.Port;
import top.ticho.rainbow.domain.entity.vo.PortModifyfVO;
import top.ticho.rainbow.domain.repository.ClientRepository;
import top.ticho.rainbow.domain.repository.PortRepository;
import top.ticho.rainbow.infrastructure.common.component.excel.ExcelHandle;
import top.ticho.rainbow.infrastructure.common.constant.DictConst;
import top.ticho.rainbow.infrastructure.common.enums.ProtocolType;
import top.ticho.rainbow.interfaces.command.PortModifyfCommand;
import top.ticho.rainbow.interfaces.command.PortSaveCommand;
import top.ticho.rainbow.interfaces.command.VersionModifyCommand;
import top.ticho.rainbow.interfaces.dto.PortDTO;
import top.ticho.rainbow.interfaces.query.PortQuery;
import top.ticho.starter.view.core.TiPageResult;
import top.ticho.starter.view.util.TiAssert;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
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
    private final IntranetExecutor intranetExecutor;
    private final DictExecutor dictExecutor;
    private final HttpServletResponse response;

    public void save(PortSaveCommand portSaveCommand) {
        check(null, portSaveCommand.getAccessKey(), portSaveCommand.getPort(), portSaveCommand.getDomain(), portSaveCommand.getType());
        Port port = portAssembler.toEntity(portSaveCommand);
        TiAssert.isTrue(portRepository.save(port), "保存失败");
    }

    public void remove(VersionModifyCommand command) {
        Port port = portRepository.find(command.getId());
        TiAssert.isNotNull(port, "删除失败，数据不存在");
        port.checkVersion(command.getVersion(), "数据已被修改，请刷新后重试");
        TiAssert.isTrue(!port.isEnable(), "删除失败，请先禁用该端口");
        TiAssert.isTrue(portRepository.remove(command.getId()), "删除失败，请刷新后重试");
    }

    public void modify(PortModifyfCommand portModifyfCommand) {
        Port port = portRepository.find(portModifyfCommand.getId());
        TiAssert.isNotNull(port, "修改失败，数据不存在");
        port.checkVersion(portModifyfCommand.getVersion(), "数据已被修改，请刷新后重试");
        check(portModifyfCommand.getId(), portModifyfCommand.getAccessKey(), portModifyfCommand.getPort(), portModifyfCommand.getDomain(), portModifyfCommand.getType());
        PortModifyfVO portModifyfVO = portAssembler.toModifyfVO(portModifyfCommand);
        port.modify(portModifyfVO);
        TiAssert.isTrue(portRepository.modify(port), "修改失败，请刷新后重试");
    }

    public void enable(List<VersionModifyCommand> datas) {
        boolean enable = modifyBatch(datas, Port::enable, port -> {
            if (!intranetExecutor.exists(port.getPort())) {
                intranetExecutor.bind(port.getAccessKey(), port.getPort(), port.getEndpoint());
            }
        });
        TiAssert.isTrue(enable, "启用失败，请刷新后重试");
    }

    public void disable(List<VersionModifyCommand> datas) {
        boolean disable = modifyBatch(datas, Port::disable, port -> {
            if (intranetExecutor.exists(port.getPort())) {
                intranetExecutor.unbind(port.getAccessKey(), port.getPort());
            }
        });
        TiAssert.isTrue(disable, "禁用失败，请刷新后重试");
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

    private void fillChannelStatus(PortDTO portDTO) {
        if (Objects.isNull(portDTO)) {
            return;
        }
        Optional<IntranetClient> clientInfoOpt = intranetExecutor.findByAccessKey(portDTO.getAccessKey());
        if (clientInfoOpt.isEmpty()) {
            return;
        }
        IntranetClient clientInfo = clientInfoOpt.get();
        Integer clientChannelStatus = Objects.nonNull(clientInfo.getChannel()) ? 1 : 0;
        Integer channelStatus = intranetExecutor.exists(portDTO.getPort()) ? 1 : 0;
        portDTO.setClientChannelStatus(clientChannelStatus);
        portDTO.setAppChannelStatus(channelStatus);
    }

    private boolean modifyBatch(List<VersionModifyCommand> modifys, Consumer<Port> modifyHandle, Consumer<Port> modifyToDbAfterHandle) {
        List<Long> ids = CollStreamUtil.toList(modifys, VersionModifyCommand::getId);
        List<Port> ports = portRepository.list(ids);
        Map<Long, Port> portMap = CollStreamUtil.toIdentityMap(ports, Port::getId);
        for (VersionModifyCommand modify : modifys) {
            Port port = portMap.get(modify.getId());
            TiAssert.isNotNull(port, StrUtil.format("操作失败, 数据不存在, id: {}", modify.getId()));
            port.checkVersion(modify.getVersion(), StrUtil.format("数据已被修改，请刷新后重试, 端口: {}", port.getPort()));
            // 修改逻辑
            modifyHandle.accept(port);
        }
        boolean modifyBatch = portRepository.modifyBatch(ports);
        if (modifyBatch) {
            ports.forEach(modifyToDbAfterHandle);
        }
        return modifyBatch;
    }

}
