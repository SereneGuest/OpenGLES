package com.example.texturedemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import com.example.texturedemo.gles.MyGlSurfaceView;

public class MainActivity extends AppCompatActivity {

    private MyGlSurfaceView glSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        glSurfaceView = new MyGlSurfaceView(this);
        setContentView(glSurfaceView);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (glSurfaceView != null) {
            glSurfaceView.release();
        }
    }
}
