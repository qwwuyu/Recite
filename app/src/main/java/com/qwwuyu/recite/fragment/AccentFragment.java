package com.qwwuyu.recite.fragment;

import com.qwwuyu.recite.R;

/**
 * 音标
 * Created by qw on 2016/8/21.
 */
public class AccentFragment extends BaseFragment {
    @Override
    protected int getContentViewId() {
        return R.layout.f_accent;
    }

    @Override
    public void getData() {
        if (isInited) {
            return;
        }
        isInited = true;
        initContent();
    }

    @Override
    protected void setData() {

    }
}
