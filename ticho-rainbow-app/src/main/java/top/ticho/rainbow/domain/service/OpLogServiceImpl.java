package top.ticho.rainbow.domain.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.ticho.boot.view.core.PageResult;
import top.ticho.boot.view.util.Assert;
import top.ticho.rainbow.application.service.OpLogService;
import top.ticho.rainbow.domain.repository.OpLogRepository;
import top.ticho.rainbow.infrastructure.entity.OpLog;
import top.ticho.rainbow.interfaces.assembler.OpLogAssembler;
import top.ticho.rainbow.interfaces.dto.OpLogDTO;
import top.ticho.rainbow.interfaces.query.OpLogQuery;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 日志信息 服务实现
 *
 * @author zhajianjun
 * @date 2024-03-24 17:55
 */
@Service
public class OpLogServiceImpl implements OpLogService {

    @Autowired
    private OpLogRepository opLogRepository;

    @Override
    public OpLogDTO getById(Long id) {
        Assert.isNotNull(id, "编号不能为空");
        OpLog opLog = opLogRepository.getById(id);
        return OpLogAssembler.INSTANCE.entityToDto(opLog);
    }

    @Override
    public PageResult<OpLogDTO> page(OpLogQuery query) {
        // @formatter:off
        query.checkPage();
        Page<OpLog> page = PageHelper.startPage(query.getPageNum(), query.getPageSize());
        opLogRepository.list(query);
        List<OpLogDTO> opLogDTOs = page.getResult()
            .stream()
            .map(OpLogAssembler.INSTANCE::entityToDto)
            .collect(Collectors.toList());
        return new PageResult<>(page.getPageNum(), page.getPageSize(), page.getTotal(), opLogDTOs);
        // @formatter:on
    }
}
