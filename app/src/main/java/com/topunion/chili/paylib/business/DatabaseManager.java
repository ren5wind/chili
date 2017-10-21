package com.topunion.chili.paylib.business;


import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

/**
 * 数据管理器
 * 使用方式
 * 继承该类，然后重写 getDatabaseHelper
 */
public abstract class DatabaseManager {

    private static DatabaseManager mInstance;

    public Dao getDao(Class clazz) {
        try {
            return getDatabaseHelper().getDao(clazz);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 获取 dataBaseHelper
     * @return
     */
    public abstract DatabaseHelper getDatabaseHelper();
}
