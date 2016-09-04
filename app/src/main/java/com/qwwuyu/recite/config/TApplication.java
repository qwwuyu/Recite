package com.qwwuyu.recite.config;

import android.app.Application;
import android.content.Context;

import com.qwwuyu.recite.bean.Word;
import com.qwwuyu.recite.db.DBUtil;
import com.qwwuyu.recite.utils.CommUtil;
import com.qwwuyu.recite.utils.FileUtil;
import com.qwwuyu.recite.utils.SpUtil;

import java.util.ArrayList;


/**
 * 应用程序入口
 * Created by qw on 2016/5/29.
 */
public class TApplication extends Application {
    public static Context context;
    public static ArrayList<Word> orderWords;
    public static ArrayList<Word> indexWords = new ArrayList<>();
    public static ArrayList<Word> collectWords = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        if (!context.getPackageName().equals(CommUtil.getProcessName(context, android.os.Process.myPid()))) {
            return;
        }
        //初始化文件路径
        FileUtil.init(context);
        //初始化数据库
        DBUtil.initUtil(context);
        ServerConfig.num4Day = SpUtil.getSpUtil().getSPValue(SpUtil.SP_WORD4DAY, 10);
        ServerConfig.nowDay = SpUtil.getSpUtil().getSPValue(SpUtil.SP_TODAY, 1);
    }
}