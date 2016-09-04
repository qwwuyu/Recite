package com.qwwuyu.recite.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.qwwuyu.recite.fragment.AccentFragment;
import com.qwwuyu.recite.fragment.BaseFragment;
import com.qwwuyu.recite.fragment.MineFragment;
import com.qwwuyu.recite.fragment.WordFragment;

/**
 * 首页ViewPager适配器
 * Created by qw on 2016/8/21.
 */
public class MainViewPagerAdapter extends FragmentPagerAdapter {
    private BaseFragment[] fragments = new BaseFragment[3];

    public MainViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0 && fragments[position] == null) fragments[position] = new WordFragment();
        else if (position == 1 && fragments[position] == null) fragments[position] = new AccentFragment();
        else if (position == 2 && fragments[position] == null) fragments[position] = new MineFragment();
        return fragments[position];
    }

    @Override
    public int getCount() {
        return fragments.length;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        fragments[position] = (BaseFragment) super.instantiateItem(container, position);
        return fragments[position];
    }

    public BaseFragment getFragment(int position) {
        if (position == 0 && fragments[position] == null) fragments[position] = new WordFragment();
        else if (position == 1 && fragments[position] == null) fragments[position] = new AccentFragment();
        else if (position == 2 && fragments[position] == null) fragments[position] = new MineFragment();
        return fragments[position];
    }
}
