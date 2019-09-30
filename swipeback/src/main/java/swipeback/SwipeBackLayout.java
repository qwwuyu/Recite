package swipeback;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

public class SwipeBackLayout extends FrameLayout {
    /**
     * Minimum velocity that will be detected as a fling
     */
    private static final int MIN_FLING_VELOCITY = 400; // dips per second

    private static final int DEFAULT_SCRIM_COLOR = 0x99000000;

    private static final int FULL_ALPHA = 255;
    /**
     * A view is currently being dragged. The position is currently changing as a result of user input or simulated user input.
     */
    public static final int STATE_DRAGGING = ViewDragHelper.STATE_DRAGGING;
    /**
     * A view is currently settling into place as a result of a fling or predefined non-interactive motion.
     */
    public static final int STATE_SETTLING = ViewDragHelper.STATE_SETTLING;
    /**
     * Default threshold of scroll
     */
    private static final float DEFAULT_SCROLL_THRESHOLD = 0.3f;

    private static final int OVERSCROLL_DISTANCE = 10;

    /** Threshold of scroll, we will close the activity, when scrollPercent over this value; */
    private float mScrollThreshold = DEFAULT_SCROLL_THRESHOLD;

    private ViewDragHelper mDragHelper;

    private Activity mActivity;

    private View mContentView;
    /** 启用 */
    private boolean mEnable = true;
    /** 拦截事件 */
    private boolean mDisallowIntercept = false;
    /** 触发滑动范围 */
    private int mTrackingEdge;
    /** 滑动监听 */
    private List<SwipeListener> mListeners;

    /** 当前滑动百分比 */
    private float mScrollPercent;
    /** 当前滑动距离 */
    private int mContentLeft;
    /** shadow */
    Drawable mShadowLeft;
    /** 1 - mScrollPercent */
    private float mScrimOpacity;

    private int mScrimColor = DEFAULT_SCRIM_COLOR;

    private boolean mInLayout;

    private Rect mTmpRect = new Rect();


    public SwipeBackLayout(Context context) {
        this(context, null);
    }

    public SwipeBackLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeBackLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
        mDragHelper = ViewDragHelper.create(this, new ViewDragCallback());

        setShadow(R.drawable.shadow_left);

        final float density = getResources().getDisplayMetrics().density;
        final float minVel = MIN_FLING_VELOCITY * density;
        setEdgeSize(getResources().getDisplayMetrics().widthPixels);
        mDragHelper.setMinVelocity(minVel);
        mDragHelper.setMaxVelocity(minVel * 2f);
        mDragHelper.setSensitivity(context, 0.3f);
        mDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT);
    }

    public void setEnable(boolean enable) {
        mEnable = enable;
    }

    public void setDisallowInterceptTouchEvent(boolean disallowIntercept) {
        mDisallowIntercept = disallowIntercept;
    }

    public void setSensitivity(Context context, float sensitivity) {
        mDragHelper.setSensitivity(context, sensitivity);
    }

    public void setEdgeSize(int size) {
        mTrackingEdge = size;
        mDragHelper.setEdgeSize(mTrackingEdge);
    }

    public void setEdgeSizePercent(float size) {
        mTrackingEdge = (int) (getResources().getDisplayMetrics().widthPixels * size);
        mDragHelper.setEdgeSize(mTrackingEdge);
    }

    public void addSwipeListener(SwipeListener listener) {
        if (mListeners == null) mListeners = new ArrayList<>();
        if (!mListeners.contains(listener)) mListeners.add(listener);
    }

    public void removeSwipeListener(SwipeListener listener) {
        if (mListeners == null) return;
        mListeners.remove(listener);
    }

    public void setScrollThreshold(float threshold) {
        if (threshold >= 1.0f || threshold <= 0) {
            throw new IllegalArgumentException("Threshold value should be between 0 and 1.0");
        }
        mScrollThreshold = threshold;
    }

    public void setScrimColor(int color) {
        mScrimColor = color;
        invalidate();
    }

    public void setShadow(Drawable shadow) {
        mShadowLeft = shadow;
        invalidate();
    }

    public void setShadow(int resId) {
        setShadow(getResources().getDrawable(resId));
    }

    /** Set up contentView which will be moved by user gesture */
    private void setContentView(View view) {
        mContentView = view;
    }

    /** Scroll out contentView and finish the activity */
    public void scrollToFinishActivity() {
        final int childWidth = mContentView.getWidth();
        int left, top = 0;
        left = childWidth + mShadowLeft.getIntrinsicWidth() + OVERSCROLL_DISTANCE;
        mDragHelper.smoothSlideViewTo(mContentView, left, top);
        invalidate();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (!mEnable || mDisallowIntercept) {
            return false;
        }
        try {
            return mDragHelper.shouldInterceptTouchEvent(event);
        } catch (Exception e) {
            return false;
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!mEnable) {
            return false;
        }
        try {
            mDragHelper.processTouchEvent(event);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        mInLayout = true;
        if (mContentView != null) {
            mContentView.layout(mContentLeft, 0,
                    mContentLeft + mContentView.getMeasuredWidth(),
                    mContentView.getMeasuredHeight());
        }
        mInLayout = false;
    }

    @Override
    public void requestLayout() {
        if (!mInLayout) {
            super.requestLayout();
        }
    }

    @Override
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        final boolean drawContent = child == mContentView;

        boolean ret = super.drawChild(canvas, child, drawingTime);
        if (mScrimOpacity > 0 && drawContent && mDragHelper.getViewDragState() != ViewDragHelper.STATE_IDLE) {
            drawShadow(canvas, child);
            //drawScrim(canvas, child);
        }
        return ret;
    }

    private void drawShadow(Canvas canvas, View child) {
        final Rect childRect = mTmpRect;
        child.getHitRect(childRect);

        mShadowLeft.setBounds(childRect.left - mShadowLeft.getIntrinsicWidth(), childRect.top,
                childRect.left, childRect.bottom);
        mShadowLeft.setAlpha((int) (mScrimOpacity * FULL_ALPHA));
        mShadowLeft.draw(canvas);
    }

    private void drawScrim(Canvas canvas, View child) {
        final int baseAlpha = (mScrimColor & 0xff000000) >>> 24;
        final int alpha = (int) (baseAlpha * mScrimOpacity);
        final int color = alpha << 24 | (mScrimColor & 0xffffff);
        canvas.clipRect(0, 0, child.getLeft(), getHeight());
        canvas.drawColor(color);
    }

    @Override
    public void computeScroll() {
        mScrimOpacity = 1 - mScrollPercent;
        if (mDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    private class ViewDragCallback extends ViewDragHelper.Callback {
        /** 滑动有效 */
        private boolean mIsScrollOverValid;

        /** 尝试开始滑动 */
        @Override
        public boolean tryCaptureView(View view, int pointerId) {
            SwipeBackUtils.log("tryCaptureView");
            boolean ret = mDragHelper.isEdgeTouched(ViewDragHelper.EDGE_LEFT, pointerId);
            if (ret) {
                if (mListeners != null && !mListeners.isEmpty()) {
                    for (SwipeListener listener : mListeners) {
                        listener.onEdgeTouch();
                    }
                }
                mIsScrollOverValid = true;
            }
            return ret;
        }

        /** 横向触发范围 */
        @Override
        public int getViewHorizontalDragRange(View child) {
            return mTrackingEdge;
        }

        /** 控制水平移动范围 */
        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            return Math.min(child.getWidth(), Math.max(left, 0));
        }

        /** 控件位置变化 */
        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            mScrollPercent = Math.abs((float) left / (mContentView.getWidth()));
            mContentLeft = left;
            invalidate();
            if (mScrollPercent < mScrollThreshold && !mIsScrollOverValid) {
                mIsScrollOverValid = true;
            }

            if (mListeners != null && !mListeners.isEmpty()) {
                for (SwipeListener listener : mListeners) {
                    listener.onScrollStateChange(mScrollPercent);
                }
            }
            if (mScrollPercent >= 1) {
                if (!mActivity.isFinishing()) {
                    if (mListeners != null && !mListeners.isEmpty() && mScrollPercent >= mScrollThreshold && mIsScrollOverValid) {
                        mIsScrollOverValid = false;
                        for (SwipeListener listener : mListeners) {
                            listener.onScrollOverThreshold();
                        }
                    }
                    mActivity.finish();
                }
            }
        }

        /** 当手势释放 */
        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            SwipeBackUtils.log("onViewReleased:" + xvel);
            final int childWidth = releasedChild.getWidth();

            //判断释放以后是应该滑到最右边(关闭)，还是最左边（还原）
            int left = xvel >= 0 && mScrollPercent > mScrollThreshold ?
                    childWidth + mShadowLeft.getIntrinsicWidth() + OVERSCROLL_DISTANCE : 0;

            if (isPageTranslucent()) {// 当窗口透明后才允许修改位置，释放手指后才可以滑动
                mDragHelper.settleCapturedViewAt(left, 0);
                invalidate();
            } else if (left > 0 && !mActivity.isFinishing()) {// 在未透明前并且超过释放范围
                SwipeBackUtils.convertActivityFromTranslucent(mActivity);
                mActivity.finish();
            }
        }

        /** 尝试第一次滑动 */
        @Override
        public void onEdgeDragStarted(int edgeFlags, int pointerId) {
            SwipeBackUtils.convertActivityToTranslucent(mActivity, new SwipeBackUtils.PageTranslucentListener() {
                @Override
                public void onPageTranslucent() {
                    SwipeBackUtils.log("convertActivityToTranslucent end");
                    setPageTranslucent(true);
                }
            });
        }

        public boolean isPageTranslucent() {
            return pageTranslucent;
        }
    }

    private boolean pageTranslucent = false;

    public void setPageTranslucent(boolean pageTranslucent) {
        this.pageTranslucent = pageTranslucent;
    }

    public boolean isPageTranslucent() {
        return pageTranslucent;
    }

    public void attachToActivity(Activity activity) {
        if (getParent() != null) {
            return;
        }
        mActivity = activity;
        TypedArray a = activity.getTheme().obtainStyledAttributes(new int[]{
                android.R.attr.windowBackground
        });
        int background = a.getResourceId(0, 0);
        a.recycle();

        ViewGroup decor = (ViewGroup) activity.getWindow().getDecorView();
        View decorChild = decor.findViewById(android.R.id.content);
        while (decorChild.getParent() != decor) {
            decorChild = (View) decorChild.getParent();
        }
        decorChild.setBackgroundResource(background);
        decor.removeView(decorChild);
        addView(decorChild);
        setContentView(decorChild);
        decor.addView(this);
    }

    public void removeFromActivity(Activity activity) {
        if (getParent() == null) return;
        ViewGroup decorChild = (ViewGroup) getChildAt(0);
        ViewGroup decor = (ViewGroup) activity.getWindow().getDecorView();
        decor.removeView(this);
        removeView(decorChild);
        decor.addView(decorChild);
    }

    public void setPercentOffset(float percent, float offset) {
        if (percent == 0) {
            setX(0);
            return;
        }
        if (mContentView == null || mContentView.getWidth() == 0) {
            return;
        }
        int width = mContentView.getWidth();
        setX(Math.min(-width * offset * Math.max(1 - percent, 0), 0));
    }
}
