package com.topunion.chili.business;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;

import com.google.gson.Gson;
import com.topunion.chili.base.RxBus;
import com.topunion.chili.data.AccountBean;

/**
 * Created by Administrator on 2017/7/31.
 */

public class JsManager {
    private final String TAG = "ttlc";
    private final String FUN_GETUSERINFO = "getUserInfo";

    private static JsManager instance;
    private final static Object syncLock = new Object();
    private Gson gson;

    public static JsManager getInstance() {
        if (instance == null) {
            synchronized (syncLock) {
                if (instance == null) {
                    instance = new JsManager();
                }
            }
        }
        return instance;
    }

    private JsManager() {
        gson = new Gson();
    }

    public void parseUrl(String url) {
        Uri uri = Uri.parse(url);
        if (uri.getScheme().equals(TAG)) {
            if (uri.getAuthority().equals(FUN_GETUSERINFO)) {
                getUserInfo(getJson(url));
            }
        }
    }

    private String getJson(String url) {
        String[] sourceStrArray = url.split("\\?");
        return sourceStrArray[1];
    }

    private void getUserInfo(String response) {
        AccountBean accountBean = gson.fromJson(response, AccountBean.class);
        AccountManager.getInstance().saveAccount(accountBean);
        RxBus.getInstance().post(AccountManager.RXBUS_ACCOUNT_LOGIN, true);
    }

    private void upLoadHead(Context context){
        final CharSequence[] charSequences = {"相册","拍照"};
        AlertDialog.Builder builder= new AlertDialog.Builder(context);

        builder.setTitle("ok")
                .setItems(charSequences, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {



                    }
                }).show();
    }

}
