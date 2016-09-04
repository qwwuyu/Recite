package com.qwwuyu.recite.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.bigkoo.pickerview.adapter.ArrayWheelAdapter;
import com.bigkoo.pickerview.lib.WheelView;
import com.qwwuyu.recite.R;

import java.util.ArrayList;

/**
 * PickerViewDialog
 * Created by qw on 2016/8/21.
 */
public class PickerDialog extends Dialog {
    private WheelView picker;
    private OnSelectListener onSelectListener;

    public PickerDialog(Context context, ArrayList<String> list, OnSelectListener onSelectListener) {
        this(context, list, onSelectListener, 0);
    }

    public PickerDialog(Context context, ArrayList<String> list, OnSelectListener onSelectListener, int position) {
        super(context, R.style.BtmDialogStyle);
        this.onSelectListener = onSelectListener;
        init(context, list, position);
    }

    private void init(Context context, ArrayList<String> list, final int position) {
        this.setContentView(R.layout.d_picker);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.BOTTOM);
        picker = (WheelView) findViewById(R.id.pickerDialog_picker);
        picker.setAdapter(new ArrayWheelAdapter<>(list));//设置数据
        picker.setTextSize(19);
        picker.setCyclic(true);//设置是否循环滚动
        picker.setCurrentItem(position);
        findViewById(R.id.pickerDialog_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSelectListener.onSelect(position, picker.getCurrentItem());
                dismiss();
            }
        });
    }

    public interface OnSelectListener {
        void onSelect(int oldPosition, int position);
    }
}
