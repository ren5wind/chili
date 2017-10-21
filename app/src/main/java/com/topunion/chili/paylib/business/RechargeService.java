package com.topunion.chili.paylib.business;

import com.topunion.chili.business.AccountManager;
import com.topunion.chili.paylib.data.RechargeResult;
import com.topunion.chili.paylib.net.core.ApiCallback;
import com.topunion.chili.paylib.net.core.ApiClient;
import com.topunion.chili.paylib.net.core.ApiJsonModelCallback;
import com.topunion.chili.paylib.net.interf.ServiceTask;
import com.topunion.chili.paylib.Constants;
import com.topunion.chili.paylib.alipay.AliPayOrder;
import com.topunion.chili.paylib.base.entity.BasePayOrder;
import com.topunion.chili.paylib.wechatpay.WechatPayOrder;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by LinYi.
 * <br/>充值
 */
public class RechargeService extends BaseService {


    /**
     * 创建充值单(即支付单)
     *
     * @param payWay      1-支付宝，2-微信支付
     * @param chargeMoney 充值金额1-10000000
     */
    public void createRechargeOrder(int payWay, int chargeMoney, ServiceTask<? extends BasePayOrder> task) {
        Map params = new HashMap();
        params.put("userId", AccountManager.getInstance().getUserId());
        params.put("payWay", payWay);
        params.put("money", chargeMoney);
        ApiJsonModelCallback callback = null;
        switch (payWay) {
            case Constants.PAYWAY_WECHAT:
                callback = new ApiJsonModelCallback(task, WechatPayOrder.class);
                break;
            case Constants.PAYWAY_ALIPAY:
                callback = new ApiJsonModelCallback(task, AliPayOrder.class);
                break;
        }
        new ApiClient(mContext, Constants.API_CREATE_RECHARGE_ORDER, params, callback).doPost();
    }
//    /**
//     * 5.19. 查看充值结果
//     * @param outTradeNo
//     */
//    public void getRechargeResult(String outTradeNo,ServiceTask<RechargeResult> task){
//        Map params = new HashMap();
//        params.put("outTradeNo",outTradeNo);
//        ApiJsonModelCallback callback = new ApiJsonModelCallback(task,RechargeResult.class);
//        new ApiClient(mContext,Constants.API_GET_RECHARGE_RESULT,params,callback).doGet();
//    }
//    /**
//     * 取消充值
//     * @param outTradeNo
//     * @param task
//     */
//    public void cancelRecharge(String outTradeNo,ServiceTask task){
//        Map params = new HashMap();
//        params.put("userId",AccountManager.getInstance().getUserId());
//        params.put("outTradeNo",outTradeNo);
//        ApiCallback callback = new ApiCallback(task);
//        new ApiClient(mContext,Constants.API_CANCEL_RECHARGE,params,callback).doPost();
//    }



}