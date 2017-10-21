package com.topunion.chili.paylib;

import static com.topunion.chili.net.HttpHelper.DEBUG_SERVER;

/**
 * Created by LinYi.
 */
public class Constants {

    public static final String SP_KEY_WECHAT_APPID = "wx0d3ea2ea23caf1f5";//SharedPreference里面存放微信支付appid的key
    public static String SP_NAME = "yyjg_data";


    //1-支付宝，2-微信支付，3-红包，4-天台币
    public static final int WECHATPAY = 2;//微信支付
    public static final int ALIPAY = 1;//支付宝支付

    public static final int STATE_CODE_CANCEL_PAY = -23;//用户取消支付code

    //状态
    public final static int STATE_CODE_SUCCESS = 200;
    public final static int STATE_CODE_FAILED = 300;

    public static final int WECHAT_PAY_RESP_SUCCESS = 0;//微信客户端回调支付结果：成功
    public static final int WECHAT_PAY_RESP_ERROR = -1;//微信客户端回调支付结果：错误  可能的原因：签名错误、未注册APPID、项目设置APPID不正确、注册的APPID与设置的不匹配、其他异常等。
    public static final int WECHAT_PAY_RESP_CANCEL = -2;//微信客户端回调支付结果：用户取消   无需处理。发生场景：用户不支付了，点击取消，返回APP。

    public final static int STATE_CODE_WAIT = 8000;//支付宝，在处理支付结果，去查服务端

    public final static String ALIPAY_PACKAGENAME = "com.eg.android.AlipayGphone";//支付宝包名

    public static final String API_CREATE_RECHARGE_ORDER = DEBUG_SERVER + "user/api/rechargeOrders";//5.16. 充值下单
//    public static final String API_GET_RECHARGE_RESULT = DEBUG_SERVER + "record/recharge/result";//5.19. 查看充值结果

    public final static int PAYWAY_OTHER = -1;//其他支付方式
    public final static int PAYWAY_ALIPAY = 1;//支付宝
    public final static int PAYWAY_WECHAT = 2;//微信支付
    public final static int PAYWAY_REPACK = 3;//红包支付
    public final static int PAYWAY_BALANCE = 4;//使用余额

}
