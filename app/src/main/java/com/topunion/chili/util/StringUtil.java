package com.topunion.chili.util;

/**
 * Author      : renxiaoming
 * Date        : 2017/7/29
 * Description :
 */
public class StringUtil {
    public static boolean isEmpt(String str) {
        if (str == null || "".equals(str)) {
            return true;
        }
        return false;
    }
}
