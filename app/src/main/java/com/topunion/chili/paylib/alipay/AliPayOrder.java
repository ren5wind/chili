package com.topunion.chili.paylib.alipay;

import com.topunion.chili.paylib.base.entity.BasePayOrder;

/**
 * 支付宝实体
 * Created by Administrator on 2015/12/24.
 */
public class AliPayOrder extends BasePayOrder {

    private String payParams;//支付订单(直接使用)
    private String publicKey;//公钥(解析支付宝结果会用到)

    public String getPayParams() {
        return payParams;
    }

    public void setPayParams(String payParams) {
        this.payParams = payParams;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }
}
