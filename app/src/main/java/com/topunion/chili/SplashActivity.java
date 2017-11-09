package com.topunion.chili;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

import com.topunion.chili.util.SPUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_splash)
public class SplashActivity extends AppCompatActivity {
    private static final int SPLASH_DELAY_MILLIS = 2000;
    private boolean isFirst;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            init();
        }
    };

    @AfterViews
    protected void afterViews() {
        initView();
    }

    public void initView() {
        // 判断是否是第一次开启应用
        boolean isFirstOpen = SPUtil.getSharedBooleanData(this, "first");
        // 如果是第一次启动，则先进入功能引导页
        if (isFirstOpen) {
            Intent intent = new Intent(this, WelcomeGuideActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        //防止按了MENU，重新走splash的bug,参考：http://stackoverflow.com/questions/19545889/app-restarts-rather-than-resumes
        if (!isTaskRoot()
                && getIntent().hasCategory(Intent.CATEGORY_LAUNCHER)
                && getIntent().getAction() != null
                && getIntent().getAction().equals(Intent.ACTION_MAIN)) {
            finish();
            return;
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && !isFirst) {
            isFirst = true;
            mHandler.sendEmptyMessageDelayed(0, 100);
        }
    }


    protected void postMainRunnable(long time) {

        new Handler().postDelayed(new MainRunnable(), time);
    }

    class MainRunnable implements Runnable {
        @Override
        public void run() {
            gotoTargetActivity("/main");
            finish();
        }
    }

    protected void gotoTargetActivity(String path) {
        switch (path) {
            case "/main": //主页面
                gotoMainPage();
                break;
        }
    }

    /**
     * 跳转到主页
     */
    protected void gotoMainPage() {
        Intent startMain = new Intent(this, MainActivity_.class);
        startActivity(startMain);
    }


    private void init() {
        postMainRunnable(SPLASH_DELAY_MILLIS);
    }


}
