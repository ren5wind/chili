package com.topunion.chili.paylib.data;

import java.io.Serializable;

/**
 * Created by LinYi.
 * <br/>充值结果
 */
public class RechargeResult implements Serializable{

    private Integer result;//充值结果,state为200时才返回；1-等待充值, 2-充值成功, 3-充值失败, 4-取消充值
    private Integer money;//充值金额

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public Integer getMoney() {
        return money;
    }

    public void setMoney(Integer money) {
        this.money = money;
    }


}
