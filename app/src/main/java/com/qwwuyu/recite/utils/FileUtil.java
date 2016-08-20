package com.qwwuyu.recite.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * 文件工具类
 * Created by qiwei on 2016/6/6.
 */
public class FileUtil {
    /** 是否创建路径成功 */
    private boolean isCrate = false;
    /** 软连接路径1 */
    private String rootAllPath = "/sdcard";
    /** 软连接路径2 小于android4.0的path */
    private String rootLessICSPath = "/mnt/sdcard";
    /** 软连接路径3 大于android4.0的path */
    private String rootMoreICSPath = "/storage/sdcard0";
    /** 当前获取root命令的path */
    private String nowRootBasePath = null;
    /** 文件存储的基础路径 */
    private String basePath;
    /** 文件存储的子路径 */
    private String savePath = File.separator + "BackHome" + File.separator;
    /** 视频存放路径 */
    private String videoPath;
    /** 屏幕截图储存路径 */
    private String screenshotPath;
    /** 临时存储路径 */
    private String cachePath;

    private static FileUtil instance;

    private FileUtil(Context context) {
        /** 创建文件路径 */
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            basePath = Environment.getExternalStorageDirectory().getAbsolutePath() + savePath;
            File file = new File(basePath);
            isCrate = file.exists() || new File(basePath).mkdirs();
        }
        if (!isCrate) {
            basePath = context.getCacheDir().getPath() + savePath;
            File file = new File(basePath);
            isCrate = file.exists() || new File(basePath).mkdirs();
        }
        if (isCrate) {
            videoPath = basePath + "Video" + File.separator;
            screenshotPath = basePath + "Screenshot" + File.separator;
            cachePath = basePath + "Cache" + File.separator;
            new File(videoPath).mkdirs();
            new File(screenshotPath).mkdirs();
            new File(cachePath).mkdirs();
        }
        /** 创建root命令路径 */
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            if (new File(rootLessICSPath).exists()) {
                nowRootBasePath = rootLessICSPath + savePath + "Su" + File.separator;
            } else if (new File(rootMoreICSPath).exists()) {
                nowRootBasePath = rootMoreICSPath + savePath + "Su" + File.separator;
            } else if (new File(rootAllPath).exists()) {
                rootAllPath = rootAllPath + savePath + "Su" + File.separator;
            }
        }
        if (nowRootBasePath == null) {
            nowRootBasePath = context.getCacheDir().getPath() + savePath + "Su" + File.separator;
        }
        if (!new File(nowRootBasePath).exists() && !new File(nowRootBasePath).mkdirs()) {
            nowRootBasePath = null;
        }
    }

    public static synchronized void init(Context context) {
        if (instance == null) {
            instance = new FileUtil(context);
        }
    }

    public static FileUtil getInstance() {
        if (instance == null) {
            throw new RuntimeException("FileUtil no init");
        }
        return instance;
    }

    public String getVideoPathByTag(Object tag) {
        return videoPath + "video_" + tag + ".mp4";
    }

    public String getScreenshotPathByTag(Object tag) {
        return screenshotPath + "img_" + tag + ".jpg";
    }

    public String getImgPathByTag(Object tag) {
        return cachePath + "img_" + tag + ".jpg";
    }

    public String getRootImgPath(Object tag) {
        return nowRootBasePath + "img_" + tag + ".jpg";
    }

    public String getRootVideoPath(Object tag) {
        return nowRootBasePath + "video_" + tag + ".mp4";
    }

    public boolean isCrate() {
        return isCrate;
    }

    public boolean isRootCrete() {
        return nowRootBasePath != null;
    }

    public String getCachePath() {
        return cachePath;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public String getScreenshotPath() {
        return screenshotPath;
    }
}
