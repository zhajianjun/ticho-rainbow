package top.ticho.intranet.server.domain.service;

import cn.hutool.core.util.IdUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ticho.boot.view.core.PageResult;
import com.ticho.boot.view.enums.BizErrCode;
import com.ticho.boot.view.util.Assert;
import com.ticho.boot.web.util.CloudIdUtil;
import com.ticho.boot.web.util.valid.ValidUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.ticho.intranet.server.application.service.ClientService;
import top.ticho.intranet.server.domain.repository.ClientRepository;
import top.ticho.intranet.server.infrastructure.entity.Client;
import top.ticho.intranet.server.interfaces.assembler.ClientAssembler;
import top.ticho.intranet.server.interfaces.dto.ClientDTO;
import top.ticho.intranet.server.interfaces.query.ClientQuery;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 客户端信息 服务实现
 *
 * @author zhajianjun
 * @date 2023-12-17 20:12
 */
@Service
public class ClientServiceImpl implements ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Override
    public void save(ClientDTO clientDTO) {
        ValidUtil.valid(clientDTO);
        Client client = ClientAssembler.INSTANCE.dtoToEntity(clientDTO);
        clientDTO.setId(CloudIdUtil.getId());
        clientDTO.setAccessKey(IdUtil.fastSimpleUUID());
        client.setSort(1);
        client.setEnabled(1);
        Assert.isTrue(clientRepository.save(client), "保存失败");
    }

    @Override
    public void removeById(Long id) {
        Assert.isTrue(clientRepository.removeById(id), "删除失败");
    }

    @Override
    public void updateById(ClientDTO clientDTO) {
        Client client = ClientAssembler.INSTANCE.dtoToEntity(clientDTO);
        Assert.isTrue(clientRepository.updateById(client), "修改失败");
    }

    @Override
    public ClientDTO getById(Long id) {
        Assert.isNotNull(id, "编号不能为空");
        Client client = clientRepository.getById(id);
        return ClientAssembler.INSTANCE.entityToDto(client);
    }

    @Override
    public PageResult<ClientDTO> page(ClientQuery query) {
        // @formatter:off
        query.checkPage();
        Page<Client> page = PageHelper.startPage(query.getPageNum(), query.getPageSize());
        clientRepository.list(query);
        List<ClientDTO> clientDTOs = page.getResult()
            .stream()
            .map(ClientAssembler.INSTANCE::entityToDto)
            .collect(Collectors.toList());
        return new PageResult<>(page.getPageNum(), page.getPageSize(), page.getTotal(), clientDTOs);
        // @formatter:on
    }

    @Override
    public List<ClientDTO> list(ClientQuery query) {
        // @formatter:off
        return clientRepository.list(query)
            .stream()
            .map(ClientAssembler.INSTANCE::entityToDto)
            .collect(Collectors.toList());
        // @formatter:on
    }
}
