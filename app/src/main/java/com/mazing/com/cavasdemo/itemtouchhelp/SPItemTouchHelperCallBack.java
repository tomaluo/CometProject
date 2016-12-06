package com.mazing.com.cavasdemo.itemtouchhelp;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

/**
 * Created by toma on 16/6/16.
 */

//callback 标记想要获得的方向与处理
public class SPItemTouchHelperCallBack extends ItemTouchHelper.Callback {

    public static final float ALPHA_FULL = 1.0f;   //默认透明度最大，用于拖动消失渐变

    private final ItemTouchAdapter mAdapter;

    public SPItemTouchHelperCallBack(ItemTouchAdapter adapter){
        mAdapter = adapter;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        //是否支持长按拖动
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        //是否允许整个控件全部支持滑动
        return true;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;          //上下移动时 获取拖动状态
        final int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;       //左右滑动时  获取滑动状态
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder source, RecyclerView.ViewHolder target) {
        if (source.getItemViewType() != target.getItemViewType()) {
            //如果前后移动item 位置一样则不作处理
            return false;
        }

        // Notify the adapter of the move
        mAdapter.onItemMove(source.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        mAdapter.onItemDismiss(viewHolder.getAdapterPosition());
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){
            final float alpha = ALPHA_FULL - Math.abs(dX) / (float) viewHolder.itemView.getWidth();     //viewholder  根据滑动距离减少透明度
            viewHolder.itemView.setAlpha(alpha);
            viewHolder.itemView.setTranslationX(dX);
        }
        else{
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        //有滑动或者拖动时调用
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            if (viewHolder instanceof ItemTouchHelperViewHolder) {
                // Let the view holder know that this item is being moved or dragged
                ItemTouchHelperViewHolder itemViewHolder = (ItemTouchHelperViewHolder) viewHolder;
                itemViewHolder.onItemSelected();
            }
        }

        super.onSelectedChanged(viewHolder, actionState);
    }


    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        //滑动或拖动放开后调用
        viewHolder.itemView.setAlpha(ALPHA_FULL);

        if (viewHolder instanceof ItemTouchHelperViewHolder) {
            // Tell the view holder it's time to restore the idle state
            ItemTouchHelperViewHolder itemViewHolder = (ItemTouchHelperViewHolder) viewHolder;
            itemViewHolder.onItemClear();
        }
    }
}
