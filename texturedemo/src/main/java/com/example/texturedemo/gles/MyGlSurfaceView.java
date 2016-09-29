package com.example.texturedemo.gles;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

/**
 * Created by wenzhe on 8/26/16.
 */

public class MyGlSurfaceView extends GLSurfaceView {

    private final MyRender myRender;

    public MyGlSurfaceView(Context context) {
        this(context,null);
    }

    public MyGlSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setEGLContextClientVersion(3);
        myRender = new MyRender(context);
        setRenderer(myRender);
        //setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        //setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }

    public void release() {
        myRender.release();
    }
}
