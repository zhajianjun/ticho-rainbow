package top.ticho.rainbow.infrastructure.core.component;

import cn.hutool.core.exceptions.ExceptionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataAccessException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import top.ticho.starter.log.interceptor.TiWebLogInterceptor;
import top.ticho.starter.view.core.TiResult;
import top.ticho.starter.view.enums.TiHttpErrCode;
import top.ticho.starter.view.log.TiHttpLog;
import top.ticho.starter.web.handle.TiResponseHandle;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * 自定义异常处理
 *
 * @author zhajianjun
 * @date 2024-04-18 17:35
 */
@Slf4j
@RestControllerAdvice
@Order(100)
public class CustomResponseHandle {

    @Resource
    private TiResponseHandle tiResponseHandle;

    public void prefix(Exception ex) {
        TiHttpLog httpLog = TiWebLogInterceptor.logInfo();
        if (Objects.nonNull(httpLog)) {
            String errorMsg = ExceptionUtil.stacktraceToString(ex);
            httpLog.setErrMessage(errorMsg);
        }
    }

    /**
     * 权限不足处理
     */
    @ExceptionHandler(AccessDeniedException.class)
    public TiResult<String> accessDeniedException(AccessDeniedException ex, HttpServletResponse res) {
        prefix(ex);
        TiResult<String> result = TiResult.of(TiHttpErrCode.ACCESS_DENIED);
        res.setStatus(result.getCode());
        log.error("catch error\t{}", ex.getMessage(), ex);
        return result;
    }

    /**
     * 数据库异常处理
     */
    @ExceptionHandler(DataAccessException.class)
    public TiResult<String> dataAccessException(DataAccessException ex, HttpServletResponse res) {
        prefix(ex);
        TiResult<String> result = TiResult.of(TiHttpErrCode.FAIL);
        result.setMsg("数据库异常");
        res.setStatus(result.getCode());
        log.error("catch error\t{}", ex.getMessage(), ex);
        return result;
    }

    @ExceptionHandler(Exception.class)
    public TiResult<String> exception(Exception ex, HttpServletResponse res) {
        prefix(ex);
        return tiResponseHandle.exception(ex, res);
    }

}
