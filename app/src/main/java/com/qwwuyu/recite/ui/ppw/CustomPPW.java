package com.qwwuyu.recite.ui.ppw;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.qwwuyu.recite.R;


/**
 * 测试PopupWindow
 */
public class CustomPPW extends PopupWindow {
    private Context context;

    public void initPopupWindow(Context context, View.OnClickListener onClickListener) {
        if (this.context == null) {
            this.context = context;
            setContentView(View.inflate(context, R.layout.ppw_custom, null));
            //设置SelectPicPopupWindow弹出窗体的宽
            setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
            //设置SelectPicPopupWindow弹出窗体的高
            setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
            //设置SelectPicPopupWindow弹出窗体可点击
            setFocusable(true);
            //设置SelectPicPopupWindow弹出窗体动画效果
//            setAnimationStyle(R.style.AnimBottom);
            //实例化一个ColorDrawable颜色为半透明
            ColorDrawable dw = new ColorDrawable(context.getResources().getColor(R.color.trans));
            //设置SelectPicPopupWindow弹出窗体的背景
            setBackgroundDrawable(dw);
        }
    }
}
