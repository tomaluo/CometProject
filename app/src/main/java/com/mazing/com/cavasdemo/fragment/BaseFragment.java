package com.mazing.com.cavasdemo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 实现懒加载所使用的basefrgament
 * Created by user on 17/3/15.
 */

public abstract class BaseFragment extends Fragment {

    private boolean isViewCreate;
    private boolean isVisible;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutID(), container, false);
        isViewCreate = true;
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            //如果当前fragment 已经要显示在页面上
            isVisible = true;
            loadData();
        } else {
            //该fragment 从显示走向隐藏
            isVisible = false;
        }

        if (isViewCreate && isVisible)
            loadForUI();

    }

    protected abstract void loadForUI();

    // provide layout id
    public abstract int getLayoutID();

    public abstract void loadData();
}
