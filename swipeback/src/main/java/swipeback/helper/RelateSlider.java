package swipeback.helper;

import swipeback.SwipeListener;

/**
 * Created by Mr.Jude on 2015/8/26.
 */
class RelateSlider implements SwipeListener {
    private SwipeBackPage curPage;

    public RelateSlider(SwipeBackPage curActivity) {
        this.curPage = curActivity;
    }

    @Override
    public void onScrollStateChange(float percent) {
        SwipeBackPage page = SwipeBackHelper.getPrePage(curPage);
        if (page != null) {
            page.setPercentOffset(percent);
        }
    }

    @Override
    public void onEdgeTouch() {

    }

    @Override
    public void onScrollOverThreshold() {
        SwipeBackPage page = SwipeBackHelper.getPrePage(curPage);
        if (page != null) {
            page.setPercentOffset(0);
        }
    }
}
