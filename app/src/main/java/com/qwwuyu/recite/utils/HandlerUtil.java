package com.qwwuyu.recite.utils;

import android.os.Handler;
import android.os.Message;

/**
 * handler封装工具类
 */
public class HandlerUtil {

    /**
     * 发送消息
     */
    public static void sendMessage(Handler handler, int what) {
        Message message = handler.obtainMessage();
        message.what = what;
        handler.sendMessage(message);
    }

    /**
     * 发送消息
     */
    public static void sendMessage(Handler handler, int what, Object object) {
        Message message = handler.obtainMessage();
        message.what = what;
        message.obj = object;
        handler.sendMessage(message);
    }

    /**
     * 发送消息
     */
    public static void sendMessage(Handler handler, int what, int arg1, Object object) {
        Message message = handler.obtainMessage();
        message.what = what;
        message.obj = object;
        message.arg1 = arg1;
        handler.sendMessage(message);
    }
}
