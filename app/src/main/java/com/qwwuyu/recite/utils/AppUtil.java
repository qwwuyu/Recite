package com.qwwuyu.recite.utils;

/**
 * 只在当前应用有用的工具类
 * Created by qiwei on 2016/8/4.
 */
public class AppUtil {
    public static String changeAccents(String accents) {
        return "[" + accents + "]";
    }

    public static String changeContent(String content) {
        return content.replace("\\n", "\n");
    }
}
