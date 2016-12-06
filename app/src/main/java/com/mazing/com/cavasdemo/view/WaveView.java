package com.mazing.com.cavasdemo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DrawFilter;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.mazing.com.cavasdemo.unit.Units;

/**
 * Created by toma on 16/4/14.
 */
public class WaveView extends View {

    private Context context;
    // 波纹颜色
    private static final int WAVE_PAINT_COLOR = 0x880000aa;
    // y = Asin(wx+b)+h  w影响周期，A影响振幅，h影响y位置，b为初相；
    private static final float STRETCH_FACTOR_A = 20;
    private static final int OFFSET_Y = 0;             //波浪初始位置
    // 第一条水波移动速度
    private static final int TRANSLATE_X_SPEED_ONE = 7;
    // 第二条水波移动速度
    private static final int TRANSLATE_X_SPEED_TWO = 5;
    private float mCycleFactorW;   //波浪在目标宽度内出现的周期数

    private int mSpeedone;  // 第一条波浪前进速度
    private int mSpeedtwo;  // 第二条波浪前进速度

    private int moffoxOne;  //当前x的步近 位置

    private float[] mpoints;
    private float[] OnePoints;
    private float[] TwoPoints;

    private int mWitch;     //总宽度
    private int mHight;     //总高度

    private Paint mPaint;
    private DrawFilter mDrawFilter;

    public WaveView(Context context) {
        super(context);

    }

    public WaveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public WaveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init() {
        mSpeedone = Units.convertDIP2PX(context, TRANSLATE_X_SPEED_ONE);
        mSpeedtwo = Units.convertDIP2PX(context, TRANSLATE_X_SPEED_TWO);

        mPaint = new Paint();
        mPaint.setColor(0x880000aa);
        // 去除画笔锯齿
        mPaint.setAntiAlias(true);
        // 设置风格为实线
        mPaint.setStyle(Paint.Style.FILL);
        // 设置画笔颜色
        mPaint.setColor(WAVE_PAINT_COLOR);

        mDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.setDrawFilter(mDrawFilter);

        Path path = new Path();

        path.moveTo(0, mHight);
        for (int i = 0; i < mWitch; i++) {
            float pointy = (float) (mHight - ((STRETCH_FACTOR_A * Math.sin(mCycleFactorW * (i + moffoxOne))+ mHight / 2)));
            path.lineTo(i,pointy);
//            Log.i("point x= ", i + "");
//            Log.i("point y= ", pointy + "");
        }
        path.lineTo(mWitch,mHight);
        path.close();
        path.setFillType(Path.FillType.WINDING);

        canvas.drawPath(path,mPaint);

        if (moffoxOne >= mWitch) {
            moffoxOne = 0;
        }

        moffoxOne += mSpeedone;

        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {

        mWitch = w;
        mHight = h;
        mpoints = new float[w];
        mCycleFactorW = (float) (2 * Math.PI / mWitch);   //震幅

//        // 根据view总宽度得出所有对应的y值
        for (int i = 0; i < mWitch; i++) {
            mpoints[i] = (float) (STRETCH_FACTOR_A * Math.sin(mCycleFactorW * i) + OFFSET_Y);
        }
    }

}
