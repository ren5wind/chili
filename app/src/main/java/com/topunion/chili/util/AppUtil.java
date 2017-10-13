package com.topunion.chili.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;

/**
 * Author      : renxiaoming
 * Date        : 2017/10/13
 * Description :
 */
public class AppUtil {
    public static boolean isApkDebugable(Context context) {
        try {
            ApplicationInfo info = context.getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {

        }
        return false;
    }

}
