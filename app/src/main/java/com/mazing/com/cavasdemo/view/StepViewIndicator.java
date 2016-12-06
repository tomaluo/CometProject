package com.mazing.com.cavasdemo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.mazing.com.cavasdemo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by toma on 16/6/27.
 */
public class StepViewIndicator extends View {

    private Paint mCompletePaint;
    private Paint mUnCompletePaint;

    private Path mPath;

    private Drawable mCompleteIcon;//完成的默认图片
    private Drawable mAttentionIcon;//正在进行的默认图片
    private Drawable mDefaultIcon;//默认的背景图

    private int mwidth;
    private int mHight;
    private int defultHight = 150;  //默认高度
    private int mStepCount = 5;

    private int CompleteColor = Color.WHITE;
    private int UnCompleteColor = Color.WHITE;

    private float mCompletedLineHeight;//完成线的高度     definition completed line height
    private float mCircleRadius;//圆的半径  definition circle radius
    private float mLinePadding;//两条连线之间的间距  definition the spacing between the two circles

    private float mCenterY;//该view的Y轴中间位置     definition view centerY position
    private float mLeftY;//左上方的Y位置  definition rectangle LeftY position
    private float mRightY;//右下方的位置  definition rectangle RightY position
    private int mComplectingPosition = 3; //当前完成进度

    private PathEffect mEffects;

    private OnDrawIndicatorListener mOnDrawListener;    //回调接口

    private List<Float> mCircleCenterPointPositionList;//定义所有圆的圆心点位置的集合

    public void setOnDrawListener(OnDrawIndicatorListener onDrawListener) {
       mOnDrawListener = onDrawListener;
    }

    public StepViewIndicator(Context context) {
        super(context);
    }

    public StepViewIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void init(){
        mEffects = new DashPathEffect(new float[]{8, 8, 8, 8}, 1); // 虚线筛选器
        mCircleCenterPointPositionList = new ArrayList<>();
        mPath = new Path();

        mCompletePaint = new Paint();
        mCompletePaint.setAntiAlias(true);
        mCompletePaint.setStrokeWidth(2);
        mCompletePaint.setColor(CompleteColor);
        mCompletePaint.setStyle(Paint.Style.FILL);

        mUnCompletePaint = new Paint();
        mUnCompletePaint.setAntiAlias(true);
        mUnCompletePaint.setStrokeWidth(2);
        mUnCompletePaint.setColor(UnCompleteColor);
        mUnCompletePaint.setPathEffect(mEffects);
        mUnCompletePaint.setStyle(Paint.Style.STROKE);

        //已经完成线的宽高 set mCompletedLineHeight
        mCompletedLineHeight = 0.05f * defultHight;
        //圆的半径  set mCircleRadius
        mCircleRadius = 0.28f * defultHight;
        //线与线之间的间距    set mLinePadding
        mLinePadding = 0.85f * defultHight;

        mCompleteIcon = ContextCompat.getDrawable(getContext(), R.drawable.complted);//已经完成的icon
        mAttentionIcon = ContextCompat.getDrawable(getContext(), R.drawable.attention);//正在进行的icon
        mDefaultIcon = ContextCompat.getDrawable(getContext(), R.drawable.default_icon);//未完成的icon
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mwidth = defultHight * 2;
        if(MeasureSpec.UNSPECIFIED != MeasureSpec.getMode(widthMeasureSpec)){
            mwidth = MeasureSpec.getSize(widthMeasureSpec);
        }

        mHight = defultHight;
        if(MeasureSpec.UNSPECIFIED != MeasureSpec.getMode(heightMeasureSpec)){
            mHight = MeasureSpec.getSize(heightMeasureSpec);
        }
        setMeasuredDimension(mwidth, mHight);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        //获取中间的高度,目的是为了让该view绘制的线和圆在该view垂直居中   get view centerY，keep current stepview center vertical
        mCenterY = 0.5f * getHeight();
        //获取左上方Y的位置，获取该点的意义是为了方便画矩形左上的Y位置
        mLeftY = mCenterY - (mCompletedLineHeight / 2);
        //获取右下方Y的位置，获取该点的意义是为了方便画矩形右下的Y位置
        mRightY = mCenterY + mCompletedLineHeight / 2;

        for(int i = 0; i < mStepCount; i++)
        {
            //先计算全部最左边的padding值（getWidth()-（圆形直径+两圆之间距离）*2）
            float paddingLeft = (getWidth() - mStepCount * mCircleRadius * 2 - (mStepCount - 1) * mLinePadding) / 2;
            //把圆心坐标写入数组保存
            mCircleCenterPointPositionList.add(paddingLeft + mCircleRadius + i * mCircleRadius * 2 + i * mLinePadding);
        }

        /**
         * set listener
         */
        if(mOnDrawListener!=null)
        {
            mOnDrawListener.ondrawIndicator();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(mOnDrawListener!=null)
        {
            mOnDrawListener.ondrawIndicator();
        }

        canvas.drawColor(Color.BLUE);
        //画完成线
        for(int i = 0; i < mCircleCenterPointPositionList.size() - 1 ; i++){

            //前一个 园坐标点
            final float preComplectedXPosition = mCircleCenterPointPositionList.get(i);
            //后一个 园坐标点
            final float afterCompectedxPosition = mCircleCenterPointPositionList.get(i + 1);

            if(i < mComplectingPosition){
                //当两个圆心中间为 完成线
                float left = preComplectedXPosition + mCircleRadius - 10; //左边坐标为 圆心加上半径
                float rgiht = afterCompectedxPosition - mCircleRadius + 10; //右边坐标为  圆心减去半径

                canvas.drawRect(left, mLeftY, rgiht , mRightY, mCompletePaint);
            }
            else{
                //当点到达未完成线
                mPath.moveTo(preComplectedXPosition + mCircleRadius, mCenterY);
                mPath.lineTo(afterCompectedxPosition - mCircleRadius, mCenterY);
                canvas.drawPath(mPath, mUnCompletePaint);
            }
        }

        //画圈
        for(int i = 0; i < mCircleCenterPointPositionList.size(); i++){
            final float currentComplectedXPosition = mCircleCenterPointPositionList.get(i);
            Rect rect = new Rect((int) (currentComplectedXPosition - mCircleRadius), (int) (mCenterY - mCircleRadius), (int) (currentComplectedXPosition + mCircleRadius), (int) (mCenterY + mCircleRadius)); //画圆的范围
            if(i < mComplectingPosition){
                //如果当前点已完成
                mCompleteIcon.setBounds(rect);
                mCompleteIcon.draw(canvas);
            }
            else if( i == mComplectingPosition && mCircleCenterPointPositionList.size() != 1){
                //目前的点是正在进行中的点
                mCompletePaint.setColor(Color.WHITE);
                canvas.drawCircle(currentComplectedXPosition, mCenterY, mCircleRadius * 1.1f, mCompletePaint);
                mAttentionIcon.setBounds(rect);
                mAttentionIcon.draw(canvas);
            }
            else{
                mDefaultIcon.setBounds(rect);
                mDefaultIcon.draw(canvas);
            }
        }
    }

    public void setSetpCount(int count){
        mStepCount = count;
        invalidate();
    }

    public void setCurrStep(int position){
        mComplectingPosition = position;
        invalidate();
    }

    public interface OnDrawIndicatorListener
    {
        void ondrawIndicator();
    }
}
