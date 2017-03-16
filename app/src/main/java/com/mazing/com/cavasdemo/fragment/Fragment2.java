package com.mazing.com.cavasdemo.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mazing.com.cavasdemo.R;

/**
 * Created by user on 17/3/16.
 */

public class Fragment2 extends BaseFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fragment2, container, false);
    }

    @Override
    protected void loadForUI() {

    }

    @Override
    public int getLayoutID() {
        return R.layout.fragment_fragment2;
    }

    @Override
    public void loadData() {
        Log.i("Fragment2","loadData");
    }
}
