package com.mazing.com.cavasdemo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Created by toma on 16/5/4.
 */
public class MoveCircle extends View {

    private Path mPath;      //绘画路径
    private Paint mPaint;    //画笔

    /** View的宽度 **/
    private int width;
    /** View的高度，这里View应该是正方形，所以宽高是一样的 **/
    private int height;
    /** View的中心坐标x **/
    private int centerX;
    /** View的中心坐标y **/
    private int centerY;

    private float maxLength;
    private float mInterpolatedTime;    //动画执行时长
    private float stretchDistance;      //动画时长
    private float moveDistance;         //当前速度
    private float cDistance;
    private float radius;               //半径
    private float c;                    //实际控制点差值
    private float blackMagic = 0.551915024494f;    //控制点常量
    private VPoint p2,p4;
    private HPoint p1,p3;

    public MoveCircle(Context context) {
        this(context, null, 0);
    }

    public MoveCircle(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MoveCircle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setColor(0xFFfe626d);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(1);
        mPaint.setAntiAlias(true);

        mPath = new Path();
        p2 = new VPoint();
        p4 = new VPoint();

        p1 = new HPoint();
        p3 = new HPoint();
    }

    //由于要计算位移步进距离所以重写onLayout 确定当前view移动位置
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        width = getWidth();
        height = getHeight();
        centerX = width / 2;
        centerY = height / 2;
        radius = 50;

        c = radius*blackMagic;
        stretchDistance = radius;
        moveDistance = radius*(3/5f);
        cDistance = c*0.45f;
        maxLength = width - radius -radius;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mPath.reset();
        canvas.translate(radius, radius);

        if(mInterpolatedTime>=0&&mInterpolatedTime<=0.2){
            model1(mInterpolatedTime);
        }else if(mInterpolatedTime>0.2&&mInterpolatedTime<=0.5){
            model2(mInterpolatedTime);
        }else if(mInterpolatedTime>0.5&&mInterpolatedTime<=0.8){
            model3(mInterpolatedTime);
        }else if(mInterpolatedTime>0.8&&mInterpolatedTime<=0.9){
            model4(mInterpolatedTime);
        }else if(mInterpolatedTime>0.9&&mInterpolatedTime<=1){
            model5(mInterpolatedTime);
        }

        float offset = maxLength*(mInterpolatedTime-0.2f);
        offset = offset>0?offset:0;
        p1.adjustAllX(offset);
        p2.adjustAllX(offset);
        p3.adjustAllX(offset);
        p4.adjustAllX(offset);

        mPath.moveTo(p1.x,p1.y);
        mPath.cubicTo(p1.right.x, p1.right.y, p2.bottom.x, p2.bottom.y, p2.x,p2.y);
        mPath.cubicTo(p2.top.x, p2.top.y, p3.right.x, p3.right.y, p3.x,p3.y);
        mPath.cubicTo(p3.left.x, p3.left.y, p4.top.x, p4.top.y, p4.x,p4.y);
        mPath.cubicTo(p4.bottom.x,p4.bottom.y,p1.left.x,p1.left.y,p1.x,p1.y);

        canvas.drawPath(mPath,mPaint);
    }

    private void model0(){
        p1.setY(radius);
        p3.setY(-radius);
        p3.x = p1.x = 0;
        p3.left.x = p1.left.x = -c;
        p3.right.x = p1.right.x = c;

        p2.setX(radius);
        p4.setX(-radius);
        p2.y = p4.y = 0;
        p2.top.y =  p4.top.y = -c;
        p2.bottom.y = p4.bottom.y = c;
    }

    private void model1(float time){//0~0.sf2
        model0();

        p2.setX(radius+stretchDistance*time*5);
    }

    private void model2(float time){//0.sf2~0.5
        model1(0.2f);
        time = (time - 0.2f) * (10f / 3);
        p1.adjustAllX(stretchDistance / 2 * time);
        p3.adjustAllX(stretchDistance/2 * time );
        p2.adjustY(cDistance * time);
        p4.adjustY(cDistance * time);
    }

    private void model3(float time){//0.5~0.8
        model2(0.5f);
        time = (time - 0.5f) * (10f / 3);
        p1.adjustAllX(stretchDistance / 2 * time);
        p3.adjustAllX(stretchDistance / 2 * time);
        p2.adjustY(-cDistance * time);
        p4.adjustY(-cDistance * time);

        p4.adjustAllX(stretchDistance / 2 * time);

    }

    private void model4(float time){//0.8~0.9
        model3(0.8f);
        time = (time - 0.8f) * 10;
        p4.adjustAllX(stretchDistance / 2 * time);
    }

    private void model5(float time){
        model4(0.9f);
        time = time - 0.9f;
        p4.adjustAllX((float) (Math.sin(Math.PI*time*10f)*(2/10f*radius)));
    }

    class VPoint{
        public float x;
        public float y;
        public PointF top = new PointF();
        public PointF bottom = new PointF();

        public void setX(float x){
            this.x = x;
            top.x = x;
            bottom.x = x;
        }

        //调整y坐标
        public void adjustY(float offset){
            top.y -= offset;
            bottom.y += offset;
        }

        //调整所有x坐标
        public void adjustAllX(float offset){
            this.x+= offset;
            top.x+= offset;
            bottom.x+=offset;
        }
    }

    class HPoint{
        public float x;
        public float y;
        public PointF left = new PointF();
        public PointF right = new PointF();

        public void setY(float y){
            this.y = y;
            left.y = y;
            right.y = y;
        }

        public void adjustAllX(float offset){
            this.x +=offset;
            left.x +=offset;
            right.x +=offset;
        }
    }


    private class MoveAnimation extends Animation {

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            mInterpolatedTime = interpolatedTime;
//            Log.i("mInterpolatedTime =" , mInterpolatedTime + "");
            postInvalidate();
        }
    }

    public void startAnimation() {
        mPath.reset();
        mInterpolatedTime = 0;
        MoveAnimation move = new MoveAnimation();
        move.setDuration(3000);
        move.setInterpolator(new AccelerateDecelerateInterpolator());
//        move.setRepeatCount(Animation.INFINITE);
//        move.setRepeatMode(Animation.REVERSE);
        startAnimation(move);
    }

}
