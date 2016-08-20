package com.qwwuyu.recite.utils;

import android.util.Log;

/**
 * Log打印类
 * Created by qw on 2016/5/29.
 */
public class LogUtil {
    /** 是否显示日志 */
    public static final boolean SHOW_LOG = true;

    public static void i(Object obj) {
        if (SHOW_LOG && obj != null) {
            Log.i("qw", obj.toString());
        }
    }
}
