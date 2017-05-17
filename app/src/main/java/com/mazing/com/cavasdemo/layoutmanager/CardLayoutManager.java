package com.mazing.com.cavasdemo.layoutmanager;

import android.support.annotation.NonNull;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by toma on 17/5/17.
 * 自定义卡片layoutmanager
 */

public class CardLayoutManager extends RecyclerView.LayoutManager {

    private RecyclerView mRecyclerView;
    private ItemTouchHelper mItemTouchHelper;
    private int maxCount = 4;   //最大显示个数
    private float DEFAULT_SCALE = 0.1f;  //默认坐标缩放值
    private float DEFAULT_TRANSLATE_Y = 14f;  //默认坐标偏移值

    public CardLayoutManager(@NonNull RecyclerView recyclerView, @NonNull ItemTouchHelper itemTouchHelper) {
        this.mRecyclerView = checkIsNull(recyclerView);
        this.mItemTouchHelper = checkIsNull(itemTouchHelper);
    }

    //检查对象是否为空
    private <T> T checkIsNull(T t) {
        if (t == null) {
            throw new NullPointerException();
        }
        return t;
    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }


    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        detachAndScrapAttachedViews(recycler);  //回收所有view
        int itemCount = getItemCount();
        // 当数据源个数大于最大显示数时
        if(itemCount > maxCount){
            //从数据最末尾开始取view，才能让最后面的view排在最上面
            for (int position = maxCount; position >= 0; position--) {
                View view = recycler.getViewForPosition(position);
                addView(view);
                measureChildWithMargins(view, 0, 0);
                // getDecoratedMeasuredWidth(view) 可以得到 Item View 的宽度
                // 所以 widthSpace 就是除了 Item View 剩余的值
                int widthSpace = getWidth() - getDecoratedMeasuredWidth(view);
                int heightSpace = getHeight() - getDecoratedMeasuredHeight(view);

                //把view 放在居中的位置
                layoutDecorated(view, widthSpace / 2, heightSpace / 2,
                        widthSpace / 2 + getDecoratedMeasuredWidth(view),
                        heightSpace / 2 + getDecoratedMeasuredHeight(view));

                if(position == maxCount){
                    //每一层都会在长宽上比下一层缩小一个固定的比值，用于实现层级效果
                    view.setScaleX(1 - (position - 1) * DEFAULT_SCALE);
                    view.setScaleY(1 - (position - 1) * DEFAULT_SCALE);
                    view.setTranslationY((position - 1) * view.getMeasuredHeight() / DEFAULT_TRANSLATE_Y);  //纵向上出现一个位置的差异
                }
                else if(position > 0){
                    view.setScaleX(1 - position * DEFAULT_SCALE);
                    view.setScaleY(1 - position * DEFAULT_SCALE);
                    view.setTranslationY(position  * view.getMeasuredHeight() / DEFAULT_TRANSLATE_Y);
                }
                else {
                    view.setOnTouchListener(mOnTouchListener);
                }
            }
        }
        else {
            // 当数据源个数小于或等于最大显示数时
            for (int position = itemCount - 1; position >= 0; position--) {
                final View view = recycler.getViewForPosition(position);
                addView(view);
                measureChildWithMargins(view, 0, 0);
                int widthSpace = getWidth() - getDecoratedMeasuredWidth(view);
                int heightSpace = getHeight() - getDecoratedMeasuredHeight(view);
                // recyclerview 布局
                layoutDecorated(view, widthSpace / 2, heightSpace / 2,
                        widthSpace / 2 + getDecoratedMeasuredWidth(view),
                        heightSpace / 2 + getDecoratedMeasuredHeight(view));

                if (position > 0) {
                    view.setScaleX(1 - position * DEFAULT_SCALE);
                    view.setScaleY(1 - position * DEFAULT_SCALE);
                    view.setTranslationY(position * view.getMeasuredHeight() / DEFAULT_TRANSLATE_Y);
                } else {
                    view.setOnTouchListener(mOnTouchListener);
                }
            }
        }

    }

    private View.OnTouchListener mOnTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            RecyclerView.ViewHolder childViewHolder = mRecyclerView.getChildViewHolder(v);
            if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                mItemTouchHelper.startSwipe(childViewHolder);
            }
            return false;
        }
    };
}
