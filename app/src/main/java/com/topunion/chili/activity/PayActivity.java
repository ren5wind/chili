package com.topunion.chili.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.topunion.chili.R;
import com.topunion.chili.paylib.Constants;
import com.topunion.chili.paylib.PayHelper;
import com.topunion.chili.paylib.base.entity.BasePayOrder;
import com.topunion.chili.paylib.business.RechargeService;
import com.topunion.chili.paylib.data.RechargeResult;
import com.topunion.chili.paylib.net.interf.ServiceTask;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Author      : renxiaoming
 * Date        : 2017/10/21
 * Description :
 */
@EActivity(R.layout.activity_pay)
public class PayActivity extends AppCompatActivity {
    @ViewById
    RadioGroup radioGroup;
    @ViewById
    RadioButton rb_weixin, rb_zhifubao;
    @ViewById
    EditText et_pay;
    private RechargeService mRechargeService;
    private int mPayWay = Constants.PAYWAY_WECHAT;
    private String mOutTradeNo;

    @AfterViews
    void init() {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (checkedId == R.id.rb_weixin) {
                    mPayWay = Constants.PAYWAY_WECHAT;
                } else if (checkedId == R.id.rb_zhifubao) {
                    mPayWay = Constants.PAYWAY_ALIPAY;
                }
            }
        });

        if (mRechargeService == null) {
            mRechargeService = new RechargeService();
        }
    }

    /**
     * 充值按钮
     */
    @Click
    void btn_pay() {
        String mMoneyAmount = et_pay.getText().toString().trim();
        if (TextUtils.isEmpty(mMoneyAmount)) {
            Snackbar.make(getWindow().getDecorView(), "请输入充值金额", Snackbar.LENGTH_SHORT).show();
            return;
        } else if (Integer.parseInt(mMoneyAmount) == 0) {
            Snackbar.make(getWindow().getDecorView(), "充值金额不能为0", Snackbar.LENGTH_SHORT).show();
            return;
        } else {
//            showLoadingDialog(rechargeTask);
            int moneyInteger = 0;
            try {
                moneyInteger = Integer.parseInt(mMoneyAmount);
            } catch (Exception e) {
                Snackbar.make(getWindow().getDecorView(), "请输入整数", Snackbar.LENGTH_SHORT).show();
            }
            mRechargeService.createRechargeOrder(mPayWay, moneyInteger, rechargeTask);
        }
    }


    /**
     * 充值task
     */
    ServiceTask<? extends BasePayOrder> rechargeTask = new ServiceTask<BasePayOrder>() {
        @Override
        protected void onComplete(int resultCode, BasePayOrder order) {
            switch (resultCode) {
                case Constants.STATE_CODE_SUCCESS:
                    mOutTradeNo = order.getOutTradeNo();
                    onCreateRechargeSuccess(order);
                    break;
            }
        }
    };

    /**
     * 创建充值订单成功,调起充值（支付）
     *
     * @param order
     */
    private void onCreateRechargeSuccess(BasePayOrder order) {
        PayHelper.getInstance(null).pay(PayActivity.this, mPayWay, order, new ServiceTask() {
            @Override
            protected void onComplete(int resultCode, Object data) {
                switch (resultCode) {
                    case Constants.STATE_CODE_SUCCESS:
//                        onRechargeSuccess(mOutTradeNo);
                        break;
                    case Constants.STATE_CODE_CANCEL_PAY:
//                        cancelRecharge();
                        break;
                    case Constants.STATE_CODE_WAIT:
                        //启动等待
                        mWaitingDialog.show(getSupportFragmentManager(), "waiting");
//                        mRechargeService.getRechargeResult(mOutTradeNo, mWaitingPayTask);
                        break;
                }
            }
        });
    }

    //因为支付宝返回的8000,所以需要等待支付结果 laoding...
    static WaitingDialogFragment mWaitingDialog = new WaitingDialogFragment();

    public static class WaitingDialogFragment extends DialogFragment {
        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = super.onCreateView(inflater, container, savedInstanceState);
            getDialog().setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    mWaitingPayTask.cancelTask();
                }
            });
            return view;
        }
    }

    /**
     * 因为支付宝返回的8000 等待中的支付结果，需要等待结果的task
     */
    static ServiceTask<RechargeResult> mWaitingPayTask = new ServiceTask<RechargeResult>() {
        @Override
        protected void onComplete(int resultCode, RechargeResult rr) {
            if (resultCode == Constants.STATE_CODE_SUCCESS && rr != null) {
                mWaitingDialog.dismiss();
                onRechargeSuccess(rr);
            }
        }
    };

    /**
     * 充值成功后的操作
     */
    private static void onRechargeSuccess(RechargeResult rechargeResult) {
    }

    @Click
    void btn_back() {
        finish();
    }

}
