package com.topunion.chili.paylib;

import android.app.Activity;

import com.topunion.chili.paylib.net.interf.ServiceTask;
import com.topunion.chili.paylib.alipay.AliPay;
import com.topunion.chili.paylib.base.interf.IPay;
import com.topunion.chili.paylib.wechatpay.WechatPay;

/**
 * Created by LinYi.
 * <br/>支付方式工厂
 */
public class PayFactory {

    private static AliPay mAliPay;
    private static WechatPay mWechatPay;

    public static IPay getPay(Activity activity, Integer payWay,ServiceTask tast) {
        switch (payWay) {
            case Constants.ALIPAY:
                if (mAliPay == null) {
                    mAliPay = new AliPay(tast,activity);
                }
                return mAliPay;
            case Constants.WECHATPAY:
                if (mWechatPay == null) {
                    mWechatPay = new WechatPay(activity);
                }
                return mWechatPay;
            default:
                return null;
        }
    }

}
