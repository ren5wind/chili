package com.topunion.chili;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Shawn on 7/12/17.
 */

public class MainFragmentAdapter extends FragmentPagerAdapter {
    private FragmentManager mFragmetnManager;
    private List<Fragment> mListfragment;

    public MainFragmentAdapter(FragmentManager fm, List<Fragment> list) {
        super(fm);
        this.mFragmetnManager = fm;
        this.mListfragment = list;
    }

    @Override
    public Fragment getItem(int arg0) {
        return mListfragment.get(arg0);
    }

    @Override
    public int getCount() {
        return mListfragment.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment f = (Fragment) super.instantiateItem(container, position);
        return f;
    }
}
