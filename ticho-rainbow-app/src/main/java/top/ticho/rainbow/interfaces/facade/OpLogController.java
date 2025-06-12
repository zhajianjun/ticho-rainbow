package top.ticho.rainbow.interfaces.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.ticho.rainbow.application.service.OpLogService;
import top.ticho.rainbow.infrastructure.common.constant.ApiConst;
import top.ticho.rainbow.interfaces.dto.query.OpLogQuery;
import top.ticho.rainbow.interfaces.dto.response.OpLogDTO;
import top.ticho.starter.view.core.TiPageResult;
import top.ticho.starter.view.core.TiResult;

import java.io.IOException;


/**
 * 操作日志
 *
 * @author zhajianjun
 * @date 2024-03-24 17:55
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("op-log")
public class OpLogController {
    private final OpLogService opLogService;

    /**
     * 查询操作日志(分页)
     */
    @PreAuthorize("@perm.hasPerms('" + ApiConst.SYSTEM_OP_LOG_PAGE + "')")
    @GetMapping("page")
    public TiResult<TiPageResult<OpLogDTO>> page(@Validated OpLogQuery query) {
        return TiResult.ok(opLogService.page(query));
    }

    /**
     * 导出操作日志
     *
     * @param query 查询
     * @throws IOException io异常
     */
    @PreAuthorize("@perm.hasPerms('" + ApiConst.SYSTEM_OP_LOG_EXPORT + "')")
    @GetMapping("excel/export")
    public void exportExcel(@Validated OpLogQuery query) throws IOException {
        opLogService.exportExcel(query);
    }

}
