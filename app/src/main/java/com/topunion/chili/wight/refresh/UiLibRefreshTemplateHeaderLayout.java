package com.topunion.chili.wight.refresh;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.topunion.chili.R;


/**
 * Author      : renxiaoming
 * Date        : 2017-07-12
 * Description :
 */
public class UiLibRefreshTemplateHeaderLayout extends UiLibRefreshHeaderLayout implements ViewTreeObserver.OnGlobalLayoutListener {
    private ImageView mIvLoading;

    private AnimationDrawable animationDrawable;

    public UiLibRefreshTemplateHeaderLayout(Context context) {
        super(context);
    }

    public UiLibRefreshTemplateHeaderLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public UiLibRefreshTemplateHeaderLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onPrepare() {
    }

    @Override
    public void onMove(int y, boolean isComplete, boolean automatic) {
    }

    @Override
    public void onRelease() {
        if (animationDrawable != null) {
            animationDrawable.start();
        }
    }

    @Override
    public void onComplete() {

    }

    @Override
    public void onReset() {
        if (animationDrawable != null) {
            animationDrawable.stop();
        }
    }

    @Override
    public void onRefresh() {
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    private boolean isFirst = true;

    @Override
    public void onGlobalLayout() {
        if (isFirst) {
            if (mIvLoading == null) {
                mIvLoading = (ImageView) findViewById(R.id.header_progressbar);
            }
            if (animationDrawable == null) {
                animationDrawable = (AnimationDrawable) mIvLoading.getDrawable();
            }
            isFirst = false;
        }
    }
}
