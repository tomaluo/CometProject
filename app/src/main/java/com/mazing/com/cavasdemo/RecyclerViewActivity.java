package com.mazing.com.cavasdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mazing.com.cavasdemo.Itemdecoration.SectionDecoration;
import com.mazing.com.cavasdemo.layoutmanager.AutoMatchLayoutManager;
import com.mazing.com.cavasdemo.unit.Units;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * Created by user on 2017/9/15.
 */

public class RecyclerViewActivity extends AppCompatActivity {

    RecyclerView mRv;
    AutoMatchLayoutManager manager;
    LinearLayout headview;
    TestAdapter adapter = new TestAdapter();
    int titleHeight = 190;
    int currScroll;

    Hashtable<Integer,Integer> mtable = new Hashtable<>();
    Hashtable<String,Integer> mtitleTable = new Hashtable<>();

    boolean moveToTop = false;
    boolean moveToBottom = false;

    private int mIndex;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_recycler);

        mRv = (RecyclerView)findViewById(R.id.rv_test);
        headview = (LinearLayout)findViewById(R.id.head_view);

//        TextView view1 = (TextView) findViewById(R.id.tv_1);
//        view1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mIndex = 12;
//                moveToPosition(mIndex);
//            }
//        });

        init();
    }

    private void init(){
//        manager = new AutoMatchLayoutManager(null);
//        mRv.setLayoutManager(manager);
        final ArrayList<String> list = new ArrayList<>();

        list.add("A");
        list.add("");
        list.add("");
        list.add("");
        list.add("B");
        list.add("");
        list.add("");
        list.add("C");
        list.add("");
        list.add("");
        list.add("D");
        list.add("");
        list.add("");

        mRv.setLayoutManager(new LinearLayoutManager(this));
        mRv.setAdapter(adapter);
        mRv.setItemAnimator(null);
        mRv.addItemDecoration(new SectionDecoration(list, this, new SectionDecoration.DecorationCallback() {
            @Override
            public String getGroupId(int position) {
                if(list.get(position)!= null) {
                    return list.get(position);
                }
                return "-1";
            }

            @Override
            public String getGroupFirstLine(int position) {
                if(list.get(position)!=null) {
                    return list.get(position);
                }
                return "";
            }
        }));

//        mRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
//
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//
//                int scrollY;
//
//                int firstpos = manager.findFirstCompletelyVisibleItemPosition();
//                View firstView = manager.findViewByPosition(firstpos);
//
//                if(manager.getChildCount() == 0){
//                    scrollY = 0;
//                }
//                else {
//                    if(firstView == null) {
//                        scrollY = 0;
//                    }
//                    else{
//                        scrollY = -firstView.getTop();  //因为列表坐标与实际需要的数据相反
//                        mtable.put(firstpos,firstView.getHeight());
//                        for(int i = 0 ; i < firstpos; i++){
//                            if(mtable.get(i) != null){
//                                scrollY += mtable.get(i);
//                            }
//                        }
//                    }
//                }
//
//                if(scrollY >= titleHeight){
//                    //隐藏头部部分
//                    headview.setVisibility(View.VISIBLE);
//                }
//                else{
//                    //打开头部部分
//                    headview.setVisibility(View.INVISIBLE);
//                }
//
//                if(moveToTop){
//                    moveToTop = false;
//                    mRv.scrollBy(0, -Units.convertDIP2PX(RecyclerViewActivity.this,40));
//                }
//
//                if (moveToBottom) {
//                    moveToBottom = false;
//                    //获取要置顶的项在当前屏幕的位置，mIndex是记录的要置顶项在RecyclerView中的位置
//                    int n = mIndex - manager.findFirstVisibleItemPosition();
//                    if (0 <= n && n < mRv.getChildCount()) {
//                        //获取要置顶的项顶部离RecyclerView顶部的距离
//                        int top = mRv.getChildAt(n).getTop();
//                        //最后的移动
//                        top = top - Units.convertDIP2PX(RecyclerViewActivity.this,40);
//                        mRv.scrollBy(0, top);
//                    }
//                }
//            }
//        });



        adapter.setData(list);
    }

//    private void moveToPosition(int index) {
//        //获取当前recycleView屏幕可见的第一项和最后一项的Position
//        int firstItem = manager.findFirstVisibleItemPosition();
//        int lastItem = manager.findLastVisibleItemPosition();
//        //然后区分情况
//        if (index <= firstItem) {
//            //当要置顶的项在当前显示的第一个项的前面时
//            mRv.scrollToPosition(index);
//            moveToTop = true;
//        } else if (index <= lastItem) {
//            //当要置顶的项已经在屏幕上显示时，计算它离屏幕原点的距离
//            int top = mRv.getChildAt(index - firstItem).getTop();
//            top = top - Units.convertDIP2PX(this,40);
//            mRv.scrollBy(0, top);
//        } else {
//            //当要置顶的项在当前显示的最后一项的后面时
//            mRv.scrollToPosition(index);
//            //记录当前需要在RecyclerView滚动监听里面继续第二次滚动
//            moveToBottom = true;
//        }
//    }

    public class TestAdapter extends RecyclerView.Adapter {

        public static final int HEAD_ITEM = 1;
        public static final int TITLE_ITEM = 2;
        public static final int TEST_ITEM = 3;

        private List<String> mData = new ArrayList<>();

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            LayoutInflater mInflater = LayoutInflater.from(RecyclerViewActivity.this);
            RecyclerView.ViewHolder holder = null;

            switch (viewType){
                case HEAD_ITEM :{
                    View v = mInflater.inflate(R.layout.item_view_head,parent,false);
                    holder = new HeadHolder(v);
                }
                break;
                case TITLE_ITEM :{
                    View v = mInflater.inflate(R.layout.item_view_title,parent,false);
                    holder = new TitleHolder(v);
                }
                break;
                case TEST_ITEM :{
                    View v = mInflater.inflate(R.layout.item_view_test,parent,false);
                    holder = new TestHolder(v);
                    Log.i("TestAdapter","onCreateViewHolder");
                }
                break;
            }

            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if(holder instanceof HeadHolder){

            }
            else if(holder instanceof TitleHolder){
                ((TitleHolder) holder).BindData(position);
            }
            else{

            }
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }

        @Override
        public int getItemViewType(int position) {

//            if(position ==1){
//                return HEAD_ITEM;
//            }
//            else if(mData.get(position).equals("title")){
//                return TITLE_ITEM;
//            }
//            else{
                return TEST_ITEM;
//            }
        }

        public void setData(ArrayList<String> list){
            mData = list;
            notifyDataSetChanged();
        }

        public class HeadHolder extends RecyclerView.ViewHolder{

            public HeadHolder(View itemView) {
                super(itemView);
            }
        }

        public class TestHolder extends RecyclerView.ViewHolder {

            public TestHolder(View itemView) {
                super(itemView);
            }
        }

        public class TitleHolder extends RecyclerView.ViewHolder {

            public TitleHolder(View itemView) {
                super(itemView);

            }

            public void BindData(int pos){
                itemView.setTag(pos);
            }

        }
    }
}
