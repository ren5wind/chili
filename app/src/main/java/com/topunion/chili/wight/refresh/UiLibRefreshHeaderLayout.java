package com.topunion.chili.wight.refresh;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Created by Aspsine on 2015/8/13.
 */
public abstract class UiLibRefreshHeaderLayout extends RelativeLayout implements UiLibRefreshRefreshInterface, UiLibRefreshTrigger {

    public UiLibRefreshHeaderLayout(Context context) {
        this(context, null);
    }

    public UiLibRefreshHeaderLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UiLibRefreshHeaderLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

}
