package com.topunion.chili.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    /**
     * 使用正则表达式提取中括号中的内容
     * @param msg
     * @return
     */
    public static List<String> extractMessageByRegular(String msg){

        List<String> list=new ArrayList<String>();
        Pattern p = Pattern.compile("(\\[[^\\]]*\\])");
        Matcher m = p.matcher(msg);
        while(m.find()){
            list.add(m.group().substring(1, m.group().length()-1));
        }
        return list;
    }
}
