package com.topunion.chili.base.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.annotation.StyleRes;
import android.view.Gravity;

import com.topunion.chili.R;

import static com.topunion.chili.base.dialog.DialogTemplate.TEMPLATE_LIST;
import static com.topunion.chili.base.dialog.DialogTemplate.TEMPLATE_NORMAL;

/**
 * Author      : renxiaoming
 * Date        : 2017-07-12
 * Description :
 *  动画、位置
 */
public class UiLibDialog {

    private UiLibDialogInterface mUiLibDialog;

    @SuppressLint("ResourceType")
    private UiLibDialog(Context context, @StyleRes int themeResId, @LayoutRes int layoutResID, @DialogTemplate.TEMPLATE int template) {
        switch (template) {
            case TEMPLATE_NORMAL:
                mUiLibDialog = new UiLibDialogNormal(context, themeResId, layoutResID);
                break;
            case TEMPLATE_LIST:
                mUiLibDialog = new UiLibDialogList(context, themeResId, layoutResID);
                break;
        }

    }

    public void show() {
        mUiLibDialog.show();
    }

    public void dismiss() {
        mUiLibDialog.dismiss();
    }

    public static class Builder {

        private UiLibDialog mDialog;

        private UiLibDialogParams p;

        private Context mContext;

        /**
         * 使用模板normal
         *
         * @param context
         */
        public Builder(Context context) {
            this(context, TEMPLATE_NORMAL, -1);
        }

        /**
         * 使用模板
         *
         * @param context
         * @param template 模板
         */
        public Builder(Context context, @DialogTemplate.TEMPLATE int template) {
            this(context, template, -1);
        }

        /**
         * 使用模板,自定义弹出动画
         *
         * @param context
         * @param template 模板
         * @param animId   动画资源
         */
        public Builder(Context context, @DialogTemplate.TEMPLATE int template, @StyleRes int animId) {
            mContext = context;
            DialogTemplate dialogTemplate = null;
            switch (template) {
                case TEMPLATE_NORMAL:
                    dialogTemplate = new DialogTemplate(template, R.layout.uilib_layout_dialog, -1, 70, Gravity.CENTER);
                    break;
                case TEMPLATE_LIST:
                    dialogTemplate = new DialogTemplate(template, R.layout.uilib_layout_dialog_list, -1, 70, Gravity.CENTER);
                    break;
            }
            p = new UiLibDialogParams(dialogTemplate, animId);
        }

        public UiLibDialog create() {
            mDialog = new UiLibDialog(mContext, R.style.uilib_dialog_style, p.mLayoutResID, p.mTemplate.getTemplate());
            mDialog.mUiLibDialog.apply(p);
            return mDialog;
        }

        public void show() {
            mDialog.show();
        }

        public Builder setIcon(Drawable drawable) {
            p.mIconDrawable = drawable;
            return this;
        }

        public Builder setIcon(int resId) {
            p.mIconId = resId;
            return this;
        }

        public Builder setTitle(@StringRes int titleId) {
            p.mTitle = mContext.getText(titleId);
            return this;
        }

        public Builder setTitle(@Nullable CharSequence title) {
            p.mTitle = title;
            return this;
        }

        public Builder setMessage(@StringRes int messageId) {
            p.mMessage = mContext.getText(messageId);
            return this;
        }

        public Builder setMessage(@Nullable CharSequence message) {
            p.mMessage = message;
            return this;
        }

        public Builder setTips(@StringRes int tipsId) {
            p.mTips = mContext.getText(tipsId);
            return this;
        }

        public Builder setTips(@Nullable CharSequence tips) {
            p.mTips = tips;
            return this;
        }

        public Builder setCheckBox(@StringRes int textId, Drawable drawable) {
            p.mCheckBoxText = mContext.getText(textId);
            p.mCheckBoxDrawable = drawable;
            p.mCheckBoxVisible = true;
            return this;
        }

        public Builder setCheckBox(CharSequence text, Drawable drawable) {
            p.mCheckBoxText = text;
            p.mCheckBoxDrawable = drawable;
            p.mCheckBoxVisible = true;
            return this;
        }

        public Builder setCheckBox(@StringRes int textId) {
            p.mCheckBoxText = mContext.getText(textId);
            p.mCheckBoxVisible = true;
            return this;
        }

        public Builder setCheckBox(CharSequence text) {
            p.mCheckBoxText = text;
            p.mCheckBoxVisible = true;
            return this;
        }

        public Builder setLeftButton(@StringRes int textId, final UiLibDialogInterface.NormalOnClickListener listener, boolean aotuDismiss) {
            p.mLeftButtonText = mContext.getText(textId);
            p.mNormalLeftButtonListener = listener;
            p.mLeftButtonAotuDismiss = aotuDismiss;
            return this;
        }

        public Builder setLeftButton(@StringRes int textId) {
            p.mLeftButtonText = mContext.getText(textId);
            p.mLeftButtonAotuDismiss = true;
            return this;
        }

        public Builder setLeftButton(CharSequence text, final UiLibDialogInterface.NormalOnClickListener listener, boolean aotuDismiss) {
            p.mLeftButtonText = text;
            p.mNormalLeftButtonListener = listener;
            p.mLeftButtonAotuDismiss = aotuDismiss;
            return this;
        }

        public Builder setLeftButton(CharSequence text) {
            p.mLeftButtonText = text;
            p.mLeftButtonAotuDismiss = true;
            return this;
        }

        public Builder setLeftButtonTextColor(int colorsResId) {
            p.mLeftButtonTextColorResId = colorsResId;
            return this;
        }

        public Builder setRightButton(@StringRes int textId, final UiLibDialogInterface.NormalOnClickListener listener, boolean aotuDismiss) {
            p.mRightButtonText = mContext.getText(textId);
            p.mNormalRightButtonListener = listener;
            p.mRightButtonAotuDismiss = aotuDismiss;
            return this;
        }

        public Builder setRightButton(@StringRes int textId) {
            p.mRightButtonText = mContext.getText(textId);
            p.mRightButtonAotuDismiss = true;
            return this;
        }

        public Builder setRightButton(CharSequence text, final UiLibDialogInterface.NormalOnClickListener listener, boolean aotuDismiss) {
            p.mRightButtonText = text;
            p.mNormalRightButtonListener = listener;
            p.mRightButtonAotuDismiss = aotuDismiss;
            return this;
        }

        public Builder setRightButton(CharSequence text) {
            p.mRightButtonText = text;
            p.mRightButtonAotuDismiss = true;
            return this;
        }

        public Builder setRightButtonTextColor(int colorsResId) {
            p.mRightButtonTextColorResId = colorsResId;
            return this;
        }

        public Builder setMidButton(@StringRes int textId, final UiLibDialogInterface.NormalOnClickListener listener, boolean aotuDismiss) {
            p.mMidButtonText = mContext.getText(textId);
            p.mNormalMidButtonListener = listener;
            p.mMidButtonAotuDismiss = aotuDismiss;
            return this;
        }

        public Builder setMidButton(@StringRes int textId) {
            p.mMidButtonText = mContext.getText(textId);
            p.mMidButtonAotuDismiss = true;
            return this;
        }

        public Builder setMidButton(CharSequence text, final UiLibDialogInterface.NormalOnClickListener listener, boolean aotuDismiss) {
            p.mMidButtonText = text;
            p.mNormalMidButtonListener = listener;
            p.mMidButtonAotuDismiss = aotuDismiss;
            return this;
        }

        public Builder setMidButton(CharSequence text) {
            p.mMidButtonText = text;
            p.mMidButtonAotuDismiss = true;
            return this;
        }

        public Builder setMidButtonTextColor(int colorsResId) {
            p.mMidButtonTextColorResId = colorsResId;
            return this;
        }

        /**
         * 设置edittexthit
         *
         * @param text 如果没有hit且需要edittext，那么请传入空串
         * @return
         */
        public Builder setEditTextHit(CharSequence text) {
            p.mEditTextHint = text;
            return this;
        }

        public Builder setEditTextHit(@StringRes int textId) {
            p.mEditTextHint = mContext.getText(textId);
            return this;
        }

        public Builder setList(String[] arrText) {
            p.mItems = arrText;
            return this;
        }

        public Builder setItems(String[] items, final UiLibDialogInterface.ListOnItemClickListener listener) {
            p.mItems = items;
            p.mListOnItemListener = listener;
            return this;
        }

        public Builder setCanceledOnTouchOutside(boolean cancel) {
            p.mCanceledOnTouchOutside = cancel;
            return this;
        }

        public Builder setAnim(@StyleRes int animId){
            p.mAnimId = animId;
            return this;
        }

        public Builder setGravity(int gravity){
            p.mGravity = gravity;
            return this;
        }

        /**
         * 设置dialog
         * @param percent 百分比，已*100
         * @return
         */
        public Builder setWidthPercentageForWindow(int percent){
            p.mWidthPercent = percent;
            return this;
        }

    }
}
