package com.mazing.com.cavasdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;

import com.mazing.com.cavasdemo.viewpager.Fragment1;
import com.mazing.com.cavasdemo.viewpager.Fragment2;
import com.mazing.com.cavasdemo.viewpager.FragmentAdpater;
import com.mazing.com.cavasdemo.viewpager.OpenFragment;
import com.mazing.com.cavasdemo.viewpager.ViewpagerTransformer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by toma on 16/6/22.
 */
public class FragmentAcitiy extends AppCompatActivity {

    ViewPager fragmentvp;
    List<OpenFragment> lists = new ArrayList<>();
    FragmentAdpater adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fragment);

        fragmentvp = (ViewPager) findViewById(R.id.fragment_viewpager);
        fragmentvp.setPageTransformer(true, new ViewpagerTransformer());
        fragmentvp.setOffscreenPageLimit(2);
        fragmentvp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                OpenFragment fragment = (OpenFragment) lists.get(fragmentvp.getCurrentItem());
                fragment.close();
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        lists.add(OpenFragment.getINstance(new Fragment1(),new Fragment2()));
        lists.add(OpenFragment.getINstance(new Fragment1(),new Fragment2()));
        lists.add(OpenFragment.getINstance(new Fragment1(),new Fragment2()));
        lists.add(OpenFragment.getINstance(new Fragment1(),new Fragment2()));
        lists.add(OpenFragment.getINstance(new Fragment1(),new Fragment2()));
        lists.add(OpenFragment.getINstance(new Fragment1(),new Fragment2()));

        adapter = new FragmentAdpater(getSupportFragmentManager(),lists);
        fragmentvp.setAdapter(adapter);

        init();
        setupViewPager(fragmentvp);
    }

    public void setupViewPager(ViewPager v) {

        ViewGroup.LayoutParams layoutParams = v.getLayoutParams();
        ((ViewGroup) v.getParent()).setClipChildren(false);
        v.setClipChildren(false);
        layoutParams.width = getWindowManager().getDefaultDisplay().getWidth() / 7 * 5;
        layoutParams.height = (int) ((layoutParams.width / 0.75));

    }

    @Override
    public void onBackPressed() {
        OpenFragment fragment = (OpenFragment) lists.get(fragmentvp.getCurrentItem());
        if(!fragment.isClosed()){
            fragment.close();
        }
        else
            super.onBackPressed();
    }

    private void init(){

    }
}
