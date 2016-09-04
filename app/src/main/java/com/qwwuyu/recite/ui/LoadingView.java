package com.qwwuyu.recite.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qwwuyu.recite.R;

/**
 * 加载控件
 * Created by qw on 2016/8/21.
 */
public class LoadingView extends RelativeLayout {
    private View img_loading;
    private TextView txt_hint;

    public LoadingView(Context context) {
        super(context);
        init(context);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        addView(View.inflate(context, R.layout.view_loading, null));
        img_loading = findViewById(R.id.loading_view_img);
        txt_hint = (TextView) findViewById(R.id.loading_txt_hint);
    }

    public void remove(boolean needAnim) {
        if (needAnim) {
            Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.loading_fade_out);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    if (getParent() != null) {
                        ViewGroup parent = (ViewGroup) getParent();
                        parent.removeView(LoadingView.this);
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            startAnimation(animation);
        } else if (getParent() != null) {
            ViewGroup parent = (ViewGroup) getParent();
            parent.removeView(this);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        img_loading.startAnimation(getRotate());
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        img_loading.clearAnimation();
    }

    private RotateAnimation getRotate() {
        RotateAnimation ra = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        ra.setDuration(1000);
        LinearInterpolator lin = new LinearInterpolator();
        ra.setInterpolator(lin);
        ra.setRepeatCount(Animation.INFINITE);
        return ra;
    }
}
