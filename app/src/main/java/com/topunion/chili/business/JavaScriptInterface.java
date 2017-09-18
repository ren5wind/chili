package com.topunion.chili.business;

import android.os.Message;
import android.webkit.JavascriptInterface;

import java.util.HashMap;

import cn.jiguang.share.android.api.JShareInterface;
import cn.jiguang.share.android.api.PlatActionListener;
import cn.jiguang.share.android.api.Platform;
import cn.jiguang.share.android.api.ShareParams;
import cn.jiguang.share.android.utils.Logger;

/**
 * Author      : renxiaoming
 * Date        : 2017/9/18
 * Description :
 */
public class JavaScriptInterface {
    public JavaScriptInterface() {
    }

    @JavascriptInterface
    public void getsharedata(String dicShare, int type) {
        System.out.println("dicShare = " + dicShare + ", type = " + type);
        ShareParams shareParams = new ShareParams();
        shareParams.setShareType(type);
        shareParams.setTitle("title");
        shareParams.setText("text");
        shareParams.setUrl("url");
        JShareInterface.share(dicShare, shareParams, mShareListener);
    }

    private PlatActionListener mShareListener = new PlatActionListener() {
        @Override
        public void onComplete(Platform platform, int action, HashMap<String, Object> data) {

        }

        @Override
        public void onError(Platform platform, int action, int errorCode, Throwable error) {
        }

        @Override
        public void onCancel(Platform platform, int action) {
        }
    };


    class ShareDate{
        String title;
        String link;
    }
}
