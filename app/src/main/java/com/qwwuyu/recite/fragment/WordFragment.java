package com.qwwuyu.recite.fragment;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.qwwuyu.recite.R;
import com.qwwuyu.recite.bean.EventBean;
import com.qwwuyu.recite.config.FinalConfig;
import com.qwwuyu.recite.config.ServerConfig;
import com.qwwuyu.recite.config.TApplication;
import com.qwwuyu.recite.ui.PressedTextView;
import com.qwwuyu.recite.ui.adapter.CommAdapter;
import com.qwwuyu.recite.ui.adapter.CommViewHolder;
import com.qwwuyu.recite.ui.dialog.PickerDialog;
import com.qwwuyu.recite.utils.SpUtil;
import com.qwwuyu.recite.utils.SpannableUtil;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;

/**
 * 单词
 * Created by qw on 2016/8/21.
 */
public class WordFragment extends BaseFragment {
    private String[] contents = new String[]{"当前选择第(\u00A0%d\u00A0)天", "该天所有单词", "分页浏览单词",
            "通过单词回忆翻译", "通过翻译回忆单词", "回顾前2天单词"};
    private ArrayList<String> items = new ArrayList<>(Arrays.asList(contents));
    @BindView(R.id.word_rv)
    RecyclerView rv;

    @Override
    protected int getContentViewId() {
        return R.layout.f_word;
    }

    @Override
    public void getData() {
        if (isInited) {
            return;
        }
        items.set(0, String.format(contents[0], ServerConfig.nowDay));
        isInited = true;
        initContent();
    }

    @Override
    protected void setData() {
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setAdapter(new CommAdapter<String>(context, items, R.layout.item_word_f) {
            @Override
            public void onBind(final int position, CommViewHolder holder, final String data) {
                PressedTextView txt_item = holder.getView(R.id.word_txt_item);
                final ArrayList<SpannableUtil.LineSpan> lineSpans = SpannableUtil.setLinkSpan(txt_item, data, "(", ")",
                        getColor(R.color.comm_color), getColor(R.color.def_txt_normal), getDimension(R.dimen.def_txt_link));
                txt_item.setPressedListener(new PressedTextView.PressedListener() {
                    @Override
                    public void onPressed(boolean pressed) {
                        for (SpannableUtil.LineSpan lineSpan : lineSpans) {
                            if (pressed) lineSpan.setLink(getColor(R.color.white), getColor(R.color.def_txt_normal), 0);
                            else lineSpan.setLink(getColor(R.color.comm_color), getColor(R.color.def_txt_normal), getDimension(R.dimen.def_txt_link));
                        }
                    }
                });
                holder.setOnClickListener(R.id.word_card, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClick(position);
                    }
                });
            }
        });
    }

    private void onItemClick(int position) {
        switch (position) {
            case 0:
                selectNum();
                break;
            default:
                break;
        }
    }

    /** 选择天 */
    private void selectNum() {
        ArrayList<String> nums = new ArrayList<>();
        int days = (TApplication.indexWords.size() + ServerConfig.num4Day - 1) / ServerConfig.num4Day;
        for (int i = 1; i <= days; i++) {
            nums.add(getString(R.string.word_txt_day, i));
        }
        PickerDialog pickerDialog = new PickerDialog(context, nums, new PickerDialog.OnSelectListener() {
            @Override
            public void onSelect(int oldPosition, int position) {
                if (position != oldPosition) {
                    ServerConfig.nowDay = position + 1;
                    items.set(0, String.format(contents[0], ServerConfig.nowDay));
                    rv.getAdapter().notifyItemChanged(0);
                    SpUtil.getSpUtil().putSPValue(SpUtil.SP_TODAY, ServerConfig.nowDay);
                }
            }
        }, ServerConfig.nowDay - 1);
        pickerDialog.show();
    }

    @Override
    protected void onMainEvent(EventBean event) {
        if (FinalConfig.EVENT_NOW_DAY_CHANGES == event.getWhat()) {
            items.set(0, String.format(contents[0], ServerConfig.nowDay));
            rv.getAdapter().notifyItemChanged(0);
        }
    }
}
