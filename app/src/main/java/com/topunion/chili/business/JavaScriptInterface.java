package com.topunion.chili.business;

import android.webkit.JavascriptInterface;

/**
 * Author      : renxiaoming
 * Date        : 2017/9/18
 * Description :
 */
public class JavaScriptInterface {
    public JavaScriptInterface() {
    }

    @JavascriptInterface
    public void getsharedata(String dicShare, String type) {
        System.out.println("dicShare = " + dicShare + ", type = " + type);
    }
}
