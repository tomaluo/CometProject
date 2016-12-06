package com.mazing.com.cavasdemo.viewpager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by toma on 16/6/22.
 */
public class FragmentAdpater extends FragmentPagerAdapter {

    List<OpenFragment> fragments;

    public FragmentAdpater(FragmentManager fm,List<OpenFragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
