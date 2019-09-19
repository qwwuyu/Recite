package me.imid.swipebacklayout.lib.helper;

import me.imid.swipebacklayout.lib.SwipeBackLayout;

/**
 * Created by Mr.Jude on 2015/8/26.
 */
public class RelateSlider implements SwipeBackLayout.SwipeListener {
    public SwipeBackPage curPage;
    private static final int DEFAULT_OFFSET = 40;
    private int offset = 500;

    public RelateSlider(SwipeBackPage curActivity) {
        this.curPage = curActivity;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public void setEnable(boolean enable) {
        if (enable) curPage.addListener(this);
        else curPage.removeListener(this);
    }

    @Override
    public void onScrollStateChange(int state, float percent) {
        SwipeBackPage page = SwipeBackHelper.getPrePage(curPage);
        if (page != null) {
            page.getSwipeBackLayout().setX(Math.min(-offset * Math.max(1 - percent, 0) + DEFAULT_OFFSET, 0));
            if (percent == 0) {
                page.getSwipeBackLayout().setX(0);
            }
        }
    }

    @Override
    public void onEdgeTouch(int edgeFlag) {

    }

    @Override
    public void onScrollOverThreshold() {
        SwipeBackPage page = SwipeBackHelper.getPrePage(curPage);
        if (page != null) {
            page.getSwipeBackLayout().setX(0);
        }
    }
}
