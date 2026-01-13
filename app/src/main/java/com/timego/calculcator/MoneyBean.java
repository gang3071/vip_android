package com.timego.calculcator;

/**
 * **********************
 *
 * @Author bug machine
 * 创建时间： 2025/3/27 18:23
 * 用途
 * **********************
 */
public class MoneyBean {
    /**
     * 开始收钱 0   结束收钱 1 收钱 2
     */
    public int type = 0;
    public String typeDesc;
    public int money;

    public MoneyBean(int type, int money) {
        this.type = type;
        this.money = money;
        if(type == 0){
            this.typeDesc = "start_collecting_money";
        }else if(type == 2){
            this.typeDesc = "collecting_money";
        }else{
            this.typeDesc = "end_collecting_money";
        }
    }
}
