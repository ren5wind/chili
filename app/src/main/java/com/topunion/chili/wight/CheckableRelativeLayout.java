package com.topunion.chili.wight;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.Checkable;

import com.topunion.chili.R;

/**
 * Created by Shawn on 7/16/17.
 */

public class CheckableRelativeLayout extends RelativeLayout implements Checkable {

    public CheckableRelativeLayout(Context context) {
        super(context);
    }

    public CheckableRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CheckableRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean isChecked() {
        return findViewById(R.id.img_choose).getVisibility() == VISIBLE;
    }

    @Override
    public void setChecked(boolean b) {
        findViewById(R.id.img_choose).setVisibility(b ? VISIBLE : GONE);
    }

    @Override
    public void toggle() {
        findViewById(R.id.img_choose).setVisibility(findViewById(R.id.img_choose).getVisibility() == VISIBLE ? GONE : VISIBLE);
    }
}
