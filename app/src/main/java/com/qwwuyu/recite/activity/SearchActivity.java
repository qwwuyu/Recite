package com.qwwuyu.recite.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.qwwuyu.recite.R;
import com.qwwuyu.recite.config.FieldConfig;
import com.qwwuyu.recite.config.TApplication;
import com.qwwuyu.recite.db.Word;
import com.qwwuyu.recite.utils.IntentUtil;

/**
 * 搜索界面
 * 
 * @Description
 * @author qw
 * @date 2015-12-19
 */
public class SearchActivity extends BaseActivity {
	private TextView txt_go;
	private EditText edit_content;
	private TextView wrod_text;
	private Word word;

	@Override
	protected int getContentViewId() {
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
		return R.layout.a_search;
	}

	@Override
	protected void findViews() {
		txt_go = (TextView) findViewById(R.id.search_txt_go);
		edit_content = (EditText) findViewById(R.id.search_edit_content);
		wrod_text = (TextView) findViewById(R.id.search_wrod_text);
	}

	@Override
	protected void initData() {
	}

	@Override
	protected void setListener() {
		txt_go.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onSearch();
			}
		});
		edit_content.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEND || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
					onSearch();
					return true;
				}
				return false;
			}
		});
		findViewById(R.id.search_ll_word).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String text = wrod_text.getText().toString();
				if (!"".equals(text) && !"没有您想要找到单词".equals(text) && word != null) {
					Bundle bundle = new Bundle();
					bundle.putSerializable(FieldConfig.intent_bean, word);
					IntentUtil.gotoActivity(context, WordDetailActivity.class, bundle);
				}
			}
		});
	}

	@Override
	protected void getData() {

	}

	private void onSearch() {
		String search_content = edit_content.getText().toString();
		for (int i = 0; i < TApplication.wordList.size(); i++) {
			word = TApplication.wordList.get(i);
			if (word.getText().equalsIgnoreCase(search_content)) {
				wrod_text.setText(word.getText());
			}
		}
		if (!wrod_text.getText().toString().equalsIgnoreCase(search_content)) {
			wrod_text.setText("没有您想要找到单词");
		}
	}
}