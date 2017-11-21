package com.topunion.chili.business;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.gson.Gson;
import com.topunion.chili.activity.HeadSettingActivity;
import com.topunion.chili.activity.PayActivity_;
import com.topunion.chili.base.RxBus;
import com.topunion.chili.data.AccountBean;
import com.topunion.chili.net.HttpHelper;
import com.topunion.chili.net.HttpHelper_;
import com.topunion.chili.util.StringUtil;

import java.io.File;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by Administrator on 2017/7/31.
 */

public class JsManager {
    private final String TAG = "ttlc";
    private final String FUN_GETUSERINFO = "getUserInfo";
    public static final String RXBUS_WEB_REFRESH_VIEW = "rxbux_web_refresh_view";

    private static JsManager instance;
    private final static Object syncLock = new Object();
    private Gson gson;
    private boolean isNotific;

    public static JsManager getInstance() {
        if (instance == null) {
            synchronized (syncLock) {
                if (instance == null) {
                    instance = new JsManager();
                }
            }
        }
        return instance;
    }

    private JsManager() {
        gson = new Gson();
    }

    public boolean parseUrl(String url, Activity activity, boolean isNotific) {
        Uri uri = Uri.parse(url);
        this.isNotific = isNotific;
        if (uri.getScheme().equals(TAG)) {
            if (uri.getAuthority().equals(FUN_GETUSERINFO)) {
                getUserInfo(getJson(url));
                return false;
            }
        } else if (url.contains("user/setting/picture.html")) {
            upLoadHead(activity, url);
            return true;
        } else if (url.contains("login.html") && isNotific) {
            AccountManager.getInstance().clearAccount();
            RxBus.getInstance().post(AccountManager.RXBUS_ACCOUNT_LOGOUT, true);
            return false;
        } else if (url.contains("user/score-recharge.html")) {
            startPayView(activity);
            return true;
        }
        return false;
    }

    private void startPayView(Activity activity) {
        activity.startActivity(new Intent(activity, PayActivity_.class));
    }

    private String getJson(String url) {
        String[] sourceStrArray = url.split("\\?");
        return sourceStrArray[1];
    }

    private void getUserInfo(String response) {
        AccountBean accountBean = gson.fromJson(response, AccountBean.class);
        AccountManager.getInstance().saveAccount(accountBean);
        RxBus.getInstance().post(AccountManager.RXBUS_ACCOUNT_LOGIN, true);
    }

    private void upLoadHead(final Activity activity, final String url) {
        final CharSequence[] charSequences = {"相册", "拍照"};
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setItems(charSequences, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int type = 0;
                if (which == 0) {
                    type = HeadSettingActivity.HEADSETTING_TYPE_LOCAL;
                } else if (which == 1) {
                    type = HeadSettingActivity.HEADSETTING_TYPE_CAMERA;
                }
                Intent intent = new Intent(activity, HeadSettingActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt(HeadSettingActivity.INTENT_BUNDLE_KEY_HEADSETTING_TYPE, type);
                intent.putExtras(bundle);
                activity.startActivity(intent);
            }
        }).show();

        Observable<String> headCallBackobservable = RxBus.getInstance().register(HeadSettingActivity.RXBUS_USER_HEAD_PATH);
        headCallBackobservable.observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String path) {
                        RxBus.getInstance().unregister(HeadSettingActivity.RXBUS_USER_HEAD_PATH);
                        if (!StringUtil.isEmpt(path)) {
                            String localPath = path;
                            HttpHelper_.getInstance_(activity).uploadImage(activity, AccountManager.getInstance().getUserId(), new File(localPath),
                                    new HttpHelper.uploadListener() {
                                        @Override
                                        public void onSuccess() {
                                            RxBus.getInstance().post(RXBUS_WEB_REFRESH_VIEW, "http://www.yibidding.com/chili-2.0-demo//assets/user/setting.html");
                                        }

                                        @Override
                                        public void onFail() {

                                        }
                                    });
                        }
                    }
                });
    }

}
