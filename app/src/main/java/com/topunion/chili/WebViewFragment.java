package com.topunion.chili;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.topunion.chili.business.JsManager;

public class WebViewFragment extends Fragment {

    public static String ARG_URL;

    private WebView webView;
    ProgressBar bar;

    private String urlStr;

    public WebViewFragment() {
    }

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
        View rootView = inflater.inflate(R.layout.fragment_webview, container, false);

        Bundle arguments = getArguments();
        if (arguments != null) {
            urlStr = arguments.getString(ARG_URL);
        }

//        TextView wv = new TextView(getActivity());
//        WebView wv = new WebView(getActivity());
//        wv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        webView = (WebView) rootView.findViewById(R.id.webView);
        bar = (ProgressBar) rootView.findViewById(R.id.progressBar);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                JsManager.getInstance().parseUrl(url);
                return super.shouldOverrideUrlLoading(view, url);

            }

            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    bar.setVisibility(View.INVISIBLE);
                } else {
                    if (View.INVISIBLE == bar.getVisibility()) {
                        bar.setVisibility(View.VISIBLE);
                    }
                    bar.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }

        });

        //魅族手机设置LayerType为LAYER_TYPE_NONE
        if (Build.MANUFACTURER.equals("Meizu")) {
            webView.setLayerType(View.LAYER_TYPE_NONE, null);
        }


        //启用WebView localStorage
        webView.getSettings().setDomStorageEnabled(true);
        //开启 database storage API 功能
        webView.getSettings().setDatabaseEnabled(true);
        webView.getSettings().setAppCacheMaxSize(1024 * 1024 * 8);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.getSettings().setAppCacheEnabled(false);
        String appCachePath = getActivity().getCacheDir().getAbsolutePath();
        webView.getSettings().setAppCachePath(appCachePath);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && BuildConfig.DEBUG) {
            WebView.setWebContentsDebuggingEnabled(true);
        }

//        SpannableString mSpannableString = new SpannableString(
//                "打开百度,拨打电话,发送短信,发送邮件,发送彩信,打开地图");
        // 设置超链接 （需要添加setMovementMethod方法附加响应）
//        mSpannableString.setSpan(new URLSpan(urlStr), 0, 4,
//                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        webView.loadUrl(urlStr);
//        wv.setMovementMethod(LinkMovementMethod.getInstance());
//        wv.setText(mSpannableString);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e("Shawn", urlStr);
    }
}
