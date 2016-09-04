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
    private String savePath = File.separator + "Base" + File.separator;
    /** 视频存放路径 */
    private String videoPath;
    /** 屏幕截图储存路径 */
    private String imagePath;
    /** 临时存储路径 */
    private String cachePath;
    /** Camera存储路径 */
    private String cameraPath;
    /** sound存储路径 */
    public String baseSoundPath;
    /** 单例 */
    private static FileUtil instance;

    private FileUtil(Context context) {
        /** 创建文件路径 */
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            basePath = Environment.getExternalStorageDirectory().getAbsolutePath() + savePath;
            isCrate = new File(basePath).exists() || new File(basePath).mkdirs();
        }
        if (!isCrate) {
            basePath = context.getCacheDir().getPath() + savePath;
            isCrate = new File(basePath).exists() || new File(basePath).mkdirs();
        }
        if (isCrate) {
            videoPath = basePath + "Video" + File.separator;
            imagePath = basePath + "Image" + File.separator;
            cachePath = basePath + "Cache" + File.separator;
            new File(videoPath).mkdirs();
            new File(imagePath).mkdirs();
            new File(cachePath).mkdirs();
        }
        baseSoundPath = context.getCacheDir().getPath() + savePath + "sound";
        new File(baseSoundPath).mkdirs();
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
        /** 创建camera路径 */
        File appDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM + "/Camera");
        if (appDir.exists() || appDir.mkdir() || appDir.mkdirs()) {
            cameraPath = appDir.getAbsolutePath();
        }
        log();
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

    public String getImagePathByTag(Object tag) {
        return imagePath + "img_" + tag + ".jpg";
    }

    public String getCacheByTag(Object tag) {
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

    public void log() {
        LogUtil.i("isCrate " + isCrate);
        LogUtil.i("nowRootBasePath " + nowRootBasePath);
        LogUtil.i("basePath " + basePath);
    }

    public String getCachePath() {
        return cachePath;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getCameraPath() {
        return cameraPath;
    }

    public String getTempImgPath() {
        return imagePath + "temp.jpg";
    }

    public String getSmallImgPath() {
        return imagePath + "small.jpg";
    }

    public static boolean dirUriPath(Context context, String path) {
        if (path != null) {
            File file = new File(path);
            if (path.contains(".")) {
                return file.getParentFile().exists() || file.getParentFile().mkdirs();
            } else {
                return file.exists() || file.mkdirs();
            }
        }
        return false;
    }
}
