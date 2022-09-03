package com.max.reggle.utli;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 麦家宝
 * @version 1.0
 */
@Data
public class R<T> {
    private String msg;//错误消息
    private Integer code;//编码 1成功，0和其他失败
    private T data;//数据
    private Map map = new HashMap();//动态数据

    public static <T> R<T> success(T object){
        R<T> r = new R<T>();
        r.data = object;
        r.code = 1;
        return  r;
    }
    public static <T> R<T> error(String mag){
        R r = new R();
        r.msg = mag;
        r.code = 0;
        return r;
    }
    public R<T> add(String key,Object value){
        this.map.put(key,value);
        return this;
    }

}
