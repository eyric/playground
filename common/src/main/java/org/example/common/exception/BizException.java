package org.example.common.exception;


import lombok.Getter;
import org.example.common.enums.ErrorCode;

import java.util.List;

@Getter
public class BizException extends BaseException {

    private List<ErrorInfo> errorInfos;

    public BizException() {
        super(ErrorCode.NONE);
    }

    public BizException(List<ErrorInfo> errorInfos) {
        super(ErrorCode.NONE);
        this.errorInfos = errorInfos;
    }

    public BizException(ErrorCode errorMapping) {
        super(errorMapping);
    }

    public BizException(String errorMsg) {
        super(errorMsg);
    }


}
