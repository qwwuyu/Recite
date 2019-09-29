package com.qwwuyu.recite.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.qwwuyu.recite.R;
import com.qwwuyu.recite.bean.Word;
import com.qwwuyu.recite.bean.WordDao;
import com.qwwuyu.recite.config.TApplication;
import com.qwwuyu.recite.db.DBExecutor;
import com.qwwuyu.recite.db.DBUtil;
import com.qwwuyu.recite.utils.CommUtil;
import com.qwwuyu.recite.utils.FileUtil;
import com.qwwuyu.recite.utils.IntentUtil;
import com.qwwuyu.recite.utils.LogUtil;
import com.qwwuyu.recite.utils.SystemBarTintManager;
import com.qwwuyu.recite.utils.SystemUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 欢迎页
 */
public class SplashActivity extends BaseActivity {
    /** 版本号展示 */
    @BindView(R.id.splash_txt_version)
    TextView txt_version;

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 0) {
                Intent intent = new Intent(context, MainActivity.class);
                startActivity(intent);
                finish();
            }
            return true;
        }
    });

    @Override
    protected int getContentViewId() {
        return R.layout.a_splash;
    }

    @OnClick({R.id.splash_txt_use})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.splash_txt_use:
                IntentUtil.gotoActivityAndFinish(context, MainActivity.class);
                break;
            default:
                break;
        }
    }

    @Override
    protected void init() {
        //背景全铺处理
        tintManager.setStatusBarTintResource(0);
        tintManager.setStatusBarTintEnabled(false);
        final SystemBarTintManager.SystemBarConfig config = tintManager.getConfig();
        View view = findViewById(android.R.id.content);
        if (view != null) {
            view.setPadding(0, 0, config.getPixelInsetRight(), config.getPixelInsetBottom());
        }
        txt_version.setText(getString(R.string.splash_txt_version, SystemUtil.getVersionCode(context), SystemUtil.getVersionName(context)));
        parsingZip();
        if (TApplication.orderWords != null && TApplication.orderWords.size() > 0) {
            LogUtil.i("TApplication live");
            goMain();
        } else if ((TApplication.orderWords = new ArrayList<>(DBUtil.getDaoSession().getWordDao().loadAll())).size() == 0) {
            LogUtil.i("initDB");
            initDB();
        } else {
            LogUtil.i("goMain");
            goMain();
        }
    }

    private void goMain() {
        Intent intent = new Intent(context, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void parsingZip() {
        DBExecutor.addTask(new Runnable() {
            @Override
            public void run() {
                File a_mp3 = new File(FileUtil.getInstance().baseSoundPath, "zoo.mp3");
                if (!a_mp3.exists()) {// 文件不存在 需解压
                    LogUtil.i("需解压文件");
                    CommUtil.parsingZipForAssets(context, "sound.zip", FileUtil.getInstance().baseSoundPath);
                }
            }
        });
    }

    /**
     * 初始化数据库数据
     */
    private void initDB() {
        WordDao dao = DBUtil.getDaoSession().getWordDao();
        dao.getSession().runInTx(new Runnable() {
            @Override
            public void run() {
                try {
                    TApplication.orderWords = new ArrayList<>(DBUtil.getDaoSession().getWordDao().loadAll());
                    if (TApplication.orderWords.size() == 0) {
                        InputStream is = context.getAssets().open("en_code.txt");
                        BufferedReader reader = new BufferedReader(new InputStreamReader(is, "utf-8"));
                        String line;
                        long id = 0;
                        while ((line = reader.readLine()) != null) {
                            handLine(line, id);
                            id++;
                        }
                        DBUtil.getDaoSession().getWordDao().insertInTx(TApplication.orderWords);
                    }
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
                TApplication.orderWords.add(word);
            }
        }
    }
}