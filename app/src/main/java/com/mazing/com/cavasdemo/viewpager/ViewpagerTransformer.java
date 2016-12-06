package com.mazing.com.cavasdemo.viewpager;

import android.os.Build;
import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * Created by toma on 16/6/22.
 */
public class ViewpagerTransformer implements ViewPager.PageTransformer {

    public static final float MAX_SCALE = 0.9f;
    public static final float MIN_SCALE = 0.8f;

    @Override
    public void transformPage(View page, float position) {

        position = position < -1 ? -1 : position;
        position = position > 1 ? 1 : position;

        float tempScale = position < 0 ? 1 + position : 1 - position;

        float slope = (MAX_SCALE - MIN_SCALE) / 1;
        float scaleValue = MIN_SCALE + tempScale * slope;
        page.setScaleX(scaleValue);
        page.setScaleY(scaleValue);
        //page.setPivotY(page.getHeight());
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            page.getParent().requestLayout();
        }
    }
}
