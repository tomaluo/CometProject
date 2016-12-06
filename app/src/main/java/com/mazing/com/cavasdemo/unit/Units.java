package com.mazing.com.cavasdemo.unit;

import android.content.Context;

/**
 * Created by toma on 16/4/14.
 */
public class Units {

    public static int convertDIP2PX(Context context, int dip) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dip*scale + 0.5f*(dip>=0?1:-1));
    }

    //转换px为dip
    public static int convertPX2DIP(Context context, int px) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int)(px/scale + 0.5f*(px>=0?1:-1));
    }
}
