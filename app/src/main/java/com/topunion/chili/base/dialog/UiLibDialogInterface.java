package com.topunion.chili.base.dialog;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.View;

/**
 * Author      : renxiaoming
 * Date        : 2017-07-12
 * Description :
 */
public interface UiLibDialogInterface {

    void init(@NonNull Context context, @LayoutRes int layoutResID);

    void setAnim(int setAnimId);

    void setOnclickListener(int viewId, View.OnClickListener listener, boolean aotuDismiss);

    void show();

    void dismiss();

    void apply(UiLibDialogParams params);


    interface NormalOnClickListener {
        void onClick(View v, boolean isChecked, String editText);
    }

    interface ListOnItemClickListener {
        void onItemClick(View v, int position, String itemText);
    }

}
