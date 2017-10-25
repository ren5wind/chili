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

import com.topunion.chili.base.RxBus;
import com.topunion.chili.business.AccountManager;
import com.topunion.chili.business.JavaScriptInterface;
import com.topunion.chili.business.JsManager;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

public class WebViewFragment extends Fragment {

    public static String ARG_URL;

    private WebView webView;
    ProgressBar bar;

    private String urlStr;

    private boolean isExit;


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

        webView = (WebView) rootView.findViewById(R.id.webView);
        bar = (ProgressBar) rootView.findViewById(R.id.progressBar);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new JavaScriptInterface(getActivity(), this), "androidClient");
        webView.getSettings()
                .setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                System.out.println("url = " + url);
                isExit = false;
                boolean b = JsManager.getInstance().parseUrl(url, getActivity());
                if (!b) {
                    view.loadUrl(url);
                    super.shouldOverrideUrlLoading(view, url);
                }
                return true;
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
                    webView.setVisibility(View.VISIBLE);
                }
//                if (newProgress == 100) {
//                    bar.setVisibility(View.INVISIBLE);
//                } else {
//                    if (View.INVISIBLE == bar.getVisibility()) {
//                        bar.setVisibility(View.VISIBLE);
//                    }
//                    bar.setProgress(newProgress);
//                }
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
        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        webView.getSettings().setAppCacheEnabled(false);
        String appCachePath = getActivity().getCacheDir().getAbsolutePath();
        webView.getSettings().setAppCachePath(appCachePath);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && BuildConfig.DEBUG) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
//        urlStr = "file:///android_asset/b.html";
        webView.loadUrl(urlStr);
        RxBus.getInstance().register(AccountManager.RXBUS_ACCOUNT_LOGIN);
        Observable<Boolean> refreshCallBackobservable = RxBus.getInstance().register(JsManager.RXBUS_WEB_REFRESH_VIEW);
        refreshCallBackobservable.observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Boolean b) {
                        if (b) {
                            webView.loadUrl(urlStr);
                        }
                    }
                });
        return rootView;
    }

    public void setEixt(boolean isExit) {
        this.isExit = isExit;
    }

    public void initfresh() {
        if (webView != null) {
            webView.setVisibility(View.GONE);
            webView.loadUrl(urlStr);
        }
    }

    public boolean back() {
        if (webView == null)
            return false;
        if (isExit) {
            return false;
        }
        webView.post(new Runnable() {
            @Override
            public void run() {
                webView.loadUrl("javascript:goBack()");
            }
        });
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e("Shawn", urlStr);
    }

}
