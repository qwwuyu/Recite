package com.qwwuyu.recite.ui;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qwwuyu.recite.R;


/**
 * 底部导航控件
 * Created by qw on 2016/8/20.
 */
public class NavigationView extends RelativeLayout {
    /** 背景色 */
    private int bg = 0xfffcfcfc;
    /** 导航栏头部线条 */
    private int lineBg = 0xffd6d6d6;
    /** 文本默认颜色 */
    private int textColor = 0xff868686;
    /** 文本选中颜色 */
    private int checkedTextColor = 0xff2299ff;
    /** Item点击背景资源ID */
    private int itemBg = -1;
    /** 文本资源ID */
    private int textsRes = R.array.mainTexts;
    /** 文本 */
    private CharSequence[] texts;
    /** 默认图片 */
    private int[] icons = new int[]{R.drawable.img_main_home0, R.drawable.img_main_setting0, R.drawable.img_main_mine0};
    /** 点击图片 */
    private int[] checkedIcons = new int[]{R.drawable.img_main_home1, R.drawable.img_main_setting1, R.drawable.img_main_mine1};
    /** Item集合 */
    private RelativeLayout[] items;
    /** 图片背景集合 */
    private View[] imgBgs;
    /** 图片集合 */
    private View[] imgs;
    /** ItemText集合 */
    private TextView[] txts;
    /** 点击监听 */
    private AdapterView.OnItemClickListener mOnItemClickListener;

    public NavigationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public NavigationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        texts = getResources().getStringArray(textsRes);
        addView(View.inflate(context, R.layout.view_navigation, null));
        View line = findViewById(R.id.navigation_line);
        LinearLayout ll = (LinearLayout) findViewById(R.id.navigation_ll);
        line.setBackgroundColor(lineBg);
        ll.setBackgroundColor(bg);
        items = new RelativeLayout[texts.length];
        imgBgs = new View[texts.length];
        imgs = new View[texts.length];
        txts = new TextView[texts.length];
        for (int i = 0; i < texts.length; i++) {
            final int position = i;
            items[i] = (RelativeLayout) View.inflate(context, R.layout.item_navigation, null);
            imgBgs[i] = items[i].findViewById(R.id.navigation_rl_imgBg);
            imgs[i] = items[i].findViewById(R.id.navigation_img);
            txts[i] = (TextView) items[i].findViewById(R.id.navigation_txt);
            if (itemBg != -1) {
                items[i].setBackgroundResource(itemBg);
            }
            txts[i].setText(texts[i]);
            imgBgs[i].setBackgroundResource(icons[i]);
            imgs[i].setBackgroundResource(checkedIcons[i]);
            imgs[i].setAlpha(0);
            LinearLayout.LayoutParams viewParams = new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT);
            viewParams.weight = 1;
            ll.addView(items[i], viewParams);
            items[i].setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(null, v, position, 0);
                    }
                    setOffset(position, 0);
                }
            });
        }
        setOffset(0, 0);
    }

    @Override
    public boolean isInEditMode() {
        return true;
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    /**
     * 设置当前进度
     *
     * @param position       当前页
     * @param positionOffset 偏移量
     */
    public void setOffset(int position, float positionOffset) {
        for (int i = 0; i < imgs.length; i++) {
            float mustAlpha = getMustAlpha(position, positionOffset, i);
            float alpha = imgs[i].getAlpha();
            if (mustAlpha != alpha) {
                imgs[i].setAlpha(mustAlpha);
                txts[i].setTextColor(getColor(textColor, checkedTextColor, mustAlpha));
            }
        }
    }

    private float getMustAlpha(int position, float positionOffset, int i) {
        float offset = position + positionOffset;
        if (i >= offset + 1 || i <= offset - 1) {
            return 0;
        } else if (i > offset) {
            return offset + 1 - i;
        } else {
            return i + 1 - offset;
        }
    }

    private int getColor(int c0, int c1, float p) {
        int a = Color.alpha(c0) + Math.round(p * (Color.alpha(c1) - Color.alpha(c0)));
        int r = Color.red(c0) + Math.round(p * (Color.red(c1) - Color.red(c0)));
        int g = Color.green(c0) + Math.round(p * (Color.green(c1) - Color.green(c0)));
        int b = Color.blue(c0) + Math.round(p * (Color.blue(c1) - Color.blue(c0)));
        return Color.argb(a, r, g, b);
    }
}
