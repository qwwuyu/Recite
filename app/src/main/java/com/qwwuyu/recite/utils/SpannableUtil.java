package com.qwwuyu.recite.utils;

import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.CharacterStyle;
import android.text.style.ClickableSpan;
import android.text.style.UpdateAppearance;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * Spannable工具
 * Created by qiwei on 2016/6/29.
 */
public class SpannableUtil {
    public static ArrayList<LineSpan> setLinkSpan(TextView textView, String txt, String left, String right, int textColor, int linkColor, float thickness) {
        ArrayList<int[]> insertList = getIndex(txt, left, right);
        ArrayList<LineSpan> lineSpans = new ArrayList<>(insertList.size());
        SpannableString spannableInfo = new SpannableString(txt.replace(left, "").replace(right, ""));
        for (int[] insert : insertList) {
            LineSpan lineSpan = new LineSpan(textColor, linkColor, thickness);
            lineSpans.add(lineSpan);
            spannableInfo.setSpan(lineSpan, insert[0], insert[1], Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        textView.setText(spannableInfo);
        return lineSpans;
    }

    public static void setClickSpan(TextView textView, String txt, String left, String right, ClickSpan clickSpan) {
        ArrayList<int[]> insertList = getIndex(txt, left, right);
        SpannableString spannableInfo = new SpannableString(txt.replace(left, "").replace(right, ""));
        for (int[] insert : insertList) {
            spannableInfo.setSpan(clickSpan.copy(), insert[0], insert[1], Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        textView.setMovementMethod(new LinkTouchMovementMethod());
        textView.setHighlightColor(0x00000000);
        textView.setText(spannableInfo);
    }

    public static ArrayList<int[]> getIndex(String txt, String left, String right) {
        ArrayList<int[]> intsList = new ArrayList<>();
        String setTxt = txt;
        StringBuffer setTxtBuffer = new StringBuffer(setTxt);
        while (setTxt.contains(left) && setTxt.contains(right)) {
            int start = setTxt.indexOf(left);
            setTxtBuffer.replace(start, start + left.length(), "");
            int end = setTxt.indexOf(right) - left.length();
            setTxtBuffer.replace(end, end + right.length(), "");
            setTxt = setTxtBuffer.toString();
            intsList.add(new int[]{start, end});
        }
        return intsList;
    }

    public static void setClickSpan(TextView textView, String txt, int start, int end, ClickSpan clickSpan) {
        SpannableString spannableInfo = new SpannableString(txt);
        spannableInfo.setSpan(clickSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setMovementMethod(new LinkTouchMovementMethod());
        textView.setHighlightColor(0x00000000);
        textView.setText(spannableInfo);
    }

    private static class LinkTouchMovementMethod extends LinkMovementMethod {
        private ClickSpan mPressedSpan;

        @Override
        public boolean onTouchEvent(TextView textView, Spannable spannable, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                mPressedSpan = getPressedSpan(textView, spannable, event);
                if (mPressedSpan != null) {
                    mPressedSpan.setPressed(true);
                    Selection.setSelection(spannable, spannable.getSpanStart(mPressedSpan), spannable.getSpanEnd(mPressedSpan));
                }
            } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                ClickSpan touchedSpan = getPressedSpan(textView, spannable, event);
                if (mPressedSpan != null && touchedSpan != mPressedSpan) {
                    mPressedSpan.setPressed(false);
                    mPressedSpan = null;
                    Selection.removeSelection(spannable);
                }
            } else {
                if (mPressedSpan != null) {
                    mPressedSpan.setPressed(false);
                    super.onTouchEvent(textView, spannable, event);
                }
                mPressedSpan = null;
                Selection.removeSelection(spannable);
            }
            return true;
        }

        private ClickSpan getPressedSpan(TextView textView, Spannable spannable, MotionEvent event) {
            int x = (int) event.getX();
            int y = (int) event.getY();
            if (y < 0 || y > textView.getHeight()) {
                return null;
            }
            x -= textView.getTotalPaddingLeft();
            y -= textView.getTotalPaddingTop();
            x += textView.getScrollX();
            y += textView.getScrollY();
            Layout layout = textView.getLayout();
            int line = layout.getLineForVertical(y);
            int off = layout.getOffsetForHorizontal(line, x);
            ClickSpan[] link = spannable.getSpans(off, off, ClickSpan.class);
            if (link.length > 0) {
                return link[0];
            }
            return null;
        }
    }

    public static class ClickSpan extends ClickableSpan {
        private View.OnClickListener listener;
        private boolean isPressed;
        private int normalTextColor;
        private int pressedTextColor;
        private int normalBgColor = 0x00000000;
        private int pressedBgColor = 0x00000000;
        private int linkColor = 0x00000000;
        private float thickness = 0f;

        public ClickSpan(View.OnClickListener listener, int normalTextColor, int pressedTextColor, int normalBgColor, int pressedBgColor) {
            this(listener, normalTextColor, pressedTextColor);
            this.normalBgColor = normalBgColor;
            this.pressedBgColor = pressedBgColor;
        }

        public ClickSpan(View.OnClickListener listener, int normalTextColor, int pressedTextColor) {
            this(listener, normalTextColor);
            this.pressedTextColor = pressedTextColor;
        }

        public ClickSpan(View.OnClickListener listener, int normalTextColor) {
            this.listener = listener;
            this.normalTextColor = normalTextColor;
            this.pressedTextColor = normalTextColor;
        }

        public void setPressed(boolean isSelected) {
            isPressed = isSelected;
        }

        @Override
        public void onClick(View widget) {
            if (listener != null) {
                listener.onClick(widget);
            }
        }

        public void setLink(int linkColor, float thickness) {
            this.linkColor = linkColor;
            this.thickness = thickness;
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setColor(isPressed ? pressedTextColor : normalTextColor);
            ds.bgColor = isPressed ? pressedBgColor : normalBgColor;
            ds.setUnderlineText(false);
            try {
                Method method = TextPaint.class.getMethod("setUnderlineText", Integer.TYPE, Float.TYPE);
                method.invoke(ds, linkColor, thickness);
            } catch (Exception ignored) {
            }
        }

        public ClickSpan copy() {
            return new ClickSpan(listener, normalTextColor, pressedTextColor, normalBgColor, pressedBgColor);
        }
    }

    public static class LineSpan extends CharacterStyle implements UpdateAppearance {
        private int textColor;
        private int linkColor;
        private float thickness;

        public LineSpan(int textColor, int linkColor, float thickness) {
            this.textColor = textColor;
            this.thickness = thickness;
            this.linkColor = linkColor;
        }

        public void setLink(int textColor, int linkColor, float thickness) {
            this.textColor = textColor;
            this.linkColor = linkColor;
            this.thickness = thickness;
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(textColor);
            ds.setUnderlineText(false);
            try {
                Method method = TextPaint.class.getMethod("setUnderlineText", Integer.TYPE, Float.TYPE);
                method.invoke(ds, linkColor, thickness);
            } catch (Exception ignored) {
            }
        }
    }
}
