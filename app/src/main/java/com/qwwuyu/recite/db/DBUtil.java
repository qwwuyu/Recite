package com.qwwuyu.recite.db;

import android.content.Context;

import com.qwwuyu.recite.bean.DaoMaster;
import com.qwwuyu.recite.bean.DaoSession;


/**
 * GreenDao数据库控制单例
 * Created by qw on 2016/8/14.
 */
public class DBUtil {
    private static DBUtil dbUtil;
    public static final String DB_NAME = "Base";
    private DaoMaster daoMaster;
    private DaoSession daoSession;

    private DBUtil() {
    }

    public synchronized static void initUtil(Context context) {
        if (dbUtil == null) {
            dbUtil = new DBUtil();
            DaoMaster.OpenHelper helper = new DaoMaster.DevOpenHelper(context, DB_NAME, null);
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

    public static DaoSession getDaoSession() {
        return getDbUtil().daoSession;
    }
}