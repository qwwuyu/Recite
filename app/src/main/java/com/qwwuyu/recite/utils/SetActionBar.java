package com.qwwuyu.recite.utils;

import android.app.Activity;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;


/**
 * Created by ylp890525 on 2015/9/15.
 */
public class SetActionBar {
    public static SystemBarTintManager setTranslucentStatus(Activity context, int color) {
        Window win = context.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams winParams = win.getAttributes();
            final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            winParams.flags |= bits;
            win.setAttributes(winParams);
        }
        SystemBarTintManager tintManager = new SystemBarTintManager(context);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(color);//状态栏背景色
        final SystemBarTintManager.SystemBarConfig config = tintManager.getConfig();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            View view = context.findViewById(android.R.id.content);
            if (view != null) {
                view.setPadding(0, config.getStatusBarHeight(), config.getPixelInsetRight(), config.getPixelInsetBottom());
            }
        }
        return tintManager;
    }

    /** 设置字体图标样式 */
    public static void setStatusBarDarkMode(Activity activity, boolean darkMode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int systemUiVisibility = activity.getWindow().getDecorView().getSystemUiVisibility();
            if (darkMode) {
                activity.getWindow().getDecorView().setSystemUiVisibility(systemUiVisibility | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            } else {
                activity.getWindow().getDecorView().setSystemUiVisibility(systemUiVisibility & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        } else if (setMiuiStatusBarDarkMode(activity, darkMode)) {

        } else if (setMeizuStatusBarDarkIcon(activity, darkMode)) {

        }
    }

    /** 小米修改MIUI */
    public static boolean setMiuiStatusBarDarkMode(Activity activity, boolean darkMode) {
        Class<? extends Window> clazz = activity.getWindow().getClass();
        try {
            int darkModeFlag;
            Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
            extraFlagField.invoke(activity.getWindow(), darkMode ? darkModeFlag : 0, darkModeFlag);
            return true;
        } catch (Exception ignored) {
        }
        return false;
    }

    /** 魅族修改Flyme */
    public static boolean setMeizuStatusBarDarkIcon(Activity activity, boolean darkMode) {
        if (activity != null) {
            try {
                WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class.getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (darkMode) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                activity.getWindow().setAttributes(lp);
                return true;
            } catch (Exception ignored) {
            }
        }
        return false;
    }
}
