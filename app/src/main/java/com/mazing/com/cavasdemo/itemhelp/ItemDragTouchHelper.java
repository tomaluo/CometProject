package com.mazing.com.cavasdemo.itemhelp;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

/**
 * Created by user on 2017/11/11.
 */

public class ItemDragTouchHelper extends ItemTouchHelper.Callback  {

    //设定需要掌握的拖动、滑动内容的范围与类型
    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags;
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();

        if(manager instanceof GridLayoutManager){
            dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT; //支持的拖动方向 （上下左右）
        } else {
            dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        }

        int swipedFlags = 0;

        //是 makeMovementFlags 不是 makeFlags
       return makeMovementFlags(dragFlags,swipedFlags);
    }


    //在开始拖动的时候调用
    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        // 不同Type之间不可移动
        if (viewHolder.getItemViewType() != target.getItemViewType()) {
            return false;
        }

        //暴露接口，完成拖动时通知页面改变
        if(recyclerView.getAdapter() instanceof  OnItemMoveListener){
            OnItemMoveListener listener = (OnItemMoveListener) recyclerView.getAdapter();
            listener.onItemMove(viewHolder.getAdapterPosition(),target.getAdapterPosition());
        }

        return true;
    }

    //选中了某个item 时候的处理（一般是高亮或者改变颜色）
    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if(actionState == ItemTouchHelper.ACTION_STATE_IDLE){
            if(viewHolder instanceof OnDragVHListener){
                ((OnDragVHListener) viewHolder).onItemSelected();
            }
        }
        super.onSelectedChanged(viewHolder, actionState);
    }

    //手指放开以后的 item 处理
    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        if(viewHolder instanceof OnDragVHListener){
            ((OnDragVHListener) viewHolder).onItemFinish();
        }
        super.clearView(recyclerView, viewHolder);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
       //因为不设计到滑动所以不做任何处理
    }

    //关闭长按拖动，需要自己确定可拖动对象
    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }

    //关闭滑动
    @Override
    public boolean isItemViewSwipeEnabled() {
        return false;
    }
}
