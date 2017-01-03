package com.example.texturedemo.data;

import android.content.Context;
import android.opengl.GLES30;
import android.opengl.Matrix;

import com.example.texturedemo.R;
import com.example.texturedemo.utils.GlslReader;
import com.example.texturedemo.utils.ProgramHelper;
import com.example.texturedemo.utils.TextureHelper;

import java.nio.FloatBuffer;

/**
 * Created by wenzhe on 9/21/16.
 */

public class Color {

    private int program;
    private int textureHandle;
    private int textureHandle1;

    private FloatBuffer verticesBuffer;

    private int STRIDE = 20;

    //private float[] transformMatrix = new float[16];
    private float[] modelMatrix = new float[16];
    private float[] viewMatrix = new float[16];
    private float[] projectionMatrix = new float[16];

    private int[] VBO = new int[1];
    private int[] VAO = new int[1];
    //立方体顶点和纹理坐标数据
    private float[] vertices = {
            -0.5f, -0.5f, -0.5f,  0.0f, 0.0f,
            0.5f, -0.5f, -0.5f,  1.0f, 0.0f,
            0.5f,  0.5f, -0.5f,  1.0f, 1.0f,
            0.5f,  0.5f, -0.5f,  1.0f, 1.0f,
            -0.5f,  0.5f, -0.5f,  0.0f, 1.0f,
            -0.5f, -0.5f, -0.5f,  0.0f, 0.0f,

            -0.5f, -0.5f,  0.5f,  0.0f, 0.0f,
            0.5f, -0.5f,  0.5f,  1.0f, 0.0f,
            0.5f,  0.5f,  0.5f,  1.0f, 1.0f,
            0.5f,  0.5f,  0.5f,  1.0f, 1.0f,
            -0.5f,  0.5f,  0.5f,  0.0f, 1.0f,
            -0.5f, -0.5f,  0.5f,  0.0f, 0.0f,

            -0.5f,  0.5f,  0.5f,  1.0f, 0.0f,
            -0.5f,  0.5f, -0.5f,  1.0f, 1.0f,
            -0.5f, -0.5f, -0.5f,  0.0f, 1.0f,
            -0.5f, -0.5f, -0.5f,  0.0f, 1.0f,
            -0.5f, -0.5f,  0.5f,  0.0f, 0.0f,
            -0.5f,  0.5f,  0.5f,  1.0f, 0.0f,

            0.5f,  0.5f,  0.5f,  1.0f, 0.0f,
            0.5f,  0.5f, -0.5f,  1.0f, 1.0f,
            0.5f, -0.5f, -0.5f,  0.0f, 1.0f,
            0.5f, -0.5f, -0.5f,  0.0f, 1.0f,
            0.5f, -0.5f,  0.5f,  0.0f, 0.0f,
            0.5f,  0.5f,  0.5f,  1.0f, 0.0f,

            -0.5f, -0.5f, -0.5f,  0.0f, 1.0f,
            0.5f, -0.5f, -0.5f,  1.0f, 1.0f,
            0.5f, -0.5f,  0.5f,  1.0f, 0.0f,
            0.5f, -0.5f,  0.5f,  1.0f, 0.0f,
            -0.5f, -0.5f,  0.5f,  0.0f, 0.0f,
            -0.5f, -0.5f, -0.5f,  0.0f, 1.0f,

            -0.5f,  0.5f, -0.5f,  0.0f, 1.0f,
            0.5f,  0.5f, -0.5f,  1.0f, 1.0f,
            0.5f,  0.5f,  0.5f,  1.0f, 0.0f,
            0.5f,  0.5f,  0.5f,  1.0f, 0.0f,
            -0.5f,  0.5f,  0.5f,  0.0f, 0.0f,
            -0.5f,  0.5f, -0.5f,  0.0f, 1.0f
    };

    private float[][] cubePositions = {
            {0.0f,  0.0f,  0.0f},
            { 2.0f,  5.0f, -15.0f},
            {-1.5f, -2.2f, -2.5f},
            {-3.8f, -2.0f, -12.3f},
            { 2.4f, -0.4f, -3.5f},
            {-1.7f,  3.0f, -7.5f},
            {1.3f, -2.0f, -2.5f},
            {1.5f,  2.0f, -2.5f},
            {1.5f,  0.2f, -1.5f},
            {-1.3f,  1.0f, -1.5f}
    };



    public Color(Context context) {
        String vertexSource = GlslReader.readSourceFromFile(context, R.raw.color_vertex);
        int vertexShader = ProgramHelper.getShaderFromSource(GLES30.GL_VERTEX_SHADER, vertexSource);
        String fragmentSource = GlslReader.readSourceFromFile(context, R.raw.color_fragment);
        int fragmentShader = ProgramHelper.getShaderFromSource(GLES30.GL_FRAGMENT_SHADER, fragmentSource);

        program = ProgramHelper.createProgram(vertexShader, fragmentShader);

        verticesBuffer = ProgramHelper.float2FloatBuffer(vertices);

        GLES30.glGenVertexArrays(1, VAO, 0);
        GLES30.glGenBuffers(1, VBO, 0);

        GLES30.glBindVertexArray(VAO[0]);

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, VBO[0]);
        GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER, vertices.length * 4, verticesBuffer, GLES30.GL_STATIC_DRAW);
        //定点坐标
        GLES30.glVertexAttribPointer(0, 3, GLES30.GL_FLOAT, false, STRIDE, 0);
        GLES30.glEnableVertexAttribArray(0);

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, 0);
        GLES30.glBindVertexArray(0);

        Matrix.setIdentityM(viewMatrix, 0);
        Matrix.translateM(viewMatrix, 0, -0.5f, 0.0f, -5.0f);
        Matrix.setIdentityM(modelMatrix, 0);
        Matrix.rotateM(viewMatrix, 0, 30, 1.0f, 1.0f, 0.0f);
        Matrix.setIdentityM(projectionMatrix, 0);
        Matrix.perspectiveM(projectionMatrix, 0, 45.0f, 1.0f, 0.1f, 100.0f);
    }

    public void draw() {
        GLES30.glUseProgram(program);


        GLES30.glUniformMatrix4fv(GLES30.glGetUniformLocation(program, "view"), 1, false,
                viewMatrix, 0);
        GLES30.glUniformMatrix4fv(GLES30.glGetUniformLocation(program, "model"), 1, false,
                modelMatrix, 0);
        GLES30.glUniformMatrix4fv(GLES30.glGetUniformLocation(program, "projection"), 1, false,
                projectionMatrix, 0);

        GLES30.glUniform3f(GLES30.glGetUniformLocation(program,"objectColor"),1.0f,0.5f,0.31f);
        GLES30.glUniform3f(GLES30.glGetUniformLocation(program,"lightColor"),1.0f,1.0f,1.0f);


        GLES30.glBindVertexArray(VAO[0]);

        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, 36);

        GLES30.glBindVertexArray(0);
    }

    public void release() {
        GLES30.glDeleteVertexArrays(1, VAO, 0);
        GLES30.glDeleteBuffers(1, VBO, 0);
    }
}
