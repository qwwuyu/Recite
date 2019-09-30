package swipeback.helper;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.ViewGroup;

import swipeback.SwipeBackLayout;

/**
 * Created by Mr.Jude on 2015/8/3.
 * 每个滑动页面的管理
 */
public class SwipeBackPage {
    /** 是否启用滑动 */
    private boolean mEnable = true;
    /** 是否启用联动 */
    private boolean mRelativeEnable = false;
    /** 设置联动偏移量 */
    private float offset = 0.25f;

    Activity activity;
    private SwipeBackLayout mSwipeBackLayout;
    private RelateSlider slider;

    SwipeBackPage(Activity activity) {
        this.activity = activity;
    }

    //页面的回调用于配置滑动效果
    void onCreate() {
        activity.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        activity.getWindow().getDecorView().setBackgroundColor(Color.TRANSPARENT);
        mSwipeBackLayout = new SwipeBackLayout(activity);
        mSwipeBackLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        slider = new RelateSlider(this);
    }

    void onPostCreate() {
        handleLayout();
    }

    private void handleLayout() {
        if (mEnable || mRelativeEnable) {
            mSwipeBackLayout.attachToActivity(activity);
        } else {
            mSwipeBackLayout.removeFromActivity(activity);
        }
    }

    /** 是否启用滑动 */
    public SwipeBackPage setSwipeBackEnable(boolean enable) {
        mEnable = enable;
        mSwipeBackLayout.setEnable(enable);
        handleLayout();
        return this;
    }

    /** 是否启用联动 */
    public SwipeBackPage setSwipeRelateEnable(boolean enable) {
        mRelativeEnable = enable;
        if (enable) {
            mSwipeBackLayout.addSwipeListener(slider);
        } else {
            mSwipeBackLayout.removeSwipeListener(slider);
        }
        handleLayout();
        return this;
    }

    /** 设置联动偏移百分比 */
    public SwipeBackPage setSwipeRelateOffset(float offset) {
        this.offset = Math.min(Math.max(0, offset), 1);
        return this;
    }

    /** 设置触发滑动范围 */
    public SwipeBackPage setSwipeEdge(int swipeEdge) {
        mSwipeBackLayout.setEdgeSize(swipeEdge);
        return this;
    }

    /** 设置触发滑动范围 */
    public SwipeBackPage setSwipeEdgePercent(float swipeEdgePercent) {
        mSwipeBackLayout.setEdgeSizePercent(swipeEdgePercent);
        return this;
    }

    /** 对横向滑动手势的敏感程度。0为迟钝 1为敏感 */
    public SwipeBackPage setSwipeSensitivity(float sensitivity) {
        mSwipeBackLayout.setSensitivity(activity, sensitivity);
        return this;
    }

    /** 底层阴影颜色 */
    public SwipeBackPage setScrimColor(int color) {
        mSwipeBackLayout.setScrimColor(color);
        return this;
    }

    /** 触发关闭Activity百分比 */
    public SwipeBackPage setClosePercent(float percent) {
        mSwipeBackLayout.setScrollThreshold(percent);
        return this;
    }

    /** 设置是否取消事件获取 */
    public SwipeBackPage setDisallowInterceptTouchEvent(boolean disallowIntercept) {
        mSwipeBackLayout.setDisallowInterceptTouchEvent(disallowIntercept);
        return this;
    }

    void setPercentOffset(float percent) {
        mSwipeBackLayout.setPercentOffset(percent, offset);
    }

    void scrollToFinishActivity() {
        mSwipeBackLayout.scrollToFinishActivity();
    }
}
