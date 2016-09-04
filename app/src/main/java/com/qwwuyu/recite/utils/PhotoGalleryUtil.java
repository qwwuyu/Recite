package com.qwwuyu.recite.utils;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore.Images;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 保存图片到相册的工具
 * Created by qiwei on 2016/8/4.
 */
public class PhotoGalleryUtil {
    private static final String IMAGE_DIR_NAME = "BackHome";
    private static final String IMAGE_FILE_NAME_TEMPLATE = "BackHome_%s.jpg";

    /**
     * @param context 上下文
     * @param bitmap  图片
     * @return 是否保存成功
     */
    public static boolean insertImage(Context context, Bitmap bitmap) {
        String cameraPath = FileUtil.getInstance().getCameraPath();
        boolean isSave = false;
        if (cameraPath != null) {
            File file = new File(cameraPath, "img_" + System.currentTimeMillis() + ".jpg");
            if (isSave = saveBitmap(bitmap, file.getAbsolutePath())) {
                scanFile(context, file);
            }
        }
        if (!isSave) {
            Uri uri = PhotoGalleryUtil.insertImage(context, bitmap, "BackHome", "screenshots");
            if (isSave = (uri != null)) {
                scanFile(context, uri);
            }
        }
        return isSave;
    }

    /** 让系统发现文件,否则需要系统重启才能发现图片 */
    public static void scanFile(Context context, String filePath) {
        if (filePath != null) {
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(filePath))));
        }
    }

    public static void scanFile(Context context, File file) {
        if (file != null) {
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
        }
    }

    /** 让系统发现文件,否则需要系统重启才能发现图片 */
    public static void scanFile(Context context, Uri url) {
        if (url != null) {
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, url));
        }
    }

    /**
     * 系统源码相册添加,修改ContentValues字段
     *
     * @deprecated use {@link #insertImage(Context, Bitmap)} 在某些手机上系统的方式无法插入到图库
     */
    @Deprecated
    public static Uri insertImage(Context context, String imagePath, String name, String description) {
        Bitmap bm = BitmapFactory.decodeFile(imagePath);
        Uri url = insertImage(context, bm, name, description);
        bm.recycle();
        return url;
    }

    /**
     * 系统源码相册添加,修改ContentValues字段
     *
     * @deprecated use {@link #insertImage(Context, Bitmap)} 在某些手机上系统的方式无法插入到图库
     */
    @Deprecated
    public static Uri insertImage(Context context, Bitmap source, String title, String description) {
        ContentResolver cr = context.getContentResolver();
        ContentValues values = new ContentValues();
        long mImageTime = System.currentTimeMillis();
        long dateSeconds = mImageTime / 1000;

        values.put(Images.ImageColumns.DISPLAY_NAME, title);
        values.put(Images.ImageColumns.DATE_TAKEN, mImageTime);
        values.put(Images.ImageColumns.DATE_ADDED, dateSeconds);
        values.put(Images.ImageColumns.DATE_MODIFIED, dateSeconds);
        values.put(Images.ImageColumns.TITLE, title);
        values.put(Images.Media.DESCRIPTION, description);
        values.put(Images.ImageColumns.MIME_TYPE, "image/jpeg");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            values.put(Images.ImageColumns.WIDTH, source.getWidth());
            values.put(Images.ImageColumns.HEIGHT, source.getHeight());
        }

        Uri url = null;

        try {
            url = cr.insert(Images.Media.EXTERNAL_CONTENT_URI, values);
            if (source != null) {
                FileUtil.dirUriPath(context, GetPathUtil.getPath(context, url));
                OutputStream imageOut = cr.openOutputStream(url);
                try {
                    source.compress(Bitmap.CompressFormat.JPEG, 100, imageOut);
                } finally {
                    imageOut.close();
                }
                long id = ContentUris.parseId(url);
                // Wait until MINI_KIND thumbnail is generated.
                Bitmap miniThumb = Images.Thumbnails.getThumbnail(cr, id, Images.Thumbnails.MINI_KIND, null);
                // This is for backward compatibility.
                StoreThumbnail(cr, miniThumb, id, 50F, 50F, Images.Thumbnails.MICRO_KIND);
            } else {
                cr.delete(url, null, null);
                url = null;
            }
        } catch (Exception e) {
            if (url != null) {
                cr.delete(url, null, null);
                url = null;
            }
        }
        return url;
    }

    private static Bitmap StoreThumbnail(ContentResolver cr, Bitmap source, long id, float width, float height, int kind) {
        // create the matrix to scale it
        Matrix matrix = new Matrix();
        float scaleX = width / source.getWidth();
        float scaleY = height / source.getHeight();
        matrix.setScale(scaleX, scaleY);
        Bitmap thumb = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
        ContentValues values = new ContentValues(4);
        values.put(Images.Thumbnails.KIND, kind);
        values.put(Images.Thumbnails.IMAGE_ID, (int) id);
        values.put(Images.Thumbnails.HEIGHT, thumb.getHeight());
        values.put(Images.Thumbnails.WIDTH, thumb.getWidth());
        Uri url = cr.insert(Images.Thumbnails.EXTERNAL_CONTENT_URI, values);
        try {
            OutputStream thumbOut = cr.openOutputStream(url);
            thumb.compress(Bitmap.CompressFormat.JPEG, 100, thumbOut);
            thumbOut.close();
            return thumb;
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * 试验失败,仿截屏保存
     *
     * @deprecated use {@link #insertImage(Context, Bitmap)} 无法使用
     */
    @Deprecated
    public static boolean insertImageByScreenshot(Context context, String imageFilePath) {
        try {
            long imageTime = System.currentTimeMillis();
            String imageDate = new SimpleDateFormat("yyyyMMdd-HHmmss", Locale.getDefault()).format(new Date(imageTime));
            String imageFileName = String.format(IMAGE_FILE_NAME_TEMPLATE, imageDate);
            long dateSeconds = imageTime / 1000;
            // Save the screenshot to the MediaStore
            ContentValues values = new ContentValues();
            ContentResolver resolver = context.getContentResolver();
            values.put(Images.ImageColumns.DATA, imageFilePath);
            values.put(Images.ImageColumns.TITLE, imageFileName);
            values.put(Images.ImageColumns.DISPLAY_NAME, imageFileName);
            values.put(Images.ImageColumns.DATE_TAKEN, imageTime);
            values.put(Images.ImageColumns.DATE_ADDED, dateSeconds);
            values.put(Images.ImageColumns.DATE_MODIFIED, dateSeconds);
            values.put(Images.ImageColumns.MIME_TYPE, "image/jpeg");
            values.put(Images.ImageColumns.SIZE, new File(imageFilePath).length());
            Uri uri = resolver.insert(Images.Media.EXTERNAL_CONTENT_URI, values);
            // update file size in the database
            values.clear();
            values.put(Images.ImageColumns.SIZE, new File(imageFilePath).length());
            resolver.update(uri, values, null, null);
            scanFile(context, uri);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 试验失败,仿截屏保存
     *
     * @deprecated use {@link #insertImage(Context, Bitmap)} 无法使用
     */
    @Deprecated
    public static boolean insertImageByScreenshot(Context context, Bitmap image) {
        try {
            long imageTime = System.currentTimeMillis();
            int imageWidth = image.getWidth();
            int imageHeight = image.getHeight();
            String imageDate = new SimpleDateFormat("yyyyMMdd-HHmmss", Locale.getDefault()).format(new Date(imageTime));
            String imageFileName = String.format(IMAGE_FILE_NAME_TEMPLATE, imageDate);
            File imageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), IMAGE_DIR_NAME);
            String imageFilePath = new File(imageDir, imageFileName).getAbsolutePath();
            imageDir.mkdirs();
            long dateSeconds = imageTime / 1000;
            OutputStream out = new FileOutputStream(imageFilePath);
            image.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            // Save the screenshot to the MediaStore
            ContentValues values = new ContentValues();
            ContentResolver resolver = context.getContentResolver();
            values.put(Images.ImageColumns.DATA, imageFilePath);
            values.put(Images.ImageColumns.TITLE, imageFileName);
            values.put(Images.ImageColumns.DISPLAY_NAME, imageFileName);
            values.put(Images.ImageColumns.DATE_TAKEN, imageTime);
            values.put(Images.ImageColumns.DATE_ADDED, dateSeconds);
            values.put(Images.ImageColumns.DATE_MODIFIED, dateSeconds);
            values.put(Images.ImageColumns.MIME_TYPE, "image/jpeg");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                values.put(Images.ImageColumns.WIDTH, imageWidth);
                values.put(Images.ImageColumns.HEIGHT, imageHeight);
            }
            values.put(Images.ImageColumns.SIZE, new File(imageFilePath).length());
            Uri uri = resolver.insert(Images.Media.EXTERNAL_CONTENT_URI, values);
            // update file size in the database
            values.clear();
            values.put(Images.ImageColumns.SIZE, new File(imageFilePath).length());
            resolver.update(uri, values, null, null);
            scanFile(context, uri);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static String saveBitmap(Bitmap bitmap) {
        String path = FileUtil.getInstance().getImagePathByTag(System.currentTimeMillis());
        if (saveBitmap(bitmap, path)) {
            return path;
        }
        return null;
    }

    public static boolean saveBitmap(Bitmap bitmap, String path) {
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
