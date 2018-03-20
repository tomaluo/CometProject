package com.mazing.com.cavasdemo.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mazing.com.cavasdemo.R;
import com.mazing.com.cavasdemo.itemhelp.ItemDragTouchHelper;
import com.mazing.com.cavasdemo.itemhelp.OnDragVHListener;
import com.mazing.com.cavasdemo.itemhelp.OnItemMoveListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * dec：内部 button 可以随意滑动切换位置的view
 */

public class SwitchButtonView extends LinearLayout {

    RecyclerView mRv;

    SwitchAdapter mSwitchAdapter;

    ArrayList<String> mList = new ArrayList<>();

    ItemTouchHelper helper;

    public SwitchButtonView(Context context) {
        this(context, null);
    }

    public SwitchButtonView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwitchButtonView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

        if(attrs != null){
           //TODO 处理自定义属性
        }

        View view = LayoutInflater.from(context).inflate(R.layout.switch_button_layout, this, true);
        mRv = (RecyclerView) view.findViewById(R.id.switch_btn_rv);
        GridLayoutManager manager = new GridLayoutManager(context, 4);
        mRv.setLayoutManager(manager);

        ItemDragTouchHelper mCall = new ItemDragTouchHelper();
        helper = new ItemTouchHelper(mCall);
        helper.attachToRecyclerView(mRv);

        mSwitchAdapter = new SwitchAdapter();
        mRv.setAdapter(mSwitchAdapter);
    }

    public void setData(String[] labels) {
        if (labels != null && labels.length > 0) {
            mList.clear();
            for (int i = 0; i < labels.length; i++) {
                mList.add(labels[i]);
            }
        }

        if (mSwitchAdapter != null)
            mSwitchAdapter.setData(mList);
    }

    public class SwitchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements OnItemMoveListener {

        ArrayList<String> mList = new ArrayList<>();

        private long startTime;

        public void setData(ArrayList<String> list) {
            mList = list;
            notifyDataSetChanged();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_switch, parent, false);
            final SwitchViewHolder myHolder = new SwitchViewHolder(view);
            myHolder.labelTv.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (MotionEventCompat.getActionMasked(event)){
                        case MotionEvent.ACTION_DOWN:
                            startTime = System.currentTimeMillis();
                            break;

                        case MotionEvent.ACTION_MOVE:
                            if(System.currentTimeMillis() - startTime > 100){
                                helper.startDrag(myHolder);
                            }
                            break;

                        case MotionEvent.ACTION_CANCEL:
                        case MotionEvent.ACTION_UP:
                            startTime = 0;
                            break;
                    }

                    return false;
                }
            });


            myHolder.labelTv.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    helper.startDrag(myHolder);
                    return false;
                }
            });

            return myHolder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if(holder instanceof SwitchViewHolder){
                ((SwitchViewHolder) holder).bindView(mList.get(position),position);
            }
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        @Override
        public void onItemMove(int fromPosition, int toPosition) {
            String label = mList.get(fromPosition);
            mList.remove(fromPosition);
            mList.add(toPosition,label);
            notifyItemMoved(fromPosition,toPosition);
        }

        class SwitchViewHolder extends RecyclerView.ViewHolder implements OnDragVHListener {

            public TextView labelTv;

            public SwitchViewHolder(View itemView) {
                super(itemView);
                labelTv = (TextView) itemView.findViewById(R.id.tv);
            }

            public void bindView(String label, int position){
               if(label != null){
                   itemView.setTag(position);
                   labelTv.setText(label);
               }
            }

            @Override
            public void onItemSelected() {
                labelTv.setBackgroundResource(R.drawable.bg_channel_p);
            }

            @Override
            public void onItemFinish() {
                labelTv.setBackgroundResource(R.drawable.bg_channel);
            }
        }
    }
}


