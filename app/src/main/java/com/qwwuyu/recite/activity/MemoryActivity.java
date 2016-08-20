package com.qwwuyu.recite.activity;

import java.util.Arrays;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.qwwuyu.recite.R;
import com.qwwuyu.recite.activity.memory.MPagerWordActivity;
import com.qwwuyu.recite.activity.memory.MTraByWordActivity;
import com.qwwuyu.recite.activity.memory.MWordByTraActivity;
import com.qwwuyu.recite.activity.memory.MWordListActivity;
import com.qwwuyu.recite.adapter.CommAdapter;
import com.qwwuyu.recite.adapter.CommAdapter.AdapterListener;
import com.qwwuyu.recite.config.FieldConfig;
import com.qwwuyu.recite.config.TApplication;
import com.qwwuyu.recite.utils.IntentUtil;

/**
 * 记忆单词界面
 * 
 * @Description
 * @author qw
 * @date 2015-12-19
 */
public class MemoryActivity extends BaseActivity implements AdapterListener {
	private TextView memory_txt_day;
	private TextView memory_txt_select;
	private ListView listView;
	private String[] items = new String[] { "该天需要记忆的所有单词", "分页浏览单词", "通过单词回忆翻译", "通过翻译回忆单词", "回顾前2天单词" };
	private CommAdapter<String> adapter;

	@Override
	protected int getContentViewId() {
		return R.layout.a_memory;
	}

	@Override
	protected void findViews() {
		memory_txt_day = (TextView) findViewById(R.id.memory_txt_day);
		memory_txt_select = (TextView) findViewById(R.id.memory_txt_select);
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
	protected void setListener() {
		memory_txt_select.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				IntentUtil.gotoActivityForResult(context, SelectDayActivity.class, FieldConfig.MEMORYACTIVITY_SELECT_DAY);
				overridePendingTransition(R.anim.activity_open_enter, 0);
			}
		});
	}

	@Override
	public void onItemClick(int position, View v) {
		switch (position) {
		case 0:
			IntentUtil.gotoActivity(context, MWordListActivity.class);
			break;
		case 1:
			IntentUtil.gotoActivity(context, MPagerWordActivity.class);
			break;
		case 2:
			IntentUtil.gotoActivity(context, MTraByWordActivity.class);
			break;
		case 3:
			IntentUtil.gotoActivity(context, MWordByTraActivity.class);
			break;
		case 4:
			IntentUtil.gotoActivity(context, SettingActivity.class);
			break;
		default:
			break;
		}
	}

	@Override
	protected void getData() {

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == FieldConfig.MEMORYACTIVITY_SELECT_DAY && resultCode == RESULT_OK) {
			TApplication.now_day = data.getIntExtra(FieldConfig.intent_id, 0) + 1;
			memory_txt_day.setText(String.valueOf(TApplication.now_day));
		}
	}
}