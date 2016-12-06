package com.mazing.com.cavasdemo.view;

import android.content.Context;
import android.graphics.Point;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mazing.com.cavasdemo.R;

/**
 * Created by toma on 16/7/18.
 */
public class DVHdemoLayout extends LinearLayout {

    private TextView tv1;
    private TextView tv2;

    private ViewDragHelper mDragger;
    private ViewDragHelper.Callback callback;

    private Point initPointPosition = new Point();

    @Override
    protected void onFinishInflate() {
        tv1 = (TextView) findViewById(R.id.tv1);
        tv2 = (TextView) findViewById(R.id.tv2);
        super.onFinishInflate();
    }

    public DVHdemoLayout(Context context) {
        super(context);
    }

    public DVHdemoLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        callback = new DraggerCallBack();
        mDragger = ViewDragHelper.create(this, 1.0f, callback);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        initPointPosition.x = tv1.getLeft();
        initPointPosition.y = tv1.getTop();
    }

    class DraggerCallBack extends ViewDragHelper.Callback {

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return child == tv1;
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            //取得左边界的坐标
            final int leftBound = getPaddingLeft();
            //取得右边界的坐标
            final int rightBound = getWidth() - child.getWidth() - leftBound;
            //这个地方的含义就是 如果left的值 在leftbound和rightBound之间 那么就返回left
            //如果left的值 比 leftbound还要小 那么就说明 超过了左边界 那我们只能返回给他左边界的值
            //如果left的值 比rightbound还要大 那么就说明 超过了右边界，那我们只能返回给他右边界的值
            return Math.min(Math.max(left, leftBound), rightBound);
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            final int topBound = getPaddingTop();

            final int bottomBound = getHeight() - child.getHeight() - topBound;

            return Math.min(Math.max(top,topBound),bottomBound);
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            if(tv1 == releasedChild){
                mDragger.settleCapturedViewAt(initPointPosition.x,initPointPosition.y);
                invalidate();
            }
        }
    }

    @Override
    public void computeScroll() {
       if(mDragger.continueSettling(true))
           invalidate();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mDragger.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDragger.processTouchEvent(event);
        return true;
    }
}
