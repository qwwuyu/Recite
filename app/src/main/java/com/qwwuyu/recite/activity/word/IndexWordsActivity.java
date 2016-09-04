package com.qwwuyu.recite.activity.word;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.qwwuyu.recite.R;
import com.qwwuyu.recite.activity.BaseActivity;
import com.qwwuyu.recite.bean.Word;
import com.qwwuyu.recite.config.FinalConfig;
import com.qwwuyu.recite.config.TApplication;
import com.qwwuyu.recite.db.DBUtil;
import com.qwwuyu.recite.ui.adapter.CommAdapter;
import com.qwwuyu.recite.ui.adapter.CommViewHolder;
import com.qwwuyu.recite.ui.adapter.SimpleItemDecoration;
import com.qwwuyu.recite.utils.IntentUtil;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * 单词列表界面
 * Created by qw on 2016/8/22.
 */
public class IndexWordsActivity extends BaseActivity implements CommAdapter.AdapterListener<Word> {
    private ArrayList<Word> words = TApplication.indexWords;
    /** 列表控件 */
    @BindView(R.id.wordList_rv)
    RecyclerView rv;

    @Override
    protected int getContentViewId() {
        return R.layout.a_letter_words;
    }

    @Override
    protected void init() {
        setBackBtn();
        rv.setHasFixedSize(true);
        rv.addItemDecoration(new SimpleItemDecoration());
        rv.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setAdapter(new CommAdapter<Word>(context, words, R.layout.item_word_list, this) {
            @Override
            public void onBind(int position, CommViewHolder holder, final Word data) {
                holder.setText(R.id.wordList_txt_word, data.getText());
                holder.itemView.setBackgroundResource(position % 2 == 0 ? R.drawable.select_btn_white_blue : R.drawable.select_btn_orange);
                final ImageView img_collect = holder.getView(R.id.wordList_img_collect);
                img_collect.setImageResource(data.getCollect() ? R.drawable.img_collect_yes : R.drawable.img_collect_no);
                img_collect.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        data.setCollect(!data.getCollect());
                        data.setCollectTime(System.currentTimeMillis());
                        img_collect.setImageResource(data.getCollect() ? R.drawable.img_collect_yes : R.drawable.img_collect_no);
                        DBUtil.getDaoSession().getWordDao().update(data);
                        if (data.getCollect()) {
                            TApplication.collectWords.add(data);
                        } else {
                            TApplication.collectWords.remove(data);
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        rv.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onItemClick(int position, View v, Word data) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(FinalConfig.it_bean, data);
        IntentUtil.gotoActivity(context, WordDetailActivity.class, bundle);
    }
}
