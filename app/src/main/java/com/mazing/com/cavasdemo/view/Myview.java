package com.mazing.com.cavasdemo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by toma on 16/6/21.
 */
public class Myview extends View {

    private Path mPath = new Path(); //路径
    private float xStar;
    private float yStar;

    public Myview(Context context) {
        super(context);
    }

    public Myview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:{
                mPath.moveTo(event.getX(),event.getY()); //手指按下作为初始起点
                xStar = event.getX();
                yStar = event.getY();
                return true;
            }
            case MotionEvent.ACTION_MOVE:{
                float xEnd = (xStar + event.getX())/2;
                float yEnd = (yStar + event.getY())/2;
                mPath.quadTo(xStar,yStar,xEnd,yEnd);
                xStar = event.getX();
                yStar = event.getY();
                invalidate();
            }
            break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint mPaint = new Paint();
        mPaint.setColor(Color.GREEN);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(2);

        canvas.drawPath(mPath,mPaint);
    }
}