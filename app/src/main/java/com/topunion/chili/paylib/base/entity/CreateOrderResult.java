package com.topunion.chili.paylib.base.entity;

/**
 * Created by LinYi.
 * <br/>创建订单结果
 */
public class CreateOrderResult {

  //  private Integer resultCode;

    private boolean isThirdParty;//是否需要第三方支付

    private Integer payWay;//第三方支付代号

    private BasePayOrder payOrder;//当需要第三方支付时，该对象存储对应订单信息

    private PayResult payResult;//当不用第三方支付时，该对象存储获取的支付结果信息


    public PayResult getPayResult() {
        return payResult;
    }

    public void setPayResult(PayResult payResult) {
        this.payResult = payResult;
    }

   // public Integer getResultCode() {
        //return resultCode;
   // }

   // public void setResultCode(Integer resultCode) {
  //      this.resultCode = resultCode;
  //  }
    public Integer getPayWay() {
        return payWay;
    }

    public void setPayWay(Integer payWay) {
        this.payWay = payWay;
    }

    public boolean isThirdParty() {
        return isThirdParty;
    }

    public void setIsThirdParty(boolean isThirdParty) {
        this.isThirdParty = isThirdParty;
    }

    public BasePayOrder getPayOrder() {
        return payOrder;
    }

    public void setPayOrder(BasePayOrder payOrder) {
        this.payOrder = payOrder;
    }
}
