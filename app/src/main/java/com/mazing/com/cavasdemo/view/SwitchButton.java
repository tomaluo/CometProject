package com.mazing.com.cavasdemo.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.mazing.com.cavasdemo.R;

/**
 * 用于表现切换开关状态的控件
 * Created by toma on 17/6/14.
 */

public class SwitchButton extends View {

    private Paint mCircleWhitePaint = new Paint();   //白色圆画笔（全填充）
    private Paint mCircleWhitePadPaint = new Paint();  //白色圆边色画笔
    private Paint mBGPaint = new Paint();     //背景色画笔
    private Paint mBGPadPaint = new Paint();   //背景边色画笔

    private static int BGCOLOR = Color.WHITE;
    private static int CIRCLECLOOR = Color.BLUE;
    private static int LINECOLOR = Color.BLACK;
    private static int CLICKCOLOR = Color.BLUE;

    public static final int DEFAULT_THUMB_SIZE_DP = 20;

    private float mWidth;
    private float mHeight;
    private float mRation; //圆半径
    private float mStrokeWidth;
    private float mBgRation; // 背景弧度半径
    private boolean mIsOn;
    private float mCirclePointX; //圆形圆心全局坐标
    private int mStatus = 1; //当前状态   0 关闭   1 开启

    private GestureDetector mGestureDetector;
    private OnToggleViewClickListener mToggleViewClickListener;

    private ValueAnimator mProcessAnimator_toRight;
    private ValueAnimator mProcessAnimator_toLeft;

    public OnToggleViewClickListener getToggleViewClickListener() {
        return mToggleViewClickListener;
    }

    public void setToggleViewClickListener(OnToggleViewClickListener l) {
        this.mToggleViewClickListener = l;
    }

    public boolean getStatus() {
        return mIsOn;
    }

    public void setStatus(boolean isOn) {
        mIsOn = isOn;
        if(mIsOn){
            mCirclePointX = mWidth - 5 - mRation;
        }
        else{
            mCirclePointX = mRation + 5;
        }
        invalidate();
    }

    public void switchStatus() {
        mIsOn = !mIsOn;
//        invalidate();
    }

    public SwitchButton(Context context) {
        super(context, null, 0);
        init(context,null);
    }

    public SwitchButton(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        init(context,attrs);
    }

    public SwitchButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    private void init(Context context,AttributeSet attrs) {
        Resources res = getResources();
        mWidth = res.getDimension(R.dimen.switch_button_width);
        mHeight = res.getDimension(R.dimen.switch_button_height);
        mRation = res.getDimension(R.dimen.switch_button_ration);
        mStrokeWidth = res.getDimension(R.dimen.switch_button_stroke_width);
        mBgRation = res.getDimension(R.dimen.switch_button_bg_ration);
        mCirclePointX = mRation + 5;

        TypedArray ta = attrs == null ? null : getContext().obtainStyledAttributes(attrs, R.styleable.SwitchButton);
        if(ta != null){
            CIRCLECLOOR = ta.getColor(R.styleable.SwitchButton_CircleColor,CIRCLECLOOR);
            BGCOLOR = ta.getColor(R.styleable.SwitchButton_BGColor,BGCOLOR);
            LINECOLOR = ta.getColor(R.styleable.SwitchButton_LineColor,LINECOLOR);
            CLICKCOLOR = ta.getColor(R.styleable.SwitchButton_ClickColor,CLICKCOLOR);
        }

        mProcessAnimator_toRight = ValueAnimator.ofFloat(mRation + 5, mWidth - 5 - mRation).setDuration(250);
        mProcessAnimator_toRight.setInterpolator(new AccelerateDecelerateInterpolator());
        mProcessAnimator_toRight.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float value = (Float) valueAnimator.getAnimatedValue();
                mCirclePointX = value;
                invalidate();
            }
        });


        mProcessAnimator_toLeft = ValueAnimator.ofFloat(mWidth - 5 - mRation, mRation + 5 ).setDuration(250);
        mProcessAnimator_toLeft.setInterpolator(new AccelerateDecelerateInterpolator());
        mProcessAnimator_toLeft.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float value = (Float) valueAnimator.getAnimatedValue();
                mCirclePointX = value;
                invalidate();
            }
        });


        mCircleWhitePaint.setStyle(Paint.Style.FILL);
        mCircleWhitePaint.setAntiAlias(true);

        mCircleWhitePadPaint.setStyle(Paint.Style.STROKE);
        mCircleWhitePadPaint.setAntiAlias(true);
        mCircleWhitePadPaint.setStrokeWidth(mStrokeWidth);

        mBGPaint.setStyle(Paint.Style.FILL);
        mBGPaint.setAntiAlias(true);

        mBGPadPaint.setStyle(Paint.Style.STROKE);
        mBGPadPaint.setAntiAlias(true);
        mBGPadPaint.setStrokeWidth(mStrokeWidth);

        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                switchStatus();
                if (mToggleViewClickListener != null) {
                    mToggleViewClickListener.onToggleViewClick(mIsOn);
                    if(mIsOn){
                        mProcessAnimator_toRight.start();
                    }
                    else{
                        mProcessAnimator_toLeft.start();
                    }

                }
                return super.onSingleTapUp(e);
            }
        });

        setClickable(true);
    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension((int)mWidth + (getPaddingRight() - getPaddingLeft()),
                ((int)mHeight + (getPaddingBottom() - getPaddingTop())));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawBackGround(canvas);
        drawCircle(canvas);
    }

    private void drawBackGround(Canvas canvas){
        if(!mIsOn){
            mBGPaint.setColor(BGCOLOR);
            mBGPadPaint.setColor(LINECOLOR);
        }
        else {
            mBGPaint.setColor(CLICKCOLOR);
            mBGPadPaint.setColor(LINECOLOR);
        }

        RectF f = new RectF(mBgRation / 2, 0, mWidth - (mBgRation / 2), mHeight);
        canvas.drawRect(f,mBGPaint);

        f = new RectF(0, 0, mBgRation + 1, mHeight);
        canvas.drawArc(f, 90, 180 , true ,mBGPaint);

        f = new RectF(mWidth - mBgRation - 1, 0, mWidth, mHeight);
        canvas.drawArc(f, 90, -180 , true ,mBGPaint);

        canvas.drawLine(mBgRation / 2, 0 , mWidth - (mBgRation / 2),0,mBGPadPaint);
        canvas.drawLine(mBgRation / 2, mHeight , mWidth - (mBgRation / 2), mHeight,mBGPadPaint);

        f = new RectF( 0, 0, mBgRation, mHeight);
        canvas.drawArc(f, 90, 180 , false ,mBGPadPaint);

        f = new RectF(mWidth - mBgRation, 0, mWidth, mHeight);
        canvas.drawArc(f, 90, -180 , false ,mBGPadPaint);
    }

    private void drawCircle(Canvas canvas){
        mCircleWhitePaint.setColor(CIRCLECLOOR);
        mCircleWhitePadPaint.setColor(LINECOLOR);
        if(!mIsOn){
            canvas.drawCircle(mCirclePointX , mHeight / 2, mRation, mCircleWhitePaint);
            canvas.drawCircle(mCirclePointX, mHeight / 2, mRation, mCircleWhitePadPaint);
        }
        else {
            canvas.drawCircle(mCirclePointX , mHeight / 2, mRation, mCircleWhitePaint);
            canvas.drawCircle(mCirclePointX , mHeight / 2, mRation, mCircleWhitePadPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    public interface OnToggleViewClickListener {
        void onToggleViewClick(boolean isOn);
    }
}

