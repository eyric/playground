package org.example.common.exception;


import org.example.common.enums.ErrorCode;
import org.example.common.util.JSON;
import org.example.common.util.StrUtil;

import java.util.Collections;
import java.util.List;

public final class ErrorContext {

    private static final ThreadLocal<ErrorContext> LOCAL = new ThreadLocal<>();

    private Integer errorCode;
    private String errorMessage;
    private Class<?> errorClass;
    private String errorMethod;
    private Throwable cause;

    private ErrorContext() {
    }

    public static ErrorContext current() {
        ErrorContext context = LOCAL.get();
        if (context == null) {
            context = new ErrorContext();
            LOCAL.set(context);
        }
        return context;
    }

    public boolean isClear() {
        return StrUtil.isEmpty(errorMessage) && cause == null && errorCode == null;
    }

    public ErrorContext code(Integer errorCode) {
        this.errorCode = errorCode;
        return this;
    }

    public ErrorContext message(String errorMessage) {
        this.errorMessage = errorMessage;
        return this;
    }

    public ErrorContext errorClass(Class<?> errorClass) {
        this.errorClass = errorClass;
        return this;
    }

    public ErrorContext method(String errorMethod) {
        this.errorMethod = errorMethod;
        return this;
    }

    public ErrorContext cause(Throwable cause) {
        this.cause = cause;
        return this;
    }

    public ErrorContext ofMapping(ErrorCode em) {
        this.errorCode = em.getCode();
        this.errorMessage = em.getMessage();
        return this;
    }

    public ErrorContext reset() {
        errorCode = null;
        errorMessage = null;
        errorClass = null;
        errorMethod = null;
        cause = null;
        LOCAL.remove();
        return this;
    }

    public List<ErrorInfo> getErrors() {
        ErrorInfo errorInfo = new ErrorInfo();
        errorInfo.setCode(this.errorCode);
        errorInfo.setMessage(this.errorMessage);
        errorInfo.setPosition(this.errorClass + "|" + this.errorMethod);
        return Collections.singletonList(errorInfo);
    }

    @Override
    public String toString() {
        return JSON.stringify(this);
    }

}