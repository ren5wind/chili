package com.topunion.chili.paylib.business;

import android.content.Context;

import com.j256.ormlite.android.AndroidConnectionSource;

import java.sql.SQLException;

/**
 * 数据库操作helper
 */
public class MyDatabaseHelper extends DatabaseHelper {

    //数据库版本号
    private static int DATABASE_VERSION = 3;

    private Context mContext;


    public MyDatabaseHelper(Context context) {
        super(context, DATABASE_VERSION);
        this.mContext = context;
    }

    @Override
    public void onCreate(AndroidConnectionSource connectionSource) throws SQLException {

    }

    @Override
    public void onUpgrade(int oldVersion, int newVersion, AndroidConnectionSource connectionSource) throws SQLException {

    }


}
