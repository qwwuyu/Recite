package com.qwwuyu.recite.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 索引控件
 */
public class LetterIndexView extends LinearLayout {
    private int num = 27;
    /** 上下文环境 */
    private Context context;
    /** 字母控件 */
    private TextView[] lettersTxt = new TextView[num];
    /** 触碰字母索引接口 */
    private OnTouchLetterIndex touchLetterIndex;

    public LetterIndexView(Context context) {
        super(context);
        this.context = context;
    }

    public LetterIndexView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public void init(OnTouchLetterIndex touchLetterIndex) {
        this.touchLetterIndex = touchLetterIndex;
        this.setBackgroundColor(0x00000000);
        this.setOrientation(LinearLayout.VERTICAL);
        this.setGravity(Gravity.CENTER_HORIZONTAL);
        setPadding(dip2px(context, 5), 0, dip2px(context, 5), 0);
        for (int i = 0; i < num; i++) {
            lettersTxt[i] = new TextView(context);
            lettersTxt[i].setGravity(Gravity.CENTER);
            char tab = (char) (i + 'A');
            if (i == num - 1)
                lettersTxt[i].setText("#");
            else
                lettersTxt[i].setText(String.valueOf(tab));
            lettersTxt[i].setBackgroundColor(0x00000000);
            lettersTxt[i].setTextSize(14);
            lettersTxt[i].setTextColor(0xdd000000);
            LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, 0);
            layoutParams.weight = 1;
            lettersTxt[i].setLayoutParams(layoutParams);
            this.addView(lettersTxt[i]);
        }

        this.setOnTouchListener(new OnTouchListener() {
            private int y;
            private int height;
            private String tab;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        LetterIndexView.this.setBackgroundColor(0x33000000);
                    case MotionEvent.ACTION_MOVE:
                        y = (int) event.getY(); // 获取触发事件点的纵坐标
                        height = LetterIndexView.this.getHeight();
                        int location = (int) (y / (height / num) + 0.5f);
                        if (location == num - 1) {
                            tab = "#";
                        } else if (location < num) {
                            tab = String.valueOf((char) (location + 'A'));
                        }
                        LetterIndexView.this.touchLetterIndex.touchLetterWitch(tab);
                        break;
                    case MotionEvent.ACTION_UP:
                        LetterIndexView.this.setBackgroundColor(0x00000000);
                        LetterIndexView.this.touchLetterIndex.touchFinish();
                        break;
                }
                return true;
            }
        });
    }

    /** dp转px */
    public int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public interface OnTouchLetterIndex {

        /** 触摸字母空间接口 */
        void touchLetterWitch(String letter);

        /** 结束查询 */
        void touchFinish();
    }
}
