package org.example.app.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.example.api.base.HttpResponse;
import org.example.api.base.MetaConf;
import org.example.common.cache.ThreadContextHolder;
import org.example.common.enums.ErrorCode;
import org.example.common.exception.BaseException;
import org.example.common.exception.ErrorInfo;
import org.example.common.util.JSON;
import org.example.common.util.StrUtil;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class InvokeInterceptor {
    private static final String REQ_ID = "reqid";
    private static final String IP = "ip";

    private final RequestHelper requestHelper;


    @Around("execution(* org.example.app.web..*Controller.*(..))")
    public Object invoke(ProceedingJoinPoint joinPoint) {

        HttpServletRequest request = requestHelper.getServletRequestAttributes().getRequest();
        String requestUrl = request.getRequestURI();
        String methodName = "";
        String returnResult = "";
        String inputParams = "";
        Integer status = MetaConf.RESPONSE_STATUS_SUCCESS;
        long cost = 0;
        long startTime = 0;
        long endTime = 0;
        try {
            startTime = System.currentTimeMillis();
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            methodName = signature.getName();
            inputParams = JSON.stringify(joinPoint.getArgs());
            setUpContext(request);
            Object result = joinPoint.proceed();
            endTime = System.currentTimeMillis();
            cost = endTime - startTime;
            returnResult = JSON.stringify(result);
            return result;
        } catch (BaseException baseException) {
            log.warn("http_invoke_biz_exception", baseException);
            status = MetaConf.RESPONSE_STATUS_FAIL;
            return addErrorInfoForResponse(baseException);
        } catch (Throwable e) {
            log.error("http_invoke_unknown_err", e);
            status = MetaConf.RESPONSE_STATUS_SYSTEM_ERROR;
            return addErrorInfoForResponse();
        } finally {
            log.info(
                    "method:{}\tparams:{}\tresponse:{}\tstartTime:{}\tendTime:{}\tusing:{}ms\tstatus:{}\turl:{}",
                    methodName,
                    inputParams,
                    returnResult,
                    startTime,
                    endTime,
                    cost,
                    status,
                    requestUrl);
            cleanUpContext();
        }
    }

    private <D> HttpResponse<D> addErrorInfoForResponse(BaseException baseException) {
        HttpResponse<D> response = new HttpResponse<>();
        ErrorInfo errorInfo = new ErrorInfo();
        errorInfo.setCode(baseException.getErrorMapping().getCode());
        errorInfo.setMessage(baseException.getErrorMapping().getMessage());

        response.setErrors(Collections.singletonList(errorInfo));
        response.setStatus(MetaConf.RESPONSE_STATUS_FAIL);
        return response;
    }

    private <D> HttpResponse<D> addErrorInfoForResponse() {
        HttpResponse<D> response = new HttpResponse<>();
        ErrorInfo errorInfo = new ErrorInfo();
        errorInfo.setCode(ErrorCode.ERROR_SYSTEM_COMMON_ERROR.getCode());
        errorInfo.setMessage(ErrorCode.ERROR_SYSTEM_COMMON_ERROR.getMessage());

        response.setErrors(Collections.singletonList(errorInfo));
        response.setStatus(MetaConf.RESPONSE_STATUS_SYSTEM_ERROR);
        return response;
    }

    private String getReqId(HttpServletRequest request) {
        if (request == null || StrUtil.isEmpty(request.getParameter(REQ_ID))) {
            return StrUtil.uuid();
        }

        return request.getParameter(REQ_ID);
    }

    private void setUpContext(HttpServletRequest httpServletRequest) {
        String remoteAddr = requestHelper.getIpAddress();
        String reqId = getReqId(httpServletRequest);
        ThreadContextHolder.setRequestIp(remoteAddr);
        ThreadContextHolder.setRequestId(reqId);
        MDC.put(IP, remoteAddr);
        MDC.put(REQ_ID, reqId);
    }

    private void cleanUpContext() {
        ThreadContextHolder.clean();
        MDC.clear();
    }

}
