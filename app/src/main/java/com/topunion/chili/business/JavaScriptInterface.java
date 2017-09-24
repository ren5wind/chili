package com.topunion.chili.business;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.webkit.JavascriptInterface;

import com.topunion.chili.R;
import com.topunion.chili.WebViewFragment;
import com.topunion.chili.net.HttpHelper;
import com.topunion.chili.net.HttpHelper_;
import com.topunion.chili.util.StringUtil;

import java.util.HashMap;

import cn.jiguang.share.android.api.JShareInterface;
import cn.jiguang.share.android.api.PlatActionListener;
import cn.jiguang.share.android.api.Platform;
import cn.jiguang.share.android.api.ShareParams;
import cn.jiguang.share.wechat.Wechat;

/**
 * Author      : renxiaoming
 * Date        : 2017/9/18
 * Description :
 */
public class JavaScriptInterface {
    private Context context;
    private WebViewFragment fragment;

    public JavaScriptInterface(Context context, WebViewFragment fragment) {
        this.context = context;
        this.fragment = fragment;
    }

    @JavascriptInterface
    public void getsharedata(int type, String text, String image, String link, String title, String audio, String video, String app, String file, String emoticon) {
        ShareParams shareParams = new ShareParams();
        shareParams.setShareType(type);
        if (!StringUtil.isEmpt(title))
            shareParams.setTitle(title);
        if (!StringUtil.isEmpt(text))
            shareParams.setText(text);
        if (!StringUtil.isEmpt(link))
            shareParams.setUrl(link);
        if (type == Platform.SHARE_IMAGE) {
            download(image, shareParams);
        } else {
            shareParams.setImageData(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher));
            JShareInterface.share(Wechat.Name, shareParams, mShareListener);
        }
    }

    @JavascriptInterface
    public void getBackState(boolean isBack) {
        System.out.println("isBack = " + isBack);
        fragment.setEixt(!isBack);
    }

    public void download(String url, final ShareParams shareParams) {
        HttpHelper_.getInstance_(context).download(url,
                Environment.getExternalStorageDirectory() + "/01yitou/account/",
                new HttpHelper.OnDownloadListener() {
                    @Override
                    public void onDownloadSuccess(String path) {
                        shareParams.setImagePath(path);
                        JShareInterface.share(Wechat.Name, shareParams, mShareListener);
                    }

                    @Override
                    public void onDownloading(int progress) {

                    }

                    @Override
                    public void onDownloadFailed() {

                    }
                });
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


    class ShareDate {
        String title;
        String link;
    }
}
