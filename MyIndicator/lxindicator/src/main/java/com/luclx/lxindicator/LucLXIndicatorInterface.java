package com.luclx.lxindicator;

import android.support.v4.view.ViewPager;

/**
 * Created by LucLX on 12/17/16.
 */

public interface LucLXIndicatorInterface {
    /**
     * @param viewPager
     * @throws PageLessException
     */
    void setViewPage(ViewPager viewPager) throws PageLessException;

    /**
     * @param duration
     */
    void setAnimateDuration(long duration);

    /**
     * @param radiusSelected
     */
    void setRadiusSelected(int radiusSelected);

    /**
     * @param radiusUnselected
     */
    void setRadiusUnselected(int radiusUnselected);

    /**
     * @param distanceDot
     */
    void setDistanceDot(int distanceDot);
}
