package com.topunion.chili.greendao;

import android.content.Context;

import com.topunion.chili.greendao.gen.DaoMaster;
import com.topunion.chili.greendao.gen.DaoSession;

import org.greenrobot.greendao.query.QueryBuilder;

/**
 * Author      : renxiaoming
 * Date        : 2017/8/22
 * Description :
 * 进行数据库的管理
 * 1.创建数据库
 * 2.创建数据库表
 * 3.对数据库进行增删查改
 * 4.对数据库进行升级
 */

public class GreenDaoManager {
    private static final String TAG = GreenDaoManager.class.getSimpleName();
    private static final String DB_NAME = "yitou.db";//数据库名称
    private volatile static GreenDaoManager mDaoManager;//多线程访问
    private static DaoMaster.DevOpenHelper mHelper;
    private static DaoMaster mDaoMaster;
    private static DaoSession mDaoSession;
    private Context context;

    private GreenDaoManager() {

    }

    /**
     * 使用单例模式获得操作数据库的对象
     *
     * @return
     */
    public static GreenDaoManager getInstance() {
        GreenDaoManager instance = null;
        if (mDaoManager == null) {
            synchronized (GreenDaoManager.class) {
                if (instance == null) {
                    instance = new GreenDaoManager();
                    mDaoManager = instance;
                }
            }
        }
        return mDaoManager;
    }

    /**
     * 初始化Context对象
     *
     * @param context
     */
    public void init(Context context) {
        this.context = context;
    }

    /**
     * 判断数据库是否存在，如果不存在则创建
     *
     * @return
     */
    public DaoMaster getMaster() {
        if (null == mDaoMaster) {
            mHelper = new DaoMaster.DevOpenHelper(context, DB_NAME, null);
            mDaoMaster = new DaoMaster(mHelper.getWritableDatabase());
        }
        return mDaoMaster;
    }

    /**
     * 完成对数据库的增删查找
     *
     * @return
     */
    public DaoSession getSession() {
        if (null == mDaoSession) {
            if (null == mDaoMaster) {
                mDaoMaster = getMaster();
            }
            mDaoSession = mDaoMaster.newSession();
        }
        return mDaoSession;
    }

    /**
     * 设置debug模式开启或关闭，默认关闭
     *
     * @param flag
     */
    public void setDebug(boolean flag) {
        QueryBuilder.LOG_SQL = flag;
        QueryBuilder.LOG_VALUES = flag;
    }

    /**
     * 关闭数据库
     */
    public void closeDataBase() {
        closeHelper();
        closeSession();
    }

    public void closeSession() {
        if (null != mDaoSession) {
            mDaoSession.clear();
            mDaoSession = null;
        }
    }

    private void closeHelper() {
        if (mHelper != null) {
            mHelper.close();
            mHelper = null;
        }
    }

}
