package com.topunion.chili.wight;

import android.content.Context;
import android.graphics.PointF;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class MyViewPager extends ViewPager {
  
    public MyViewPager(Context context) {
        super(context);  
    }  
  
    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);  
    }  
  

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;

    }
}  