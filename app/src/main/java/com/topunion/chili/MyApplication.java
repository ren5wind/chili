package com.topunion.chili;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.topunion.chili.business.AccountManager;
import com.topunion.chili.net.HttpHelper;
import com.topunion.chili.util.SPUtil;
import com.topunion.chili.util.StringUtil;

import org.androidannotations.annotations.EApplication;

import cn.jpush.im.android.api.JMessageClient;

/**
 * Created by Shawn on 7/13/17.
 */

@EApplication
public class MyApplication extends Application {
    private static MyApplication baseApplication;

    private String token;

    private String myUserId;

    public synchronized static MyApplication getInstance() {
        if (null == baseApplication) {
            baseApplication = new MyApplication();
        }
        return baseApplication;
    }

    public static Context getContext() {
        return baseApplication;
    }

    public static Context getAppContext() {
        return baseApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //请求图片的库，配合 com.facebook.drawee.view.SimpleDraweeView 使用
        Fresco.initialize(this);

//        JMessageClient.setDebugMode(true);
//        JMessageClient.init(this);
        baseApplication = this;

    }

    public String getToken() {
        if (StringUtil.isEmpt(token)) {
            token = SPUtil.getSharedStringData(MyApplication.getAppContext(), AccountManager.KEY_SP_ACCOUNT_TOKEN);
        }
//        if (TextUtils.isEmpty(token)) {
//            token = "7f3c694ccea6c31373b1391095f7762a";
//        }
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setMyUserId(String userId) {
        myUserId = userId;
    }

    public String getMyUserId() {
        if (StringUtil.isEmpt(myUserId)) {//没有
            myUserId = SPUtil.getSharedStringData(MyApplication.getAppContext(), AccountManager.KEY_SP_ACCOUNT_USERID);
        }
        return myUserId;
    }


}
