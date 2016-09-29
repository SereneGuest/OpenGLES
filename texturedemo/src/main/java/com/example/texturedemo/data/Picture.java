package com.example.texturedemo.data;

import android.content.Context;
import android.opengl.GLES30;

import com.example.texturedemo.R;
import com.example.texturedemo.utils.GlslReader;
import com.example.texturedemo.utils.ProgramHelper;
import com.example.texturedemo.utils.TextureHelper;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by wenzhe on 9/9/16.
 */

public class Picture {

    private int mPositionHandle;
    private int mTexCoordHandle;

    private int vertexShaderHandle;
    private int fragmentShaderHandle;
    private int programHandle;
    private int mTextureHandle;

    private FloatBuffer mModelDataBuffer;
    private FloatBuffer mTexCoordBuffer;

    private final int mBytePerFloat = 4;
    private final int mPositionDataSize = 3;
    private final int mTexCoordSize = 2;

    private static final float[] positionData = {
            -1.0f, 1.0f, 0.0f,
            -1.0f, -1.0f, 0.0f,
            1.0f, 1.0f, 0.0f,
            1.0f, -1.0f, 0.0f

    };

    private static final float[] texCoord = {
            0.0f,0.0f,
            0.0f,1.0f,
            1.0f,0.0f,
            1.0f,1.0f

    };

    public Picture(Context context) {
        mModelDataBuffer = ByteBuffer.allocateDirect(positionData.length * mBytePerFloat)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        mModelDataBuffer.put(positionData).position(0);
        mTexCoordBuffer = ByteBuffer.allocateDirect(texCoord.length * mBytePerFloat)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        mTexCoordBuffer.put(texCoord).position(0);

        String vertexShader = GlslReader.readSourceFromFile(context, R.raw.vertex);
        String fragmentShader = GlslReader.readSourceFromFile(context, R.raw.fragment);

        vertexShaderHandle = ProgramHelper.getShaderFromSource(GLES30.GL_VERTEX_SHADER, vertexShader);
        fragmentShaderHandle = ProgramHelper.getShaderFromSource(GLES30.GL_FRAGMENT_SHADER, fragmentShader);

        programHandle = ProgramHelper.createProgram(vertexShaderHandle, fragmentShaderHandle,
                new String[]{"a_Position", "a_TexCoord"});

        mTextureHandle = TextureHelper.createAndLoadTexture(context, R.mipmap.img2);
    }

    public void draw() {
        GLES30.glUseProgram(programHandle);
        //mTextureHandle = GLES30.glGetUniformLocation(programHandle, "u_Texture");
        //int tmp = GLES30.glGetUniformLocation(programHandle, "u_Texture");
        mPositionHandle = GLES30.glGetAttribLocation(programHandle, "a_Position");
        mTexCoordHandle = GLES30.glGetAttribLocation(programHandle, "a_TexCoord");


        GLES30.glActiveTexture(GLES30.GL_TEXTURE0);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, mTextureHandle);

        GLES30.glVertexAttribPointer(mPositionHandle, mPositionDataSize, GLES30.GL_FLOAT, false, 0, mModelDataBuffer);
        GLES30.glEnableVertexAttribArray(mPositionHandle);

        GLES30.glVertexAttribPointer(mTexCoordHandle, mTexCoordSize, GLES30.GL_FLOAT, false, 0, mTexCoordBuffer);
        GLES30.glEnableVertexAttribArray(mTexCoordHandle);

        GLES30.glDrawArrays(GLES30.GL_TRIANGLE_STRIP, 0, 4);

    }
}
