package com.mazing.com.cavasdemo.opengl;

import android.content.Context;
import android.opengl.GLSurfaceView;

/**
 * Created by toma on 16/8/5.
 */

public class MyGLSurfaceView extends GLSurfaceView {

    public MyGLSurfaceView(Context context) {
        super(context);

        setEGLContextClientVersion(2);

        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

        setRenderer(new MyRenderer());
    }
}
