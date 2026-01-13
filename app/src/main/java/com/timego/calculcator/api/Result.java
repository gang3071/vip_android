package com.timego.calculcator.api;



import com.timego.calculcator.GsonUtils;

import java.io.Serializable;

/**
 * created by wmm on 2020/9/8
 */
public class Result<T> implements Serializable {

    public String msg;
    public int code;
    public T data;


    public boolean isSuccessful() {
        return code == 200;
    }

    @Override
    public String toString() {
        return "Result{" +
                "message='" + msg + '\'' +
                ", code=" + code +
                '}';
    }
}
