package org.example.api.base;

import lombok.Data;
import org.example.common.exception.ErrorInfo;

import java.util.List;

@Data
public class HttpResponse<T> {

    /**
     * 数据对象
     */
    private T data;

    /**
     * 状态码
     */
    private Integer status;

    /**
     * 错误信息
     */
    private List<ErrorInfo> errors;

}