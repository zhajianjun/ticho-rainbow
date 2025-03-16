package top.ticho.rainbow.interfaces.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.ticho.rainbow.application.dto.query.OpLogQuery;
import top.ticho.rainbow.application.dto.response.OpLogDTO;
import top.ticho.rainbow.application.service.OpLogService;
import top.ticho.starter.view.core.TiPageResult;
import top.ticho.starter.view.core.TiResult;

import javax.validation.constraints.NotNull;
import java.io.IOException;


/**
 * 操作日志
 *
 * @author zhajianjun
 * @date 2024-03-24 17:55
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("opLog")
public class OpLogController {
    private final OpLogService opLogService;

    /**
     * 查询操作日志
     *
     * @param id 编号
     */
    @PreAuthorize("@perm.hasPerms('system:opLog:getById')")
    @GetMapping
    public TiResult<OpLogDTO> getById(@NotNull(message = "编号不能为空") Long id) {
        return TiResult.ok(opLogService.getById(id));
    }

    /**
     * 查询全部操作日志(分页)
     */
    @PreAuthorize("@perm.hasPerms('system:opLog:page')")
    @GetMapping("page")
    public TiResult<TiPageResult<OpLogDTO>> page(@Validated @RequestBody OpLogQuery query) {
        return TiResult.ok(opLogService.page(query));
    }

    /**
     * 导出操作日志
     *
     * @param query 查询
     * @throws IOException io异常
     */
    @PreAuthorize("@perm.hasPerms('system:opLog:expExcel')")
    @GetMapping("excel/export")
    public void expExcel(@RequestBody OpLogQuery query) throws IOException {
        opLogService.expExcel(query);
    }

}
