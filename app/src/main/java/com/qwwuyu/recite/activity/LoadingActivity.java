package com.qwwuyu.recite.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.qwwuyu.recite.R;
import com.qwwuyu.recite.config.Config;
import com.qwwuyu.recite.config.TApplication;
import com.qwwuyu.recite.db.DBExecutor;
import com.qwwuyu.recite.db.DBUtil;
import com.qwwuyu.recite.db.Word;
import com.qwwuyu.recite.db.WordDao;
import com.qwwuyu.recite.utils.CommUtil;
import com.qwwuyu.recite.utils.LogUtil;
import com.qwwuyu.recite.utils.TimeUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 加载页面
 *
 * @author qw
 * @Description
 * @date 2015-12-16
 */
public class LoadingActivity extends BaseActivity {
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 0) {
                Intent intent = new Intent(context, MainActivity.class);
                startActivity(intent);
                finishNoAnim();
            }
            return true;
        }
    });

    @Override
    protected int getContentViewId() {
        return R.layout.a_loading;
    }

    @Override
    protected void findViews() {
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void setListener() {
    }

    @Override
    protected void getData() {
        parsingZip();
        initDB();
    }

    /**
     * 解压发音文件
     *
     * @updateTime 2015-12-17,上午11:04:45
     * @updateAuthor qw
     */
    private void parsingZip() {
        TimeUtil.setBegin();
        DBExecutor.addTask(new Runnable() {
            @Override
            public void run() {
                File outFile = new File(getFilesDir(), "sound");
                Config.baseSoundPath = outFile.getPath();
                outFile.mkdir();
                File a_mp3 = new File(outFile.getPath(), "a.mp3");
                if (!a_mp3.exists()) {// 文件不存在 需解压
                    CommUtil.parsingZipForAssets(context, "sound.zip", outFile.getPath());
                    LogUtil.i("文件成功解压");
                } else {
                    LogUtil.i("文件无需解压");
                }
                TimeUtil.logDiffer("文件解压完毕");
            }
        });
    }

    /**
     * 初始化数据库数据
     *
     * @updateTime 2015-12-17,上午11:09:33
     * @updateAuthor qw
     */
    private void initDB() {
        WordDao dao = DBUtil.getDbUtil().getDaoSession().getWordDao();
        dao.getSession().runInTx(new Runnable() {
            @Override
            public void run() {
                try {
                    TimeUtil.setBegin();
                    TApplication.wordList = DBUtil.getDbUtil().getDaoSession().getWordDao().loadAll();
                    if (TApplication.wordList.size() == 0) {
                        TimeUtil.logDiffer("需要重新加载数据");
                        InputStream is = context.getAssets().open("en_code.txt");
                        BufferedReader reader = new BufferedReader(new InputStreamReader(is, "utf-8"));
                        String line = null;
                        long id = 0;
                        while ((line = reader.readLine()) != null) {
                            handLine(line, id);
                            id++;
                        }
                        DBUtil.getDbUtil().getDaoSession().getWordDao().insertInTx(TApplication.wordList);
                    }
                    TimeUtil.logDiffer("数据输入完成:" + TApplication.wordList.size());
                    handler.sendEmptyMessage(0);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    /** 寻找需要的单词 */
    public static void handLine(String line, long id) throws Exception {
        if (!line.matches("[A-Z]:")) {
            if (line.contains("#")) {
                String[] split = line.split("#");
                Word word = new Word();
                word.setText(split[0]);
                word.setAccents(split[1]);
                word.setContent(split[2]);
                word.setId(id);
                word.setIndex(Integer.parseInt(split[3]));
                TApplication.wordList.add(word);
            }
        }
    }
}