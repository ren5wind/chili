package com.topunion.chili.activity;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;

import com.topunion.chili.R;
import com.topunion.chili.WebViewFragment;
import com.topunion.chili.net.HttpHelper_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

/**
 * Author      : renxiaoming
 * Date        : 2017/11/19
 * Description :
 */
@EActivity(R.layout.activity_login)
public class LoginActivity extends FragmentActivity {

    @AfterViews
    void init() {
        WebViewFragment fragment = WebViewFragment.newInstance(HttpHelper_.DEBUG_SERVER + "/assets/user/index.html", false);
        FragmentManager mFragmentManager = getSupportFragmentManager();
        FragmentTransaction mTransaction = mFragmentManager.beginTransaction();
        mTransaction.add(R.id.fragment, fragment);
        mTransaction.commit();


        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels; // 屏幕宽度（像素）
        int height = metric.heightPixels; // 屏幕高度（像素）

        android.view.WindowManager.LayoutParams p = getWindow().getAttributes();
        p.height = (int) (height * 0.9); // 高度设置为屏幕的0.8
        p.width = width;
        p.gravity = Gravity.TOP;
        getWindow().setAttributes(p);
    }

}
