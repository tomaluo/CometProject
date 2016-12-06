package com.mazing.com.cavasdemo.itemtouchhelp;

/**
 * Created by toma on 16/6/16.
 */
//由于需要有点中后的效果所以需要暴露 viewholder 被点中时的接口
public interface ItemTouchHelperViewHolder {

    void onItemSelected();

    void onItemClear();
}
