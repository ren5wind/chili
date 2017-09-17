package com.topunion.chili;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;

import com.topunion.chili.activity.MessageMainFragment_;
import com.topunion.chili.net.HttpHelper_;
import com.yinglan.alphatabs.AlphaTabsIndicator;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.activity_main)
public class MainActivity extends FragmentActivity {


    @ViewById(R.id.mViewPager)
    ViewPager mViewPager;

    @ViewById(R.id.alphaIndicator)
    AlphaTabsIndicator alphaIndicator;

    @AfterViews
    protected void afterViews(){
        List<Fragment> listFragment = new ArrayList<>();

        Fragment find = WebViewFragment.newInstance(HttpHelper_.DEBUG_SERVER + "/assets/home.html");
        Fragment bid = WebViewFragment.newInstance(HttpHelper_.DEBUG_SERVER + "/assets/bid-management/bid-on.html");
        Fragment message = MessageMainFragment_.builder().build();
        Fragment square = WebViewFragment.newInstance(HttpHelper_.DEBUG_SERVER + "/assets/square/index.html");
        Fragment my = WebViewFragment.newInstance(HttpHelper_.DEBUG_SERVER + "/assets/user/index.html");

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

    }

}
