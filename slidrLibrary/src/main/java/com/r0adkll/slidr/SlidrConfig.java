package com.r0adkll.slidr;

import android.support.annotation.FloatRange;

/**
 * This class contains the configuration information for all the options available in
 * this library
 * <p/>
 * Created by r0adkll on 1/12/15.
 */
public class SlidrConfig {
    //灵敏度
    private float sensitivity = 1f;
    //加速退出
    private float velocityThreshold = 2000f;
    //允许退出的位子
    private float distanceThreshold = 0.25f;
    //只允许边缘滑动
    private boolean edgeOnly = true;
    //非边缘时占比
    private float edgeSize = 0.1f;

    private SlidrListener listener;

    private SlidrConfig() {
    }

    public float getVelocityThreshold() {
        return velocityThreshold;
    }

    public float getDistanceThreshold() {
        return distanceThreshold;
    }

    public float getSensitivity() {
        return sensitivity;
    }

    public SlidrListener getListener() {
        return listener;
    }

    public boolean isEdgeOnly() {
        return edgeOnly;
    }

    public float getEdgeSize(float size) {
        return edgeSize * size;
    }

    public void setSensitivity(float sensitivity) {
        this.sensitivity = sensitivity;
    }

    public void setVelocityThreshold(float velocityThreshold) {
        this.velocityThreshold = velocityThreshold;
    }

    public void setDistanceThreshold(float distanceThreshold) {
        this.distanceThreshold = distanceThreshold;
    }

    public static class Builder {

        private SlidrConfig config;

        public Builder() {
            config = new SlidrConfig();
        }

        public Builder sensitivity(float sensitivity) {
            config.sensitivity = sensitivity;
            return this;
        }

        public Builder velocityThreshold(float threshold) {
            config.velocityThreshold = threshold;
            return this;
        }

        public Builder distanceThreshold(@FloatRange(from = .1f, to = .9f) float threshold) {
            config.distanceThreshold = threshold;
            return this;
        }

        public Builder edge(boolean flag) {
            config.edgeOnly = flag;
            return this;
        }

        public Builder edgeSize(@FloatRange(from = 0f, to = 1f) float edgeSize) {
            config.edgeSize = edgeSize;
            return this;
        }

        public Builder listener(SlidrListener listener) {
            config.listener = listener;
            return this;
        }

        public SlidrConfig build() {
            return config;
        }
    }

    public interface SlidrListener {
        /**
         * This is called when the {@link android.support.v4.widget.ViewDragHelper} calls it's state change callback.
         *
         * @param state the {@link android.support.v4.widget.ViewDragHelper} state
         * @see android.support.v4.widget.ViewDragHelper#STATE_IDLE
         * @see android.support.v4.widget.ViewDragHelper#STATE_DRAGGING
         * @see android.support.v4.widget.ViewDragHelper#STATE_SETTLING
         */
        void onSlideStateChanged(int state);

        void onSlideChange(float percent);

        void onSlideOpened();

        void onSlideClosed();
    }

    public static class SlidrListenerAdapter implements SlidrListener {
        @Override
        public void onSlideStateChanged(int state) {
        }

        @Override
        public void onSlideChange(float percent) {
        }

        @Override
        public void onSlideOpened() {
        }

        @Override
        public void onSlideClosed() {
        }
    }
}
