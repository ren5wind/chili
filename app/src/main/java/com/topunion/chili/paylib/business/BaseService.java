package com.topunion.chili.paylib.business;

import android.content.Context;

import com.topunion.chili.MyApplication;

/**
 * 基础Service
 *
 * @param <T>
 * @param <ID>
 */
public class BaseService<T, ID> extends DaoBaseService<T, ID> {

    protected Context mContext = MyApplication.getContext();

    @Override
        protected MyDatabaseManager getDatabseManager() {
        return MyDatabaseManager.getInstance();
    }

    @Override
    protected Class getTypeClass() {
        return getSuperClassGenricType(getClass(), BaseService.class, 0);
    }

}
