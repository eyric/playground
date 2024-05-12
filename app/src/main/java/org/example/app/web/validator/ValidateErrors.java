package org.example.app.web.validator;

import org.example.common.enums.ErrorCode;
import org.example.common.exception.BizException;
import org.example.common.exception.ErrorInfo;

import java.util.List;


public interface ValidateErrors {

    boolean hasErrors();

    List<ErrorInfo> getErrors();

    void reject(String field, ErrorCode errorMapping);

    void reject(String field, BizException apiException);

}
