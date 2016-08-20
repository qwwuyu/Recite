package com.qwwuyu.recite.db;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

/**
 * 简单的线程
 * Created by qw on 2016/5/29.
 */
public abstract class EasyTask<T> implements Runnable {

    /** 异步处理Handler对象 */
    protected Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            onPost((T) msg.obj);
        }
    };

    public EasyTask() {
    }


    public EasyTask(Context context) {
    }

    @Override
    public void run() {
        mHandler.sendMessage(mHandler.obtainMessage(0, doInBack()));
    }

    public abstract T doInBack();

    public abstract void onPost(T object);

}
