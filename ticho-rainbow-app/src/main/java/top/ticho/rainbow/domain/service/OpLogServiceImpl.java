package top.ticho.rainbow.domain.service;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.util.NumberUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.ticho.boot.view.core.PageResult;
import top.ticho.boot.view.util.Assert;
import top.ticho.rainbow.application.service.OpLogService;
import top.ticho.rainbow.domain.handle.DictTemplate;
import top.ticho.rainbow.domain.repository.OpLogRepository;
import top.ticho.rainbow.infrastructure.core.component.excel.ExcelHandle;
import top.ticho.rainbow.infrastructure.core.constant.DictConst;
import top.ticho.rainbow.infrastructure.entity.OpLog;
import top.ticho.rainbow.interfaces.assembler.OpLogAssembler;
import top.ticho.rainbow.interfaces.dto.OpLogDTO;
import top.ticho.rainbow.interfaces.excel.OpLogExp;
import top.ticho.rainbow.interfaces.query.OpLogQuery;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.Map;
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

    @Autowired
    private DictTemplate dictTemplate;

    @Resource
    private HttpServletResponse response;

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

    @Override
    public void expExcel(OpLogQuery query) throws IOException {
        String sheetName = "操作日志";
        String fileName = "操作日志导出-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern(DatePattern.PURE_DATETIME_PATTERN));
        Map<Integer, String> labelMap = dictTemplate.getLabelMap(DictConst.YES_OR_NO, NumberUtil::parseInt);
        ExcelHandle.writeToResponseBatch(x-> this.excelExpHandle(x, labelMap), query, fileName, sheetName, OpLogExp.class, response);
    }

    private Collection<OpLogExp> excelExpHandle(OpLogQuery query, Map<Integer, String> labelMap) {
        query.checkPage();
        Page<OpLog> page = PageHelper.startPage(query.getPageNum(), query.getPageSize(), false);
        opLogRepository.list(query);
        List<OpLog> result = page.getResult();
        return result
            .stream()
            .map(x-> {
                OpLogExp opLogExp = OpLogAssembler.INSTANCE.entityToExp(x);
                opLogExp.setIsErrName(labelMap.get(x.getIsErr()));
                return opLogExp;
            })
            .collect(Collectors.toList());
    }

}
