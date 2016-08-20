package com.qwwuyu.recite.db;

import android.content.Context;

import com.qwwuyu.recite.db.DaoMaster.OpenHelper;

/**
 * GreenDao数据库控制单例
 * Created by qw on 2015-12-16
 */
public class DBUtil {
    private static DBUtil dbUtil;
    public static final String DB_NAME = "Recite";
    private DaoMaster daoMaster;
    private DaoSession daoSession;

    private DBUtil() {
    }

    public synchronized static void initUtil(Context context) {
        if (dbUtil == null) {
            dbUtil = new DBUtil();
            OpenHelper helper = new DaoMaster.DevOpenHelper(context, DB_NAME, null);
            dbUtil.daoMaster = new DaoMaster(helper.getWritableDatabase());
            dbUtil.daoSession = dbUtil.daoMaster.newSession();
        }
    }

    public static DBUtil getDbUtil() {
        if (dbUtil == null) {
            throw new RuntimeException("use initUtil before");
        }
        return dbUtil;
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }

}