package com.mazing.com.cavasdemo;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.mazing.com.cavasdemo.mp3lame.MP3Recorder;

import java.io.File;
import java.io.IOException;

/**
 * Created by user on 2017/12/26.
 */

public class Mp3RecorderActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_mp3);

        Button btnStare = (Button)findViewById(R.id.stare);

        Button btnPause = (Button)findViewById(R.id.pause);

        Button btnRestate = (Button)findViewById(R.id.restate);

        String voicePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() + "/audio";
        final MP3Recorder recorder = new MP3Recorder(8000);
        recorder.setFilePath(voicePath);//录音保存目录
        recorder.getVoiceLevel();

        btnStare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recorder.start();//开始录音
            }
        });

        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recorder.stop();//录音结束
            }
        });

        btnRestate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recorder.restore();//继续录音
            }
        });
    }
}
