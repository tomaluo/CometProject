package com.mazing.com.cavasdemo.view;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by user on 17/3/17.
 */

public class PluralCitySelectView extends FrameLayout {
    private Context mContext;

    public PluralCitySelectView(@NonNull Context context) {
        this(context, null, 0);
    }

    public PluralCitySelectView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PluralCitySelectView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init(){

    }
}
