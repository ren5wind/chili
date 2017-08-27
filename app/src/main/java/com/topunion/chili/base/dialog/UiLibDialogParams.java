package com.topunion.chili.base.dialog;

import android.graphics.drawable.Drawable;
import android.support.annotation.StyleRes;

/**
 * Author      : renxiaoming
 * Date        : 2017-07-12
 * Description :
 */
class UiLibDialogParams {

    DialogTemplate mTemplate;
    int mLayoutResID;
    int mAnimId = -1;
    int mIconId = -1;
    /**
     * dialog在窗口中布局位置
     */
    int mGravity;
    /**
     * dialog的宽度，在窗口中的百分比
     */
    int mWidthPercent;

    CharSequence mTitle;
    CharSequence mMessage;
    CharSequence mTips;
    CharSequence mLeftButtonText;
    CharSequence mRightButtonText;
    CharSequence mMidButtonText;
    CharSequence mCheckBoxText;
    CharSequence mEditTextHint;

    Drawable mIconDrawable;
    Drawable mCheckBoxDrawable;

    int mLeftButtonTextColorResId = -1;
    int mMidButtonTextColorResId = -1;
    int mRightButtonTextColorResId = -1;

    UiLibDialogInterface.NormalOnClickListener mNormalLeftButtonListener;
    UiLibDialogInterface.NormalOnClickListener mNormalRightButtonListener;
    UiLibDialogInterface.NormalOnClickListener mNormalMidButtonListener;

    UiLibDialogInterface.ListOnItemClickListener mListOnItemListener;

    String[] mItems;

    boolean mLeftButtonAotuDismiss;
    boolean mRightButtonAotuDismiss;
    boolean mMidButtonAotuDismiss;
    boolean mCheckBoxVisible;
    boolean mCanceledOnTouchOutside = true;

    UiLibDialogParams(DialogTemplate template, @StyleRes int animId) {
        mTemplate = template;
        mLayoutResID = template.getLayoutId();
        mAnimId = (animId == -1) ? template.getAnimationId() : animId;
        mGravity = template.getGravity();
        mWidthPercent = template.getPercent();
    }
}
