package com.qwwuyu.recite.utils;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.SparseArray;
import android.view.View;
import android.widget.EditText;

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
 * 常用工具类
 */
public class CommUtil {

    public static String changeAccents(String accents) {
        return "[" + accents + "]";
    }

    public static String changeContent(String content) {
        return content.replace("\\n", "\n");
    }

    /**
     * 拷贝资源文件到data/data/包名/files下
     */
    public static File copyFile(Context context, String filename) {
        File file = new File(context.getFilesDir(), filename);
        try {
            if ((file.exists() && file.length() > 0))
                return file;
            InputStream is = context.getAssets().open(filename);
            FileOutputStream fos = new FileOutputStream(file);
            byte[] bt = new byte[1024];
            int len;
            while ((len = is.read(bt)) != -1) {
                fos.write(bt, 0, len);
            }
            is.close();
            fos.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return file;
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

    private static final Pattern reUnicode = Pattern.compile("\\\\u([0-9a-zA-Z]{4})");

    /** unicode编码转中文 */
    public static String decode(String s) {
        Matcher m = reUnicode.matcher(s);
        StringBuffer sb = new StringBuffer(s.length());
        while (m.find()) {
            m.appendReplacement(sb, Character.toString((char) Integer.parseInt(m.group(1), 16)));
        }
        m.appendTail(sb);
        return sb.toString();
    }

    /**
     * 判断当前应用程序处于前台还是后台
     *
     * @return true 在后台
     */
    public static boolean isApplicationBroughtToBackground(final Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 当ViewHolder使用
     */
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

    /** 获取当前网络链接状态. */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE); // 获取网络服务
        if (connectivity == null) {
            // 判断网络服务是否为空
            return false;
        } else {
            // 判断当前是否有任意网络服务开启
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /** 处理按钮和文本空监听 */
    public static void setTxtWatch(final View btn, final EditText... edits) {
        btn.setEnabled(!isTextEmpty(edits));
        for (int i = 0; i < edits.length; i++) {
            edits[i].addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    btn.setEnabled(!isTextEmpty(edits));
                }
            });
        }
    }

    private static boolean isTextEmpty(EditText... edits) {
        for (int i = 0; i < edits.length; i++) {
            if ("".equals(edits[i].getText().toString())) {
                return true;
            }
        }
        return false;
    }

    /** 获取通知栏高度 */
    public static int getStatusBarHeight(Context context) {
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());
            return context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
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
}
