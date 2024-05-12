package org.example.common.exception;


import lombok.Getter;
import org.example.common.enums.ErrorCode;

@Getter
public class BaseException extends RuntimeException {
    private ErrorCode errorMapping;
    private ErrorContext errorContext;
    private Integer status;

    public BaseException(ErrorCode em) {
        this.errorMapping = em;
        this.errorContext = ErrorContext.current();
        if (em != null) {
            this.errorContext.code(em.getCode()).message(em.getMessage());
        }
    }

    public BaseException(String errorMsg) {
        this.errorContext = ErrorContext.current();
        this.errorMapping = ErrorCode.ERROR_SYSTEM_COMMON_ERROR;
        errorContext.code(ErrorCode.ERROR_SYSTEM_COMMON_ERROR.getCode()).message(errorMsg);
    }

    public BaseException(Throwable t) {
        this.errorContext = ErrorContext.current();
        this.errorContext.cause(t);
    }

    public static BaseException instance() {
        return new BaseException(ErrorCode.NONE) {
        };
    }

    public BaseException withContext(ErrorContext ctx) {
        this.errorContext = ctx;
        return this;
    }

    public BaseException ofMessage(String message) {
        this.errorContext.message(message);
        return this;
    }

    public BaseException ofCode(Integer code) {
        this.errorContext.code(code);
        return this;
    }

    public BaseException ofMapping(ErrorCode em) {
        this.errorMapping = em;
        if (em != null) {
            this.errorContext.code(em.getCode()).message(em.getMessage());
        }
        return this;
    }

    public BaseException ofCause(Throwable cause) {
        this.errorContext.cause(cause);
        return this;
    }

    public BaseException ofStatus(Integer status) {
        this.status = status;
        return this;
    }

    public void tryThrow() {
        throw this;
    }

    @Override
    public String toString() {
        return getErrorContext().toString();
    }
}
