package com.topunion.chili.paylib.wechatpay;

import com.google.gson.annotations.SerializedName;
import com.topunion.chili.paylib.base.entity.BasePayOrder;

/**
 * Created by LinYi.
 * <p/>微信支付单实体
 */
public class WechatPayOrder extends BasePayOrder {
    @SerializedName("appid")
    private String appid;//公众账号ID
    @SerializedName("partnerid")
    private String partnerId;//商户号
    @SerializedName("prepayid")
    private String prepayId;//预支付交易会话ID
    @SerializedName("package")
    private String packageValue;//字段，暂填写固定值Sign=WXPay
    @SerializedName("noncestr")
    private String noncestr;//随机字符串
    @SerializedName("timestamp")
    private String timestamp;//时间戳
    @SerializedName("sign")
    private String sign;//签名

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public String getPrepayId() {
        return prepayId;
    }

    public void setPrepayId(String prepayId) {
        this.prepayId = prepayId;
    }

    public String getPackageValue() {
        return packageValue;
    }

    public void setPackageValue(String packageValue) {
        this.packageValue = packageValue;
    }

    public String getNoncestr() {
        return noncestr;
    }

    public void setNoncestr(String noncestr) {
        this.noncestr = noncestr;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
