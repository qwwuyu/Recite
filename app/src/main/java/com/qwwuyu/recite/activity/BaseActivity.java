package com.qwwuyu.recite.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.MenuRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.qwwuyu.recite.R;
import com.qwwuyu.recite.bean.EventBean;
import com.qwwuyu.recite.config.FinalConfig;
import com.qwwuyu.recite.utils.LogUtil;
import com.qwwuyu.recite.utils.SystemBarSetUtil;
import com.qwwuyu.recite.utils.SystemBarTintManager;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * 所有Activity的基类
 * Created by qiwei on 2016/8/4.
 */
public abstract class BaseActivity extends AppCompatActivity {
    protected Context context = BaseActivity.this;
    /** 处理键盘外点击处理,当有2个EditText的时候使用 */
    protected List<EditText> edits = new ArrayList<>();
    /** 获取当前类字节码 */
    private Class<? extends Activity> c = this.getClass();
    /** 最前面界面类的名称 */
    private static String clazzName;
    /** 判断界面是否finish */
    public boolean isFinish = false;
    /** 沉浸式 */
    protected SystemBarTintManager tintManager;
    /** ToolBar */
    protected Toolbar toolBar;

    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BaseActivity.clazzName = c.getName();
        setContentView(getContentViewId());
        ButterKnife.bind(this);
        toolBar = (Toolbar) findViewById(R.id.view_toolBar);
        tintManager = SystemBarSetUtil.setTranslucentStatus(this, R.color.colorPrimaryDark);
        init();
        EventBus.getDefault().register(this);
    }

    protected final void setBackBtn() {
        toolBar.setNavigationIcon(R.drawable.img_back);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    protected final void inflateMenu(@MenuRes int resId, final Toolbar.OnMenuItemClickListener listener) {
        toolBar.inflateMenu(resId);
        toolBar.setOnMenuItemClickListener(listener);
        final ArrayList<View> outViews = new ArrayList<>();
        toolBar.findViewsWithText(outViews, getString(R.string.ActionButtonOverflow), View.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
        if (outViews.size() > 0 && outViews.get(0) instanceof AppCompatImageView) {
            outViews.get(0).setOnTouchListener(null);
        }
        try {
            Method m = toolBar.getMenu().getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
            m.setAccessible(true);
            m.invoke(toolBar.getMenu(), true);
        } catch (Exception ignored) {
        }
        //MenuItemCompat.getActionProvider(toolBar.getMenu().findItem(R.id.menu_defProvider)
    }

    @Override
    protected void onResume() {
        super.onResume();
        BaseActivity.clazzName = c.getName();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (toolBar != null && toolBar.getMenu() != null) {
            toolBar.getMenu().close();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        LogUtil.i(c.getSimpleName() + "==>onDestroy");
    }

    /** EventBus通信方法 */
    public final void onEventMainThread(EventBean event) {
        if (FinalConfig.EVENT_ACT_FINISH == event.getWhat()) {
            finishNoAnim();
        } else if (FinalConfig.EVENT_TEST == event.getWhat()) {
            LogUtil.i(c.getName());
        }
        onMainEvent(event);
    }

    protected void onMainEvent(EventBean event) {
    }

    /** @return 布局xml id */
    protected abstract int getContentViewId();

    /** 初始化数据,设置数据 */
    protected abstract void init();

    @Override
    public void finish() {
        finishNoAnim();
        //TODO 添加动画
        overridePendingTransition(R.anim.exit_enter, R.anim.exit_exit);
    }

    public void finishNoAnim() {
        isFinish = true;
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
        if (v != null && v.isShown() && (v instanceof EditText)) {
            Rect rect = new Rect();
            v.getGlobalVisibleRect(rect);
            return !rect.contains((int) event.getRawX(), (int) event.getRawY());
        }
        return false;
    }
}
