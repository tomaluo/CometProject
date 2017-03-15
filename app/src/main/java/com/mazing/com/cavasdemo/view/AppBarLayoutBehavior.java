package com.mazing.com.cavasdemo.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

/**
 * 用户联动特定view 一起做下滑拉伸的 Behavior
 * Created by user on 17/3/14.
 */

public class AppBarLayoutBehavior extends AppBarLayout.Behavior {
    private static final String TAG = "overScroll";   //标记需要跟随变动的目标view
    private static final float TARGET_HEIGHT = 500;   //目标view 最终高度
    private View mTargetView;   //目标view
    private int mTargetViewHeight; // 目标view 初始高度
    private int mParentViewHeight;  // 父控件高度
    private float mTotalDy;     // 总滑动的像素数
    private float mLastScale;   // 最终放大比例
    private int mLastBottom;    // AppBarLayout的最终Bottom值
    private boolean isAnimate;  // 是否正在进行位置复原动画

    public AppBarLayoutBehavior(){

    }

    public AppBarLayoutBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, AppBarLayout abl, int layoutDirection) {
        boolean handled = super.onLayoutChild(parent, abl, layoutDirection);

        if(mTargetView == null ){
            mTargetView = parent.findViewWithTag(TAG);
            initial(abl);
        }
        return handled;
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout parent, AppBarLayout child, View directTargetChild, View target, int nestedScrollAxes) {
        isAnimate = true;
        return super.onStartNestedScroll(parent, child, directTargetChild, target, nestedScrollAxes);
    }

    // 1.mTargetView不为null
    // 2.是向下滑动，dy<0表示向下滑动
    // 3.AppBarLayout已经完全展开，child.getBottom() >= mParentHeight
    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, int dx, int dy, int[] consumed) {
        if(mTargetView != null && ((dy < 0 && child.getBottom() >= mParentViewHeight) || (dy > 0 && child.getBottom() > mParentViewHeight))){
            //以上情况出现在 head 被拖动滚动并且没有超出边界的变动中的样式
            scale(child,target,dy);
        }
        else {
            super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed);
        }
    }

    private void scale(AppBarLayout abl, View target, int dy) {
        // 累减垂直方向上滑动的像素数
        mTotalDy += -dy;
        mTotalDy = Math.min(mTotalDy, TARGET_HEIGHT);

        // 计算目标View缩放比例，不能小于1
        mLastScale = Math.max(1f, 1f + mTotalDy / TARGET_HEIGHT);
        // 缩放目标View
        ViewCompat.setScaleX(mTargetView, mLastScale);
        ViewCompat.setScaleY(mTargetView, mLastScale);

        // 计算目标View缩放后变更的高度
        mLastBottom = mParentViewHeight + (int) (mTargetViewHeight / 2 * (mLastScale - 1));
        abl.setBottom(mLastBottom);
        target.setScrollY(0);
    }

    @Override
    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, AppBarLayout abl, View target) {
        recovery(abl);
        super.onStopNestedScroll(coordinatorLayout, abl, target);
    }

    @Override
    public boolean onNestedPreFling(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, float velocityX, float velocityY) {
        // 如果触发了快速滚动且垂直方向上速度大于100，则禁用动画
        if (velocityY > 100) {
            isAnimate = false;
        }
        return super.onNestedPreFling(coordinatorLayout, child, target, velocityX, velocityY);
    }

    private void recovery(final AppBarLayout abl) {
        //手放开了 使用动画平滑过度属性到初始位置
        if (mTotalDy > 0) {
            mTotalDy = 0;
            if (isAnimate) {
                final ValueAnimator anim = ValueAnimator.ofFloat(mLastScale, 1f).setDuration(200);
                anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float value = (float) animation.getAnimatedValue();
                        ViewCompat.setScaleX(mTargetView,value);
                        ViewCompat.setScaleY(mTargetView,value);
                        abl.setBottom((int) (mLastBottom - (mLastBottom - mParentViewHeight) * animation.getAnimatedFraction()));
                    }
                });
                anim.start();

            } else {
                ViewCompat.setScaleX(mTargetView, 1f);
                ViewCompat.setScaleY(mTargetView, 1f);
                abl.setBottom(mParentViewHeight);
            }
        }
    }

    private void initial(AppBarLayout abl) {
        abl.setClipChildren(false); // 设置AppBarLayout 可以超出默认大小
        mTargetViewHeight = mTargetView.getHeight();  //获取初始高度
        mParentViewHeight = abl.getHeight();          //获取初始高度
    }
}
