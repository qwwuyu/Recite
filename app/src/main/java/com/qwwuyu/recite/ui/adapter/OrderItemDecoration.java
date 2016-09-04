package com.qwwuyu.recite.ui.adapter;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutParams;
import android.view.View;

import com.qwwuyu.recite.R;


/**
 * 自定义分割线,仅适用于GridLayoutManager,LinearLayoutManager
 * Created by qiwei on 2016/8/8.
 */
public class OrderItemDecoration extends RecyclerView.ItemDecoration {
    private Drawable divider;
    private int leftPadding, rightPadding;

    private void getMode(RecyclerView parent) {
        if (divider == null) {
            divider = parent.getResources().getDrawable(R.drawable.shape_bg_divider);
            leftPadding = (int) parent.getResources().getDimension(R.dimen.def_item_padding);
            rightPadding = (int) parent.getResources().getDimension(R.dimen.view_letter_w);
        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        drawRaw(c, parent);

    }

    public void drawRaw(Canvas c, RecyclerView parent) {
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final LayoutParams params = (LayoutParams) child.getLayoutParams();
            final int left = child.getLeft() + params.leftMargin;
            final int top = child.getBottom() + params.bottomMargin + Math.round(ViewCompat.getTranslationY(child));
            final int right = child.getRight() + params.rightMargin;
            final int bottom = top + divider.getIntrinsicHeight();
            divider.setBounds(left + leftPadding, top, right - rightPadding, bottom);
            divider.draw(c);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        getMode(parent);
        outRect.set(0, 0, 0, divider.getIntrinsicHeight());
    }
}
