package com.r0adkll.slidr;

import android.util.Log;


/**
 * 日志工具
 * Created by qiwei on 2016/8/4.
 */
public class LogUtil {
    private static final boolean SHOW_LOG = true;

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
