package com.mazing.com.cavasdemo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.mazing.com.cavasdemo.model.User;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by toma on 16/4/8.
 */
public class PieView extends View {

    private int[] mColors = {0xFFCCFF00, 0xFF6495ED, 0xFFE32636, 0xFF800000, 0xFF808000, 0xFFFF8C69, 0xFF808080,
            0xFFE6B800, 0xFF7CFC00};

    private float mStartAngle = 0;

    public List<User> mData = new ArrayList<>();

    public int mWitch;
    public int mHight;

    public Paint paint;

    public PieView(Context context){
        super(context);
    }

    public PieView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w,h,oldw,oldh);

        mWitch = w;
        mHight = h;
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);

        float currentStartAngle = mStartAngle;                    // 当前起始角度
        canvas.translate(mWitch / 2, mHight / 2);                // 将画布坐标原点移动到中心位置
        float r = (float) (Math.min(mWitch, mHight) / 2 * 0.8);  // 饼状图半径
        RectF rect = new RectF(-r, -r, r, r);

        for(int i = 0;i < mData.size(); i ++ ){
            User user = mData.get(i);
            paint.setColor(user.color);
            canvas.drawArc(rect, currentStartAngle, user.angle, true, paint);
            currentStartAngle += user.angle;
        }
    }

    public void setStarangle(int angle){
        mStartAngle = angle;
        invalidate();
    }

    public void setData(List<User> data){
        mData = data;

        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);

        initData();
        invalidate();
    }

    private void initData(){

        float suvalue = 0;
        for(int i = 0 ; i < mData.size(); i++){
            User user = mData.get(i);
            suvalue += user.value;       //计算数值和
            int j = i % mColors.length;       //设置颜色
            user.color = mColors[j];
        }

        for(int i = 0 ; i < mData.size(); i++){
            User user = mData.get(i);
            float value = user.value / suvalue;
            user.angle = value * 360;
            user.percentage = value;
        }
    }
}
