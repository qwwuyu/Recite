package com.qwwuyu.recite.ui;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;

import com.qwwuyu.recite.R;

/**
 * isPressed改变卡片背景颜色
 * Created by qw on 2016/9/3.
 */
public class PressedCardView extends CardView {
    public PressedCardView(Context context) {
        super(context);
    }

    public PressedCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PressedCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        if (isPressed()) {
            this.setCardBackgroundColor(getContext().getResources().getColor(R.color.comm_color));
        } else {
            this.setCardBackgroundColor(getContext().getResources().getColor(R.color.white));
        }
    }
}
