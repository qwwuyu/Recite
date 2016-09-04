package com.qwwuyu.recite.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 监听Pressed
 * Created by qw on 2016/9/4.
 */
public class PressedTextView extends TextView {
    public PressedTextView(Context context) {
        super(context);
    }

    public PressedTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PressedTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setPressed(boolean pressed) {
        if (pressedListener != null) {
            pressedListener.onPressed(pressed);
        }
        super.setPressed(pressed);
    }

    private PressedListener pressedListener;

    public void setPressedListener(PressedListener pressedListener) {
        this.pressedListener = pressedListener;
    }

    public interface PressedListener {
        void onPressed(boolean pressed);
    }
}
