package com.qwwuyu.recite.activity.memory;

import android.view.View;
import android.widget.ListView;

import com.qwwuyu.recite.R;
import com.qwwuyu.recite.activity.BaseActivity;
import com.qwwuyu.recite.adapter.CommAdapter;
import com.qwwuyu.recite.adapter.CommAdapter.AdapterListener;
import com.qwwuyu.recite.config.TApplication;
import com.qwwuyu.recite.db.Word;
import com.qwwuyu.recite.utils.SortUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @Description
 * @author qw
 * @date 2015-12-17
 */
public class MWordListActivity extends BaseActivity implements AdapterListener {
	private ListView listView;
	private CommAdapter<Word> adapter;
	private List<Word> wrods = new ArrayList<Word>();

	@Override
	protected int getContentViewId() {
		return R.layout.a_mword_list;
	}

	@Override
	protected void findViews() {
		listView = (ListView) findViewById(R.id.listView);
	}

	@Override
	protected void initData() {
		SortUtil.sortListByIndex(TApplication.wordList);
		int begin = TApplication.now_day * 100 - 100;
		int end = TApplication.now_day * 100;
		if (end > TApplication.wordList.size()) {
			end = TApplication.wordList.size();
		}
		wrods.addAll(TApplication.wordList.subList(begin, end));
		adapter = new CommAdapter<Word>(context, wrods, R.layout.item_word_list, this) {
			@Override
			public void onGetView(int position, View convertView, Word data) {
				setText(convertView, R.id.wordList_text, data.getText());
			}
		};
		listView.setAdapter(adapter);
	}

	@Override
	public void onItemClick(int position, View v) {

	}

	@Override
	protected void setListener() {

	}

	@Override
	protected void getData() {

	}

}