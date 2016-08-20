package com.qwwuyu.recite.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.qwwuyu.recite.R;
import com.qwwuyu.recite.config.BroadcastFilters;
import com.qwwuyu.recite.utils.SetActionBar;
import com.qwwuyu.recite.utils.SystemBarTintManager;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Activity基类
 * Created by qiwei on 2016/6/26.
 */
public abstract class BaseActivity extends Activity {
    protected Context context = BaseActivity.this;
    /** 沉浸式工具 */
    protected SystemBarTintManager tintManager;
    /** 统一广播接收器 */
    protected BroadcastReceiver receiver;
    /** 广播过滤器 */
    protected IntentFilter filter = new IntentFilter();
    /** 获取当前类 */
    private Class<? extends Activity> c = this.getClass();
    /** 当前Activity的类名,用于判断当前是哪个Activity在最前面 */
    public static String clazzName;
    /** 处理键盘外点击处理,当有2个EditText的时候使用,将所有EditText引用储存 */
    protected List<EditText> edits = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());
        tintManager = SetActionBar.setTranslucentStatus(this, R.color.comm_color);
        findViews();
        initData();
        setListener();
        getData();
        if (useEventBus()) {
            EventBus.getDefault().register(this);
        }
        registerReceiver();
    }

    /** 是否需要注册EventBus */
    protected boolean useEventBus() {
        return false;
    }

    /** 获取布局id */
    protected abstract int getContentViewId();

    /** 查询View对象 */
    protected abstract void findViews();

    /** 初始化数据,设置数据 */
    protected abstract void initData();

    /** 设置监听 */
    protected abstract void setListener();

    /** 获取网络数据 */
    protected abstract void getData();

    @Override
    protected final void onResume() {
        super.onResume();
        clazzName = c.getName();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (useEventBus()) {
            EventBus.getDefault().unregister(this);
        }
        unregisterReceiver(receiver);
    }

    @Override
    public void finish() {
        finishNoAnim();
        overridePendingTransition(R.anim.exit_enter, R.anim.exit_exit);
    }

    public void finishNoAnim() {
//        ProcessDialogUtil.dismissDialog();
        closeKeyboard();
        super.finish();
    }

    public void closeKeyboard() {
        try {
            if (getCurrentFocus() != null && getCurrentFocus().getApplicationWindowToken() != null) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getApplicationWindowToken(), 0);
            }
        } catch (Exception ignored) {
        }
    }

    /** 注册广播 */
    protected void registerReceiver() {
        setFilterActions();
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                BaseActivity.this.onReceive(context, intent);
            }
        };
        registerReceiver(receiver, filter);
    }

    /** 广播监听回调 父类集中处理共同的请求 */
    protected void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(BroadcastFilters.ACTION_ACT_FINISH)) {//关闭所有基类
            finishNoAnim();
        } else if (intent.getAction().equals(BroadcastFilters.ACTION_LOGIN_CONFLICT)) {//登录冲突
            if (clazzName.equals(c.getName())) {//当前显示界面
//                DialogUtil.showOffLineDg(this);
            } else {//后台界面
                finishNoAnim();
            }
        }
    }

    protected void setFilterActions() {
        filter.addAction(BroadcastFilters.ACTION_ACT_FINISH);
        filter.addAction(BroadcastFilters.ACTION_LOGIN_CONFLICT);
    }

    /** 监听点击事件 处理键盘关闭 */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            boolean isHind = true;
            for (int i = 0; i < edits.size(); i++) {
                isHind = isHind & isShouldHideInput(edits.get(i), ev);
            }
            View v = getCurrentFocus();
            if (isHind && isShouldHideInput(v, ev)) {
                closeKeyboard();
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    private boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            Rect rect = new Rect();
            v.getGlobalVisibleRect(rect);
            return !rect.contains((int) event.getRawX(), (int) event.getRawY());
        }
        return false;
    }
}
