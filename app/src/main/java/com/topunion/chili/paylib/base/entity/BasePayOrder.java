package com.topunion.chili.paylib.base.entity;

/**
 * Created by LinYi.
 * <p/>支付订单父类
 */
public class BasePayOrder {
   // protected Integer orderId;//支付单id
    protected String outTradeNo;//商户订单号

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }
}
