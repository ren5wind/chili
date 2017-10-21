package com.topunion.chili.paylib.alipay;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.topunion.chili.paylib.net.interf.ServiceTask;
import com.topunion.chili.paylib.Constants;
import com.topunion.chili.paylib.base.entity.PayResult;
import com.topunion.chili.paylib.base.interf.IPay;

/**
 * TODO 重写调起支付宝支付方法{@link #invoke(AliPayOrder)}
 */
public class AliPay implements IPay<AliPayOrder> {

    private static final int SDK_PAY_FLAG = 1;//支付结果
    private static final int SDK_CHECK_FLAG = 2;//检查结果
    private Activity mActivity;
    ServiceTask tast;

    public AliPay(ServiceTask tast, Activity activity) {
        this.tast = tast;
        this.mActivity = activity;
    }


    @Override
    public void invoke(AliPayOrder aliPayOrder) {
        pay(aliPayOrder);
    }

    /**
     * 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
     * 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
     * 判断resultStatus 为非“9000”则代表可能支付失败
     * “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
     * 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
     */
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    int code = Constants.STATE_CODE_FAILED;
                    PayResult payResult = null;
                    AliPayResult aliPayResult = new AliPayResult((String) msg.obj);
                    String resultInfo = aliPayResult.getResult();
                    String resultStatus = aliPayResult.getResultStatus();

                    if (TextUtils.equals(resultStatus, "9000")) {
                        code = Constants.STATE_CODE_SUCCESS;
                    } else if (TextUtils.equals(resultStatus, "8000")) {
                        code = Constants.STATE_CODE_WAIT;
                    } else if (TextUtils.equals(resultStatus, "6001")) {
                        code = Constants.STATE_CODE_CANCEL_PAY;
                    }
                    tast.complete(code, payResult);
                    break;
                }
                case SDK_CHECK_FLAG: {
                    tast.complete(Constants.STATE_CODE_FAILED, null);
                    break;
                }
                default:
                    break;
            }
        }
    };


    private void pay(final AliPayOrder aliPayOrder) {
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(mActivity);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(aliPayOrder.getPayParams());

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * check whether the device has authentication alipay account.
     * 查询终端设备是否存在支付宝认证账户
     */
    private void check() {
        Runnable checkRunnable = new Runnable() {
            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask payTask = new PayTask(mActivity);
                // 调用查询接口，获取查询结果
                boolean isExist = payTask.checkAccountIfExist();

                Message msg = new Message();
                msg.what = SDK_CHECK_FLAG;
                msg.obj = isExist;
                mHandler.sendMessage(msg);
            }
        };

        Thread checkThread = new Thread(checkRunnable);
        checkThread.start();

    }

    /**
     * get the sdk version. 获取SDK版本号
     */
    public void getSDKVersion() {
        PayTask payTask = new PayTask(mActivity);
        String version = payTask.getVersion();
        Toast.makeText(mActivity, version, Toast.LENGTH_SHORT).show();
    }
}
