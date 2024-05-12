package org.example.app.web.validator;


import org.example.common.enums.ErrorCode;
import org.example.common.exception.BizException;
import org.example.common.exception.ErrorInfo;
import org.example.common.util.CollUtil;

import java.util.ArrayList;
import java.util.List;


public class SimpleValidateErrors implements ValidateErrors {

    private final List<ErrorInfo> errors = new ArrayList<>();

    public static SimpleValidateErrors newInstance() {
        return new SimpleValidateErrors();
    }

    @Override
    public boolean hasErrors() {
        return CollUtil.isNotEmpty(errors);
    }

    @Override
    public List<ErrorInfo> getErrors() {
        return errors;
    }

    @Override
    public void reject(String field, ErrorCode errorMapping) {
        ErrorInfo errorInfo = new ErrorInfo();
        errorInfo.setField(field);
        errorInfo.setCode(errorMapping.getCode());
        errorInfo.setMessage(errorMapping.getMessage());
        errors.add(errorInfo);
    }

    @Override
    public void reject(String field, BizException apiException) {
        ErrorInfo errorInfo = new ErrorInfo();
        errorInfo.setField(field);
        errorInfo.setMessage(apiException.getMessage());
        errors.add(errorInfo);
    }
}
