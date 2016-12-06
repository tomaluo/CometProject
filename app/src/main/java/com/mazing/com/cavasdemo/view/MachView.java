package com.mazing.com.cavasdemo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ImageView;

import com.mazing.com.cavasdemo.R;

import java.util.Random;

/**
 * Created by toma on 16/11/29.
 */

public class MachView extends ImageView{

    private int mHeight;   //控件高
    private int mWidth;    //控件宽

    //验证码滑块的宽高
    private int mMachWidth;
    private int mMachHeight;

    //验证码的左上角(起点)的x y
    private int mCaptchaX;
    private int mCaptchaY;

    //验证的误差允许值
    private float mMatchDeviation;

    private PorterDuffXfermode mPorterDuffXfermode;

    private Random mRandom;
    private Paint mPaint;

    //滑块Bitmap
    private Bitmap mMaskBitmap;
    private Paint mMaskPaint;

    //阴影
    private Paint mMaskShadowPaint;
    private Bitmap mMaskShadowBitmap;

    //滑块的位移
    private int mDragerOffset;

    //是否处于验证模式，在验证成功后 为false，其余情况为true
    private boolean isMatchMode;

    //验证码 阴影、抠图的Path
    private Path mCaptchaPath;

    public final String TAG = MachView.class.getName();

    public MachView(Context context) {
        super(context);
        init(context,null,0);
    }

    public MachView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs,0);
    }

    public MachView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs,defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr){
        mMatchDeviation = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, getResources().getDisplayMetrics());

        int defaultSize = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()); //按照屏幕密度尺寸转换

        mMachWidth = defaultSize;
        mMachHeight = defaultSize;

        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SwipeCaptchaView, defStyleAttr, 0);
        int n = ta.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = ta.getIndex(i);
            if (attr == R.styleable.SwipeCaptchaView_captchaHeight) {
                mMachHeight = (int) ta.getDimension(attr, defaultSize);
            } else if (attr == R.styleable.SwipeCaptchaView_captchaWidth) {
                mMachWidth = (int) ta.getDimension(attr, defaultSize);
            } else if (attr == R.styleable.SwipeCaptchaView_matchDeviation) {
                mMatchDeviation = ta.getDimension(attr, mMatchDeviation);
            }
        }
        ta.recycle();

        mRandom = new Random(System.nanoTime()); //获得随机数
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG); //防抖动与抗锯齿
        mPaint.setColor(0x77000000);
        // 设置画笔遮罩滤镜
        mPaint.setMaskFilter(new BlurMaskFilter(20, BlurMaskFilter.Blur.SOLID));

        mPorterDuffXfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
        mMaskPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);

        // 实例化阴影画笔
        mMaskShadowPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mMaskShadowPaint.setColor(Color.BLACK);
        mMaskShadowPaint.setMaskFilter(new BlurMaskFilter(10, BlurMaskFilter.Blur.SOLID));

        mCaptchaPath = new Path();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mWidth = w;
        mHeight = h;

        post(new Runnable() {
            @Override
            public void run() {
                createCaptcha();
            }
        });
    }

    //创建验证码区域
    private void createCaptcha(){
        if (getDrawable() != null) {
            createCaptchaPath();
            craeteMask();
            invalidate();
        }
    }

    //生成验证码path
    private void createCaptchaPath(){
       int gap = mMachWidth / 3;

        //随机生成验证码阴影左上角 x y 点，
        mCaptchaX = mRandom.nextInt(mWidth - mMachWidth - gap);
        mCaptchaY = mRandom.nextInt(mHeight - mMachHeight - gap);

        //在控件内部选定一个随机生成的坐标作为抠图path的起点坐标

        mCaptchaPath.reset();
        mCaptchaPath.lineTo(0, 0);

        //从左上角开始 绘制一个不规则的阴影
        mCaptchaPath.moveTo(mCaptchaX, mCaptchaY);//左上角
        mCaptchaPath.lineTo(mCaptchaX + gap, mCaptchaY);
        //draw一个随机凹凸的圆
        drawPartCircle(new PointF(mCaptchaX + gap, mCaptchaY),
                new PointF(mCaptchaX + gap * 2, mCaptchaY),
                mCaptchaPath, mRandom.nextBoolean());


        mCaptchaPath.lineTo(mCaptchaX + mMachWidth, mCaptchaY);//右上角
        mCaptchaPath.lineTo(mCaptchaX + mMachWidth, mCaptchaY + gap);
        //draw一个随机凹凸的圆
        drawPartCircle(new PointF(mCaptchaX + mMachWidth, mCaptchaY + gap),
                new PointF(mCaptchaX + mMachWidth, mCaptchaY + gap * 2),
                mCaptchaPath, mRandom.nextBoolean());


        mCaptchaPath.lineTo(mCaptchaX + mMachWidth, mCaptchaY + mMachHeight);//右下角
        mCaptchaPath.lineTo(mCaptchaX + mMachWidth - gap, mCaptchaY + mMachHeight);
        //draw一个随机凹凸的圆
        drawPartCircle(new PointF(mCaptchaX + mMachWidth - gap, mCaptchaY + mMachHeight),
                new PointF(mCaptchaX + mMachWidth - gap * 2, mCaptchaY + mMachHeight),
                mCaptchaPath, mRandom.nextBoolean());


        mCaptchaPath.lineTo(mCaptchaX, mCaptchaY + mMachHeight);//左下角
        mCaptchaPath.lineTo(mCaptchaX, mCaptchaY + mMachHeight - gap);
        //draw一个随机凹凸的圆
        drawPartCircle(new PointF(mCaptchaX, mCaptchaY + mMachHeight - gap),
                new PointF(mCaptchaX, mCaptchaY + mMachHeight - gap * 2),
                mCaptchaPath, mRandom.nextBoolean());

        mCaptchaPath.close(); //闭合已经完成的不规则样式图形

        /*        mCaptchaPath.lineTo(mCaptchaX + gap, mCaptchaY);
        //画出凹凸 由于是多段Path 无法闭合，简直阿西吧
        int r = mWidth / 2 - gap;
        RectF oval = new RectF(mCaptchaX + gap, mCaptchaY - (r), mCaptchaX + gap + r * 2, mCaptchaY + (r));
        mCaptchaPath.arcTo(oval, 180, 180);*/
    }

    //生成滑块
    private void craeteMask() {
        mMaskBitmap = getMaskBitmap(((BitmapDrawable) getDrawable()).getBitmap(), mCaptchaPath);
        //滑块阴影
        mMaskShadowBitmap = mMaskBitmap.extractAlpha();
        //拖动的位移重置
        mDragerOffset = 0;
        //isDrawMask  绘制失败闪烁动画用
//        isDrawMask = true;
    }

    //抠图
    private Bitmap getMaskBitmap(Bitmap mBitmap, Path mask) {
        //以控件宽高 create一块bitmap
        Bitmap tempBitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888);
        //把创建的bitmap作为画板
        Canvas mCanvas = new Canvas(tempBitmap);
        // 抗锯齿
        mCanvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        //绘制用于抠图的图形
        mCanvas.drawPath(mask, mMaskPaint);
        //设置遮罩模式(图像混合模式)
        mMaskPaint.setXfermode(mPorterDuffXfermode);
        //★考虑到scaleType等因素，要用Matrix对Bitmap进行缩放
        mCanvas.drawBitmap(mBitmap, getImageMatrix(), mMaskPaint);
        mMaskPaint.setXfermode(null);
        return tempBitmap;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mCaptchaPath != null) {
            canvas.drawPath(mCaptchaPath, mPaint);
        }

        if (null != mMaskBitmap && null != mMaskShadowBitmap) {
            // 先绘制阴影
            canvas.drawBitmap(mMaskShadowBitmap, -mCaptchaX + mDragerOffset, 0, mMaskShadowPaint);
            canvas.drawBitmap(mMaskBitmap, -mCaptchaX + mDragerOffset, 0, null);
        }
    }

    /**
     * 设置当前滑动值
     * @param value
     */
    public void setCurrentSwipeValue(int value) {
        mDragerOffset = value;
        invalidate();
    }

    /**
     * 最大可滑动值
     * @return
     */
    public int getMaxSwipeValue() {
        //返回控件宽度
        return mWidth - mMachWidth;
    }

    public static void drawPartCircle(PointF start, PointF end, Path path, boolean outer) {
        float c = 0.551915024494f;
        //中点
        PointF middle = new PointF(start.x + (end.x - start.x) / 2, start.y + (end.y - start.y) / 2);
        //半径
        float r1 = (float) Math.sqrt(Math.pow((middle.x - start.x), 2) + Math.pow((middle.y - start.y), 2));
        //gap值
        float gap1 = r1 * c;

        if (start.x == end.x) {
            //绘制竖直方向的

            //是否是从上到下
            boolean topToBottom = end.y - start.y > 0 ? true : false;
            //以下是我写出了所有的计算公式后推的，不要问我过程，只可意会。
            int flag;//旋转系数
            if (topToBottom) {
                flag = 1;
            } else {
                flag = -1;
            }
            if (outer) {
                //凸的 两个半圆
                path.cubicTo(start.x + gap1 * flag, start.y,
                        middle.x + r1 * flag, middle.y - gap1 * flag,
                        middle.x + r1 * flag, middle.y);
                path.cubicTo(middle.x + r1 * flag, middle.y + gap1 * flag,
                        end.x + gap1 * flag, end.y,
                        end.x, end.y);
            } else {
                //凹的 两个半圆
                path.cubicTo(start.x - gap1 * flag, start.y,
                        middle.x - r1 * flag, middle.y - gap1 * flag,
                        middle.x - r1 * flag, middle.y);
                path.cubicTo(middle.x - r1 * flag, middle.y + gap1 * flag,
                        end.x - gap1 * flag, end.y,
                        end.x, end.y);
            }
        } else {
            //绘制水平方向的

            //是否是从左到右
            boolean leftToRight = end.x - start.x > 0 ? true : false;
            //以下是我写出了所有的计算公式后推的，不要问我过程，只可意会。
            int flag;//旋转系数
            if (leftToRight) {
                flag = 1;
            } else {
                flag = -1;
            }
            if (outer) {
                //凸 两个半圆
                path.cubicTo(start.x, start.y - gap1 * flag,
                        middle.x - gap1 * flag, middle.y - r1 * flag,
                        middle.x, middle.y - r1 * flag);
                path.cubicTo(middle.x + gap1 * flag, middle.y - r1 * flag,
                        end.x, end.y - gap1 * flag,
                        end.x, end.y);
            } else {
                //凹 两个半圆
                path.cubicTo(start.x, start.y + gap1 * flag,
                        middle.x - gap1 * flag, middle.y + r1 * flag,
                        middle.x, middle.y + r1 * flag);
                path.cubicTo(middle.x + gap1 * flag, middle.y + r1 * flag,
                        end.x, end.y + gap1 * flag,
                        end.x, end.y);
            }


/*
            没推导之前的公式在这里
            if (start.x < end.x) {
                if (outer) {
                    //上左半圆 顺时针
                    path.cubicTo(start.x, start.y - gap1,
                            middle.x - gap1, middle.y - r1,
                            middle.x, middle.y - r1);
                    //上右半圆:顺时针
                    path.cubicTo(middle.x + gap1, middle.y - r1,
                            end.x, end.y - gap1,
                            end.x, end.y);
                } else {
                    //下左半圆 逆时针
                    path.cubicTo(start.x, start.y + gap1,
                            middle.x - gap1, middle.y + r1,
                            middle.x, middle.y + r1);
                    //下右半圆 逆时针
                    path.cubicTo(middle.x + gap1, middle.y + r1,
                            end.x, end.y + gap1,
                            end.x, end.y);
                }
            } else {
                if (outer) {
                    //下右半圆 顺时针
                    path.cubicTo(start.x, start.y + gap1,
                            middle.x + gap1, middle.y + r1,
                            middle.x, middle.y + r1);
                    //下左半圆 顺时针
                    path.cubicTo(middle.x - gap1, middle.y + r1,
                            end.x, end.y + gap1,
                            end.x, end.y);
                }
            }*/
        }
    }
}
