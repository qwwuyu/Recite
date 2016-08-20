package com.qwwuyu.recite.activity;

import java.io.File;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.qwwuyu.recite.R;
import com.qwwuyu.recite.config.Config;
import com.qwwuyu.recite.config.FieldConfig;
import com.qwwuyu.recite.db.Word;
import com.qwwuyu.recite.utils.LogUtil;
import com.qwwuyu.recite.utils.PlayUtil;
import com.qwwuyu.recite.utils.CommUtil;

/**
 * 单词详情界面
 * 
 * @Description
 * @author qw
 * @date 2016-1-8
 */
public class WordDetailActivity extends BaseActivity {
	private TextView txt_text;
	private TextView txt_accents;
	private TextView txt_content;
	private TextView txt_sound;
	private Word word;

	@Override
	protected int getContentViewId() {
		return R.layout.a_wrod_detail;
	}

	@Override
	protected void findViews() {
		txt_text = (TextView) findViewById(R.id.wrodDetail_txt_text);
		txt_accents = (TextView) findViewById(R.id.wrodDetail_txt_accents);
		txt_content = (TextView) findViewById(R.id.wrodDetail_txt_content);
		txt_sound = (TextView) findViewById(R.id.wrodDetail_txt_sound);
	}

	@Override
	protected void initData() {
		word = (Word) getIntent().getExtras().getSerializable(FieldConfig.intent_bean);
		txt_text.setText(word.getText());
		txt_content.setText(CommUtil.changeContent(word.getContent()));
		txt_accents.setText(CommUtil.changeAccents(word.getAccents()));
	}

	@Override
	protected void setListener() {
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

	@Override
	protected void getData() {

	}
}