package com.mazing.com.cavasdemo.opengl;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by toma on 16/8/5.
 */

public class OpenView extends AppCompatActivity {

    private GLSurfaceView mGLview;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

        mGLview = new MyGLSurfaceView(getApplicationContext());
        setContentView(mGLview);
    }
}
