package com.mazing.com.cavasdemo.transition;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.AppOpsManagerCompat;
import android.transition.Explode;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.View;
import android.widget.Button;

import com.mazing.com.cavasdemo.R;

/**
 * Created by user on 2018/1/15.
 */

public class TransitionActivity extends Activity {

    FloatingActionButton mBtnFloat;

    Button btGo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_transition);
        btGo = (Button) findViewById(R.id.bt_go);
        mBtnFloat = (FloatingActionButton) findViewById(R.id.fab);
        mBtnFloat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getWindow().setExitTransition(null);
                getWindow().setEnterTransition(null);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options =
                            ActivityOptions.makeSceneTransitionAnimation(TransitionActivity.this,mBtnFloat,mBtnFloat.getTransitionName());
                    startActivity(new Intent(TransitionActivity.this,RegisterMainActivity.class),options.toBundle());
                }
            }
        });

        btGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Explode explode = new Explode();
                explode.setDuration(500);

                getWindow().setExitTransition(explode);
                getWindow().setEnterTransition(explode);
                ActivityOptionsCompat oc2 = ActivityOptionsCompat.makeSceneTransitionAnimation(TransitionActivity.this);
                Intent i2 = new Intent(TransitionActivity.this,RegisterMainActivity.class);
                startActivity(i2, oc2.toBundle());
            }
        });

    }
}
