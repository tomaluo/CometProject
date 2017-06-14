package com.mazing.com.cavasdemo;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.mazing.com.cavasdemo.transition.SharedElementActivity;

/**
 * Created by user on 17/6/5.
 */

public class TransitionMainActivity extends AppCompatActivity {

    ImageView mIvShow;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_transition);

        mIvShow = (ImageView) findViewById(R.id.show_iv);
        mIvShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TransitionMainActivity.this, SharedElementActivity.class);

                View sharedView = mIvShow;
                String transitionName = getString(R.string.app_name);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions transitionActivityOptions = null;
                    transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(TransitionMainActivity.this, sharedView, transitionName);

                    startActivity(i, transitionActivityOptions.toBundle());
                }
            }
        });
    }
}
