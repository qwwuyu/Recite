package swipeback;

public interface SwipeListener {
    /**
     * Invoke when state or scrollPercent changed
     * @param scrollPercent scroll percent of this view
     */
    void onScrollStateChange(float scrollPercent);

    /**
     * Invoke when edge touched
     */
    void onEdgeTouch();

    /**
     * Invoke when scroll percent over the threshold for the first time
     */
    void onScrollOverThreshold();
}