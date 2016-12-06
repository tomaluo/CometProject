package com.mazing.com.cavasdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.mazing.com.cavasdemo.view.VideoTextureView;

/**
 * Created by toma on 16/10/16.
 */

public class MainVideActivity extends AppCompatActivity {

    VideoTextureView tvVideo;
    ImageView ivplay;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.actvity_video);

        init();
    }

    public void  init(){
        tvVideo = (VideoTextureView) findViewById(R.id.tv_video);
        ivplay = (ImageView) findViewById(R.id.iv_player);

        tvVideo.setIvTip(ivplay);
        tvVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!tvVideo.getPlayStatus())
                    tvVideo.startMediaPlayer();
                else{
                    tvVideo.stopMediaPlayer();
                }
            }
        });
    }
}
