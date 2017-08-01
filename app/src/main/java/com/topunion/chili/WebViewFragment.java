package com.topunion.chili;

import android.graphics.MaskFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.topunion.chili.data.JsObject;

public class WebViewFragment extends Fragment {

    public static String ARG_URL;

    private WebView webView;

    private String urlStr;

    public WebViewFragment() {}

    public static WebViewFragment newInstance(String urlStr) {
        WebViewFragment fragment = new WebViewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_URL, urlStr);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            urlStr = getArguments().getString(ARG_URL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        if (arguments != null) {
            urlStr = arguments.getString(ARG_URL);
        }

//        TextView wv = new TextView(getActivity());
        WebView wv = new WebView(getActivity());
        wv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        wv.getSettings().setJavaScriptEnabled(true);
        wv.setWebViewClient(new WebViewClient() {
           public boolean shouldOverrideUrlLoading(WebView view, String url) {
               view.loadUrl(url);
               return true;
           }
            @Override
            public void onLoadResource(WebView view, String url) {
                // 分享
                if (url.contains("/user/api/login.do")) {
                    Toast.makeText(getActivity(), url, Toast.LENGTH_SHORT).show();
                    System.out.println(url);
                    return;
                }
                super.onLoadResource(view, url);
            }
        });


        //魅族手机设置LayerType为LAYER_TYPE_NONE
        if (Build.MANUFACTURER.equals("Meizu")) {
            wv.setLayerType(View.LAYER_TYPE_NONE, null);
        }


        //启用WebView localStorage
        wv.getSettings().setDomStorageEnabled(true);
        //开启 database storage API 功能
        wv.getSettings().setDatabaseEnabled(true);
        wv.getSettings().setAppCacheMaxSize(1024 * 1024 * 8);
        wv.getSettings().setAllowFileAccess(true);
        wv.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        wv.getSettings().setAppCacheEnabled(false);
        String appCachePath = getActivity().getCacheDir().getAbsolutePath();
        wv.getSettings().setAppCachePath(appCachePath);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && BuildConfig.DEBUG) {
            WebView.setWebContentsDebuggingEnabled(true);
        }

//        SpannableString mSpannableString = new SpannableString(
//                "打开百度,拨打电话,发送短信,发送邮件,发送彩信,打开地图");
        // 设置超链接 （需要添加setMovementMethod方法附加响应）
//        mSpannableString.setSpan(new URLSpan(urlStr), 0, 4,
//                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        wv.addJavascriptInterface(new JsObject(), "hybridProtocol");

        wv.loadUrl(urlStr);
//        wv.setMovementMethod(LinkMovementMethod.getInstance());
//        wv.setText(mSpannableString);

        return wv;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e("Shawn", urlStr);
    }
}
