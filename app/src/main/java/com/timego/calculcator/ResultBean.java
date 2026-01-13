package com.timego.calculcator;

/**
 * **********************
 *
 * @Author bug machine
 * 创建时间： 2025/3/28 9:12
 * 用途
 * **********************
 */
public class ResultBean {
    public  int code = 200;
    public MoneyBean data = null;

    public ResultBean(int code, MoneyBean data) {
        this.code = code;
        this.data = data;
    }
}
