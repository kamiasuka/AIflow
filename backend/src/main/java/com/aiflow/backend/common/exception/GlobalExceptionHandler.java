package com.aiflow.backend.common.exception;

import com.aiflow.backend.common.response.JsonResult;
import com.aiflow.backend.common.response.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 * 处理所有由控制器Controller抛出的异常
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理业务层异常
     */
    @ExceptionHandler(ServiceException.class)
    public JsonResult doHandleServiceException(ServiceException ex) {
        log.error("ServiceException: {}", ex.getMessage());
        return new JsonResult(ex.getStatusCode(), ex.getMessage());
    }

    /**
     * 处理参数异常
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public JsonResult doHandleIllegalArgumentException(IllegalArgumentException ex) {
        String message = ex.getMessage();
        log.error("IllegalArgumentException: {}", message);
        return new JsonResult(StatusCode.VALIDATED_ERROR, message);
    }

    /**
     * 处理运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    public JsonResult doHandleRuntimeException(RuntimeException ex) {
        String message = ex.getMessage();
        log.error("RuntimeException: {}", message);
        return new JsonResult(StatusCode.OPERATION_FAILED, message);
    }

    /**
     * 处理参数校验异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public JsonResult doHandleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        String message = ex.getFieldError().getDefaultMessage();
        log.error("Validation error: {}", message);
        return new JsonResult(StatusCode.VALIDATED_ERROR, message);
    }

    /**
     * 处理所有未捕获的异常
     */
    @ExceptionHandler(Exception.class)
    public JsonResult doHandleException(Exception ex) {
        String message = ex.getMessage();
        log.error("Exception: {}", message, ex);
        return new JsonResult(StatusCode.OPERATION_FAILED, "系统异常: " + message);
    }

    /**
     * 处理Throwable异常
     */
    @ExceptionHandler(Throwable.class)
    public JsonResult doHandleThrowable(Throwable ex) {
        String message = ex.getMessage();
        log.error("Throwable: {}", message, ex);
        return new JsonResult(StatusCode.OPERATION_FAILED, "系统错误: " + message);
    }
}
