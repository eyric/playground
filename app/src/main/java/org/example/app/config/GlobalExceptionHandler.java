package org.example.app.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.example.api.base.HttpResponse;
import org.example.api.base.MetaConf;
import org.example.common.enums.ErrorCode;
import org.slf4j.MDC;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public Object exceptionHandler(HttpServletRequest request, HttpServletResponse response, Exception e) {
        MDC.put("reqid", StringUtils.trimToEmpty(request.getParameter("reqid")));
        log.error("GlobalExceptionHandler_exceptionHandler_exception", e);
        HttpResponse<Void> result = new HttpResponse<>();
        result.setStatus(MetaConf.RESPONSE_STATUS_SYSTEM_ERROR);
        result.setErrors(ErrorCode.ERROR_SYSTEM_COMMON_ERROR.exception().getErrorInfos());
        return result;
    }
}
