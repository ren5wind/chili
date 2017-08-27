package com.topunion.chili.base.dialog;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Author      : renxiaoming
 * Date        : 2017-07-12
 * Description :
 */
abstract class UiLibDialogImp extends Dialog implements UiLibDialogInterface {
    protected Context mContext;
    protected View mRootView;
    private boolean mCanceledOnTouchOutside;

    protected UiLibDialogImp(@NonNull Context context, @StyleRes int themeResId, int layoutResID) {
        super(context, themeResId);
        init(context, layoutResID);
    }

    @Override
    public void init(@NonNull Context context, @LayoutRes int layoutResID) {
        mContext = context;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mRootView = LayoutInflater.from(context).inflate(layoutResID, null);
        setContentView(mRootView);
    }

    @Override
    public void setAnim(@StyleRes int animId) {
        Window window = getWindow();
        window.setWindowAnimations(animId);
    }

    @Override
    public void apply(UiLibDialogParams params) {
        if (params.mAnimId != -1) {
            setAnim(params.mAnimId);
        }
        setWindow(params.mGravity, params.mWidthPercent);
        mCanceledOnTouchOutside = params.mCanceledOnTouchOutside;
        applyDialog(params);
    }

    private void setWindow(int gravity, int percent) {
        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = gravity;
        lp.width = mContext.getResources().getDisplayMetrics().widthPixels * percent / 100;
        window.setAttributes(lp);
    }


    @Override
    public void setOnclickListener(int viewId, final View.OnClickListener listener,
                                   final boolean aotuDismiss) {
        View view = findViewById(viewId);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(v);
                }
                if (aotuDismiss) {
                    dismiss();
                }
            }
        });
    }

    @Override
    public void show() {
        if (mContext == null) {
            return;
        }
        Activity act = null;
        if (mContext instanceof Activity) {
            act = (Activity) mContext;
        }

        if (!act.isFinishing() && !isShowing()) {
            super.show();
        }
        if (!mCanceledOnTouchOutside) {
            setCanceledOnTouchOutside(mCanceledOnTouchOutside);
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    protected abstract void applyDialog(UiLibDialogParams params);

    protected View getViewById(int viewId) {
        return mRootView.findViewById(viewId);
    }

    protected void setTextViewText(int viewId, CharSequence text) {
        TextView view = (TextView) mRootView.findViewById(viewId);
        view.setText(text);
    }

    protected void setImageViewSrc(int viewId, Drawable drawable) {
        ImageView view = (ImageView) mRootView.findViewById(viewId);
        view.setImageDrawable(drawable);
    }

    protected void setImageViewSrc(int viewId, Bitmap bm) {
        ImageView view = (ImageView) mRootView.findViewById(viewId);
        view.setImageBitmap(bm);
    }

    protected void setImageViewSrc(int viewId, int resId) {
        ImageView view = (ImageView) mRootView.findViewById(viewId);
        view.setImageResource(resId);
    }

    protected void setCheckBox(int viewId, CharSequence text, Drawable drawable) {
        CheckBox view = (CheckBox) mRootView.findViewById(viewId);
        view.setText(text);
        view.setButtonDrawable(drawable);
    }

    protected void setCheckBox(int viewId, CharSequence text) {
        CheckBox view = (CheckBox) mRootView.findViewById(viewId);
        view.setText(text);
    }
}
