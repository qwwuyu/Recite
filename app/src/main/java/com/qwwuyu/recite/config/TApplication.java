package com.qwwuyu.recite.config;

import android.app.Application;
import android.content.Context;

import com.qwwuyu.recite.db.DBUtil;
import com.qwwuyu.recite.db.Word;

import java.util.List;

/**
 * 应用程序入口
 * Created by qw on 2016/5/29.
 */
public class TApplication extends Application {
    public static List<Word> wordList;
    public static int DAY_MEMORY = 100;
    public static int now_day = 1;
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        DBUtil.initUtil(getApplicationContext());
    }

    public static Context getContext() {
        return context;
    }
}
