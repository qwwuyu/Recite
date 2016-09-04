package com.qwwuyu.recite.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qwwuyu.recite.R;
import com.qwwuyu.recite.bean.Word;
import com.qwwuyu.recite.config.TApplication;
import com.qwwuyu.recite.db.DBUtil;
import com.qwwuyu.recite.utils.AppUtil;
import com.qwwuyu.recite.utils.FileUtil;
import com.qwwuyu.recite.utils.PlayUtil;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 单词页
 */
public class WordView extends RelativeLayout {
    /** 英文文本 */
    @BindView(R.id.word_txt_text)
    TextView txt_text;
    /** 发音 */
    @BindView(R.id.word_txt_accents)
    TextView txt_accents;
    /** 中文文本 */
    @BindView(R.id.word_txt_content)
    TextView txt_content;
    /** 收藏 */
    @BindView(R.id.word_img_collect)
    ImageView img_collect;
    private Word word;

    public WordView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public WordView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public WordView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        View view = View.inflate(context, R.layout.view_word, null);
        addView(view, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        ButterKnife.bind(this);
    }

    @OnClick({R.id.word_txt_accents, R.id.word_img_collect})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.word_txt_accents:
                sound();
                break;
            case R.id.word_img_collect:
                collect();
                break;
            default:
                break;
        }
    }

    /** 发音 */
    private void sound() {
        if (word != null) {
            File file = new File(FileUtil.getInstance().baseSoundPath, word.getText() + ".mp3");
            if (file.exists()) PlayUtil.getPlayUtil().play(file.getPath());
        }
    }

    /** 收藏 */
    private void collect() {
        word.setCollect(!word.getCollect());
        word.setCollectTime(System.currentTimeMillis());
        img_collect.setImageResource(word.getCollect() ? R.drawable.img_collect_yes : R.drawable.img_collect_no);
        DBUtil.getDaoSession().getWordDao().update(word);
        if (word.getCollect()) {
            TApplication.collectWords.add(word);
        } else {
            TApplication.collectWords.remove(word);
        }
    }

    public void setWord(Word word) {
        this.word = word;
        txt_text.setText(word.getText());
        txt_content.setText(AppUtil.changeContent(word.getContent()));
        txt_accents.setText(AppUtil.changeAccents(word.getAccents()));
        img_collect.setImageResource(word.getCollect() ? R.drawable.img_collect_yes : R.drawable.img_collect_no);
    }
}
