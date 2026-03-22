package com.aiflow.backend.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StatusCode{
    NOT_LOGIN(1000,"未登录"),
    LOGIN_SUCCESS(1001,"登录成功"),
    PASSWORD_ERROR(1002,"密码错误"),
    USERNAME_ERROR(1003,"用户名错误"),
    LOGIN_OUT_TIME(1004,"登录超时"),
    OPERATION_SUCCESS(2001,"操作成功"),
    OPERATION_FAILED(2002,"操作失败"),
    VALIDATED_ERROR(3002,"参数校验失败");
    private Integer code;
    private String msg;
}
