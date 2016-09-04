package com.qwwuyu.recite.activity.word;


import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qwwuyu.recite.R;
import com.qwwuyu.recite.activity.BaseActivity;
import com.qwwuyu.recite.bean.Word;
import com.qwwuyu.recite.config.FinalConfig;
import com.qwwuyu.recite.config.TApplication;
import com.qwwuyu.recite.db.DBUtil;
import com.qwwuyu.recite.ui.LetterIndexView;
import com.qwwuyu.recite.ui.adapter.CommAdapter;
import com.qwwuyu.recite.ui.adapter.CommViewHolder;
import com.qwwuyu.recite.ui.adapter.OrderItemDecoration;
import com.qwwuyu.recite.utils.IntentUtil;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;

/**
 * 单词列表界面
 * Created by qw on 2016/8/22.
 */
public class OrderWordsActivity extends BaseActivity {
    private ArrayList<Word> words = TApplication.orderWords;
    /** 列表控件 */
    @BindView(R.id.orderWords_rv)
    RecyclerView rv;
    /** LetterIndexView */
    @BindView(R.id.orderWords_indexView)
    LetterIndexView indexView;
    /** 大文字提示 */
    @BindView(R.id.orderWords_txt_letter)
    TextView txt_letter;

    @Override
    protected int getContentViewId() {
        return R.layout.a_order_words;
    }

    @Override
    protected void init() {
        setBackBtn();
        rv.setHasFixedSize(true);
        rv.addItemDecoration(new OrderItemDecoration());
        final LinearLayoutManager manager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(manager);
        rv.setItemAnimator(new DefaultItemAnimator());
        /** 首字母 */
        final HashMap<String, Integer> headMap = new HashMap<>();
        rv.setAdapter(new CommAdapter<Word>(context, words, R.layout.item_order_words) {
            @Override
            protected void onCreate() {
                for (int i = 0; i < list.size(); i++) {
                    if (!headMap.containsKey(list.get(i).getText().substring(0, 1).toUpperCase())) {
                        headMap.put(list.get(i).getText().substring(0, 1).toUpperCase(), i);
                    }
                }
            }

            @Override
            public void onBind(int position, CommViewHolder holder, final Word data) {
                final ImageView img_collect = holder.getView(R.id.orderWords_img_collect);
                LinearLayout ll_content = holder.getView(R.id.orderWords_ll_content);
                TextView txt_title = holder.getView(R.id.orderWords_txt_title);
                if (isHead(data.getText().substring(0, 1).toUpperCase(), position)) {
                    txt_title.setVisibility(View.VISIBLE);
                    txt_title.setText(data.getText().substring(0, 1).toUpperCase());
                } else {
                    txt_title.setVisibility(View.GONE);
                }
                holder.setText(R.id.orderWords_txt_word, data.getText());
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
                ll_content.setBackgroundResource(position % 2 == 0 ? R.drawable.select_btn_white_blue : R.drawable.select_btn_orange);
                ll_content.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putParcelable(FinalConfig.it_bean, data);
                        IntentUtil.gotoActivity(context, WordDetailActivity.class, bundle);
                    }
                });
            }

            /** 检查是否是头一个首字母 */
            private boolean isHead(String headChar, int position) {
                return headMap.containsKey(headChar) && headMap.get(headChar) == position;
            }
        });
        indexView.setVisibility(View.VISIBLE);
        indexView.init(new LetterIndexView.OnTouchLetterIndex() {
            @Override
            public void touchLetterWitch(String letter) {
                txt_letter.setVisibility(View.VISIBLE);
                txt_letter.setText(letter);
                if (headMap.containsKey(letter)) {
                    manager.scrollToPositionWithOffset(headMap.get(letter), 0);
                }
            }

            @Override
            public void touchFinish() {
                txt_letter.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        rv.getAdapter().notifyDataSetChanged();
    }
}
