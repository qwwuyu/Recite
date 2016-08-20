package com.qwwuyu.recite.activity;

import android.view.KeyEvent;
import android.view.View;
import android.widget.ListView;

import com.qwwuyu.recite.R;
import com.qwwuyu.recite.adapter.CommAdapter;
import com.qwwuyu.recite.adapter.CommAdapter.AdapterListener;
import com.qwwuyu.recite.utils.IntentUtil;
import com.qwwuyu.recite.utils.ToastUtil;

import java.util.Arrays;

/**
 * 主界面
 *
 * @author qw
 * @Description
 * @date 2015-12-17
 */
public class MainActivity extends BaseActivity implements AdapterListener {
    /** 上次按退出的时间 */
    private long lastExitTime = 0;
    private String[] items = new String[]{"所有单词列表", "乱序列表", "单词搜索", "记忆单词", "设置"};
    private ListView listView;
    private CommAdapter<String> adapter;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected void findViews() {
        listView = (ListView) findViewById(R.id.listView);
    }

    @Override
    protected void initData() {
        adapter = new CommAdapter<String>(context, Arrays.asList(items), R.layout.item_main, this) {

            @Override
            public void onGetView(int position, View convertView, String data) {
                setText(convertView, R.id.main_text, data);
            }
        };
        listView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(int position, View v) {
        switch (position) {
            case 0:
                IntentUtil.gotoActivity(context, WordListActivity.class);
                break;
            case 1:
                IntentUtil.gotoActivity(context, OutWordListActivity.class);
                break;
            case 2:
                IntentUtil.gotoActivity(context, SearchActivity.class);
                break;
            case 3:
                IntentUtil.gotoActivity(context, MemoryActivity.class);
                break;
            case 4:
                IntentUtil.gotoActivity(context, SettingActivity.class);
                break;
            default:
                break;
        }
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void getData() {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - lastExitTime) > 2000) {
                ToastUtil.showToast(this, R.string.toast_double_click_exit);
                lastExitTime = System.currentTimeMillis();
                return true;
            } else {
                finishNoAnim();
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}