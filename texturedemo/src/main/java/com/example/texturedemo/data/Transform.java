package com.example.texturedemo.data;

import android.content.Context;
import android.opengl.GLES30;
import android.opengl.Matrix;
import android.os.SystemClock;
import android.util.Log;

import com.example.texturedemo.R;
import com.example.texturedemo.utils.GlslReader;
import com.example.texturedemo.utils.ProgramHelper;
import com.example.texturedemo.utils.TextureHelper;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

/**
 * Created by wenzhe on 9/21/16.
 */

public class Transform {

    private int program;
    private int textureHandle;
    private int textureHandle1;

    private FloatBuffer verticesBuffer;
    private IntBuffer indicesBuffer;

    private int STRIDE = 32;

    private float[] transformMatrix = new float[16];

    private int[] VBO = new int[1];
    private int[] VAO = new int[1];

    private float[] vertices = {
            1.0f, 1.0f, 0.0f,       1.0f, 0.0f, 0.0f,   1.0f,0.0f,
            1.0f, -1.0f, 0.0f,      0.0f, 1.0f, 0.0f,   1.0f,1.0f,
            -1.0f, -1.0f, 0.0f,     0.0f, 0.0f, 1.0f,   0.0f,1.0f,
            -1.0f, 1.0f, 0.0f,      0.0f, 0.0f, 0.0f,   0.0f,0.0f
    };

    private int[] indices = {
            0, 1, 3,
            1, 2, 3
    };

    public Transform(Context context) {
        String vertexSource = GlslReader.readSourceFromFile(context, R.raw.trans_vertex);
        int vertexShader = ProgramHelper.getShaderFromSource(GLES30.GL_VERTEX_SHADER, vertexSource);
        String fragmentSource = GlslReader.readSourceFromFile(context, R.raw.trans_fragment);
        int fragmentShader = ProgramHelper.getShaderFromSource(GLES30.GL_FRAGMENT_SHADER, fragmentSource);

        program = ProgramHelper.createProgram(vertexShader, fragmentShader);

        verticesBuffer = ProgramHelper.float2FloatBuffer(vertices);
        indicesBuffer = ProgramHelper.int2IntBuffer(indices);

        GLES30.glGenVertexArrays(1, VAO, 0);
        GLES30.glGenBuffers(1, VBO, 0);

        GLES30.glBindVertexArray(VAO[0]);

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, VBO[0]);
        GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER, vertices.length * 4, verticesBuffer, GLES30.GL_STATIC_DRAW);

        GLES30.glVertexAttribPointer(0, 3, GLES30.GL_FLOAT, false, STRIDE, 0);
        GLES30.glEnableVertexAttribArray(0);
        GLES30.glVertexAttribPointer(1, 3, GLES30.GL_FLOAT, false, STRIDE, 12);
        GLES30.glEnableVertexAttribArray(1);
        GLES30.glVertexAttribPointer(2, 2, GLES30.GL_FLOAT, false, STRIDE, 24);
        GLES30.glEnableVertexAttribArray(2);

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, 0);
        GLES30.glBindVertexArray(0);

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

        Matrix.setIdentityM(transformMatrix, 0);
        Matrix.translateM(transformMatrix, 0, 0.5f, -0.5f, 0f);
        Matrix.scaleM(transformMatrix, 0, 0.3f, 0.3f, 0.3f);
        Matrix.rotateM(transformMatrix, 0,30.0f, 0.0f, 0.0f, 1.0f);
    }


    public void draw() {
        GLES30.glUseProgram(program);

        int transformLoc = GLES30.glGetUniformLocation(program, "transform");
        GLES30.glUniformMatrix4fv(transformLoc, 1, false, transformMatrix, 0);


        GLES30.glBindVertexArray(VAO[0]);
        GLES30.glDrawElements(GLES30.GL_TRIANGLES, 6, GLES30.GL_UNSIGNED_INT, indicesBuffer);
        GLES30.glBindVertexArray(0);
    }

    public void release() {
        GLES30.glDeleteVertexArrays(1, VAO, 0);
        GLES30.glDeleteBuffers(1, VBO, 0);
    }
}
