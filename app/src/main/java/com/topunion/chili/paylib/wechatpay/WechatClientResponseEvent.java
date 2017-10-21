package com.topunion.chili.paylib.wechatpay;

import com.tencent.mm.sdk.modelbase.BaseResp;

/**
 * Created by LinYi.</br>
 * 微信客户端支付结果回调通知Event
 */
public class WechatClientResponseEvent {

    private BaseResp response;

    public WechatClientResponseEvent(BaseResp response) {
        this.response = response;
    }

    public BaseResp getResponse() {
        return response;
    }
}
