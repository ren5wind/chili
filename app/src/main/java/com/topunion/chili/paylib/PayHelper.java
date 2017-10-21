package com.topunion.chili.paylib;

import android.app.Activity;

import com.tencent.mm.sdk.modelbase.BaseResp;
import com.topunion.chili.paylib.net.interf.ServiceTask;
import com.topunion.chili.paylib.base.entity.BasePayOrder;
import com.topunion.chili.paylib.base.entity.CreateOrderResult;
import com.topunion.chili.paylib.base.entity.OrderRequest;
import com.topunion.chili.paylib.base.entity.PayResult;
import com.topunion.chili.paylib.config.PayHelperConfig;
import com.topunion.chili.paylib.service.PayService;
import com.topunion.chili.paylib.wechatpay.WechatClientResponseEvent;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;

/**
 * 对外暴露的接口：<br/>
 * 1、{@link #createPayOrder(Activity, OrderRequest, ServiceTask)} 当需要第三方支付时：创建订单，当不用第三方支付时：直接支付<br/>
 * 2、{@link #pay(Activity, Integer, BasePayOrder, ServiceTask)}  纯粹为第三方支付调用。当需要第三方支付时，才调用此接口。<br/>
 * 2、{@link #cancelPay(Activity, Integer, String, ServiceTask)}  取消支付
 * <br/>Created by LinYi.
 */
public class PayHelper {

    PayService mPayService;
    private EventBus mEventBus = EventBus.getDefault();

    PayHelperConfig mPayHelperConfig;

    BasePayOrder mPayOrder;
    Activity mActivity;
    ServiceTask mPayResultServiceTask;
    Integer mUserId;

    private PayHelper(PayHelperConfig config) {
        mPayService = new PayService();
        mEventBus.register(this);
        this.mPayHelperConfig = config;
    }

    private static PayHelper mInstance;

    public static PayHelper getInstance(PayHelperConfig config) {
        if (null == mInstance)
            mInstance = new PayHelper(config);
        return mInstance;
    }

    /**
     * 创建订单
     *
     * @param activity
     * @param orderRequest
     * @param task
     */
    public void createPayOrder(Activity activity, OrderRequest orderRequest, final ServiceTask<CreateOrderResult> task) {
        mUserId = orderRequest.getUserId();
        mPayService.createPayOrder(activity, mPayHelperConfig.getCreatePayOrderUrl(), orderRequest, task);
    }


    /**
     * 注：此函数纯粹为第三方支付调用。当需要第三方支付时，才调用此接口。
     * <br/>执行前添加了获取支付方式配置信息操作
     *
     * @param payWay 第三方支付代号
     * @param order
     * @param task
     */
    public void pay(final Activity activity, Integer payWay, final BasePayOrder order, final ServiceTask task) {
        this.mPayResultServiceTask = task;
        this.mPayOrder = order;
        this.mActivity = activity;
        PayFactory.getPay(activity, payWay,task).invoke(order);//唤起支付客户端进行支付
    }


    /**
     * 获取支付结果
     *
     * @param userId
     * @param outTradeNo
     */
    public void getPayResult(Activity activity, Integer userId, String outTradeNo, ServiceTask<PayResult> task) {
        mPayService.getPayResult(activity, mPayHelperConfig.getObtainPayResultUrl(), userId, outTradeNo, task);
    }

    /**
     * 5.14. 微信/支付宝取消支付
     *
     * @param activity
     * @param userId
     * @param outTradeNo
     * @param task
     */
    public void cancelPay(Activity activity, Integer userId, String outTradeNo, final ServiceTask task) {
        mPayService.cancelPay(activity, mPayHelperConfig.getCancelPayUrl(), userId, outTradeNo, task);
    }


    /**
     * 订阅等待接收微信支付客户端返回的支付结果<p/>
     * 0 	成功 	展示成功页面<br/>
     * -1 	错误 	可能的原因：签名错误、未注册APPID、项目设置APPID不正确、注册的APPID与设置的不匹配、其他异常等。<br/>
     * -2 	用户取消 	无需处理。发生场景：用户不支付了，点击取消，返回APP。
     *
     * @param event
     */
    @Subscribe
    public void onWechatPayResponse(WechatClientResponseEvent event) {
        BaseResp response = event.getResponse();
        switch (response.errCode) {
            case Constants.WECHAT_PAY_RESP_SUCCESS:
                mPayResultServiceTask.complete(Constants.STATE_CODE_SUCCESS,null);//成功
                break;
            case Constants.WECHAT_PAY_RESP_ERROR:
                mPayResultServiceTask.complete(Constants.STATE_CODE_FAILED, null);//失败
                break;
            case Constants.WECHAT_PAY_RESP_CANCEL:
                mPayResultServiceTask.complete(Constants.STATE_CODE_CANCEL_PAY, null);//用户取消
                break;
        }
    }


}
