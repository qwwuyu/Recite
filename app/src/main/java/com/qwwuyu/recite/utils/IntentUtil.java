package com.qwwuyu.recite.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.qwwuyu.recite.R;


/**
 * 简化Intent使用工具类
 * Created by qiwei on 2016/8/4.
 */
public class IntentUtil {
    public static void gotoActivity(Context context, Class<?> gotoClass) {
        Intent intent = new Intent(context, gotoClass);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.enter_enter, R.anim.enter_exit);
    }

    public static void gotoActivity(Context context, Class<?> gotoClass, Bundle bundle) {
        Intent intent = new Intent(context, gotoClass);
        intent.putExtras(bundle);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.enter_enter, R.anim.enter_exit);
    }

    public static void gotoActivityForResult(Context context, Class<?> gotoClass, int requestCode) {
        Intent intent = new Intent(context, gotoClass);
        ((Activity) context).startActivityForResult(intent, requestCode);
        ((Activity) context).overridePendingTransition(R.anim.enter_enter, R.anim.enter_exit);
    }

    public static void gotoActivityForResult(Context context, Class<?> gotoClass, Bundle bundle, int requestCode) {
        Intent intent = new Intent(context, gotoClass);
        intent.putExtras(bundle);
        ((Activity) context).startActivityForResult(intent, requestCode);
        ((Activity) context).overridePendingTransition(R.anim.enter_enter, R.anim.enter_exit);
    }

    public static void gotoActivityAndFinish(Context context, Class<?> gotoClass) {
        Intent intent = new Intent(context, gotoClass);
        context.startActivity(intent);
        ((Activity) context).finish();
        ((Activity) context).overridePendingTransition(R.anim.enter_enter, R.anim.enter_exit);
    }
}
