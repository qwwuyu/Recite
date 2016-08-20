package com.qwwuyu.recite.utils;

import android.media.MediaPlayer;

public class PlayUtil {
    private static PlayUtil instance = new PlayUtil();
    private MediaPlayer mp;

    private PlayUtil() {
    }

    public static PlayUtil getPlayUtil() {
        return instance;
    }

    public void play(String path) {
        if (mp == null) {
            // 创建MediaPlayer对象并设置Listener
            mp = new MediaPlayer();
        } else {
            // 复用MediaPlayer对象
            mp.reset();
        }
        try {
            mp.setDataSource(path);
            mp.prepare();
            mp.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
