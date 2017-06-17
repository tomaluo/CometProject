package com.mazing.com.cavasdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.mazing.com.cavasdemo.view.SwitchButton;

/**
 * Created by user on 17/6/14.
 */

public class SwitchButtonActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_switch_button);

        SwitchButton swBtn = (SwitchButton) findViewById(R.id.sw_btn);
        swBtn.setStatus(false);
        swBtn.setToggleViewClickListener(new SwitchButton.OnToggleViewClickListener() {
            @Override
            public void onToggleViewClick(boolean isOn) {

            }
        });
    }
}
