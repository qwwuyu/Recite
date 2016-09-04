package com.qwwuyu.recite.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

/**
 * 简化的移动ItemTouchHelper.Callback
 * Created by qw on 2016/8/27.
 */
public class MoveCallback extends ItemTouchHelper.Callback {
    private IMove move;
    private boolean isLongPressDragEnabled = true;

    public MoveCallback(IMove move) {
        this.move = move;
    }

    public void setIsLongPressDragEnabled(boolean isLongPressDragEnabled) {
        this.isLongPressDragEnabled = isLongPressDragEnabled;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        return makeFlag(ItemTouchHelper.ACTION_STATE_DRAG, ItemTouchHelper.UP | ItemTouchHelper.DOWN);//支持上下拖曳
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder form, RecyclerView.ViewHolder to) {
        move.moveItem(form.getAdapterPosition(), to.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

    }

    @Override
    public boolean isLongPressDragEnabled() {
        return isLongPressDragEnabled;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return false;
    }
}
