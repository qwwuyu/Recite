package com.qwwuyu.recite.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.qwwuyu.recite.R;


/**
 * qw自己写的SwitchBtn
 */
public class QwSwitchView extends View {
    /** 仿ios圆类型 */
    private static final int ROUND = 0x00;
    /** 仿微信矩形类型 */
    private static final int SQUARE = 0x01;
    /** 当前类型 */
    private int style = ROUND;
    /** 打开时的背景色 */
    private int bg_open = 0xff45C01A;
    /** 关闭时的背景色 */
    private int bg_close = 0xffAAAAAA;
    /** 背景的线条色 */
    private int bg_line = 0xffAAAAAA;
    /** 按钮的背景色 */
    private int btn_bg = 0xffffffff;
    /** 按钮的线条色 */
    private int btn_line = 0xffffffff;
    /** 线条宽度 */
    private int sw;
    /** 内间距 */
    private int step;
    /** 按钮背景色的画笔 */
    private Paint btn_bg_paint;
    /** 按钮线条的画笔 */
    private Paint btn_line_paint;
    /** 整体背景色的画笔 */
    private Paint bg_line_paint;
    /** 整体线条的画笔 */
    private Paint bg_paint;
    /** 背景的矩形 */
    private RectF bg_rect;
    /** 按钮的固定矩形 */
    private RectF btn_rect;
    /** 按钮的移动矩形 */
    private RectF btn_rect2;
    /** 按钮移动的最大距离 */
    private float max_left;
    /** 按钮的宽度 */
    private float differ;
    /** 是否选中 */
    private boolean isDown = false;
    /** 选中的初始X坐标 */
    private float bX = 0;
    /** 比较点击事件的矩形 */
    private RectF downR;
    /** 之前的状态 */
    private boolean oldChecked;
    /** 当前的选中状态 */
    private boolean mChecked = false;

    public QwSwitchView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    public QwSwitchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.QwSwitchView);
        style = a.getInteger(R.styleable.QwSwitchView_btn_style, ROUND);
        bg_open = a.getColor(R.styleable.QwSwitchView_bg_open, 0xff45C01A);
        bg_close = a.getColor(R.styleable.QwSwitchView_bg_close, 0xffAAAAAA);
        bg_line = a.getColor(R.styleable.QwSwitchView_bg_line, 0xffAAAAAA);
        btn_bg = a.getColor(R.styleable.QwSwitchView_btn_bg, 0xffffffff);
        btn_line = a.getColor(R.styleable.QwSwitchView_btn_line, 0xffffffff);
        mChecked = a.getBoolean(R.styleable.QwSwitchView_checked, false);
        sw = dip2px(context, 1);
        step = dip2px(context, 1);
        // 画btn实心
        btn_bg_paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        btn_bg_paint.setStyle(Paint.Style.FILL);
        btn_bg_paint.setColor(btn_bg);
        // 画btn线
        btn_line_paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        btn_line_paint.setStyle(Paint.Style.STROKE);
        btn_line_paint.setColor(btn_line);
        btn_line_paint.setStrokeWidth(sw);
        // 画bg_line线
        bg_line_paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bg_line_paint.setStyle(Paint.Style.STROKE);
        bg_line_paint.setColor(bg_line);
        bg_line_paint.setStrokeWidth(step);
        // 画bg_line线
        bg_paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bg_paint.setStyle(Paint.Style.FILL);
        bg_paint.setColor(bg_close);
        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        bg_rect = null;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (bg_rect == null && getWidth() != 0) {
            bg_rect = new RectF(step, step, getWidth() - step, getHeight() - step);
            int width;
            if (style == ROUND) {
                width = getHeight();
            } else {
                width = getWidth() / 2;
            }
            btn_rect = new RectF(sw + step, sw + step, width - sw - step, getHeight() - sw - step);
            max_left = bg_rect.right - btn_rect.right + step;
            differ = btn_rect.right - btn_rect.left;
            float beginLeft = sw + step;
            if (mChecked) {
                beginLeft = max_left;
            }
            btn_rect2 = new RectF(beginLeft, sw + step, beginLeft + differ, getHeight() - sw - step);
            bg_paint.setColor(getColor(bg_close, bg_open, (btn_rect2.left - btn_rect.left) / max_left));
            bg_line_paint.setColor(getColor(bg_close, bg_open, (btn_rect2.left - btn_rect.left) / max_left));
        }
        if (bg_rect != null) {
            if (style == ROUND) {
                canvas.drawRoundRect(bg_rect, (getHeight() - step) / 2, (getHeight() - step) / 2, bg_paint);
                canvas.drawRoundRect(bg_rect, (getHeight() - step) / 2, (getHeight() - step) / 2, bg_line_paint);
                canvas.drawOval(btn_rect2, btn_bg_paint);
                canvas.drawOval(btn_rect2, btn_line_paint);
            } else {
                canvas.drawRoundRect(bg_rect, step, step, bg_paint);
                canvas.drawRoundRect(bg_rect, step, step, bg_line_paint);
                canvas.drawRoundRect(btn_rect2, step, step, btn_bg_paint);
                canvas.drawRoundRect(btn_rect2, step, step, btn_line_paint);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isDown = false;
                oldChecked = mChecked;
                if (btn_rect2 != null && btn_rect2.contains(event.getX(), event.getY())) {
                    isDown = true;
                    bX = event.getX() - (mChecked ? max_left : 0);
                }
                downR = new RectF(event.getX() - 10, event.getY() - 10, event.getX() + 10, event.getY() + 10);
                break;
            case MotionEvent.ACTION_MOVE:
                if (downR != null && !downR.contains(event.getX(), event.getY())) {// 处理点击事件
                    downR = null;
                }
                if (isDown) {// 处理btn滑动
                    btn_rect2.left = btn_rect.left + event.getX() - bX;
                    if (btn_rect2.left < btn_rect.left) {
                        btn_rect2.left = btn_rect.left;
                    } else if (btn_rect2.left > max_left) {
                        btn_rect2.left = max_left;
                    }
                    btn_rect2.right = btn_rect2.left + differ;
                    bg_paint.setColor(getColor(bg_close, bg_open, (btn_rect2.left - btn_rect.left) / max_left));
                    bg_line_paint.setColor(getColor(bg_close, bg_open, (btn_rect2.left - btn_rect.left) / max_left));
                    postInvalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (downR != null && downR.contains(event.getX(), event.getY())) {// 点击事件
                    btn_rect2.left = (mChecked = !mChecked) ? max_left : btn_rect.left;
                    bg_paint.setColor(getColor(bg_close, bg_open, (btn_rect2.left - btn_rect.left) / max_left));
                    bg_line_paint.setColor(getColor(bg_close, bg_open, (btn_rect2.left - btn_rect.left) / max_left));
                    btn_rect2.right = btn_rect2.left + differ;
                    postInvalidate();
                } else if (isDown) {// 滑动完毕事件
                    if (btn_rect2.left + btn_rect2.right >= getWidth()) {
                        btn_rect2.left = max_left;
                        mChecked = true;
                    } else {
                        btn_rect2.left = btn_rect.left;
                        mChecked = false;
                    }
                    bg_paint.setColor(getColor(bg_close, bg_open, (btn_rect2.left - btn_rect.left) / max_left));
                    bg_line_paint.setColor(getColor(bg_close, bg_open, (btn_rect2.left - btn_rect.left) / max_left));
                    btn_rect2.right = btn_rect2.left + differ;
                    postInvalidate();
                }
                if (listener != null && oldChecked != mChecked) {
                    listener.onCheckedChanged(this, mChecked);
                }
                isDown = false;
                break;
            default:
                break;
        }
        return true;
    }

    private int getColor(int c0, int c1, float p) {
        int a = Color.alpha(c0) + Math.round(p * (Color.alpha(c1) - Color.alpha(c0)));
        int r = Color.red(c0) + Math.round(p * (Color.red(c1) - Color.red(c0)));
        int g = Color.green(c0) + Math.round(p * (Color.green(c1) - Color.green(c0)));
        int b = Color.blue(c0) + Math.round(p * (Color.blue(c1) - Color.blue(c0)));
        return Color.argb(a, r, g, b);
    }

    private int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private OnCheckedChangeListener listener;

    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        this.listener = listener;
    }

    public interface OnCheckedChangeListener {
        void onCheckedChanged(QwSwitchView qwSwitchBtn, boolean isChecked);
    }

    /** 不会同时触发OnCheckedChangeListener */
    public void setChecked(boolean checked) {
        if (mChecked != checked) {
            mChecked = checked;
            if (btn_rect2 != null) {
                btn_rect2.left = mChecked ? max_left : btn_rect.left;
                bg_paint.setColor(getColor(bg_close, bg_open, (btn_rect2.left - btn_rect.left) / max_left));
                bg_line_paint.setColor(getColor(bg_close, bg_open, (btn_rect2.left - btn_rect.left) / max_left));
                btn_rect2.right = btn_rect2.left + differ;
                postInvalidate();
            }
        }
    }

    public boolean isChecked() {
        return mChecked;
    }
}