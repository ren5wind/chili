package com.topunion.chili;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.widget.Toast;

import com.topunion.chili.activity.LoginActivity_;
import com.topunion.chili.activity.MessageMainFragment_;
import com.topunion.chili.business.AccountManager;
import com.topunion.chili.net.HttpHelper_;
import com.topunion.chili.util.StringUtil;
import com.topunion.chili.wight.NoScrollViewPager;
import com.yinglan.alphatabs.AlphaTabsIndicator;
import com.yinglan.alphatabs.OnTabChangedListner;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.activity_main)
public class MainActivity extends FragmentActivity {


    @ViewById(R.id.mViewPager)
    NoScrollViewPager mViewPager;

    @ViewById(R.id.alphaIndicator)
    AlphaTabsIndicator alphaIndicator;

    WebViewFragment find, bid, square, my;
    private int postion;

    @AfterViews
    protected void afterViews() {
        List<Fragment> listFragment = new ArrayList<>();

        find = WebViewFragment.newInstance(HttpHelper_.DEBUG_SERVER + "/assets/home.html", false);
        bid = WebViewFragment.newInstance(HttpHelper_.DEBUG_SERVER + "/assets/bid-management/bid-on.html", false);
        Fragment message = MessageMainFragment_.builder().build();
        square = WebViewFragment.newInstance(HttpHelper_.DEBUG_SERVER + "/assets/square/index.html", false);
        my = WebViewFragment.newInstance(HttpHelper_.DEBUG_SERVER + "/assets/user/index.html", true);

        listFragment.add(find);
        listFragment.add(bid);
        listFragment.add(message);
        listFragment.add(square);
        listFragment.add(my);
        FragmentManager fragmentManager = getSupportFragmentManager();
        MainFragmentAdapter adapter = new MainFragmentAdapter(fragmentManager, listFragment);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(adapter);

        alphaIndicator.setViewPager(mViewPager);                     //Set ViewPager
//        alphaIndicator.setOnTabChangedListner(OnTabChangedListner listner);   //Settings TAB at the bottom click to monitor
//        mAlphaTabsIndicator.removeAllBadge();                                      //Remove all remind the TAB
        alphaIndicator.setTabCurrenItem(0);
        alphaIndicator.setOnTabChangedListner(new OnTabChangedListner() {
            @Override
            public void onTabSelected(int tabNum) {
                postion = tabNum;
                switch (tabNum) {
                    case 0:
                        find.initfresh();
                        bid.setVisibility(false);
                        square.setVisibility(false);
                        my.setVisibility(false);
                        break;
                    case 1:
                        find.setVisibility(false);
                        bid.initfresh();
                        square.setVisibility(false);
                        my.setVisibility(false);
                        break;
                    case 2:
                        find.setVisibility(false);
                        bid.setVisibility(false);
                        square.setVisibility(false);
                        my.setVisibility(false);
                        if (StringUtil.isEmpt(AccountManager.getInstance().getUserId())) {
                            LoginActivity_.intent(MainActivity.this).start();
                        }
                        break;
                    case 3:
                        find.setVisibility(false);
                        bid.setVisibility(false);
                        square.initfresh();
                        my.setVisibility(false);
                        break;
                    case 4:
                        find.setVisibility(false);
                        bid.setVisibility(false);
                        square.setVisibility(false);
                        my.initfresh();
                        break;
                }
            }
        });
    }

    private long exitTime = 0;

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == event.getKeyCode() && event.getAction() == KeyEvent.ACTION_UP) {
            WebViewFragment fragment = null;

            switch (postion) {
                case 0:
                    fragment = find;
                    break;
                case 1:
                    fragment = bid;
                    break;
                case 3:
                    fragment = square;
                    break;
                case 4:
                    fragment = my;
                    break;
            }
            if (postion != 2 && fragment.back()) {
                return true;
            }
            // 判断是否在两秒之内连续点击返回键，是则退出，否则不退出
            if (event.getAction() == KeyEvent.ACTION_UP) {
                if (System.currentTimeMillis() - exitTime > 2000) {
                    // 将系统当前的时间赋值给exitTime
                    Toast.makeText(this, "再按一次退出应用", Toast.LENGTH_SHORT).show();
                    exitTime = System.currentTimeMillis();
                } else {
                    this.finish();
                    System.exit(0);
                }
            }
            return true;
        }
        return false;
    }
}
