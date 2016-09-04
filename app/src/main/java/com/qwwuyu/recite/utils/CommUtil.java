package com.qwwuyu.recite.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * 常用工具
 * Created by qiwei on 2016/8/4.
 */
public class CommUtil {

    /** 判断字符串是否为空 */
    public static boolean isEmpty(CharSequence str) {
        return str == null || "".equals(str);
    }

    /** 判断字符串是否不为空 */
    public static boolean isExist(CharSequence str) {
        return !isEmpty(str);
    }

    /**
     * 判断当前应用程序处于前台还是后台
     *
     * @return true 在后台
     */
    public static boolean isApplicationBroughtToBackground(final Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        return !tasks.isEmpty() && !tasks.get(0).topActivity.getPackageName().equals(context.getPackageName());
    }

    /** Adapter中当ViewHolder使用 */
    @SuppressWarnings("unchecked")
    public static <V extends View> V get(View convertView, int id) {
        SparseArray<View> viewHolder = (SparseArray<View>) convertView.getTag();
        if (viewHolder == null) {
            viewHolder = new SparseArray<>();
            convertView.setTag(viewHolder);
        }
        View childView = viewHolder.get(id);
        if (childView == null) {
            childView = convertView.findViewById(id);
            viewHolder.put(id, childView);
        }
        return (V) childView;
    }

    /** 获取状态栏高度 */
    public static int getStatusBarHeight(Context context) {
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());
            return context.getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            return 0;
        }
    }

    /** dp转px */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /** px转dp */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /** 获取屏幕宽度 */
    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /** 获取屏幕高度 */
    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    /** 获取当前网络链接状态. */
    public static boolean isNetworkAvailable(Context context) {
        // 获取网络服务
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        // 判断网络服务是否为空
        if (connectivity != null) {
            // 判断当前是否有任意网络服务开启
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (NetworkInfo anInfo : info) {
                    if (anInfo.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /** Checks if is connected. */
    public static boolean isConnected(final Context ctx) {
        ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isConnected();
    }

    /** Checks if is wifi. */
    public static boolean isWifi(final Context ctx) {
        WifiManager wm = (WifiManager) ctx.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wi = wm.getConnectionInfo();
        if (wi != null && (WifiInfo.getDetailedStateOf(wi.getSupplicantState()) == NetworkInfo.DetailedState.OBTAINING_IPADDR
                || WifiInfo.getDetailedStateOf(wi.getSupplicantState()) == NetworkInfo.DetailedState.CONNECTED)) {
            return false;
        }
        return false;
    }

    /** Checks if is umts. */
    public static boolean isUmts(final Context ctx) {
        TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getNetworkType() >= TelephonyManager.NETWORK_TYPE_UMTS;
    }

    /** Checks if is edge. */
    public static boolean isEdge(final Context ctx) {
        TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getNetworkType() == TelephonyManager.NETWORK_TYPE_EDGE;
    }

    /** 处理unicode字符集 */
    public static String decode(String s) {
        Matcher m = Pattern.compile("\\\\u([0-9a-zA-Z]{4})").matcher(s);
        StringBuffer sb = new StringBuffer(s.length());
        while (m.find()) {
            m.appendReplacement(sb, Character.toString((char) Integer.parseInt(m.group(1), 16)));
        }
        m.appendTail(sb);
        return sb.toString();
    }

    /** 返回是否点击在View上 */
    public static boolean isClickView(MotionEvent ev, View... views) {
        Rect rect = new Rect();
        for (View v : views) {
            if (!v.isShown()) {
                continue;
            }
            v.getGlobalVisibleRect(rect);
            if (rect.contains((int) ev.getRawX(), (int) ev.getRawY()))
                return true;
        }
        return false;
    }

    /** 获取对应的进程名 */
    public static String getProcessName(Context context, int pid) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (am != null) {
            List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
            if (runningApps != null && !runningApps.isEmpty()) {
                for (ActivityManager.RunningAppProcessInfo procInfo : runningApps) {
                    if (procInfo.pid == pid) {
                        return procInfo.processName;
                    }
                }
            }
        }
        return null;
    }

    /**
     * 打开应用市场
     *
     * @param context 上下文
     * @param goNow   立即开启意图?
     * @return 不存在 null,存在返回对应的Intent
     */
    public static Intent go2Market(Context context, boolean goNow) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + context.getPackageName()));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        List<ResolveInfo> localList = context.getPackageManager().queryIntentActivities(intent, PackageManager.GET_INTENT_FILTERS);
        if (localList == null || localList.size() == 0) {
            return null;
        } else if (goNow) {
            context.startActivity(intent);
        }
        return intent;
    }

    public static String getAssetsTxt(Context context, String fileName) {
        try {
            InputStream in = context.getResources().getAssets().open(fileName);
            StringBuilder sb = new StringBuilder("");
            byte[] bytes = new byte[1024];
            int read;
            while (-1 != (read = in.read(bytes))) {
                sb.append(new String(bytes, 0, read, "UTF-8"));
            }
            in.close();
            return sb.toString();
        } catch (Exception ignored) {
        }
        return "";
    }

    /**
     * 解压资源文件到outPath目录
     */
    public static void parsingZipForAssets(Context context, String filename, String outPath) {
        try {
            InputStream is = context.getAssets().open(filename);
            parsingZip(is, outPath);
            is.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 解压指定流到outPath目录
     */
    private static void parsingZip(InputStream inFile, String outPath) {
        try {
            ZipInputStream zips = new ZipInputStream(inFile);
            ZipEntry zey;
            String fName;
            while ((zey = zips.getNextEntry()) != null) {
                fName = zey.getName();
                if (zey.isDirectory()) {
                    fName = fName.substring(0, fName.length() - 1);
                    File folder = new File(outPath, fName);
                    folder.mkdir();// 创建一个文件夹
                } else {
                    File file = new File(outPath, fName);
                    file.createNewFile();// 创建一个文件
                    FileOutputStream fps = new FileOutputStream(file);
                    int ch;
                    byte[] bs = new byte[1024];
                    while ((ch = zips.read(bs)) != -1) {
                        fps.write(bs, 0, ch);
                        fps.flush();
                    }
                    fps.close();
                }
            }
            zips.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
