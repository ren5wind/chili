package com.topunion.chili;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.widget.Toast;

import com.topunion.chili.activity.MessageMainFragment_;
import com.topunion.chili.net.HttpHelper_;
import com.yinglan.alphatabs.AlphaTabsIndicator;
import com.yinglan.alphatabs.OnTabChangedListner;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.activity_main)
public class MainActivity extends FragmentActivity {


    @ViewById(R.id.mViewPager)
    ViewPager mViewPager;

    @ViewById(R.id.alphaIndicator)
    AlphaTabsIndicator alphaIndicator;

    WebViewFragment find, bid, square, my;
    private int postion;

    @AfterViews
    protected void afterViews() {
        List<Fragment> listFragment = new ArrayList<>();

        find = WebViewFragment.newInstance(HttpHelper_.DEBUG_SERVER + "/assets/home.html");
        bid = WebViewFragment.newInstance(HttpHelper_.DEBUG_SERVER + "/assets/bid-management/bid-on.html");
        Fragment message = MessageMainFragment_.builder().build();
        square = WebViewFragment.newInstance(HttpHelper_.DEBUG_SERVER + "/assets/square/index.html");
        my = WebViewFragment.newInstance(HttpHelper_.DEBUG_SERVER + "/assets/user/index.html");

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
                        break;
                    case 1:
                        bid.initfresh();
                        break;
                    case 2:
                        break;
                    case 3:
                        square.initfresh();
                        break;
                    case 4:
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
