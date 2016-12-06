package com.mazing.com.cavasdemo.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.mazing.com.cavasdemo.R;
import com.mazing.com.cavasdemo.unit.Units;

import java.util.logging.LogRecord;

/**
 * Created by toma on 16/4/18.
 */
public class PorterDuffXfermodeViewV2 extends View {

    private Context context;
    private Paint wavePaint,circePaint;
    private static final int WAVE_TRANS_SPEED = 4;

    private int mTotalWidth, mTotalHeight;   //控件长宽
    private int mCenterX, mCenterY;
    private int mSpeed;

    private Bitmap mSrcBitmap,mMaskBitmap;  //波浪图、遮罩图
    private Rect mSrcRect, mDestRect;       //波浪图显示与裁切范围

    private PorterDuffXfermode mPorterDuffXfermode;

    private Rect mMaskSrcRect, mMaskDestRect;
    private PaintFlagsDrawFilter mDrawFilter;

    private int mCurrentPosition;
    private HandlerThread thread;
    private Handler handler;

    public PorterDuffXfermodeViewV2(Context context) {
        super(context);
    }

    public PorterDuffXfermodeViewV2(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public PorterDuffXfermodeViewV2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void init(){
        circePaint = new Paint();
        // 防抖动
        circePaint.setDither(true);
        // 开启图像过滤
        circePaint.setFilterBitmap(true);

        wavePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        wavePaint.setDither(true);
        wavePaint.setColor(Color.RED);

        mMaskBitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.circle_500)).getBitmap();
        mSrcBitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.wave_2000)).getBitmap();

        mPorterDuffXfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);
        mSpeed = Units.convertDIP2PX(context, WAVE_TRANS_SPEED);
        mDrawFilter = new PaintFlagsDrawFilter(Paint.ANTI_ALIAS_FLAG, Paint.DITHER_FLAG);

        thread = new HandlerThread("MyHandlerThread");
        thread.start();
        handler = new Handler(thread.getLooper()){
            @Override
            public void handleMessage(Message msg) {
                // Do action based on this message
                switch (msg.what){
                    case 1:
                        mCurrentPosition += mSpeed;
                        if(mCurrentPosition > mSrcBitmap.getWidth())
                            mCurrentPosition = 0;

                        postInvalidate();

                        handler.sendEmptyMessageDelayed(1, 30);
                        break;
                }
            }
        };

        handler.sendEmptyMessage(1);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w,h,oldw,oldh);

        mTotalWidth = w;
        mTotalHeight = h;

        mCenterX = mTotalWidth / 2;
        mCenterY = mTotalHeight / 2;

        mSrcRect = new Rect();                                  //波浪裁切区域
        mDestRect = new Rect(0, 0, mTotalWidth, mTotalHeight);  //波浪显示区域

        int maskWidth = mMaskBitmap.getWidth();
        int maskHeight = mMaskBitmap.getHeight();
        mMaskSrcRect = new Rect(0, 0, maskWidth, maskHeight);       //圆形裁切区域
        mMaskDestRect = new Rect(0, 0, mTotalWidth, mTotalHeight);  //圆形显示区域
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 从canvas层面去除锯齿
        canvas.setDrawFilter(mDrawFilter);
        canvas.drawColor(Color.TRANSPARENT); //画布设置为透明÷

        /*
         * 将绘制操作保存到新的图层
         */
        int sc = canvas.saveLayer(0, 0, mTotalWidth, mTotalHeight, null, Canvas.ALL_SAVE_FLAG);

        mSrcRect.set(mCurrentPosition,0,mCurrentPosition + mCenterX,mTotalHeight);
        canvas.drawBitmap(mSrcBitmap,mSrcRect,mDestRect,wavePaint);                 //从使用的部分拆切目标波浪图像并绘制到控件上

        // 设置图像的混合模式
        circePaint.setXfermode(mPorterDuffXfermode);
        // 绘制遮罩圆
        canvas.drawBitmap(mMaskBitmap, mMaskSrcRect, mMaskDestRect,
                circePaint);
        circePaint.setXfermode(null);
        canvas.restoreToCount(sc);
    }
}
