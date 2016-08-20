package com.qwwuyu.recite.utils;

import android.content.Context;
import android.widget.Toast;


/**
 * Toast操作工具类
 */
public class ToastUtil {

    private static Toast toast;

    public static void showToast(Context context, Object text) {
        if (text == null || context == null) {
            return;
        }
        if (toast == null) {
            toast = Toast.makeText(context, text.toString(), Toast.LENGTH_SHORT);
        } else {
            toast.setText(text.toString());
        }
        toast.show();
    }

    public static void showToast(Context context, int res) {
        if (context == null) {
            return;
        }
        if (toast == null) {
            toast = Toast.makeText(context, res, Toast.LENGTH_SHORT);
        } else {
            toast.setText(res);
        }
        toast.show();
    }

//    private static Toast makeText(Context context, CharSequence text, int duration) {
//        Toast result = new Toast(context);
//        LayoutInflater inflate = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View v = inflate.inflate(R.layout.view_toast, null);
//        TextView tv = (TextView) v.findViewById(R.id.message);
//        tv.setText(text);
//        result.setDuration(duration);
//        result.setView(v);
//        return result;
//    }
//
//    private static Toast makeText(Context context, int resId, int duration) throws Resources.NotFoundException {
//        return makeText(context, context.getResources().getText(resId), duration);
//    }
}