package com.qwwuyu.recite.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import com.qwwuyu.recite.R;
import com.qwwuyu.recite.bean.EventBean;
import com.qwwuyu.recite.config.FinalConfig;
import com.qwwuyu.recite.ui.LoadingView;
import com.qwwuyu.recite.utils.LogUtil;

import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * 基类Fragment
 * Created by qiwei on 2016/8/4.
 */
public abstract class BaseFragment extends Fragment {
    protected Context context;
    /** 父视图 */
    protected RelativeLayout view_Parent;
    /** 内容View */
    protected View view_content;
    /** 是否初始化 */
    protected boolean isInited = false;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view_Parent = (RelativeLayout) View.inflate(context, R.layout.f_loading, null);
        EventBus.getDefault().register(this);
    }

    /** EventBus通信方法 */
    public final void onEventMainThread(EventBean event) {
        if (FinalConfig.EVENT_TEST == event.getWhat()) {
            LogUtil.i(getClass().getName());
        }
        onMainEvent(event);
    }

    protected void onMainEvent(EventBean event) {
    }

    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view_Parent.getParent() != null) {
            ((ViewGroup) view_Parent.getParent()).removeView(view_Parent);
        }
        return view_Parent;
    }

    protected void initContent() {
        if (view_content == null) {
            view_content = View.inflate(context, getContentViewId(), null);
        }
        if (view_content.getParent() != null) {
            ((ViewGroup) view_content.getParent()).removeView(view_content);
        }
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        view_Parent.addView(view_content, 0, params);
        view_content.setVisibility(View.INVISIBLE);
        view_content.setAnimation(AnimationUtils.loadAnimation(context, R.anim.loading_fade_in));
        view_content.setVisibility(View.VISIBLE);
        ButterKnife.bind(this, view_Parent);
        LoadingView view_loading = view_Parent.findViewById(R.id.base_view_loading);
        if (view_loading != null) {
            view_loading.remove(true);
        }
        setData();
    }

    /** @return 布局xml id */
    protected abstract int getContentViewId();

    /** 控件未初始化,准备画面需要开始获取数据,获取完后在主线程调用initContent() */
    public abstract void getData();

    /** 初始化数据,设置数据 */
    protected abstract void setData();

    /** 每次显示调用 */
    protected void onResumeM(boolean isActivityResume) {
    }

    /** 每次消失调用 */
    protected void onStopM(boolean isActivityStop) {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public final void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            onStopM(false);
        } else {
            onResumeM(false);
        }
    }

    @Override
    public final void onResume() {
        super.onResume();
        onResumeM(true);
    }

    @Override
    public final void onStop() {
        super.onStop();
        onStopM(true);
    }
}
