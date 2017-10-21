package com.topunion.chili.paylib.business;

import android.util.Log;


/**
 * 数据访问管理者
 */
public class MyDatabaseManager extends DatabaseManager {
    private final String TAG = MyDatabaseManager.class.getSimpleName();
    static MyDatabaseHelper mHelper;

    private static MyDatabaseManager mInstance;

    public static MyDatabaseManager getInstance() {
        if (mInstance == null) {
            mInstance = new MyDatabaseManager();
        }
        return mInstance;
    }



    @Override
    public DatabaseHelper getDatabaseHelper() {
        if (mHelper == null) {
            Log.e(TAG,"dataBaseHelper is null ,did not call init(Context context) ??");
        }
        return mHelper;
    }

}
