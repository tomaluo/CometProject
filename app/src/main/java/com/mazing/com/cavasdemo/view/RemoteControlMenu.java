package com.mazing.com.cavasdemo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * 支持手指触碰的 圆型自定义menu(类似空调遥控器)
 * ps.使用时务必关闭硬件加速 否则坐标变换会出现问题
 * Created by toma on 2017/8/10.
 */

public class RemoteControlMenu extends View {

    private Paint mPaint;
    private Path up_p, down_p, left_p, right_p, center_p;   //对应部件的绘画路径
    private Region up, down, left, right, center;           //对应部件的区域

    private Matrix mMatrix = null;  //坐标变换矩阵 用于将手指触碰坐标变换到绘图坐标

    private final int CENTER = 0;
    private final int UP = 1;
    private final int RIGHT = 2;
    private final int DOWN = 3;
    private final int LEFT = 4;

    int touchFlag = -1;    //当前点击的区域
    int currentFlag = -1;  //当前命中的操作区域

    int mViewHight;
    int mViewWidth;

    int mDefauColor = 0xFF4E5268;
    int mTouchedColor = 0xFFDF9C81;

    MenuListener mListener = null;

    public RemoteControlMenu(Context context) {
        this(context, null);
    }

    public RemoteControlMenu(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(mDefauColor);

        center_p = new Path();
        up_p = new Path();
        down_p = new Path();
        left_p = new Path();
        right_p = new Path();

        center = new Region();
        up = new Region();
        down = new Region();
        left = new Region();
        right = new Region();

        mMatrix = new Matrix();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mViewWidth = w;
        mViewHight = h;

        mMatrix.reset();

        // 注意这个区域的大小
        Region globalRegion = new Region(-w, -h, w, h);
        int minWidth = w > h ? h : w;
        minWidth *= 0.8;

        int br = minWidth / 2;
        RectF bigCircle = new RectF(-br, -br, br, br);

        int sr = minWidth / 4;
        RectF smallCircle = new RectF(-sr, -sr, sr, sr);

        float bigSweepAngle = 84;
        float smallSweepAngle = -80;

        center_p.addCircle(0, 0, 0.2f * minWidth, Path.Direction.CW);
        center.setPath(center_p, globalRegion);   // setPath(Path path, Region clip)：根据路径的区域与某区域的交集，构造出新区域

        right_p.addArc(bigCircle, -40, bigSweepAngle);
        right_p.arcTo(smallCircle, 40, smallSweepAngle);
        right_p.close();
        right.setPath(right_p, globalRegion);

        down_p.addArc(bigCircle, 50, bigSweepAngle);
        down_p.arcTo(smallCircle, 130, smallSweepAngle);
        down_p.close();
        down.setPath(down_p, globalRegion);

        left_p.addArc(bigCircle, 140, bigSweepAngle);
        left_p.arcTo(smallCircle, 220, smallSweepAngle);
        left_p.close();
        left.setPath(left_p, globalRegion);

        up_p.addArc(bigCircle, 230, bigSweepAngle);
        up_p.arcTo(smallCircle, 310, smallSweepAngle);
        up_p.close();
        up.setPath(up_p, globalRegion);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.translate(mViewWidth / 2, mViewHight / 2);

        //获取 canvs 的逆矩阵（用于计算点击坐标的变换坐标）
        if (mMatrix.isIdentity()) {
            canvas.getMatrix().invert(mMatrix);
        }

        canvas.drawPath(center_p, mPaint);
        canvas.drawPath(right_p, mPaint);
        canvas.drawPath(down_p, mPaint);
        canvas.drawPath(left_p, mPaint);
        canvas.drawPath(up_p, mPaint);

        mPaint.setColor(mTouchedColor);
        switch (currentFlag) {
            case CENTER:
                canvas.drawPath(center_p, mPaint);
                break;
            case UP:
                canvas.drawPath(up_p, mPaint);
                break;
            case LEFT:
                canvas.drawPath(left_p, mPaint);
                break;
            case RIGHT:
                canvas.drawPath(right_p, mPaint);
                break;
            case DOWN:
                canvas.drawPath(down_p, mPaint);
                break;
            case -1:
                break;
        }
        mPaint.setColor(mDefauColor);
    }

    // 获取当前触摸点在哪个区域
    int getTouchedPath(int x, int y) {
        if (center.contains(x, y)) {
            return 0;
        } else if (up.contains(x, y)) {
            return 1;
        } else if (right.contains(x, y)) {
            return 2;
        } else if (down.contains(x, y)) {
            return 3;
        } else if (left.contains(x, y)) {
            return 4;
        }
        return -1;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float[] pts = new float[2];
        pts[0] = event.getRawX();
        pts[1] = event.getRawY();

        mMatrix.mapPoints(pts);

        int x = (int) pts[0];
        int y = (int) pts[1];

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                touchFlag = getTouchedPath(x, y);
                currentFlag = touchFlag;
                break;
            case MotionEvent.ACTION_MOVE:
                currentFlag = getTouchedPath(x, y);
                break;
            case MotionEvent.ACTION_UP:
                currentFlag = getTouchedPath(x, y);
                // 如果手指按下区域和抬起区域相同且不为空，则判断点击事件
                if (currentFlag == touchFlag && currentFlag != -1 && mListener != null) {
                    mListener.onClick(currentFlag);
                }
                touchFlag = currentFlag = -1;
                break;
            case MotionEvent.ACTION_CANCEL:
                currentFlag = -1;
                touchFlag = -1;
                break;
        }

        invalidate();
        return true;
    }

    public void setListener(MenuListener listener) {
        mListener = listener;
    }

    public interface MenuListener {
        void onClick(int currentFlag);
    }
}
