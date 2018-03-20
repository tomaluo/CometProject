package com.mazing.com.cavasdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.mazing.com.cavasdemo.view.MoreLineTextView;
import com.mazing.com.cavasdemo.view.MoreLineTextViewV2;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2018/1/17.
 */

public class SwitchTabGroupActivity extends AppCompatActivity {

//    private SwitchTabGroupView switchGroupView;

    private MoreLineTextViewV2 mTvMore;

//    private TextView mTvmore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_switch_group);

//        mTvmore = (TextView) findViewById(R.id.more_line_tv);
//        mTvmore.setText(this.getResources().getString(R.string.more_value));
//        switchGroupView = (SwitchTabGroupView) findViewById(R.id.switch_group);

//        List<String> dataList = new ArrayList<>();
//        dataList.add("简介");
//        dataList.add("课单");
//        dataList.add("相关推荐");

        mTvMore = (MoreLineTextViewV2) findViewById(R.id.more_tv);
        mTvMore.setText("");
//        switchGroupView.setViewData(dataList);
    }
}
