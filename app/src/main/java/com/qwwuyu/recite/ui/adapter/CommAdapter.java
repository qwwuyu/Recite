package com.qwwuyu.recite.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;

/**
 * 简单使用RecyclerView的通用Adapter
 * Created by qiwei on 2016/8/5.
 */
public abstract class CommAdapter<T> extends RecyclerView.Adapter<CommViewHolder> implements IMove {
    /** 上下文 */
    protected Context context;
    /** 数据 */
    protected List<T> list;
    /** 布局填充器 */
    protected LayoutInflater inflater;
    /** 布局id */
    protected int itemLayoutId;
    /** Item监听 */
    protected AdapterListener<T> adapterListener;

    public CommAdapter(Context context, List<T> list, int itemLayoutId, AdapterListener<T> adapterListener) {
        this(context, list, itemLayoutId);
        this.adapterListener = adapterListener;
    }

    public CommAdapter(Context context, List<T> list, int itemLayoutId) {
        this.context = context;
        this.list = list;
        this.itemLayoutId = itemLayoutId;
        this.inflater = LayoutInflater.from(context);
        onCreate();
    }

    protected void onCreate() {
    }

    @Override
    public final int getItemCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public final CommViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CommViewHolder(inflater.inflate(itemLayoutId, parent, false), context);
    }

    @Override
    public final void onBindViewHolder(final CommViewHolder holder, int i) {
        final int position = holder.getAdapterPosition();
        final T t = list.get(position);
        onBind(position, holder, t);
        if (adapterListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adapterListener.onItemClick(position, v, t);
                }
            });
        }
    }

    public abstract void onBind(int position, CommViewHolder holder, T data);

    @Override
    public void moveItem(int formPosition, int toPosition) {
        int diff = formPosition < toPosition ? 1 : -1;
        for (int index = formPosition; index != toPosition; index += diff) {
            Collections.swap(list, index, index + diff);
        }
        notifyItemMoved(formPosition, toPosition);
    }

    public void addData(T t, int position) {
        list.add(position, t);
        notifyDataSetChanged();
    }

    public void addDataWithAnim(T t, int position) {
        list.add(position, t);
        notifyItemInserted(position);
    }

    public void removeData(int position) {
        list.remove(position);
        notifyDataSetChanged();
    }

    public void removeDataWithAnim(int position) {
        list.remove(position);
        notifyItemRemoved(position);
    }

    public int getColor(int color) {
        return context.getResources().getColor(color);
    }

    public float getDimension(int dimen) {
        return context.getResources().getDimension(dimen);
    }

    /** Item的点击监听 */
    public interface AdapterListener<T> {
        /** 当item某条目某控件被点击 */
        void onItemClick(int position, View v, T data);
    }
}
