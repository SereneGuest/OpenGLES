package com.example.texturedemo.data;

import android.content.Context;
import android.opengl.GLES30;
import android.opengl.Matrix;
import android.os.SystemClock;

import com.example.texturedemo.R;
import com.example.texturedemo.utils.GlslReader;
import com.example.texturedemo.utils.ProgramHelper;
import com.example.texturedemo.utils.TextureHelper;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

/**
 * Created by wenzhe on 9/21/16.
 */

public class Cube {

    private int program;
    private int textureHandle;
    private int textureHandle1;

    private FloatBuffer verticesBuffer;

    private int STRIDE = 20;

    //private float[] transformMatrix = new float[16];
    //private float[] modelMatrix = new float[16];
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



    public Cube(Context context) {
        String vertexSource = GlslReader.readSourceFromFile(context, R.raw.cube_vertex);
        int vertexShader = ProgramHelper.getShaderFromSource(GLES30.GL_VERTEX_SHADER, vertexSource);
        String fragmentSource = GlslReader.readSourceFromFile(context, R.raw.cube_fragment);
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
        //纹理坐标
        GLES30.glVertexAttribPointer(2, 2, GLES30.GL_FLOAT, false, STRIDE, 12);
        GLES30.glEnableVertexAttribArray(2);

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, 0);
        GLES30.glBindVertexArray(0);
        //两个纹理
        textureHandle = TextureHelper.createAndLoadTexture(context, R.mipmap.img2);
        textureHandle1 = TextureHelper.createAndLoadTexture(context,R.mipmap.img1);

        GLES30.glActiveTexture(GLES30.GL_TEXTURE0);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textureHandle);
        GLES30.glUniform1i(GLES30.glGetUniformLocation(program,"ourTexture"),0);
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D,GLES30.GL_TEXTURE_WRAP_S,GLES30.GL_MIRRORED_REPEAT);
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D,GLES30.GL_TEXTURE_WRAP_S,GLES30.GL_MIRRORED_REPEAT);

        GLES30.glActiveTexture(GLES30.GL_TEXTURE1);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textureHandle1);
        GLES30.glUniform1i(GLES30.glGetUniformLocation(program,"ourTexture1"),1);
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D,GLES30.GL_TEXTURE_WRAP_S,GLES30.GL_CLAMP_TO_EDGE);
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D,GLES30.GL_TEXTURE_WRAP_T,GLES30.GL_CLAMP_TO_EDGE);


        Matrix.setIdentityM(viewMatrix, 0);
        Matrix.translateM(viewMatrix, 0, 0.0f, 0.0f, -5.0f);
        Matrix.setIdentityM(projectionMatrix, 0);
        Matrix.perspectiveM(projectionMatrix, 0, 45.0f, 1.0f, 0.1f, 100.0f);
    }


    private int angle =0;
    public void draw() {
        GLES30.glUseProgram(program);

        GLES30.glUniformMatrix4fv(GLES30.glGetUniformLocation(program, "view"), 1, false,
                viewMatrix, 0);
        GLES30.glUniformMatrix4fv(GLES30.glGetUniformLocation(program, "projection"), 1, false,
                projectionMatrix, 0);


        GLES30.glBindVertexArray(VAO[0]);
        //GLES30.glDrawElements(GLES30.GL_TRIANGLES, 6, GLES30.GL_UNSIGNED_INT, indicesBuffer);
        for (int i=0;i<cubePositions.length;i++) {
            float[] modelTmp = new float[16];
            Matrix.setIdentityM(modelTmp, 0);
            Matrix.translateM(modelTmp,0,cubePositions[i][0],cubePositions[i][1],
                    cubePositions[i][2]);
            Matrix.rotateM(modelTmp, 0, angle, i%2, 0.3f, i%2);
            GLES30.glUniformMatrix4fv(GLES30.glGetUniformLocation(program, "model"), 1, false,
                    modelTmp, 0);

            GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, 36);
        }
        angle ++;

        GLES30.glBindVertexArray(0);
    }

    public void release() {
        GLES30.glDeleteVertexArrays(1, VAO, 0);
        GLES30.glDeleteBuffers(1, VBO, 0);
    }
}
