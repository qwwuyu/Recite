package com.qwwuyu.recite.utils;

import android.util.Log;

import com.qwwuyu.recite.config.ReleaseConfig;


/**
 * 日志工具
 * Created by qiwei on 2016/8/4.
 */
public class LogUtil {
    private static final boolean SHOW_LOG = ReleaseConfig.IS_DEBUG;

    public static void exception(Exception e) {
        if (SHOW_LOG && e != null) {
            e.printStackTrace();
        }
    }

    public static void i(Object obj) {
        if (SHOW_LOG && obj != null) {
            Log.i("qw", obj.toString());
        }
    }
}
