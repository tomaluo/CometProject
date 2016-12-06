package com.mazing.com.cavasdemo.view;

import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.pdf.PdfRenderer;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Created by toma on 16/6/21.
 */
public class PathWaveView extends View {

    private int dx;
    private int dy;
    private int waveLength = 600; //一个完整波形的长度
    private int orginY = 400;    //初始波高
    private Paint mPaint;
    private Path mPath;

    public PathWaveView(Context context) {
        super(context);
    }

    public PathWaveView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mPaint = new Paint();
        mPaint.setAntiAlias(true); //抗锯齿
        mPaint.setStrokeWidth(10);
        mPaint.setColor(Color.GREEN);

        mPath = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mPath.reset();

        int halfWaveLength = waveLength / 2; //获得半个波形长度
        mPath.moveTo(-waveLength + dx,orginY + dy);  //为了让波形完全跨越整个屏幕，移动起点到屏幕外
        for(int i = -waveLength; i < getWidth() + waveLength; i += waveLength){
            mPath.rQuadTo(halfWaveLength/2,-100,halfWaveLength,0);
            mPath.rQuadTo(halfWaveLength/2,100,halfWaveLength,0);
        }

        mPath.lineTo(getWidth(),getHeight());
        mPath.lineTo(0,getHeight());
        mPath.close();

        canvas.drawPath(mPath,mPaint);
        dy += 1;
    }

    public void stareAnimet(){
        ValueAnimator animator = ValueAnimator.ofInt(0,waveLength);
        animator.setDuration(2000);
        animator.setInterpolator(new LinearInterpolator());
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                dx = (int)animation.getAnimatedValue();
                postInvalidate();
            }
        });

        animator.start();
    }
}
