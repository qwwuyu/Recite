package com.qwwuyu.recite.activity.word;

import com.qwwuyu.recite.R;
import com.qwwuyu.recite.activity.BaseActivity;
import com.qwwuyu.recite.bean.Word;
import com.qwwuyu.recite.config.FinalConfig;
import com.qwwuyu.recite.config.TApplication;
import com.qwwuyu.recite.ui.WordView;

import butterknife.BindView;
import swipeback.SwipeBackUtils;
import swipeback.helper.SwipeBackHelper;

/**
 * 单词详情界面
 * Created by qw on 2016/8/31.
 */
public class WordDetailActivity extends BaseActivity {
    /** 单词详情View */
    @BindView(R.id.wordDetail_wordView)
    WordView wordView;
    private Word word;

    @Override
    protected boolean enableSlider() {
        return true;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.a_word_detail;
    }

    @Override
    protected void init() {
        setBackBtn();
        if (getIntent() != null && getIntent().getExtras() != null) {
            Word wordCopy = getIntent().getExtras().getParcelable(FinalConfig.it_bean);
            word = TApplication.indexWords.get(wordCopy.getIndex());
            wordView.setWord(word);
        } else {
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        SwipeBackUtils.convertActivityToTranslucent(this, new SwipeBackUtils.PageTranslucentListener() {
            @Override
            public void onPageTranslucent() {
                SwipeBackHelper.finish(WordDetailActivity.this);
            }
        });
    }
}
