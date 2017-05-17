package com.mazing.com.cavasdemo;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.mazing.com.cavasdemo.layoutmanager.CardItemTouchHelpCallback;
import com.mazing.com.cavasdemo.layoutmanager.CardLayoutManager;
import com.mazing.com.cavasdemo.layoutmanager.OnSwipeListener;
import com.mazing.com.cavasdemo.model.User;

import java.util.ArrayList;
import java.util.List;

import static com.mazing.com.cavasdemo.layoutmanager.CardItemTouchHelpCallback.SWIPING_LEFT;
import static com.mazing.com.cavasdemo.layoutmanager.CardItemTouchHelpCallback.SWIPING_RIGHT;

/**
 * Created by toma on 17/5/17.
 * 可左右滑动滑出界面cardview
 */

public class CardActivity extends AppCompatActivity {

    private RecyclerView mCardRv;
    private List<Integer> mList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);

        mCardRv = (RecyclerView)findViewById(R.id.card_rv);
        init();
    }

    private void init(){

        mList.add(R.drawable.icon_1);
        mList.add(R.drawable.icon_2);
        mList.add(R.drawable.icon_3);
        mList.add(R.drawable.icon_2);
        mList.add(R.drawable.icon_3);
        mCardRv.setAdapter(new MyAdapter());
        CardItemTouchHelpCallback mCallBack = new CardItemTouchHelpCallback(mCardRv.getAdapter(),mList);
        mCallBack.setOnSwipedListener(new OnSwipeListener() {
            @Override
            public void onSwiping(RecyclerView.ViewHolder viewHolder, float ratio, int direction) {

                MyAdapter.MyViewHolder myHolder = (MyAdapter.MyViewHolder) viewHolder;
                viewHolder.itemView.setAlpha(1 - Math.abs(ratio) * 0.2f);
                if (direction == SWIPING_LEFT) {
                    myHolder.avatarImageView.setAlpha(Math.abs(ratio));
                } else if (direction == SWIPING_RIGHT) {
                    myHolder.avatarImageView.setAlpha(Math.abs(ratio));
                } else {
                    myHolder.avatarImageView.setAlpha(1f);
                }
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, Object o, int direction) {
                MyAdapter.MyViewHolder myHolder = (MyAdapter.MyViewHolder) viewHolder;
                viewHolder.itemView.setAlpha(1f);
                myHolder.itemView.setAlpha(1f);
            }

            @Override
            public void onSwipedClear() {
                mCardRv.postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                        initData();
                        mCardRv.getAdapter().notifyDataSetChanged();
                    }
                }, 3000L);
            }
        });
        final ItemTouchHelper touchHelper = new ItemTouchHelper(mCallBack);
        final CardLayoutManager cardLayoutManager = new CardLayoutManager(mCardRv, touchHelper);

        mCardRv.setLayoutManager(cardLayoutManager);
        touchHelper.attachToRecyclerView(mCardRv);
    }

    private class MyAdapter extends RecyclerView.Adapter {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ImageView avatarImageView = ((MyViewHolder) holder).avatarImageView;
            avatarImageView.setImageResource(mList.get(position));
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            ImageView avatarImageView;
//            ImageView likeImageView;
//            ImageView dislikeImageView;

            MyViewHolder(View itemView) {
                super(itemView);
                avatarImageView = (ImageView) itemView.findViewById(R.id.image_view_icon);
            }

        }
    }
}
