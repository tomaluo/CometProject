package com.mazing.com.cavasdemo.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.Adapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mazing.com.cavasdemo.R;
import com.mazing.com.cavasdemo.overscroll.HorizontalOverScrollBounceEffectDecorator;
import com.mazing.com.cavasdemo.overscroll.RecyclerViewOverScrollDecorAdapter;

import java.util.ArrayList;
import java.util.List;

import static android.widget.LinearLayout.HORIZONTAL;

/**
 * Created by user on 2017/12/9.
 */

public class HorizontalTabView extends FrameLayout {

    RecyclerView mRvTab;

    RecyclerView.LayoutManager mLayout;

    HorizontalTabAdapter mAdapter;

    List<Label> mList = new ArrayList<>();

    int mCurPos = -1;

    int mprePos = -1;

    public HorizontalTabView(Context context) {
        this(context, null);
    }

    public HorizontalTabView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HorizontalTabView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

        if(attrs != null){
            //TODO 自定义属性实现
        }

        View view = LayoutInflater.from(context).inflate(R.layout.view_horizonttal, this, true);
        mRvTab = (RecyclerView) view.findViewById(R.id.rv_horizontal);
        mAdapter = new HorizontalTabAdapter();
        mLayout = new LinearLayoutManager(context,HORIZONTAL,false);

        mRvTab.setLayoutManager(mLayout);
        mRvTab.setAdapter(mAdapter);
        mRvTab.setItemAnimator(null);
        mAdapter.setListener(new HorizontalTabAdapter.ItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if(mCurPos != position){
                    for(int i = 0 ; i < mList.size() ; i++){
                        Label label = mList.get(i);
                        if(i == position){
                            label.isChosen = true;
                        }
                        else{
                            label.isChosen = false;
                        }
                    }
                    if(mAdapter != null){
                        mAdapter.notifyDataSetChanged();
                    }

                    mCurPos = position;
                    mLayout.scrollToPosition(position);
                }
//                int visitPoint = ((LinearLayoutManager)mLayout).findFirstCompletelyVisibleItemPosition();
//                if(visitPoint > position){
//                    View view = mLayout.findViewByPosition(position);
//                    int viewLeft = view.getLeft();
//                }
            }
        });

        new HorizontalOverScrollBounceEffectDecorator(new RecyclerViewOverScrollDecorAdapter(mRvTab));
    }

    public void setData(List<Label> list){
        mList = list;
        if(mAdapter != null){
            mAdapter.setLabelData(mList);
        }
    }

    static class HorizontalTabAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        ItemClickListener mListener;

        List<Label> mData;

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RecyclerView.ViewHolder holder;
            holder = new HorizontalViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_horizontal_view, parent, false));
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
              if(holder instanceof HorizontalViewHolder){
                  ((HorizontalViewHolder) holder).bindView(mData.get(position),position);
                  ((HorizontalViewHolder) holder).setListener(mListener);
              }
        }

        @Override
        public int getItemCount() {
            return mData != null ? mData.size() : 0;
        }

        public void setLabelData(List<Label> list){
            mData = list;
            notifyDataSetChanged();
        }

        public void setListener(ItemClickListener listener){
            mListener = listener;
        }

        public interface ItemClickListener{
            void onItemClick(int position);
        }

        static class HorizontalViewHolder extends RecyclerView.ViewHolder{

            TextView tvLabel;

            ItemClickListener mListener;

            public HorizontalViewHolder(View itemView) {
                super(itemView);
                tvLabel = (TextView) itemView.findViewById(R.id.label_tv);
            }

            public void bindView(Label label ,int position){
                if(label != null){
                    itemView.setTag(position);
                    tvLabel.setText(label.name);
                    if(label.isChosen){
                        tvLabel.setTextColor(Color.parseColor("#FF26BF81"));
//                        if(tvLabel.getTextSize() == itemView.getContext().getResources().getDimension(R.dimen.horizontal_text)){

                        ObjectAnimator.ofFloat(tvLabel, "textSize", 15f,18f)
                                    .setDuration(200)
                                    .start();

                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                        params.setMargins(30,22,30,22);
                        tvLabel.setLayoutParams(params);
                    }
                    else{
                        tvLabel.setTextColor(Color.parseColor("#FF666666"));
                        tvLabel.setTextSize(15f);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                        params.setMargins(30,26,30,26);
                        tvLabel.setLayoutParams(params);
                    }
                    itemView.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mListener.onItemClick(v.getTag() != null ? (Integer) v.getTag() : 0);
                        }
                    });
                }
            }

            public void setListener(ItemClickListener listener){
                mListener = listener;
            }
        }
    }

    /**
    测试用模拟数据集合
     **/
   public static class Label {
        /**
         * id值
         */
        public int id;
        /**
         * 字符串值
         */
        public String name;

        /**
         * 是否被选中
         */
        public boolean isChosen;
    }
}


