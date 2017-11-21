package com.topunion.chili.activity;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

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
public class LoginActivity extends AppCompatActivity {

    @AfterViews
    void init() {
        WebViewFragment fragment = WebViewFragment.newInstance(HttpHelper_.DEBUG_SERVER + "/assets/user/index.html", false);
        FragmentManager mFragmentManager = getSupportFragmentManager();
        FragmentTransaction mTransaction = mFragmentManager.beginTransaction();
        mTransaction.add(R.id.fragment, fragment);
        mTransaction.commit();
    }

}
