package com.topunion.chili.business;

import android.app.Dialog;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
import cn.jiguang.share.qqmodel.QQ;
import cn.jiguang.share.qqmodel.QZone;
import cn.jiguang.share.wechat.Wechat;
import cn.jiguang.share.wechat.WechatFavorite;
import cn.jiguang.share.wechat.WechatMoments;
import cn.jiguang.share.weibo.SinaWeibo;

/**
 * Author      : renxiaoming
 * Date        : 2017/9/18
 * Description :
 */
public class JavaScriptInterface {
    private Context context;
    private CallBack callBack;
    public JavaScriptInterface(Context context, CallBack callBack) {
        this.context = context;
        this.callBack = callBack;
    }

    @JavascriptInterface
    public void getsharedata(final int type, final String text, final String image, final String link, final String title, final String audio, final String video, final String app, final String file, final String emoticon) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_share, null);
        // 设置style 控制默认dialog带来的边距问题
        final Dialog dialog = new Dialog(context, R.style.uilib_dialog_style);
        dialog.setContentView(view);
        dialog.show();

        // 监听
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.Wechat:
                        // 分享到微信
                        share(Wechat.Name, type, text, image, link, title, audio, video, app, file, emoticon);
                        break;
                    case R.id.WechatMoments:
                        // 分享到朋友圈
                        share(WechatMoments.Name, type, text, image, link, title, audio, video, app, file, emoticon);
                        break;
                    case R.id.WechatFavorite:
                        share(WechatFavorite.Name, type, text, image, link, title, audio, video, app, file, emoticon);
                        break;
                    case R.id.SinaWeibo:
                        share(SinaWeibo.Name, type, text, image, link, title, audio, video, app, file, emoticon);
                        break;
                    case R.id.QQ:
                        share(QQ.Name, type, text, image, link, title, audio, video, app, file, emoticon);
                        break;
                    case R.id.QZone:
                        share(QZone.Name, type, text, image, link, title, audio, video, app, file, emoticon);
                        break;
                }
                dialog.dismiss();
            }
        };

        view.findViewById(R.id.Wechat).setOnClickListener(listener);
        view.findViewById(R.id.WechatMoments).setOnClickListener(listener);
        view.findViewById(R.id.WechatFavorite).setOnClickListener(listener);
        view.findViewById(R.id.SinaWeibo).setOnClickListener(listener);
        view.findViewById(R.id.QQ).setOnClickListener(listener);
        view.findViewById(R.id.QZone).setOnClickListener(listener);

        // 设置相关位置，一定要在 show()之后
        Window window = dialog.getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.gravity = Gravity.BOTTOM;
        window.setAttributes(params);


    }

    @JavascriptInterface
    public void getBackState(boolean isBack) {
        System.out.println("isBack = " + isBack);
        callBack.setEixt(!isBack);
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

    private void share(String name, int type, String text, String image, String link, String title, String audio, String video, String app, String file, String emoticon) {
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
            JShareInterface.share(name, shareParams, mShareListener);
        }
    }

    public interface CallBack {
        void setEixt(boolean isExit);
    }
}
