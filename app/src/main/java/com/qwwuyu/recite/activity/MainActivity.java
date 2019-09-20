package com.qwwuyu.recite.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.qwwuyu.recite.R;
import com.qwwuyu.recite.activity.word.CollectWordsActivity;
import com.qwwuyu.recite.activity.word.IndexWordsActivity;
import com.qwwuyu.recite.activity.word.OrderWordsActivity;
import com.qwwuyu.recite.activity.word.SearchWordActivity;
import com.qwwuyu.recite.bean.Word;
import com.qwwuyu.recite.config.TApplication;
import com.qwwuyu.recite.ui.NavigationView;
import com.qwwuyu.recite.ui.adapter.MainViewPagerAdapter;
import com.qwwuyu.recite.utils.IntentUtil;
import com.qwwuyu.recite.utils.SortUtil;

import butterknife.BindView;
import me.imid.swipebacklayout.lib.helper.SwipeBackHelper;

/**
 * 主界面
 */
public class MainActivity extends BaseActivity implements Toolbar.OnMenuItemClickListener, AdapterView.OnItemClickListener {
    /** 导航栏 */
    @BindView(R.id.main_navigation)
    NavigationView navigation;
    /** 内容ViewPager */
    @BindView(R.id.main_viewPager)
    ViewPager viewPager;

    @Override
    protected boolean enableSlider() {
        return true;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.a_main;
    }

    @Override
    protected void init() {
        inflateMenu(R.menu.menu_main, this);
        navigation.setOnItemClickListener(this);
        final MainViewPagerAdapter adapter = new MainViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(Integer.MAX_VALUE);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                navigation.setOffset(position, positionOffset);
                if (positionOffset == 0) {
                    adapter.getFragment(position).getData();
                }
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        SwipeBackHelper.getCurrentPage(this)
                .setSwipeBackEnable(false)
                .setDisallowInterceptTouchEvent(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (TApplication.indexWords.size() == 0 && TApplication.orderWords != null) {
            TApplication.indexWords.addAll(TApplication.orderWords);
            SortUtil.sortListByIndex(TApplication.indexWords);
            for (Word word : TApplication.orderWords) {
                if (word.getCollect()) {
                    TApplication.collectWords.add(word);
                }
            }
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        Bundle bundle = new Bundle();
        switch (item.getItemId()) {
            case R.id.menu_search:
                startActivity(new Intent(context, SearchWordActivity.class));
                overridePendingTransition(R.anim.search_in, R.anim.anim_no);
                break;
            case R.id.menu_collect:
                IntentUtil.gotoActivity(context, CollectWordsActivity.class, bundle);
                break;
            case R.id.menu_orderWords:
                IntentUtil.gotoActivity(context, OrderWordsActivity.class, bundle);
                break;
            case R.id.menu_indexWords:
                IntentUtil.gotoActivity(context, IndexWordsActivity.class, bundle);
                break;
        }
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        viewPager.setCurrentItem(position, false);
    }
}