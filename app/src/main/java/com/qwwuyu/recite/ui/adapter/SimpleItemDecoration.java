package com.qwwuyu.recite.ui.adapter;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutParams;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.qwwuyu.recite.R;


/**
 * 自定义分割线,仅适用于GridLayoutManager,LinearLayoutManager
 * Created by qiwei on 2016/8/8.
 */
public class SimpleItemDecoration extends RecyclerView.ItemDecoration {
    enum LayoutMode {
        LinearMode,
        GridMode,
        StaggeredGridMode
    }

    private Drawable divider;
    private LayoutMode layoutMode;
    private int orientation;
    private int spanCount;

    private void getMode(RecyclerView parent) {
        if (layoutMode == null) {
            divider = parent.getResources().getDrawable(R.drawable.shape_bg_divider);
            RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
            if (layoutManager instanceof GridLayoutManager) {
                spanCount = ((GridLayoutManager) layoutManager).getSpanCount();
                layoutMode = LayoutMode.GridMode;
            } else if (layoutManager instanceof LinearLayoutManager) {
                orientation = ((LinearLayoutManager) layoutManager).getOrientation();
                layoutMode = LayoutMode.LinearMode;
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                spanCount = ((StaggeredGridLayoutManager) layoutManager).getSpanCount();
                orientation = ((StaggeredGridLayoutManager) layoutManager).getOrientation();
                layoutMode = LayoutMode.StaggeredGridMode;
                throw new RuntimeException("Mismatch LayoutManager");
            } else {
                throw new RuntimeException("Mismatch LayoutManager");
            }
        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (layoutMode == LayoutMode.LinearMode) {
            if (orientation == OrientationHelper.VERTICAL) {
                drawRaw(c, parent);
            } else {
                drawColumns(c, parent);
            }
        } else if (layoutMode == LayoutMode.GridMode || layoutMode == LayoutMode.StaggeredGridMode) {
            drawRaw(c, parent);
            drawColumns(c, parent);
        }
    }

    public void drawRaw(Canvas c, RecyclerView parent) {
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int left = child.getLeft() + params.leftMargin;
            final int top = child.getBottom() + params.bottomMargin + Math.round(ViewCompat.getTranslationY(child));
            final int right = child.getRight() + params.rightMargin;
            final int bottom = top + divider.getIntrinsicHeight();
            divider.setBounds(left, top, right, bottom);
            divider.draw(c);
        }
    }

    public void drawColumns(Canvas c, RecyclerView parent) {
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int left = child.getRight() + params.rightMargin + Math.round(ViewCompat.getTranslationX(child));
            final int top = child.getTop() + params.topMargin;
            final int right = left + divider.getIntrinsicWidth();
            final int bottom = child.getBottom() + params.bottomMargin + divider.getIntrinsicHeight();
            divider.setBounds(left, top, right, bottom);
            divider.draw(c);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        getMode(parent);
        int position = ((LayoutParams) view.getLayoutParams()).getViewLayoutPosition();
        if (layoutMode == LayoutMode.LinearMode) {
            if (orientation == OrientationHelper.VERTICAL) {
                outRect.set(0, 0, 0, divider.getIntrinsicHeight());
            } else {
                outRect.set(0, 0, divider.getIntrinsicWidth(), 0);
            }
        } else if (layoutMode == LayoutMode.GridMode) {
            outRect.set(0, 0, (position + 1) % spanCount == 0 ? 0 : divider.getIntrinsicWidth(), divider.getIntrinsicHeight());
        } else {
            if (orientation == OrientationHelper.VERTICAL) {
//                outRect.set(0, 0, view.getRight() == parent.getRight() ? 0 : divider.getIntrinsicWidth(), divider.getIntrinsicHeight());
            } else {

            }
        }
    }
}
