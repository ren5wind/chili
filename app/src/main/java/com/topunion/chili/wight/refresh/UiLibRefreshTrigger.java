package com.topunion.chili.wight.refresh;

/**
 * Created by Aspsine on 2015/8/17.
 */
public interface UiLibRefreshTrigger {
    void onPrepare();

    void onMove(int y, boolean isComplete, boolean automatic);

    void onRelease();

    void onComplete();

    void onReset();
}