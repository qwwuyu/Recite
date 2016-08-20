package com.qwwuyu.recite.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * 通用Adapter
 *
 * @author qw
 * @Description
 * @date 2015-10-14
 */
public abstract class CommAdapter<T> extends BaseAdapter {
    /** 上下文 */
    protected Context context;
    /** 数据 */
    protected List<T> list;
    /** 布局填充器 */
    protected LayoutInflater inflater;
    /** 布局id */
    protected int itemLayoutId;
    /** Item监听 */
    protected AdapterListener adapterListener;

    public CommAdapter(Context context, List<T> list, int itemLayoutId) {
        this.context = context;
        this.list = list;
        this.itemLayoutId = itemLayoutId;
        this.inflater = LayoutInflater.from(context);
    }

    public CommAdapter(Context context, List<T> list, int itemLayoutId, AdapterListener adapterListener) {
        this(context, list, itemLayoutId);
        this.adapterListener = adapterListener;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(itemLayoutId, null);
        }
        onGetView(position, convertView, list.get(position));
        if (adapterListener != null) {
            setConvertViewClick(position, convertView);
        }
        return convertView;
    }

    public abstract void onGetView(int position, View convertView, T data);

    /**
     * 当ViewHolder使用
     */
    @SuppressWarnings("unchecked")
    protected <V extends View> V get(View convertView, int id) {
        SparseArray<View> viewHolder = (SparseArray<View>) convertView.getTag();
        if (viewHolder == null) {
            viewHolder = new SparseArray<>();
            convertView.setTag(viewHolder);
        }
        View childView = viewHolder.get(id);
        if (childView == null) {
            childView = convertView.findViewById(id);
            viewHolder.put(id, childView);
        }
        return (V) childView;
    }

    /** 给TextView设置文本 */
    protected <V extends TextView> V setText(View convertView, int viewId, CharSequence text) {
        V view = get(convertView, viewId);
        view.setText(text);
        return view;
    }

    /** 给TextView设置文本 */
    protected <V extends TextView> V setText(View convertView, int viewId, int resId) {
        return setText(convertView, viewId, context.getString(resId));
    }

    /** 给ImageView设置资源图片 */
    protected <V extends ImageView> V setImageRes(View convertView, int viewId, int resId) {
        V view = get(convertView, viewId);
        view.setImageResource(resId);
        return view;
    }

    /** 给View设置点击事件 */
    protected <V extends View> V setOnClickListener(View convertView, int viewId, OnClickListener onClickListener) {
        V view = get(convertView, viewId);
        view.setOnClickListener(onClickListener);
        return view;
    }

    /** 给View设置隐藏显示 */
    protected <V extends View> V setVisibility(View convertView, int viewId, int visibility) {
        V view = get(convertView, viewId);
        view.setVisibility(visibility);
        return view;
    }

    /** 给View设置背景资源 */
    protected <V extends View> V setBackgroundResource(View convertView, int viewId, int resId) {
        V view = get(convertView, viewId);
        view.setBackgroundResource(resId);
        return view;
    }

    /** 给View设置激活 */
    protected <V extends View> V setBackgroundResource(View convertView, int viewId, boolean enabled) {
        V view = get(convertView, viewId);
        view.setEnabled(enabled);
        return view;
    }

    /**
     * Adapter的点击监听
     */
    public interface AdapterListener {
        /** 当item某条目某控件被点击 */
        public void onItemClick(int position, View v);
    }

    /** 设置item点击事件 */
    private void setConvertViewClick(final int position, View convertView) {
        convertView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                adapterListener.onItemClick(position, v);
            }
        });
    }
}