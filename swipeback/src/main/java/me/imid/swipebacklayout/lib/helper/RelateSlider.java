package me.imid.swipebacklayout.lib.helper;

import me.imid.swipebacklayout.lib.SwipeBackLayout;

/**
 * Created by Mr.Jude on 2015/8/26.
 */
class RelateSlider implements SwipeBackLayout.SwipeListener {
    public SwipeBackPage curPage;
    private static final int DEFAULT_OFFSET = 40;
    private float offset = 0.25f;

    public RelateSlider(SwipeBackPage curActivity) {
        this.curPage = curActivity;
    }

    public void setOffset(float offset) {
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
            page.getSwipeBackLayout().setPercentOffset(percent, offset);
        }
    }

    @Override
    public void onEdgeTouch(int edgeFlag) {

    }

    @Override
    public void onScrollOverThreshold() {
        SwipeBackPage page = SwipeBackHelper.getPrePage(curPage);
        if (page != null) {
            page.getSwipeBackLayout().setPercentOffset(0, offset);
        }
    }
}
