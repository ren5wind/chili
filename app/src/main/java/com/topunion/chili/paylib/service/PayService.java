package com.topunion.chili.paylib.service;

import android.app.Activity;
import android.util.Log;

import com.android.volley.toolbox.JsonObjectRequest;
import com.topunion.chili.paylib.net.core.ApiCallback;
import com.topunion.chili.paylib.net.core.ApiClient;
import com.topunion.chili.paylib.net.core.ApiHttpClient;
import com.topunion.chili.paylib.net.core.ApiJsonModelCallback;
import com.topunion.chili.paylib.net.interf.Callback;
import com.topunion.chili.paylib.net.interf.ServiceTask;
import com.topunion.chili.paylib.Constants;
import com.topunion.chili.paylib.alipay.AliPayOrder;
import com.topunion.chili.paylib.base.entity.CreateOrderResult;
import com.topunion.chili.paylib.base.entity.OrderRequest;
import com.topunion.chili.paylib.base.entity.PayResult;
import com.topunion.chili.paylib.wechatpay.WechatPayOrder;
import com.topunion.chili.util.GsonHelper;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by LinYi.
 */
public class PayService {

    boolean DEBUG = true;

    /**
     * 获取支付结果
     *
     * @param userId
     * @param outTradeNo
     */
    public void getPayResult(Activity activity, String url, Integer userId, String outTradeNo, ServiceTask<PayResult> task) {
        Map params = new HashMap();
        params.put("userId", userId);
        params.put("outTradeNo", outTradeNo);
        ApiJsonModelCallback callback = new ApiJsonModelCallback(task, PayResult.class);
        new ApiClient(activity, url, params, callback).doGet();
    }


    /**
     * 5.12. 下单/支付
     * <br/>说明 凡使用微信或支付宝支付的情况都叫下单，其他情况就是支付
     *
     * @param activity
     * @param orderRequest
     * @param task
     */
    public void createPayOrder(Activity activity, String url, OrderRequest orderRequest, final ServiceTask<CreateOrderResult> task) {
        final CreateOrderResult createOrderResult = new CreateOrderResult();
        JsonObjectRequest request = null;
        String jsonParams = GsonHelper.entity2JsonObject(orderRequest);
        Log.i("payParams====",jsonParams.toString());
        if (null == orderRequest.getThirdPartyMoney() || 0 == orderRequest.getThirdPartyMoney()) {
            request = doCommonPay(activity, url, task, createOrderResult, jsonParams);
        } else {
            if (orderRequest.getPayWays().contains(Constants.ALIPAY)) {
                //使用支付宝支付，回调支付订单
                request = doAliPay(activity, url, task, createOrderResult, jsonParams);
            } else if (orderRequest.getPayWays().contains(Constants.WECHATPAY)) {
                request = doWechatPay(activity, url, task, createOrderResult, jsonParams);
            }
        }
        task.addRequest(request);
    }



    /**
     * 微信支付
     *
     * @param activity
     * @param url
     * @param task
     * @param createOrderResult
     * @param jsonParams
     * @return
     */
    private JsonObjectRequest doWechatPay(Activity activity, String url, final ServiceTask<CreateOrderResult> task, final CreateOrderResult createOrderResult, String jsonParams) {
        return ApiHttpClient.getInstance(activity).doPost(url, jsonParams, new Callback<JSONObject>() {
            @Override
            public void onCall(int resultCode, JSONObject jsonObject) {
                if(DEBUG){
                }
                switch (resultCode) {
                    case Constants.STATE_CODE_SUCCESS:
                        WechatPayOrder wechatPayOrder = GsonHelper.getGson().fromJson(jsonObject.toString(), WechatPayOrder.class);
                        createOrderResult.setIsThirdParty(true);
                        createOrderResult.setPayWay(Constants.WECHATPAY);//使用支付宝支付
                        createOrderResult.setPayOrder(wechatPayOrder);
                        task.complete(Constants.STATE_CODE_SUCCESS, createOrderResult);
                        break;
                    default:
                        task.complete(resultCode, null);
                        break;
                }
            }
        });
    }

    /**
     * 支付宝
     *
     * @param activity
     * @param url
     * @param task
     * @param createOrderResult
     * @param jsonParams
     * @return
     */
    private JsonObjectRequest doAliPay(Activity activity, String url, final ServiceTask<CreateOrderResult> task, final CreateOrderResult createOrderResult, String jsonParams) {
        return ApiHttpClient.getInstance(activity).doPost(url, jsonParams, new Callback<JSONObject>() {
            @Override
            public void onCall(int resultCode, JSONObject jsonObject) {
                if(DEBUG){
                }
                switch (resultCode) {
                    case Constants.STATE_CODE_SUCCESS:
                        AliPayOrder aliPayOrder = GsonHelper.getGson().fromJson(jsonObject.toString(), AliPayOrder.class);
                        createOrderResult.setIsThirdParty(true);
                        createOrderResult.setPayWay(Constants.ALIPAY);//使用支付宝支付
                        createOrderResult.setPayOrder(aliPayOrder);
                        task.complete(Constants.STATE_CODE_SUCCESS, createOrderResult);
                        break;
                    default:
                        task.complete(resultCode, null);
                        break;
                }
            }
        });
    }

    /**
     * 普通支付，不涉及第三方支付平台
     *
     * @param activity
     * @param url
     * @param task
     * @param createOrderResult
     * @param jsonParams
     * @return
     */
    private JsonObjectRequest doCommonPay(Activity activity, String url, final ServiceTask<CreateOrderResult> task, final CreateOrderResult createOrderResult, String jsonParams) {
        return ApiHttpClient.getInstance(activity).doPost(url, jsonParams, new Callback<JSONObject>() {
            @Override
            public void onCall(int resultCode, JSONObject jsonObject) {
                if(DEBUG){
                }
                switch (resultCode) {
                    case Constants.STATE_CODE_SUCCESS:
                        PayResult payResult = GsonHelper.getGson().fromJson(jsonObject.toString(), PayResult.class);
                        createOrderResult.setIsThirdParty(false);
                        createOrderResult.setPayResult(payResult);//把支付结果丢给createOrderResult
                        task.complete(Constants.STATE_CODE_SUCCESS, createOrderResult);
                        break;
                    default:
                        task.complete(resultCode, null);
                        break;
                }
            }
        });
    }


    /**
     * 取消支付
     *
     * @param activity
     * @param url
     * @param userId
     * @param outTradeNo
     * @param task
     */
    public void cancelPay(Activity activity, String url, Integer userId, String outTradeNo, ServiceTask task) {
        Map params = new HashMap();
        params.put("userId", userId);
        params.put("outTradeNo", outTradeNo);
        ApiCallback callback = new ApiCallback(task);
        new ApiClient(activity, url, params, callback).doPost();
    }


}
