package com.mazing.com.cavasdemo.layoutmanager;

import android.graphics.Rect;

import android.support.v4.util.SparseArrayCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by user on 2017/12/13.
 */

public class AutoMatchLayoutManager extends RecyclerView.LayoutManager {

    private int mDecorationMeasureWidth;
    private int mDecorationMeasureHeight;

    private int offsetHeight, mVerticalOffset, mTotalHeight ,mResizeHeight;

    private Pool<Rect> mItemFrames;

    private int[] mMeasuredDimension = new int[2];

    public AutoMatchLayoutManager(RecyclerView.LayoutManager Layout) {
        mItemFrames = new Pool<>(new Pool.New<Rect>() {
            @Override
            public Rect get() {
                return new Rect();
            }
        });
    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
                , ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        super.onLayoutChildren(recycler, state);

        if (getItemCount() <= 0 || state.isPreLayout()) {
            return;
        }
        detachAndScrapAttachedViews(recycler);

        View firstView = recycler.getViewForPosition(0);
        addView(firstView);
        measureChildWithMargins(firstView, 0, 0);

        mDecorationMeasureWidth = getDecoratedMeasuredWidth(firstView);
        mDecorationMeasureHeight = getDecoratedMeasuredHeight(firstView);

        detachAndScrapView(firstView, recycler);

        for (int i = 0; i < getItemCount(); i++) {
            Rect item = mItemFrames.get(i);
            item.set(0, i * mDecorationMeasureHeight, mDecorationMeasureWidth, (i * mDecorationMeasureHeight) + mDecorationMeasureHeight);
            mResizeHeight += mDecorationMeasureHeight;
        }

        int totalHeight = getItemCount() * mDecorationMeasureHeight;
        mTotalHeight = Math.max(totalHeight,getVerticalSpace());

        fill(recycler, state);
    }

    private void fill(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (getItemCount() <= 0 || state.isPreLayout()) {
            return;
        }

        for (int i = 0; i < getChildCount(); i++) {
            final View view = getChildAt(i);
            detachAndScrapView(view,recycler);
        }

        //根据竖向偏移确定当前可以显示的区间范围
        Rect displayRect = new Rect(0, mVerticalOffset, getHorizontalSpace(), getVerticalSpace() + mVerticalOffset);

        offsetHeight = 0;
        for (int i = 0; i < getItemCount(); i++) {
            Rect frame = mItemFrames.get(i);
            if (Rect.intersects(displayRect, frame)) {
                //判断当前区间在目标区间内
                View scrap = recycler.getViewForPosition(i);
                addView(scrap);
                measureChildWithMargins(scrap, 0, 0);
                layoutDecorated(scrap, frame.left, frame.top - mVerticalOffset,
                        frame.right, frame.bottom - mVerticalOffset);

                offsetHeight = Math.abs(getDecoratedEnd(scrap) - getEndAfterPadding());
                if (offsetHeight <= 0) {
                    //如果填充过后的view 底部已经没有剩余空间则不需要继续请求新的view
                    return;
                }
            }
        }

//        SparseArray<View> viewCache = new SparseArray(getChildCount());
//        if (getChildCount() != 0) {
//
//            for (int i = 0; i < getChildCount(); i++) {
//                final View view = getChildAt(i);
//                viewCache.put(i, view);
//            }
//
//            for (int i = 0; i < viewCache.size(); i++) {
//                detachViewAt(i);
//            }
//        }
//
//        int position = 0;
//        offsetHeight = 0;
//        while (offsetHeight < getVerticalSpace()){
//            View nextView = viewCache.get(position);
//            if(nextView == null){
//                View scrap = recycler.getViewForPosition(position);
//                addView(scrap);
//                measureChildWithMargins(scrap, 0, 0);
//            }
//            else{
//                attachView(nextView);
//                viewCache.remove(position);
//            }
//
//            position++;
//            offsetHeight += mDecorationMeasureHeight;
//        }
//
//        for(int i = 0 ; i < viewCache.size(); i++){
//            recycler.recycleView(viewCache.get(i));
//        }
    }

    @Override
    public boolean canScrollVertically() {
        return true;
    }

    @Override
    public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int widthSpec, int heightSpec) {
        final int widthMode = View.MeasureSpec.getMode(widthSpec);
        final int heightMode = View.MeasureSpec.getMode(heightSpec);
        final int widthSize = View.MeasureSpec.getSize(widthSpec);
        final int heightSize = View.MeasureSpec.getSize(heightSpec);
        int width = 0;
        int height = 0;

        for (int i = 0; i < getItemCount(); i++) {
            try {
                measureScrapChild(recycler, i,
                        widthSpec,
                        View.MeasureSpec.makeMeasureSpec(i, View.MeasureSpec.UNSPECIFIED),
                        mMeasuredDimension);
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }

//            if (getOrientation() == HORIZONTAL) {
//                width = width + mMeasuredDimension[0];
//                if (i == 0) {
//                    height = mMeasuredDimension[1];
//                }
//            } else {
                height = height + mMeasuredDimension[1];
                if (i == 0) {
                    width = mMeasuredDimension[0];
                }
//            }
        }

//        switch (heightMode) {
//            case View.MeasureSpec.EXACTLY:
//                height = heightSize;
//            case View.MeasureSpec.AT_MOST:
//            case View.MeasureSpec.UNSPECIFIED:
//        }
        setMeasuredDimension(widthSpec, height);
    }

    private void measureScrapChild(RecyclerView.Recycler recycler, int position, int widthSpec, int heightSpec, int[] measuredDimension) {
        View view = recycler.getViewForPosition(position);

        // For adding Item Decor Insets to view
        //super.measureChildWithMargins(view, 0, 0);

        if (view != null) {
            RecyclerView.LayoutParams p = (RecyclerView.LayoutParams) view.getLayoutParams();
            int childHeightSpec = ViewGroup.getChildMeasureSpec(heightSpec,
                    getPaddingTop() + getPaddingBottom(), p.height);
            view.measure(widthSpec, childHeightSpec);
            measuredDimension[0] = view.getMeasuredWidth() + p.leftMargin + p.rightMargin;
            measuredDimension[1] = view.getMeasuredHeight() + p.bottomMargin + p.topMargin;
            recycler.recycleView(view);
        }
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        detachAndScrapAttachedViews(recycler);
        if (mVerticalOffset + dy < 0) {
            dy = -mVerticalOffset;
        } else if (mVerticalOffset + dy > mTotalHeight - getVerticalSpace()) {
            dy = mTotalHeight - getVerticalSpace() - mVerticalOffset;
        }

        offsetChildrenVertical(-dy);
        mVerticalOffset += dy;
        fill(recycler, state);
        return dy;
    }

    private int getHorizontalSpace() {
        return getWidth() - getPaddingRight() - getPaddingLeft();
    }

    private int getVerticalSpace() {
        return getHeight() - getPaddingBottom() - getPaddingTop();
    }

    public int getDecoratedEnd(View view) {
        final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams)
                view.getLayoutParams();
        return getDecoratedBottom(view) + params.bottomMargin;
    }

    public int getEndAfterPadding() {
        return getHeight() - getPaddingBottom();
    }

    static class Pool<T> {
        private SparseArrayCompat<T> mPool;
        private New<T> mNewInstance;

        public Pool(New<T> newInstance) {
            mPool = new SparseArrayCompat<>();
            mNewInstance = newInstance;
        }

        public T get(int key) {
            T res = mPool.get(key);
            if (res == null) {
                res = mNewInstance.get();
                mPool.put(key, res);
            }
            return res;
        }

        public interface New<T> {
            T get();
        }
    }
}


