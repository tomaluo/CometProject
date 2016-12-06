package com.mazing.com.cavasdemo;

import android.graphics.Path;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.SeekBar;

import com.mazing.com.cavasdemo.view.MachView;
import com.mazing.com.cavasdemo.view.PathWaveView;

/**
 * Created by toma on 16/6/21.
 */
public class MainPathActivity extends AppCompatActivity {

    private MachView mMachview;
    private SeekBar mSeekbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dvhview);

        mMachview = (MachView) findViewById(R.id.machview);
        mSeekbar = (SeekBar) findViewById(R.id.seekbar);

        mSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mMachview.setCurrentSwipeValue(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mSeekbar.setMax(mMachview.getMaxSwipeValue());
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

//        final PathWaveView myView = (PathWaveView)findViewById(R.id.path);
//        myView.stareAnimet();
    }
}
