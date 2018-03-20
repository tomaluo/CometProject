package com.mazing.com.cavasdemo;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.mazing.com.cavasdemo.view.HorizontalTabView;
import com.mazing.com.cavasdemo.view.SwitchButtonView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2017/11/11.
 */

public class ChangeTabActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_tab);
//        SwitchButtonView btn = (SwitchButtonView) findViewById(R.id.view_sb);
//        if (btn != null) {
//            btn.setData(new String[]{"职业高手","武林高手","江湖菜鸡","隐藏大佬","女装大佬","超能力","原力使者","绝地武士","职业高手","武林高手","江湖菜鸡","隐藏大佬","女装大佬","超能力","原力使者"});
//        }

        List<HorizontalTabView.Label> list = new ArrayList<>();
        for(int i = 0 ; i < 10 ; i++){
            HorizontalTabView.Label label = new HorizontalTabView.Label();
            label.name = "label" + i;
            label.id = i;
            label.isChosen = false;
            list.add(label);
        }

        HorizontalTabView view = (HorizontalTabView) findViewById(R.id.view_horizonttal);
        view.setData(list);
    }
}
