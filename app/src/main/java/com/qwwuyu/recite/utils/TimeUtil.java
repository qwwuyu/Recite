package com.qwwuyu.recite.utils;

public class TimeUtil {
    private static long beginTime;
    private static long lastTime;

    public static void setBegin() {
        beginTime = System.currentTimeMillis();
        lastTime = System.currentTimeMillis();
    }

    public static void logDiffer(String mark) {
        long timeMillis = System.currentTimeMillis();
        LogUtil.i(mark + ":Dif " + (timeMillis - lastTime));
        lastTime = timeMillis;
    }

    public static void logDifferBegin(String mark) {
        long timeMillis = System.currentTimeMillis();
        LogUtil.i(mark + ":DifB " + (timeMillis - beginTime));
    }
}
