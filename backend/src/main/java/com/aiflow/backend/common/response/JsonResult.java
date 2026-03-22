package com.aiflow.backend.common.response;


import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class JsonResult {
    //自定义状态码，提示消息，具体数据
    private Integer code;
    private String msg;
    private Object data;


    public JsonResult(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public JsonResult(StatusCode statusCode){
        this.code = statusCode.getCode();
        this.msg = statusCode.getMsg();
    }

    public JsonResult(StatusCode statusCode, Object data){
        this.code = statusCode.getCode();
        this.msg = statusCode.getMsg();
        this.data = data;
    }

    /*
            | 1000 |    未登录    |
            | 1001 |   登录成功   |
            | 1002 |   密码错误   |
            | 1003 |  用户名错误  |
            | 2001 |   操作成功   |
            | 2002 |   操作失败   |
            */

    public static JsonResult ok(Object data){
        return new JsonResult(StatusCode.OPERATION_SUCCESS, data);
    }

    public static JsonResult fail(StatusCode statusCode,String msg){
        JsonResult jsonResult = new JsonResult();
        jsonResult.code = statusCode.getCode();
        jsonResult.msg = msg;
        return jsonResult;
    }
    public static JsonResult ok(){
        return ok(null);
    }
}