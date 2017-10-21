package com.topunion.chili.paylib;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;

/**
 * 作者：林铱 于 2015/9/30 15:15创建
 * 邮箱：815777326@qq.com
 */
public class SPUtil {

    /**
     * 保存在手机里面的文件名
     */
    private static final String FILE_NAME = Constants.SP_NAME;

    private SPUtil() {
        throw new UnsupportedOperationException("SPUtil is not allowed to be instantiated");
    }

    /**
     * 保存数据
     *
     * @param context 上下文
     * @param key
     * @param object  保存的数据
     */
    public static void put(Context context, String key, Object object) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (object instanceof String)
            editor.putString(key, (String) object);
        else if (object instanceof Integer)
            editor.putInt(key, (Integer) object);
        else if (object instanceof Boolean)
            editor.putBoolean(key, (Boolean) object);
        else if (object instanceof Float)
            editor.putFloat(key, (Float) object);
        else if (object instanceof Long)
            editor.putLong(key, (Long) object);
        else
            editor.putString(key, object.toString());
        editor.apply();
    }

    /**
     * 获取数据
     *
     * @param context
     * @param key
     * @param defValue
     * @return
     */
    public static Object get(Context context, String key, Object defValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        if (defValue instanceof String)
            return sharedPreferences.getString(key, (String) defValue);
        else if (defValue instanceof Integer)
            return sharedPreferences.getInt(key, (Integer) defValue);
        else if (defValue instanceof Boolean)
            return sharedPreferences.getBoolean(key, (Boolean) defValue);
        else if (defValue instanceof Float)
            return sharedPreferences.getFloat(key, (Float) defValue);
        else if (defValue instanceof Long)
            return sharedPreferences.getLong(key, (Long) defValue);
        return null;
    }


    /**
     * 移除某个key值已经对应的值
     *
     * @param context
     * @param key
     */
    public static void remove(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key).apply();
    }


    /**
     * 清除所有
     *
     * @param context
     */
    public static void clear(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear().apply();
    }


    /**
     * 查询某个key是否已经存在
     *
     * @param context
     * @param key
     */
    public static boolean contains(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.contains(key);
    }


    /**
     * 获取所有的键值对
     *
     * @param context
     * @return
     */
    public static Map<String, ?> getAll(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getAll();
    }


}
