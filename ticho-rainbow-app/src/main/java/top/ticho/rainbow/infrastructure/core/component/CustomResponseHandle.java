package top.ticho.rainbow.infrastructure.core.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataAccessException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import top.ticho.boot.log.interceptor.WebLogInterceptor;
import top.ticho.boot.view.core.Result;
import top.ticho.boot.view.enums.HttpErrCode;
import top.ticho.boot.view.log.HttpLog;
import top.ticho.boot.web.handle.BaseResponseHandle;

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

    @Autowired
    private BaseResponseHandle baseResponseHandle;

    public void prefix(Exception ex) {
        HttpLog httpLog = WebLogInterceptor.logInfo();
        if (Objects.nonNull(httpLog)) {
            httpLog.setErrMessage(ex.getMessage());
        }
    }

    /**
     * 权限不足处理
     */
    @ExceptionHandler(AccessDeniedException.class)
    public Result<String> accessDeniedException(AccessDeniedException ex, HttpServletResponse res) {
        prefix(ex);
        Result<String> result = Result.of(HttpErrCode.ACCESS_DENIED);
        res.setStatus(result.getCode());
        log.error("catch error\t{}", ex.getMessage(), ex);
        return result;
    }

    /**
     * 数据库异常处理
     */
    @ExceptionHandler(DataAccessException.class)
    public Result<String> dataAccessException(DataAccessException ex, HttpServletResponse res) {
        prefix(ex);
        Result<String> result = Result.of(HttpErrCode.FAIL);
        result.setMsg("数据库异常");
        res.setStatus(result.getCode());
        log.error("catch error\t{}", ex.getMessage(), ex);
        return result;
    }

    @ExceptionHandler(Exception.class)
    public Result<String> exception(Exception ex, HttpServletResponse res) {
        prefix(ex);
        return baseResponseHandle.exception(ex, res);
    }

}
