package com.mazing.com.cavasdemo.itemtouchhelp;

import android.support.v7.widget.RecyclerView;

/**
 * Created by toma on 16/6/16.
 */

//自定义接口,用来向外暴露滑动回调与删除回调
public interface ItemTouchAdapter {

    boolean onItemMove(int fromPosition, int toPosition);

    void onItemDismiss(int position);
}
