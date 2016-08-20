package com.qwwuyu.recite.activity;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

import com.bigkoo.pickerview.adapter.ArrayWheelAdapter;
import com.bigkoo.pickerview.lib.WheelView;
import com.qwwuyu.recite.R;
import com.qwwuyu.recite.config.FieldConfig;
import com.qwwuyu.recite.config.TApplication;

import java.util.ArrayList;

/**
 * 选择日期
 */
public class SelectDayActivity extends BaseActivity {
    private WheelView wheelView;
    private ArrayList<String> list = new ArrayList<>();

    @Override
    protected int getContentViewId() {
        return R.layout.a_select_day;
    }

    @Override
    protected void findViews() {
        wheelView = (WheelView) findViewById(R.id.selectDay_wheel_day);
    }

    @Override
    protected void initData() {
        int dayNum = TApplication.wordList.size() / TApplication.DAY_MEMORY + 2;
        for (int i = 1; i < dayNum; i++) {
            list.add("第" + i + "天");
        }
        //设置数据
        wheelView.setAdapter(new ArrayWheelAdapter<>(list));
        //设置字体
        wheelView.setTextSize(20);
        //设置是否循环滚动
        wheelView.setCyclic(true);
        wheelView.setCurrentItem(0);
    }

    @Override
    protected void setListener() {
        findViewById(R.id.city_cancel).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.city_ok).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK, new Intent().putExtra(FieldConfig.intent_id, wheelView.getCurrentItem()));
                finish();
            }
        });
    }

    @Override
    protected void getData() {
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.activity_close_exit);
    }
}