package com.mazing.com.cavasdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by user on 17/5/27.
 */

public class TabLayoutActivity extends AppCompatActivity {

    private TabLayout mTabHeadLayout;
    private final String[] title = new String[]{
            "推荐", "热点", "视频"};

    private TextView mTitle1;
    private TextView mTitle2;
    private TextView mTitle3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_tablayout);

        mTabHeadLayout = (TabLayout) findViewById(R.id.tab_head);

        for (int i = 0; i < title.length; i++) {
            TabLayout.Tab tab = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.GINGERBREAD) {
                if (mTabHeadLayout != null) {
//                    tab = mTabHeadLayout.newTab();
//                    tab.setText(title[i]);
//                    // tab.setIcon(R.mipmap.ic_launcher);//icon会显示在文字上面
//                    mTabHeadLayout.addTab(tab);
                    mTabHeadLayout.addTab(mTabHeadLayout.newTab().setCustomView(R.layout.layout_title1));
                    mTitle1 = (TextView) findViewById(R.id.title1);
                    mTitle1.setText("推荐");
                    mTitle1.setTextColor(getResources().getColor(R.color.colorPrimary));
                    mTitle1.setTextSize(14);

                    mTabHeadLayout.addTab(mTabHeadLayout.newTab().setCustomView(R.layout.layout_title2));
                    mTitle2= (TextView) findViewById(R.id.title2);
                    mTitle2.setText("热点");
                    mTitle2.setTextColor(getResources().getColor(R.color.colorPrimary));
                    mTitle2.setTextSize(15);

                    mTabHeadLayout.addTab(mTabHeadLayout.newTab().setCustomView(R.layout.layout_title3));
                    mTitle3 = (TextView) findViewById(R.id.title3);
                    mTitle3.setText("视频");
                    mTitle3.setTextColor(getResources().getColor(R.color.colorPrimary));
                    mTitle3.setTextSize(14);

                }
            }
        }

        mTabHeadLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        mTitle1.setTextColor(getResources().getColor(R.color.sienna));
                        mTitle1.setTextSize(15);
                        break;
                    case 1:
                        mTitle2.setTextColor(getResources().getColor(R.color.sienna));
                        mTitle2.setTextSize(15);
                        break;
                    case 2:
                        mTitle3.setTextColor(getResources().getColor(R.color.sienna));
                        mTitle3.setTextSize(15);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                mTitle1.setTextColor(getResources().getColor(R.color.colorPrimary));
                mTitle1.setTextSize(14);
                mTitle2.setTextColor(getResources().getColor(R.color.colorPrimary));
                mTitle2.setTextSize(14);
                mTitle3.setTextColor(getResources().getColor(R.color.colorPrimary));
                mTitle3.setTextSize(14);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}
