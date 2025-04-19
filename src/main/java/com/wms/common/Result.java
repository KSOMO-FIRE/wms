package com.wms.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {
    private int code;//编码200/400
    private String message;// 成功/失败
    private Long total;//总记录数
    private Object data;//数据

    public static Result fail(){
        return result(400,"失败",0L,null);
    }

    public static Result fail(String err_message){
        return result(400,err_message,0L,null);
    }

    public static Result success(){
        return result(200,"成功",0L,null);
    }

    public static Result success(Object data){
        return result(200,"成功",0L,data);
    }

    public static Result success(Object data,Long total){
        return result(200,"成功",total,data);
    }

    private static Result result(int code,String message,Long total,Object data){
        Result res = new Result();
        res.setCode(code);
        res.setMessage(message);
        res.setTotal(total);
        res.setData(data);

        return res;
    }

}
