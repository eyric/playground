package org.example.common.exception;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 错误信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorInfo {

    /**
     * 错误码
     */
    private Integer code;

    /**
     * 错误描述
     */
    private String message;

    /**
     * 错误数据索引
     */
    private String position;

    /**
     * 错误属性
     */
    private String field;

    public ErrorInfo(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}