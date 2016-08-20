package com.qwwuyu.recite.activity.memory;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qwwuyu.recite.R;
import com.qwwuyu.recite.activity.BaseActivity;
import com.qwwuyu.recite.config.TApplication;
import com.qwwuyu.recite.db.Word;
import com.qwwuyu.recite.utils.SortUtil;
import com.qwwuyu.recite.view.WordPageView;

/**
 * 翻页查询单词
 * 
 * @Description
 * @author qw
 * @date 2015-12-21
 */
public class MPagerWordActivity extends BaseActivity {
	private ViewPager viewPager;
	private TextView txt_up;
	private TextView txt_down;
	private TextView txt_progress;
	/** 所有页面 */
	private List<WordPageView> list = new ArrayList<WordPageView>();
	private List<Word> wrods = new ArrayList<Word>();
	private ViewPagerAdapter adapter;

	@Override
	protected int getContentViewId() {
		return R.layout.a_mpager_word;
	}

	@Override
	protected void findViews() {
		viewPager = (ViewPager) findViewById(R.id.viewPager);
		txt_up = (TextView) findViewById(R.id.mpagerWord_txt_up);
		txt_down = (TextView) findViewById(R.id.mpagerWord_txt_down);
		txt_progress = (TextView) findViewById(R.id.mpagerWord_txt_progress);
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
		for (int i = 0; i < 5; i++) {
			WordPageView view = new WordPageView(context);
			list.add(view);
		}
		adapter = new ViewPagerAdapter();
		viewPager.setAdapter(adapter);
		txt_progress.setText(1 + "/" + wrods.size());
	}

	@Override
	protected void setListener() {
		txt_up.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
			}
		});
		txt_down.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
			}
		});
		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				txt_progress.setText(position + 1 + "/" + wrods.size());
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
	}

	@Override
	protected void getData() {

	}

	private class ViewPagerAdapter extends PagerAdapter {
		@Override
		public int getCount() {
			return wrods.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(list.get(position % list.size()));
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			ViewPager viewPage = (ViewPager) container;
			WordPageView wordPageView = list.get(position % list.size());
			wordPageView.setWord(wrods.get(position));
			viewPage.addView(wordPageView);
			return wordPageView;
		}
	}
}