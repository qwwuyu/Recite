package com.qwwuyu.recite.activity.word;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.TextView;

import com.qwwuyu.recite.R;
import com.qwwuyu.recite.activity.BaseActivity;
import com.qwwuyu.recite.bean.Word;
import com.qwwuyu.recite.config.FinalConfig;
import com.qwwuyu.recite.config.TApplication;
import com.qwwuyu.recite.db.DBUtil;
import com.qwwuyu.recite.ui.SearchEditText;
import com.qwwuyu.recite.ui.adapter.CommAdapter;
import com.qwwuyu.recite.ui.adapter.CommViewHolder;
import com.qwwuyu.recite.ui.adapter.SimpleItemDecoration;
import com.qwwuyu.recite.utils.IntentUtil;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * 单词搜索界面
 */
public class SearchWordActivity extends BaseActivity implements Toolbar.OnMenuItemClickListener, TextWatcher, CommAdapter.AdapterListener<Word> {
    /** 搜索框 */
    @BindView(R.id.searchWord_edit)
    SearchEditText edit;
    /** 数据展示 */
    @BindView(R.id.searchWord_rv)
    RecyclerView rv;
    private LinearLayoutManager manager;
    private ArrayList<Word> words = new ArrayList<>();
    private View error_inflate;
    private String search = "";
    private int color;

    @Override
    protected int getContentViewId() {
        return R.layout.a_search_word;
    }

    @Override
    protected void init() {
        color = getResources().getColor(R.color.comm_color);
        setBackBtn();
        words.addAll(TApplication.orderWords);
//        inflateMenu(R.menu.menu_search_word, this);
        edit.addTextChangedListener(this);
        edit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    closeKeyboard();
                    return true;
                }
                return false;
            }
        });
        rv.setHasFixedSize(true);
        rv.addItemDecoration(new SimpleItemDecoration());
        manager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setAdapter(new CommAdapter<Word>(context, words, R.layout.item_search_word, this) {
            @Override
            public void onBind(int position, CommViewHolder holder, final Word data) {
                int index;
                if ((index = data.getText().indexOf(search)) != -1) {
                    Spannable spannable = new SpannableString(data.getText());
                    spannable.setSpan(new ForegroundColorSpan(color), index, index + search.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    ((TextView) holder.getView(R.id.searchWord_txt_word)).setText(spannable);
                } else {
                    holder.setText(R.id.searchWord_txt_word, data.getText());
                }
                holder.itemView.setBackgroundResource(position % 2 == 0 ? R.drawable.select_btn_white_blue : R.drawable.select_btn_orange);
                final ImageView img_collect = holder.getView(R.id.searchWord_img_collect);
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
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.anim_no, R.anim.anim_no);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return true;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        words.clear();
        search = edit.getText().toString();
        int startsIndex = 0;
        for (Word word : TApplication.orderWords) {
            if (word.getText().contains(search)) {
                if (word.getText().startsWith(search))
                    words.add(startsIndex++, word);
                else
                    words.add(word);
            }
        }
        notifyAdapter();
    }

    private void notifyAdapter() {
        rv.getAdapter().notifyDataSetChanged();
        onError();
    }

    private void onError() {
        if (words.size() == 0) {
            ViewStub error_vs = (ViewStub) findViewById(R.id.error_vs);
            if (error_vs != null) {
                error_inflate = error_vs.inflate();
                ((ImageView) error_inflate.findViewById(R.id.error_img)).setImageResource(R.drawable.img_huaji);
                ((TextView) error_inflate.findViewById(R.id.error_txt)).setText(R.string.searchWord_error);
            }
            error_inflate.setVisibility(View.VISIBLE);
        } else {
            if (error_inflate != null) {
                error_inflate.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    public void onItemClick(int position, View v, Word data) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(FinalConfig.it_bean, data);
        IntentUtil.gotoActivity(context, WordDetailActivity.class, bundle);
    }
}
