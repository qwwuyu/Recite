package com.qwwuyu.recite.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * 自动计算高度的ListView控件
 */
public class AutoSizeListView extends ListView {
    public AutoSizeListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public AutoSizeListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoSizeListView(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
