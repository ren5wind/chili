package com.topunion.chili.paylib.wechatpay;

import android.app.Activity;
import android.text.TextUtils;
import android.widget.Toast;

import com.tencent.mm.sdk.constants.Build;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.topunion.chili.paylib.Constants;
import com.topunion.chili.paylib.SPUtil;
import com.topunion.chili.paylib.base.interf.IPay;

/**
 * Created by LinYi.<p/>
 * 微信支付对象
 */
public class WechatPay implements IPay<WechatPayOrder> {

    private IWXAPI mApi;
    private Activity mActivity;
    private final String TIP = "正在调起微信，请稍等...";;

    public WechatPay(Activity activity) {
        this.mActivity = activity;
    }

    @Override
    public void invoke(WechatPayOrder order) {
        Toast.makeText(mActivity, TIP, Toast.LENGTH_SHORT).show();
        if (mApi == null) {
            String spAppId = (String) SPUtil.get(mActivity, Constants.SP_KEY_WECHAT_APPID, "");
            if (TextUtils.isEmpty(spAppId) || !spAppId.equals(order.getAppid())) {
                SPUtil.put(mActivity, Constants.SP_KEY_WECHAT_APPID, order.getAppid());
            }
            mApi = WXAPIFactory.createWXAPI(mActivity, order.getAppid());
        }

        boolean isPaySupported = mApi.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
        if (!isPaySupported) {
            Toast.makeText(mActivity, "抱歉，微信不可用！", Toast.LENGTH_SHORT).show();
        }

        PayReq req = new PayReq();
        req.appId = order.getAppid();
        req.partnerId = order.getPartnerId();
        req.prepayId = order.getPrepayId();
        req.nonceStr = order.getNoncestr();
        req.timeStamp = order.getTimestamp();
        req.packageValue = order.getPackageValue();
        req.sign = order.getSign();
        mApi.sendReq(req);//发起请求调起微信支付
    }
}

