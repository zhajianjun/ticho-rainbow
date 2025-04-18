package top.ticho.rainbow.infrastructure.common.advice;

import cn.hutool.core.exceptions.ExceptionUtil;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import top.ticho.starter.log.interceptor.TiWebLogInterceptor;
import top.ticho.starter.view.core.TiResult;
import top.ticho.starter.view.enums.TiBizErrCode;
import top.ticho.starter.view.enums.TiHttpErrCode;
import top.ticho.starter.view.log.TiHttpLog;
import top.ticho.starter.web.advice.TiResponseBodyAdvice;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;

/**
 * 自定义异常处理
 *
 * @author zhajianjun
 * @date 2024-04-18 17:35
 */
@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
@Order(100)
public class CustomResponseBodyAdvice {

    private final TiResponseBodyAdvice tiResponseBodyAdvice;
    private final HttpServletResponse response;

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
    public TiResult<String> accessDeniedExceptionHandler(AccessDeniedException ex) {
        prefix(ex);
        TiResult<String> result = TiResult.of(TiHttpErrCode.ACCESS_DENIED);
        response.setStatus(result.getCode());
        log.error("catch error\t{}", ex.getMessage(), ex);
        return result;
    }

    /**
     * 参数校验异常处理
     */
    @ExceptionHandler(BindException.class)
    public TiResult<String> bindExceptionExceptionHandler(BindException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        StringJoiner joiner = new StringJoiner(",", "{", "}");
        List<FieldError> errors = fieldErrors
            .stream()
            .sorted(Comparator.comparing(ObjectError::getObjectName))
            .peek(next -> joiner.add(next.getField() + ":" + next.getDefaultMessage()))
            .toList();
        log.warn("catch BindException error\t{}", joiner);
        response.setStatus(HttpStatus.OK.value());
        return TiResult.fail(TiBizErrCode.PARAM_ERROR, errors.get(0).getDefaultMessage());
    }

    /**
     * 参数校验异常处理
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public TiResult<String> constraintViolationExceptionHandler(ConstraintViolationException ex) {
        Set<ConstraintViolation<?>> fieldErrors = ex.getConstraintViolations();
        StringJoiner joiner = new StringJoiner(",", "{", "}");
        List<ConstraintViolation<?>> errors = fieldErrors
            .stream()
            .sorted(Comparator.comparing(ConstraintViolation::getMessage))
            .peek(next -> joiner.add(next.getPropertyPath() + ":" + next.getMessage()))
            .toList();
        log.warn("catch ConstraintViolationException error\t{}", joiner);
        response.setStatus(HttpStatus.OK.value());
        return TiResult.fail(TiBizErrCode.PARAM_ERROR, errors.get(0).getMessage());
    }

    /**
     * 数据库异常处理
     */
    @ExceptionHandler(DataAccessException.class)
    public TiResult<String> dataAccessExceptionHandler(DataAccessException ex) {
        prefix(ex);
        TiResult<String> result = TiResult.of(TiHttpErrCode.FAIL);
        result.setMsg("数据库异常");
        response.setStatus(result.getCode());
        log.error("catch error\t{}", ex.getMessage(), ex);
        return result;
    }

    @ExceptionHandler(Exception.class)
    public TiResult<String> exception(Exception ex) {
        prefix(ex);
        return tiResponseBodyAdvice.exception(ex);
    }

}
