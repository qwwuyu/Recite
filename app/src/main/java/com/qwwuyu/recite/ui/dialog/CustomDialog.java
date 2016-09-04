package com.qwwuyu.recite.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qwwuyu.recite.R;


/**
 * 自定义对话框
 */
public class CustomDialog extends Dialog {
    /** 标题 */
    private TextView txt_title;
    /** 消息 */
    private TextView txt_msg;
    /** 左边按钮 */
    private TextView txt_btn_left;
    /** 右边按钮 */
    private TextView txt_btn_right;
    /** 按钮线条 */
    private View view_line;

    public CustomDialog(Context context) {
        super(context, R.style.DefDialogStyle);
        init();
    }

    private void init() {
        this.setContentView(R.layout.d_custom);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.CENTER);
        txt_title = (TextView) findViewById(R.id.custom_txt_title);
        txt_msg = (TextView) findViewById(R.id.custom_txt_msg);
        txt_btn_left = (TextView) findViewById(R.id.custom_txt_btn_left);
        txt_btn_right = (TextView) findViewById(R.id.custom_txt_btn_right);
        view_line = findViewById(R.id.custom_view_line);
    }

    public CustomDialog setTxt(String title, String msg) {
        txt_title.setText(title);
        txt_msg.setText(title);
        txt_msg.setVisibility(View.VISIBLE);
        return this;
    }

    public CustomDialog setTitle(String title) {
        txt_title.setText(title);
        return this;
    }

    public CustomDialog setMsg(String msg) {
        txt_msg.setText(msg);
        txt_msg.setVisibility(View.VISIBLE);
        return this;
    }

    public CustomDialog setSingleBtn() {
        txt_btn_left.setVisibility(View.GONE);
        view_line.setVisibility(View.GONE);
        txt_btn_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return this;
    }

    public CustomDialog setSingleBtn(View.OnClickListener listener) {
        txt_btn_left.setVisibility(View.GONE);
        view_line.setVisibility(View.GONE);
        txt_btn_right.setOnClickListener(listener);
        return this;
    }

    public CustomDialog setSingleBtnWithDis(final View.OnClickListener listener) {
        txt_btn_left.setVisibility(View.GONE);
        view_line.setVisibility(View.GONE);
        txt_btn_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (listener != null) {
                    listener.onClick(v);
                }
            }
        });
        return this;
    }

    public CustomDialog setSingleBtn(String btnTxt, View.OnClickListener listener) {
        txt_btn_right.setText(btnTxt);
        setSingleBtn(listener);
        return this;
    }

    public CustomDialog setSingleBtnWithDis(String btnTxt, final View.OnClickListener listener) {
        txt_btn_right.setText(btnTxt);
        setSingleBtnWithDis(listener);
        return this;
    }

    public CustomDialog setLeftBtn(String leftTxt, View.OnClickListener listener) {
        txt_btn_left.setText(leftTxt);
        txt_btn_left.setOnClickListener(listener);
        return this;
    }

    public CustomDialog setRightBtn(String rightTxt, View.OnClickListener listener) {
        txt_btn_right.setText(rightTxt);
        txt_btn_right.setOnClickListener(listener);
        return this;
    }

    public CustomDialog setLeftBtnWithDis(String leftTxt, final View.OnClickListener listener) {
        txt_btn_left.setText(leftTxt);
        txt_btn_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (listener != null) {
                    listener.onClick(v);
                }
            }
        });
        return this;
    }

    public CustomDialog setRightBtnWithDis(String rightTxt, final View.OnClickListener listener) {
        txt_btn_right.setText(rightTxt);
        txt_btn_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (listener != null) {
                    listener.onClick(v);
                }
            }
        });
        return this;
    }

    @Override
    public void show() {
        super.show();
    }
}
