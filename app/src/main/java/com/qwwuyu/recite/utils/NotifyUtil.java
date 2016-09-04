package com.qwwuyu.recite.utils;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.qwwuyu.recite.R;


/**
 * 通知工具类
 * Created by qiwei on 2016/8/4.
 */
public class NotifyUtil {
    public static final int NOTIFY_ID = 100;

    /**
     * 发送通知栏通知
     *
     * @param context        上下文
     * @param notificationID 通知识别ID
     * @param smallTitle     闪现时的标题
     * @param title          挂载时的标题
     * @param message        挂载时的内容
     * @param intent         点击时的意图
     * @param requestCode    点击时的请求码
     */
    public static void showPushMessageNotification(Context context, int notificationID, String smallTitle, String title, String message,
                                                   Intent intent, int requestCode) {
        //创建用户点击的意图 PendingIntent.FLAG_CANCEL_CURRENT 表示相应的PendingIntent已经存在，则取消前者，然后创建新的PendingIntent
        PendingIntent pIntent = PendingIntent.getActivity(context, requestCode, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Activity.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
        mBuilder.setContentTitle(title)
                .setContentText(message)
                .setContentIntent(pIntent)
                .setTicker(smallTitle)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.app_icon)
                .setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND);//提醒方式 震动|声音
        Notification notify = mBuilder.build();
        notify.flags = Notification.FLAG_AUTO_CANCEL;//用户单击通知后自动消失
        notificationManager.cancel(notificationID);
        notificationManager.notify(notificationID, notify);
    }

    /**
     * 清除一个通知
     *
     * @param notificationID 通知识别ID
     */
    public static void clearNotification(Context context, int notificationID) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Activity.NOTIFICATION_SERVICE);
        notificationManager.cancel(notificationID);
    }

    /**
     * 清除所有通知
     */
    public static void cancelAll(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Activity.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }
}
