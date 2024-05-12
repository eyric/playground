package org.example.app.web;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.example.api.base.HttpResponse;
import org.example.api.base.MetaConf;
import org.example.app.web.validator.ParamValidator;
import org.example.app.web.validator.ValidateErrors;
import org.example.common.enums.ErrorCode;
import org.example.common.exception.BizException;
import org.example.common.exception.ErrorInfo;
import org.example.common.util.CollUtil;
import org.example.common.util.StrUtil;
import org.springframework.validation.BindingResult;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
public class BaseController {

    public <D> HttpResponse<D> act(Supplier<D> supplier) {
        StackTraceElement stackTrace = Thread.currentThread().getStackTrace()[2];
        String className = StrUtil.splitAndGetLast(stackTrace.getClassName(), "\\.");
        String methodName = stackTrace.getMethodName();

        return act(supplier, null, null, null, null, className, methodName);
    }

    public <D> HttpResponse<D> act(Supplier<D> supplier, BindingResult bindingResult) {
        StackTraceElement stackTrace = Thread.currentThread().getStackTrace()[2];
        String className = StrUtil.splitAndGetLast(stackTrace.getClassName(), "\\.");
        String methodName = stackTrace.getMethodName();

        return act(supplier, null, null, bindingResult, null, className, methodName);
    }

    public <D> HttpResponse<D> act(Supplier<D> supplier, String moduleName, String methodName) {
        return act(supplier, null, null, null, null, moduleName, methodName);
    }

    public <D> HttpResponse<D> act(Supplier<D> supplier, BindingResult bindingResult, String moduleName, String methodName) {
        return act(supplier, null, null, bindingResult, null, moduleName, methodName);
    }

    public <D, T> HttpResponse<D> act(Supplier<D> supplier, ParamValidator<T> validator, T request,
                                      String moduleName, String methodName) {
        return act(supplier, validator, request, null, null, moduleName, methodName);
    }

    public <D, T> HttpResponse<D> act(Supplier<D> supplier, ParamValidator<T> validator, T request,
                                      BindingResult bindingResult, Integer originalSize, String moduleName, String methodName) {
        HttpResponse<D> response = new HttpResponse<>();
        var failedSize = 0;
        try {
            // JSR-303 参数校验
            if (bindingResult != null && bindingResult.hasErrors()) {
                List<ErrorInfo> errorInfos = bindingResult.getFieldErrors().stream().map(item -> {
                    ErrorInfo errorInfo = new ErrorInfo();
                    errorInfo.setCode(ErrorCode.ERROR_PARAM_COMMON_ERROR.getCode());
                    errorInfo.setMessage(item.getDefaultMessage());
                    errorInfo.setField(item.getField());
                    return errorInfo;
                }).collect(Collectors.toList());

                throw new BizException(errorInfos);
            }

            // 自定义参数校验
            if (validator != null) {
                ValidateErrors errors = validator.validate(request);
                if (errors.hasErrors()) {
                    throw new BizException(errors.getErrors());
                }
            }

            D data = Optional.ofNullable(supplier).map(Supplier::get).orElse(null);
            response.setData(data);
            if (originalSize != null && data instanceof List) {
                val successSize = ((List<?>) data).size();
                failedSize = originalSize - successSize;
                if (successSize == 0) {
                    throw ErrorCode.ERROR_BATCH_FAILED.exception();
                } else if (successSize < originalSize) {
                    throw ErrorCode.ERROR_BATCH_FAILED.exception();
                }
            }
            response.setStatus(MetaConf.RESPONSE_STATUS_SUCCESS);
        } catch (BizException be) {
            if (CollUtil.isNotEmpty(be.getErrorInfos())) {
                response.setErrors(be.getErrorInfos());
            } else {
                if (originalSize == null) {
                    addError(response, be.getErrorMapping());
                } else {
                    addErrorList(response, be.getErrorMapping(), failedSize);
                }
            }
            log.warn("{}_{}_caughtError: {}", moduleName, methodName, be.getErrorMapping());
        } catch (Exception e) {
            addError(response, ErrorCode.ERROR_SYSTEM_COMMON_ERROR);
            log.error("{}_{}_uncaughtError", moduleName, methodName, e);
        }
        return response;
    }

    public <D> void addError(HttpResponse<D> response, ErrorCode errorMapping) {
        val errorInfo = new ErrorInfo();
        errorInfo.setCode(errorMapping.getCode());
        errorInfo.setMessage(errorMapping.getMessage());
        response.setErrors(Collections.singletonList(errorInfo));
        if (response.getStatus() == null) {
            response.setStatus(MetaConf.RESPONSE_STATUS_FAIL);
        }
    }

    public <D> void addErrorList(HttpResponse<D> response, ErrorCode errorMapping, Integer failedSize) {
        val errors = IntStream.range(0, failedSize).mapToObj(index -> {
            ErrorInfo errorInfo = new ErrorInfo();
            errorInfo.setCode(errorMapping.getCode());
            errorInfo.setMessage(errorMapping.getMessage());
            return errorInfo;
        }).collect(Collectors.toList());
        response.setErrors(errors);
        if (response.getStatus() == null) {
            response.setStatus(MetaConf.RESPONSE_STATUS_FAIL);
        }
    }

    protected <D> HttpResponse<D> addResultForResponse(D data) {
        HttpResponse<D> response = new HttpResponse<>();
        response.setData(data);
        response.setStatus(MetaConf.RESPONSE_STATUS_SUCCESS);
        return response;
    }
}
