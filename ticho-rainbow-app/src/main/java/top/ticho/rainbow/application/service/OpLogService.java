package top.ticho.rainbow.application.service;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.util.NumberUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.ticho.rainbow.application.assembler.OpLogAssembler;
import top.ticho.rainbow.application.dto.excel.OpLogExcelExport;
import top.ticho.rainbow.application.executor.DictExecutor;
import top.ticho.rainbow.application.repository.OpLogAppRepository;
import top.ticho.rainbow.infrastructure.common.component.excel.ExcelHandle;
import top.ticho.rainbow.infrastructure.common.constant.DictConst;
import top.ticho.rainbow.interfaces.dto.OpLogDTO;
import top.ticho.rainbow.interfaces.query.OpLogQuery;
import top.ticho.starter.view.core.TiPageResult;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 日志信息 服务实现
 *
 * @author zhajianjun
 * @date 2024-03-24 17:55
 */
@Service
@RequiredArgsConstructor
public class OpLogService {
    private final OpLogAppRepository opLogAppRepository;
    private final OpLogAssembler opLogAssembler;
    private final DictExecutor dictExecutor;
    private final HttpServletResponse response;

    public TiPageResult<OpLogDTO> page(OpLogQuery query) {
        return opLogAppRepository.page(query);
    }

    public void exportExcel(OpLogQuery query) throws IOException {
        String sheetName = "操作日志";
        String fileName = "操作日志导出-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern(DatePattern.PURE_DATETIME_PATTERN));
        Map<Integer, String> labelMap = dictExecutor.getLabelMap(DictConst.YES_OR_NO, NumberUtil::parseInt);
        query.setCount(false);
        ExcelHandle.writeToResponseBatch(x -> this.excelExpHandle(x, labelMap), query, fileName, sheetName, OpLogExcelExport.class, response);
    }

    private Collection<OpLogExcelExport> excelExpHandle(OpLogQuery query, Map<Integer, String> labelMap) {
        TiPageResult<OpLogDTO> page = opLogAppRepository.page(query);
        return page.getRows()
            .stream()
            .map(x -> {
                OpLogExcelExport opLogExcelExport = opLogAssembler.toExcelExport(x);
                opLogExcelExport.setIsErrName(labelMap.get(x.getIsErr()));
                return opLogExcelExport;
            })
            .collect(Collectors.toList());
    }

}
