package com.topunion.chili.paylib.config;

/**
 * PayHelper的配置
 */
public class PayHelperConfig {
    private String createPayOrderUrl;//创建订单url
    private String payUrl;//支付url(暂同创建订单url)
    private String cancelPayUrl;//取消支付url
    private String obtainWechatPayConfigUrl;//获取微信支付配置url
    private String obtainAliPayConfigUrl;//获取支付宝配置url
    private String obtainPayResultUrl;//获取支付结果url

    public String getObtainPayResultUrl() {
        return obtainPayResultUrl;
    }

    public void setObtainPayResultUrl(String obtainPayResultUrl) {
        this.obtainPayResultUrl = obtainPayResultUrl;
    }

    public String getCreatePayOrderUrl() {
        return createPayOrderUrl;
    }

    public void setCreatePayOrderUrl(String createPayOrderUrl) {
        this.createPayOrderUrl = createPayOrderUrl;
    }

    public String getPayUrl() {
        return payUrl;
    }

    public void setPayUrl(String payUrl) {
        this.payUrl = payUrl;
    }

    public String getCancelPayUrl() {
        return cancelPayUrl;
    }

    public void setCancelPayUrl(String cancelPayUrl) {
        this.cancelPayUrl = cancelPayUrl;
    }

    public String getObtainWechatPayConfigUrl() {
        return obtainWechatPayConfigUrl;
    }

    public void setObtainWechatPayConfigUrl(String obtainWechatPayConfigUrl) {
        this.obtainWechatPayConfigUrl = obtainWechatPayConfigUrl;
    }

    public String getObtainAliPayConfigUrl() {
        return obtainAliPayConfigUrl;
    }

    public void setObtainAliPayConfigUrl(String obtainAliPayConfigUrl) {
        this.obtainAliPayConfigUrl = obtainAliPayConfigUrl;
    }
}
