package com.topunion.chili.base.dialog;

import android.support.annotation.IntDef;
import android.view.Gravity;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Author      : renxiaoming
 * Date        : 2017-07-12
 * Description :
 */
public class DialogTemplate {

    public static final int TEMPLATE_NORMAL = 0;

    public static final int TEMPLATE_LIST = 1;

    private int template;

    private int layoutId = -1;
    private int animationId = -1;
    private int percent = 70;
    private int gravity = Gravity.CENTER;

    DialogTemplate(int template, int layoutId, int animationId, int percent, int gravity) {
        this.layoutId = layoutId;
        this.animationId = animationId;
        this.percent = percent;
        this.gravity = gravity;
        this.template = template;
    }

    int getLayoutId() {
        return layoutId;
    }

    int getAnimationId() {
        return animationId;
    }

    int getPercent() {
        return percent;
    }

    int getGravity() {
        return gravity;
    }

    @TEMPLATE
    int getTemplate() {
        return template;
    }

    @IntDef({TEMPLATE_NORMAL, TEMPLATE_LIST})
    @Retention(RetentionPolicy.SOURCE)
    @interface TEMPLATE {
    }
}
