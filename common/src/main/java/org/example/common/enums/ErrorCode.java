package org.example.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.common.exception.BizException;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    NONE(0, ""),
    ERROR_SYSTEM_COMMON_ERROR(1, "系统异常"),
    ERROR_AUTH_COMMON_ERROR(2, "权限异常"),
    ERROR_PARAM_COMMON_ERROR(3, "参数异常"),
    ERROR_BATCH_EXCEED_ERROR(4, "超过最大批量数"),
    ERROR_BATCH_FAILED(5, "分批处理异常");

    private final int code;
    private final String message;

    public BizException exception() {
        return new BizException(this);
    }

    public BizException exception(String msg) {
        return new BizException(msg);
    }
}
