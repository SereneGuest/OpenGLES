package com.westalgo.camera.data;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;

import com.smewise.camera2.R;
import com.westalgo.camera.utils.GlslReader;
import com.westalgo.camera.utils.ProgramHelper;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

/**
 * Created by wenzhe on 9/21/16.
 */

public class CameraNV21 {

    private int program;
    private int mPositionHandle;
    private int mTexCoordHandle;
    private int mYTextureHandle;
    private int mUTextureHandle;

    private int[] mYTextureID = new int[1];
    private int[] mUTextureID = new int[1];

    private int mWidth;
    private int mHeight;

    private FloatBuffer modelDataBuffer;
    private FloatBuffer texDataBuffer;

    private float[] transformMatrix = new float[16];


    private final float[] positionData = {
            -1.0f, 1.0f,
            -1.0f, -1.0f,
            1.0f, 1.0f,
            1.0f, -1.0f,
    };

    private final float[] texCoord = {
            0.0f, 1.0f,
            1.0f, 1.0f,
            0.0f, 0.0f,
            1.0f, 0.0f

    };

    public CameraNV21(Context context, int width, int height) {
        mWidth = width;
        mHeight = height;
        String vertexSource = GlslReader.readSourceFromFile(context, R.raw.camera_nv21_vertex);
        int vertexShader = ProgramHelper.getShaderFromSource(GLES20.GL_VERTEX_SHADER, vertexSource);
        String fragmentSource = GlslReader.readSourceFromFile(context, R.raw.camera_nv21_fragment);
        int fragmentShader = ProgramHelper.getShaderFromSource(GLES20.GL_FRAGMENT_SHADER, fragmentSource);

        program = ProgramHelper.createProgram(vertexShader, fragmentShader, new
                String[]{"a_Position", "a_TexCoord"});

        if (program != -1) {
            mPositionHandle = GLES20.glGetAttribLocation(program, "a_Position");
            mTexCoordHandle = GLES20.glGetAttribLocation(program, "a_TexCoord");
            mYTextureHandle = GLES20.glGetUniformLocation(program, "SamplerY");
            mUTextureHandle = GLES20.glGetUniformLocation(program, "SamplerU");
        }

        modelDataBuffer = ProgramHelper.float2FloatBuffer(positionData);
        texDataBuffer = ProgramHelper.float2FloatBuffer(texCoord);

        GLES20.glEnable(GLES20.GL_TEXTURE_2D);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE1);

        createTextureId(mWidth, mHeight, GLES20.GL_LUMINANCE, mYTextureID);
        createTextureId(mWidth >> 1, mHeight >> 1, GLES20.GL_LUMINANCE_ALPHA, mUTextureID);

        Matrix.setIdentityM(transformMatrix, 0);
        Matrix.translateM(transformMatrix, 0, 0.5f, -0.5f, 0f);
        Matrix.scaleM(transformMatrix, 0, 0.3f, 0.3f, 0.3f);
        Matrix.rotateM(transformMatrix, 0,30.0f, 0.0f, 0.0f, 1.0f);
    }

    private void createTextureId(int width, int height, int format, int[] textureId) {
        //create and bind texture
        GLES20.glGenTextures(1, textureId, 0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId[0]);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, format, width, height,
                0, format, GLES20.GL_UNSIGNED_BYTE, null);
    }


    public void draw(ByteBuffer yBuffer,ByteBuffer uBuffer, ByteBuffer vBuffer) {
        GLES20.glUseProgram(program);

        vBuffer.position(0);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mYTextureID[0]);
        GLES20.glTexSubImage2D(GLES20.GL_TEXTURE_2D, 0, 0, 0, mWidth, mHeight,
                GLES20.GL_LUMINANCE, GLES20.GL_UNSIGNED_BYTE, yBuffer);
        uBuffer.position(0);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mUTextureID[0]);
        GLES20.glTexSubImage2D(GLES20.GL_TEXTURE_2D, 0, 0, 0, mWidth >> 1, mHeight >> 1,
                GLES20.GL_LUMINANCE_ALPHA, GLES20.GL_UNSIGNED_BYTE, uBuffer);

        GLES20.glUniform1i(mYTextureHandle, 0);
        GLES20.glUniform1i(mUTextureHandle, 1);

        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glEnableVertexAttribArray(mTexCoordHandle);

        modelDataBuffer.position(0);
        GLES20.glVertexAttribPointer(mPositionHandle, 2, GLES20.GL_FLOAT, false, 0, modelDataBuffer);

        texDataBuffer.position(0);
        GLES20.glVertexAttribPointer(mTexCoordHandle, 2, GLES20.GL_FLOAT, false, 0, texDataBuffer);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
        GLES20.glDisableVertexAttribArray(mPositionHandle);
        GLES20.glDisableVertexAttribArray(mTexCoordHandle);
    }

    public void release() {
    }
}
