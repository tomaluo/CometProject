package com.mazing.com.cavasdemo;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

/**
 * @description: 复数可互相切换 tabview
 * @author: Toma
 * @date: 2018-01-12 10:25
 * @version: 1.0
 */

public class SwitchTabGroupView extends LinearLayout {

    private LinearLayout mLvTabView;
    private FrameLayout mFlBottomView;
    private View mDotView;

    private onItemSwitchListener mItemSwitchListener;

    private WeakHashMap<Integer,View> mTabMap = new WeakHashMap<>();

    /**
     * 当前选中项id
     */
    private int mCurrentId = 0;
    private int mTagId = -1;

    private List<String> mList = new ArrayList<>();

    private Context mContent;

    private int mTextSize;
    private float lineHeight, lineWeight ,tabHeight;

    public SwitchTabGroupView(Context context) {
        this(context,null);
    }

    public SwitchTabGroupView(Context context, AttributeSet attrs) {
        this(context,attrs, 0);
    }

    public SwitchTabGroupView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs){
        mContent = context;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.switch_tab_item, null);

        if(attrs != null){
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.SwitchTabGroupBtn);
            lineHeight = array.getDimension(R.styleable.SwitchTabGroupBtn_lineHeight, 1);
            tabHeight = array.getDimension(R.styleable.SwitchTabGroupBtn_tabHeight, 1);
            lineWeight = array.getDimension(R.styleable.SwitchTabGroupBtn_lineWidth, RelativeLayout.LayoutParams.MATCH_PARENT);
            mTextSize = array.getDimensionPixelSize(R.styleable.SwitchTabGroupBtn_textSize, mTextSize);
            array.recycle();
        }

        mLvTabView = (LinearLayout) view.findViewById(R.id.tab_item_view);
        mFlBottomView = (FrameLayout) view.findViewById(R.id.tab_item_bottom_view);

        mDotView = new View(mContent);
        mDotView.setBackgroundColor(Color.parseColor("#26BF81"));
        FrameLayout.LayoutParams viewParams = new FrameLayout.LayoutParams((int) lineWeight, (int) lineHeight);
        mDotView.setLayoutParams(viewParams);
        mFlBottomView.removeAllViews();
        mFlBottomView.addView(mDotView);

        addView(view);
    }

    private void changeData(){
        if(mList.size() > 0){
            for(int i = 0 ; i < mList.size() ; i++){
               if(mTabMap.get(i) != null){
                   TextView tabTextView = (TextView) mTabMap.get(i);
                   if(i == mCurrentId){
                       tabTextView.setTextColor(Color.parseColor("#354048"));
                       tabTextView.setTextSize(mTextSize);
                   }
                   else{
                       tabTextView.setTextColor(Color.parseColor("#666666"));
                       tabTextView.setTextSize(mTextSize);
                   }
               }
            }
        }
    }

    private void reSetView(){
        if(mList != null && mList.size() > 0){
            mLvTabView.removeAllViews();
            mTabMap.clear();
            for(int i = 0 ; i < mList.size() ; i++){
                String tabText = mList.get(i);
                TextView tv = new TextView(mContent);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, (int)tabHeight, 1.0f);
                tv.setLayoutParams(lp);
                tv.setText(tabText);
                if(i == mCurrentId){
                    tv.setTextColor(Color.parseColor("#354048"));
                    tv.setTextSize(mTextSize);
                }
                else{
                    tv.setTextColor(Color.parseColor("#666666"));
                    tv.setTextSize(mTextSize);
                }
                tv.setGravity(Gravity.CENTER);
                mLvTabView.addView(tv);
                tv.setTag(i);
                tv.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(v.getTag() != null){
                            mTagId = (int)v.getTag();
                            if(mTagId != mCurrentId){
                                AnimetMove(mCurrentId,mTagId);
                                if(mItemSwitchListener != null){
                                    mItemSwitchListener.onItemClick(mTagId);
                                }
                            }
                        }
                    }
                });

                mTabMap.put(i,tv);
            }
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        setBottomView();
    }

    public void AnimetMove(int starIndex, int endIndex){

        int priceWidth = getWidth() / mList.size();

        int starPos = priceWidth * (starIndex + 1) - priceWidth / 2;
        int endPos = priceWidth * (endIndex + 1) - priceWidth / 2 - (int)(lineWeight / 2);

        ValueAnimator valueAnimator = ValueAnimator.ofFloat(starPos, endPos);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        MoveAnimation zoomAnim = new MoveAnimation();
        valueAnimator.addUpdateListener(zoomAnim);
        valueAnimator.addListener(zoomAnim);
        valueAnimator.setDuration(250);
        valueAnimator.start();
    }

    class MoveAnimation implements ValueAnimator.AnimatorUpdateListener, Animator.AnimatorListener {

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            float current = (Float) animation.getAnimatedValue();
            mDotView.setTranslationX(current);
        }

        @Override
        public void onAnimationCancel(Animator animation) {
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            mCurrentId = mTagId;
            changeData();
        }

        @Override
        public void onAnimationRepeat(Animator animation) {
        }

        @Override
        public void onAnimationStart(Animator animation) {
        }

    }

    private void setBottomView(){
        if(mList.size() > 0){
            int currentIndex = ((getWidth() / mList.size()) * (mCurrentId + 1)) / 2;
            mDotView.setTranslationX(currentIndex - (int)(lineWeight / 2));
        }
    }

    public void setSelectItem(int id){
        AnimetMove(mCurrentId,id);
    }

    public void setViewData(List<String> list){
        mList.clear();
        mList = list;
        reSetView();
    }

    public void setItemSwitchListener(onItemSwitchListener listener){
        mItemSwitchListener = listener;
    }

    public interface onItemSwitchListener {
        void onItemClick(int postion);
    }
}
