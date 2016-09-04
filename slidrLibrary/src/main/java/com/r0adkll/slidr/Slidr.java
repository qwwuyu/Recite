package com.r0adkll.slidr;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by r0adkll
 */
public class Slidr {
    public static void detached(SlidrInterface slidr) {
        SliderControl.getInstance().removeSlidr(slidr);
    }

    public static SlidrInterface attached(final Activity activity, final SlidrConfig config) {
        SlidrInterface lastSlidr = SliderControl.getInstance().getLastSlidr();
        final SliderPanel panel = initSliderPanel(activity, config);
        panel.setSlidr(lastSlidr);
        panel.setOnPanelSlideListener(new SliderPanel.OnPanelSlideListener() {
            @Override
            public void onStateChanged(int state) {
                if (config.getListener() != null) {
                    config.getListener().onSlideStateChanged(state);
                }
            }

            @Override
            public void onClosed() {
                if (config.getListener() != null) {
                    config.getListener().onSlideClosed();
                }
                activity.finish();
                activity.overridePendingTransition(0, 0);
            }

            @Override
            public void onOpened() {
                if (config.getListener() != null) {
                    config.getListener().onSlideOpened();
                }
            }

            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onSlideChange(float percent) {
                if (config.getListener() != null) {
                    config.getListener().onSlideChange(percent);
                }
            }
        });
        SlidrInterface slidr = initInterface(panel);
        int id = SliderControl.getInstance().addSlidr(slidr);
        slidr.getPanel().setId(id);
        return slidr;
    }

    private static SliderPanel initSliderPanel(final Activity activity, final SlidrConfig config) {
        // Hijack the decorview
        ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
        View oldScreen = decorView.getChildAt(0);
        decorView.removeViewAt(0);
        // Setup the slider panel and attached it to the decor
        SliderPanel panel = new SliderPanel(activity, oldScreen, config);
        panel.setId(R.id.slidable_panel);
        oldScreen.setId(R.id.slidable_content);
        panel.addView(oldScreen);
        decorView.addView(panel, 0);
        return panel;
    }

    private static SlidrInterface initInterface(final SliderPanel panel) {
        SlidrInterface slidr = new SlidrInterface() {
            private boolean release = false;

            @Override
            public void lock() {
                panel.lock();
            }

            @Override
            public void unlock() {
                panel.unlock();
            }

            @Override
            public boolean isLock() {
                return panel.isLock();
            }

            @Override
            public SliderPanel getPanel() {
                return panel;
            }

            @Override
            public void setRelease(boolean release) {
                this.release = release;
            }

            @Override
            public boolean isRelease() {
                return release;
            }
        };
        return slidr;
    }

    public interface SlidrInterface {
        void lock();

        void unlock();

        boolean isLock();

        SliderPanel getPanel();

        void setRelease(boolean release);

        boolean isRelease();
    }
}