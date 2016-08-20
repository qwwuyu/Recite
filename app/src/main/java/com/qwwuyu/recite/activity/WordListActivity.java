package com.qwwuyu.recite.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.qwwuyu.recite.R;
import com.qwwuyu.recite.adapter.CommAdapter;
import com.qwwuyu.recite.adapter.CommAdapter.AdapterListener;
import com.qwwuyu.recite.config.FieldConfig;
import com.qwwuyu.recite.config.TApplication;
import com.qwwuyu.recite.db.Word;
import com.qwwuyu.recite.utils.IntentUtil;
import com.qwwuyu.recite.utils.SortUtil;

/**
 * 
 * @Description
 * @author qw
 * @date 2015-12-17
 */
public class WordListActivity extends BaseActivity implements AdapterListener {
	private ListView listView;
	private CommAdapter<Word> adapter;

	@Override
	protected int getContentViewId() {
		return R.layout.a_word_list;
	}

	@Override
	protected void findViews() {
		listView = (ListView) findViewById(R.id.listView);
	}

	@Override
	protected void initData() {
		SortUtil.sortListById(TApplication.wordList);
		adapter = new CommAdapter<Word>(context, TApplication.wordList, R.layout.item_word_list, this) {
			@Override
			public void onGetView(int position, View convertView, Word data) {
				setText(convertView, R.id.wordList_text, data.getText());
			}
		};
		listView.setAdapter(adapter);
	}

	@Override
	public void onItemClick(int position, View v) {
		Bundle bundle = new Bundle();
		bundle.putSerializable(FieldConfig.intent_bean, TApplication.wordList.get(position));
		IntentUtil.gotoActivity(context, WordDetailActivity.class, bundle);
	}

	@Override
	protected void setListener() {

	}

	@Override
	protected void getData() {

	}

}