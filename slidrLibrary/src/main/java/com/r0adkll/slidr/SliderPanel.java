/*
 * Copyright (c) 2014. 52inc
 * All Rights Reserved.
 */

package com.r0adkll.slidr;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewGroupCompat;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Project: PilotPass
 * Package: com.ftinc.mariner.pilotpass.widgets
 * Created by drew.heavner on 8/14/14.
 */
public class SliderPanel extends FrameLayout {
    private int mScreenWidth;
    private View mDecorView;
    private ViewDragHelper mDragHelper;
    private OnPanelSlideListener mListener;
    private boolean mIsLocked = false;
    private boolean mIsEdgeTouched = false;
    private SlidrConfig mConfig;
    private Slidr.SlidrInterface slidr;
    private int id = 0;

    public SliderPanel(Context context, View decorView, SlidrConfig config) {
        super(context);
        mDecorView = decorView;
        mConfig = (config == null ? new SlidrConfig.Builder().build() : config);
        init();
    }

    private void init() {
        mScreenWidth = getResources().getDisplayMetrics().widthPixels;
        mDragHelper = ViewDragHelper.create(this, mConfig.getSensitivity(), callback);
        mDragHelper.setMinVelocity(mConfig.getVelocityThreshold() - 400);
        mDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT);
        ViewGroupCompat.setMotionEventSplittingEnabled(this, false);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {//是否拦截事件,在可以滑动后返回true.
        if (mIsLocked) return super.onInterceptTouchEvent(ev);
        mIsEdgeTouched = !mConfig.isEdgeOnly() && ev.getX() < mConfig.getEdgeSize(getWidth());
        try {
            return mDragHelper.shouldInterceptTouchEvent(ev);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {//预处理事件
        if (mIsLocked) return super.onTouchEvent(event);
        try {
            mDragHelper.processTouchEvent(event);
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }


    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mDragHelper.continueSettling(true)) {//执行完所有动画
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    private ViewDragHelper.Callback callback = new ViewDragHelper.Callback() {
        @Override
        public boolean tryCaptureView(View child, int pointerId) {//是否可以滑动,true之后会拦截事件
            boolean edgeCase = (!mConfig.isEdgeOnly() && mIsEdgeTouched) || mDragHelper.isEdgeTouched(ViewDragHelper.EDGE_LEFT, pointerId);
            return child.getId() == mDecorView.getId() && edgeCase;
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            return Math.max(0, Math.min(mScreenWidth, left));
        }

        @Override
        public int getViewHorizontalDragRange(View child) {
            return mScreenWidth;
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            int settleLeft = 0;
            if (Math.abs(xvel) > mConfig.getVelocityThreshold() && Math.abs(yvel) < mConfig.getVelocityThreshold()) {
                settleLeft = mScreenWidth;
            } else if (releasedChild.getLeft() > getWidth() * mConfig.getDistanceThreshold()) {
                settleLeft = mScreenWidth;
            }
            mDragHelper.settleCapturedViewAt(settleLeft, releasedChild.getTop());
            invalidate();
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
            float percent = 1f - ((float) left / (float) mScreenWidth);
            if (mListener != null) mListener.onSlideChange(percent);
            if (slidr != null && slidr.isRelease()) {
                slidr = null;
            }
            LogUtil.i("onViewPositionChanged:" + id);
            if (slidr != null) {
                LogUtil.i("offsetLeftAndRight:" + slidr.getPanel().getId());
                View moveView = slidr.getPanel();
                ViewCompat.offsetLeftAndRight(moveView, (changedView.getLeft() - mScreenWidth) / 5 - moveView.getLeft());
            }
        }

        @Override
        public void onViewDragStateChanged(int state) {
            super.onViewDragStateChanged(state);
            if (mListener != null) mListener.onStateChanged(state);
            switch (state) {
                case ViewDragHelper.STATE_IDLE:
                    if (mDecorView.getLeft() == 0) {
                        if (mListener != null) mListener.onOpened();
                    } else {
                        if (mListener != null) mListener.onClosed();
                    }
                    break;
                case ViewDragHelper.STATE_DRAGGING:
                    break;
                case ViewDragHelper.STATE_SETTLING:
                    break;
            }
        }
    };

    public void lock() {
        mDragHelper.abort();
        mIsLocked = true;
    }

    public void unlock() {
        mDragHelper.abort();
        mIsLocked = false;
    }

    public boolean isLock() {
        return mIsLocked;
    }

    public void setSlidr(Slidr.SlidrInterface slidr) {
        this.slidr = slidr;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public void setOnPanelSlideListener(OnPanelSlideListener listener) {
        mListener = listener;
    }

    public interface OnPanelSlideListener {
        void onStateChanged(int state);

        void onClosed();

        void onOpened();

        void onSlideChange(float percent);
    }
}
