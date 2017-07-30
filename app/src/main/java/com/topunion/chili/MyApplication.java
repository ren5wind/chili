package com.topunion.chili;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.topunion.chili.net.HttpHelper;

import org.androidannotations.annotations.EApplication;

import cn.jpush.im.android.api.JMessageClient;

/**
 * Created by Shawn on 7/13/17.
 */

@EApplication
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //请求图片的库，配合 com.facebook.drawee.view.SimpleDraweeView 使用
        Fresco.initialize(this);

        JMessageClient.setDebugMode(true);
        JMessageClient.init(this);
    }

}
