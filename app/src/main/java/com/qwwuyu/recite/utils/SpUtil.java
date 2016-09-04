package com.qwwuyu.recite.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.qwwuyu.recite.config.TApplication;


/**
 * sp数据操作工具类
 */
public class SpUtil {
    private static SpUtil spUtil;
    private static SharedPreferences sp;
    private static final String SP_NAME = "SharedPreferences_comm";
    public static final String SP_FIRST_USE = "sp_first_use";
    public static final String SP_COLLECT_MODE = "sp_collect_mode";
    public static final String SP_WORD4DAY = "sp_word4day";
    public static final String SP_TODAY = "sp_today";

    /**
     * 获取一个操作sp数据的实例
     */
    public static SpUtil getSpUtil() {
        if (spUtil == null) {
            synchronized (SP_NAME) {
                if (spUtil == null) {
                    spUtil = new SpUtil(SP_NAME, Context.MODE_PRIVATE);
                }
            }
        }
        return spUtil;
    }

    private SpUtil(String fileKey, int mode) {
        sp = TApplication.context.getSharedPreferences(fileKey, mode);
    }

    /**
     * sp保存一个int数据
     */
    public void putSPValue(String valueKey, int value) {
        sp.edit().putInt(valueKey, value).apply();
    }

    /**
     * sp保存一个float数据
     */
    public void putSPValue(String valueKey, float value) {
        sp.edit().putFloat(valueKey, value).apply();
    }

    /**
     * sp保存一个String数据
     */
    public void putSPValue(String valueKey, String value) {
        sp.edit().putString(valueKey, value).apply();
    }

    /**
     * sp保存一个boolean数据
     */
    public void putSPValue(String valueKey, boolean value) {
        sp.edit().putBoolean(valueKey, value).apply();
    }

    /**
     * sp保存一个long数据
     */
    public void putSPValue(String valueKey, long value) {
        sp.edit().putLong(valueKey, value).apply();
    }

    /**
     * sp获取一个int数据
     */
    public int getSPValue(String valueKey, int value) {
        return sp.getInt(valueKey, value);
    }

    /**
     * sp获取一个float数据
     */
    public float getSPValue(String valueKey, float value) {
        return sp.getFloat(valueKey, value);
    }

    /**
     * sp获取一个String数据
     */
    public String getSPValue(String valueKey, String value) {
        return sp.getString(valueKey, value);
    }

    /**
     * sp获取一个boolean数据
     */
    public boolean getSPValue(String valueKey, boolean value) {
        return sp.getBoolean(valueKey, value);
    }

    /**
     * sp获取一个long数据
     */
    public long getSPValue(String valueKey, long value) {
        return sp.getLong(valueKey, value);
    }

    /**
     * 清理sp数据
     */
    public void clear() {
        sp.edit().clear().apply();
    }

}
