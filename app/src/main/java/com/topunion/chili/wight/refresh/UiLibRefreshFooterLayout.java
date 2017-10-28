package com.topunion.chili.wight.refresh;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Created by Aspsine on 2015/8/13.
 */
public abstract class UiLibRefreshFooterLayout extends RelativeLayout implements UiLibRefreshLoadMoreInterface, UiLibRefreshTrigger {

    public UiLibRefreshFooterLayout(Context context) {
        this(context, null);
    }

    public UiLibRefreshFooterLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UiLibRefreshFooterLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

}
