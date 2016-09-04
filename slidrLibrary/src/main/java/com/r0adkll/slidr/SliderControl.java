package com.r0adkll.slidr;

import com.r0adkll.slidr.Slidr.SlidrInterface;

import java.util.ArrayList;

/**
 * 控制上个界面的控件跟随移动
 * Created by qiwei on 2016/8/31.
 */
public class SliderControl {
    private static SliderControl instance = new SliderControl();
    private ArrayList<SlidrInterface> slidrs = new ArrayList<>();

    private SliderControl() {
    }

    public static SliderControl getInstance() {
        return instance;
    }

    public int addSlidr(SlidrInterface slidr) {
        LogUtil.i("addSlidr:" + slidrs.size());
        slidrs.add(slidr);
        return slidrs.size() - 1;
    }

    public void removeSlidr(SlidrInterface slidr) {
        LogUtil.i("removeSlidr:" + slidrs.size());
        slidrs.remove(slidr);
        slidr.setRelease(true);
    }

    public void removeSlidr(int index) {
        slidrs.remove(index);
    }

    public SlidrInterface getSlidr(int index) {
        return slidrs.get(index);
    }

    public SlidrInterface getLastSlidr() {
        LogUtil.i("getLastSlidr:" + slidrs.size());
        if (slidrs.size() == 0) return null;
        return slidrs.get(slidrs.size() - 1);
    }
}
