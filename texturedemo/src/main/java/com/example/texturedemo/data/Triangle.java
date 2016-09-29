package com.example.texturedemo.data;

import android.opengl.GLES30;


import com.example.texturedemo.utils.ProgramHelper;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by wenzhe on 8/26/16.
 */

public class Triangle {

    private int mProgram;
    private int mPositionHandle;
    private int mColorHandle;



    private final String vertexShaderCode =
            "#version 300 es \n" +
            "in vec4 vPosition;" +
            "void main(){" +
            "   gl_Position = vPosition;" +
            "}";

    private final String fragmentShaderCode =
            "#version 300 es \n" +
            "precision mediump float;" +
            "uniform vec4 vColor;" +
            "out vec4 FragColor;" +
            "void main(){" +
            "   FragColor = vColor;" +
            "}";

    private FloatBuffer vertexBuffer;
    static final int COORDS_PER_VERTEX = 3;
    private final int vertexCount = triangleCoords.length / COORDS_PER_VERTEX;
    private final int vertexStride = COORDS_PER_VERTEX * 4;

    static float triangleCoords[] = {
            0.0f, 0.5f, 0f,
            -0.5f, -0.5f, 0f,
            0.5f, -0.5f, 0f
    };

    float color[] = { 0.63671875f, 0.76953125f, 0.22265625f, 1.0f };

    public Triangle(){
        ByteBuffer bb = ByteBuffer.allocateDirect(triangleCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(triangleCoords);
        vertexBuffer.position(0);

        int vertexShader = ProgramHelper.getShaderFromSource(GLES30.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = ProgramHelper.getShaderFromSource(GLES30.GL_FRAGMENT_SHADER, fragmentShaderCode);

        mProgram = GLES30.glCreateProgram();
        GLES30.glAttachShader(mProgram, vertexShader);
        GLES30.glAttachShader(mProgram, fragmentShader);
        GLES30.glLinkProgram(mProgram);
    }

    public void draw() {
        GLES30.glUseProgram(mProgram);
        mPositionHandle = GLES30.glGetAttribLocation(mProgram, "vPosition");
        GLES30.glEnableVertexAttribArray(mPositionHandle);
        GLES30.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
                GLES30.GL_FLOAT, false, vertexStride, vertexBuffer);

        mColorHandle = GLES30.glGetUniformLocation(mProgram, "vColor");
        GLES30.glUniform4fv(mColorHandle, 1, color, 0);
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, vertexCount);
        GLES30.glDisableVertexAttribArray(mPositionHandle);
    }
}
