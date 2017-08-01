package com.topunion.chili.data;

import android.webkit.JavascriptInterface;

/**
 * Created by Administrator on 2017/7/31.
 */

public class JsObject {

    @JavascriptInterface
    public void function(int userId,String username) {
        System.out.println("userId = " + userId + ",username = " + username);

    }
}
