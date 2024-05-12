package org.example.api.base;

public class MetaConf {
    /**
     * 全部成功
     */
    public static final Integer RESPONSE_STATUS_SUCCESS = 200;
    /**
     * 部分成功
     */
    public static final Integer RESPONSE_STATUS_PARTLY_SUCCESS = 300;
    /**
     * 全部失败
     */
    public static final Integer RESPONSE_STATUS_FAIL = 400;
    /**
     * 系统错误/异常
     */
    public static final Integer RESPONSE_STATUS_SYSTEM_ERROR = 500;
    /**
     * 参数错误/异常
     */
    public static final Integer RESPONSE_STATUS_PARAM_ERROR = 600;
    /**
     * 权限验证失败
     */
    public static final Integer RESPONSE_STATUS_AUTHORITY_ERROR = 700;
}
