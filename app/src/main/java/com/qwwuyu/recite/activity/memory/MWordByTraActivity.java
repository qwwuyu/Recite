package com.qwwuyu.recite.activity.memory;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.qwwuyu.recite.R;
import com.qwwuyu.recite.activity.BaseActivity;
import com.qwwuyu.recite.config.Config;
import com.qwwuyu.recite.config.TApplication;
import com.qwwuyu.recite.db.Word;
import com.qwwuyu.recite.utils.LogUtil;
import com.qwwuyu.recite.utils.PlayUtil;
import com.qwwuyu.recite.utils.SortUtil;
import com.qwwuyu.recite.utils.CommUtil;

/**
 * 通过单词-翻译
 * 
 * @Description
 * @author qw
 * @date 2015-12-21
 */
public class MWordByTraActivity extends BaseActivity {
	/** 单词 */
	private TextView txt_text;
	/** 翻译 */
	private TextView txt_content;
	/** 发音 */
	private TextView txt_sound;
	/** 显示翻译 */
	private TextView txt_show;
	/** 进度 */
	private TextView txt_progress;
	/** 上一个 */
	private TextView txt_up;
	/** 下一个 */
	private TextView txt_down;
	/** 数据 */
	private List<Word> words = new ArrayList<Word>();
	/** 显示第几个 */
	private int position = 1;
	/** 当前显示的Word */
	private Word word;

	@Override
	protected int getContentViewId() {
		return R.layout.a_mwordbytra;
	}

	@Override
	protected void findViews() {
		txt_text = (TextView) findViewById(R.id.mwordbytra_txt_text);
		txt_content = (TextView) findViewById(R.id.mwordbytra_txt_content);
		txt_sound = (TextView) findViewById(R.id.mwordbytra_txt_sound);
		txt_show = (TextView) findViewById(R.id.mwordbytra_txt_show);
		txt_progress = (TextView) findViewById(R.id.mwordbytra_txt_progress);
		txt_up = (TextView) findViewById(R.id.mwordbytra_txt_up);
		txt_down = (TextView) findViewById(R.id.mwordbytra_txt_down);
	}

	@Override
	protected void initData() {
		SortUtil.sortListByIndex(TApplication.wordList);
		int begin = TApplication.now_day * 100 - 100;
		int end = TApplication.now_day * 100;
		if (end > TApplication.wordList.size()) {
			end = TApplication.wordList.size();
		}
		words.addAll(TApplication.wordList.subList(begin, end));
		Collections.shuffle(words);
		initWord();
	}

	@Override
	protected void setListener() {
		txt_show.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				txt_text.setVisibility(View.VISIBLE);
			}
		});
		txt_up.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				position = position - 1;
				position = Math.max(1, position);
				initWord();
			}
		});
		txt_down.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				position = position + 1;
				position = Math.min(words.size(), position);
				initWord();
			}
		});
		txt_sound.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (word != null) {
					File file = new File(Config.baseSoundPath + File.separator + word.getText() + ".mp3");
					LogUtil.i(file.getPath());
					if (file.exists()) {
						PlayUtil.getPlayUtil().play(file.getPath());
					}
				}
			}
		});
	}

	private void initWord() {
		word = words.get(position - 1);
		txt_progress.setText(position + "/" + words.size());
		txt_text.setText(word.getText());
		txt_content.setText(CommUtil.changeContent(word.getContent()));
		txt_text.setVisibility(View.INVISIBLE);
	}

	@Override
	protected void getData() {

	}
}