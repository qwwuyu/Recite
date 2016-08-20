package com.qwwuyu.recite.view;

import java.io.File;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qwwuyu.recite.R;
import com.qwwuyu.recite.config.Config;
import com.qwwuyu.recite.db.Word;
import com.qwwuyu.recite.utils.LogUtil;
import com.qwwuyu.recite.utils.PlayUtil;
import com.qwwuyu.recite.utils.CommUtil;

/**
 * 单词页
 * 
 * @Description
 * @author qw
 * @date 2015-12-21
 */
public class WordPageView extends RelativeLayout {
	private TextView txt_text;
	private TextView txt_accents;
	private TextView txt_content;
	private Word word;

	public WordPageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public WordPageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public WordPageView(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context) {
		View view = View.inflate(context, R.layout.view_word_page, null);
		txt_text = (TextView) view.findViewById(R.id.wordPage_txt_text);
		txt_accents = (TextView) view.findViewById(R.id.wordPage_txt_accents);
		txt_content = (TextView) view.findViewById(R.id.wordPage_txt_content);
		view.findViewById(R.id.wordPage_txt_sound).setOnClickListener(new OnClickListener() {
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
		addView(view);
	}

	public void setWord(Word word) {
		this.word = word;
		txt_text.setText(word.getText());
		txt_content.setText(CommUtil.changeContent(word.getContent()));
		txt_accents.setText(CommUtil.changeAccents(word.getAccents()));
	}
}
