package com.qwwuyu.recite.activity.word;


import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.TextView;

import com.qwwuyu.recite.R;
import com.qwwuyu.recite.activity.BaseActivity;
import com.qwwuyu.recite.bean.Word;
import com.qwwuyu.recite.config.FinalConfig;
import com.qwwuyu.recite.config.TApplication;
import com.qwwuyu.recite.db.DBUtil;
import com.qwwuyu.recite.ui.adapter.CommAdapter;
import com.qwwuyu.recite.ui.adapter.CommViewHolder;
import com.qwwuyu.recite.ui.adapter.MoveCallback;
import com.qwwuyu.recite.ui.adapter.SimpleItemDecoration;
import com.qwwuyu.recite.ui.dialog.CustomDialog;
import com.qwwuyu.recite.utils.IntentUtil;
import com.qwwuyu.recite.utils.SortUtil;
import com.qwwuyu.recite.utils.SpUtil;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * 单词列表界面
 * Created by qw on 2016/8/22.
 */
public class CollectWordsActivity extends BaseActivity implements CommAdapter.AdapterListener<Word>, Toolbar.OnMenuItemClickListener {
    private ArrayList<Word> words = TApplication.collectWords;
    private MoveCallback itemTouchHelper;

    private enum CollectMode {
        LETTER_MODE, TIME_MODE,
    }

    /** 列表控件 */
    @BindView(R.id.wordList_rv)
    RecyclerView rv;

    @Override
    protected int getContentViewId() {
        return R.layout.a_collect_words;
    }

    @Override
    protected void init() {
        setBackBtn();
        inflateMenu(R.menu.menu_collect_words, this);
        if (SpUtil.getSpUtil().getSPValue(SpUtil.SP_COLLECT_MODE, CollectMode.LETTER_MODE.ordinal()) == CollectMode.LETTER_MODE.ordinal()) {
            SortUtil.sortListById(words);
        } else {
            SortUtil.sortListByTime(words);
        }
        rv.setHasFixedSize(true);
        rv.addItemDecoration(new SimpleItemDecoration());
        rv.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setAdapter(new CommAdapter<Word>(context, words, R.layout.item_collect_words, this) {
            @Override
            public void onBind(int position, CommViewHolder holder, final Word data) {
                holder.setText(R.id.wordList_txt_word, data.getText());
                holder.itemView.setBackgroundResource(R.drawable.select_btn_white_blue);
                final ImageView img_collect = holder.getView(R.id.wordList_img_collect);
                img_collect.setImageResource(R.drawable.img_collect_del);
                img_collect.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        data.setCollect(false);
                        DBUtil.getDaoSession().getWordDao().update(data);
                        TApplication.collectWords.remove(data);
                        notifyAdapter();
                    }
                });
            }
        });
        itemTouchHelper = new MoveCallback((CommAdapter) rv.getAdapter()) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder form, RecyclerView.ViewHolder to) {
                Word formWord = words.get(form.getAdapterPosition());
                Word toWord = words.get(to.getAdapterPosition());
                long formTime = formWord.getCollectTime();
                formWord.setCollectTime(toWord.getCollectTime());
                toWord.setCollectTime(formTime);
                return super.onMove(recyclerView, form, to);
            }
        };
        itemTouchHelper.setIsLongPressDragEnabled(SpUtil.getSpUtil().getSPValue(SpUtil.SP_COLLECT_MODE, CollectMode.LETTER_MODE.ordinal()) == CollectMode.TIME_MODE.ordinal());
        new ItemTouchHelper(itemTouchHelper).attachToRecyclerView(rv);
        onError();
    }

    private void notifyAdapter() {
        rv.getAdapter().notifyDataSetChanged();
        onError();
    }

    private void onError() {
        if (words.size() == 0) {
            ViewStub error_vs = (ViewStub) findViewById(R.id.error_vs);
            if (error_vs != null) {
                View inflate = error_vs.inflate();
                ((ImageView) inflate.findViewById(R.id.error_img)).setImageResource(R.drawable.img_tucao);
                ((TextView) inflate.findViewById(R.id.error_txt)).setText(R.string.collectWords_error);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        notifyAdapter();
    }

    @Override
    protected void onPause() {
        super.onPause();
        DBUtil.getDaoSession().getWordDao().updateInTx(words);
    }

    @Override
    public void onItemClick(int position, View v, Word data) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(FinalConfig.it_bean, data);
        IntentUtil.gotoActivity(context, WordDetailActivity.class, bundle);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.collectWords_time://时间排序
                SpUtil.getSpUtil().putSPValue(SpUtil.SP_COLLECT_MODE, CollectMode.TIME_MODE.ordinal());
                SortUtil.sortListByTime(words);
                notifyAdapter();
                itemTouchHelper.setIsLongPressDragEnabled(true);
                break;
            case R.id.collectWords_letter://字母排序
                SpUtil.getSpUtil().putSPValue(SpUtil.SP_COLLECT_MODE, CollectMode.LETTER_MODE.ordinal());
                SortUtil.sortListById(words);
                notifyAdapter();
                itemTouchHelper.setIsLongPressDragEnabled(false);
                break;
            case R.id.collectWords_del://清空收藏
                new CustomDialog(context).setMsg(getString(R.string.collectWords_delALl)).setLeftBtnWithDis(getString(R.string.ok),
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                for (Word word : words) {
                                    word.setCollect(false);
                                }
                                DBUtil.getDaoSession().getWordDao().updateInTx(words);
                                words.clear();
                                notifyAdapter();
                            }
                        }).setRightBtnWithDis(getString(R.string.cancel), null).show();
                break;
            default:
                break;
        }
        return true;
    }
}
