package com.example.texturedemo.utils;

import android.opengl.GLES30;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

/**
 * Created by wenzhe on 9/9/16.
 */

public class ProgramHelper {

    private static String TAG = ProgramHelper.class.getSimpleName();

    public static int getShaderFromSource(int type, String source) {
        int shaderHandle = GLES30.glCreateShader(type);
        GLES30.glShaderSource(shaderHandle, source);
        if (shaderHandle != 0) {
            GLES30.glCompileShader(shaderHandle);
            int[] compileStatue = new int[1];
            GLES30.glGetShaderiv(shaderHandle, GLES30.GL_COMPILE_STATUS, compileStatue, 0);
            if (compileStatue[0] == 0) {
                Log.e(TAG, "compile shader error:" + GLES30.glGetShaderInfoLog(shaderHandle));
                GLES30.glDeleteShader(shaderHandle);
                shaderHandle = 0;
            }
        }
        if (shaderHandle == 0) {
            throw new RuntimeException("error create shader");
        }
        return shaderHandle;
    }

    public static int createProgram(int vertexShader, int fragmentShader, String attributes[]) {
        int program = GLES30.glCreateProgram();
        if (program != 0) {
            GLES30.glAttachShader(program, vertexShader);
            GLES30.glAttachShader(program, fragmentShader);
            if (attributes != null) {
                int length = attributes.length;
                for (int i = 0; i < length; i++) {
                    GLES30.glBindAttribLocation(program, i, attributes[i]);
                }
                GLES30.glLinkProgram(program);
                int[] linkStatus = new int[1];
                GLES30.glGetProgramiv(program, GLES30.GL_LINK_STATUS, linkStatus, 0);
                if (linkStatus[0] == 0) {
                    Log.e(TAG, "link error:" + GLES30.glGetProgramInfoLog(program));
                    GLES30.glDeleteProgram(program);
                    program = 0;
                }
            }
        }
        if (program == 0) {
            throw new RuntimeException("error create program");
        }
        return program;
    }

    public static int createProgram(int vertexShader, int fragmentShader) {
        int program = GLES30.glCreateProgram();
        if (program != 0) {
            GLES30.glAttachShader(program, vertexShader);
            GLES30.glAttachShader(program, fragmentShader);
            GLES30.glLinkProgram(program);
            int[] linkStatus = new int[1];
            GLES30.glGetProgramiv(program, GLES30.GL_LINK_STATUS, linkStatus, 0);
            if (linkStatus[0] == 0) {
                Log.e(TAG, "link error:" + GLES30.glGetProgramInfoLog(program));
                GLES30.glDeleteProgram(program);
                program = 0;
            }
        }
        if (program == 0) {
            throw new RuntimeException("error create program");
        }
        GLES30.glDeleteShader(vertexShader);
        GLES30.glDeleteShader(fragmentShader);
        return program;
    }

    public static FloatBuffer float2FloatBuffer(float[] data) {
        FloatBuffer result;
        ByteBuffer bb = ByteBuffer.allocateDirect(data.length * 4);
        bb.order(ByteOrder.nativeOrder());
        result = bb.asFloatBuffer();
        result.put(data);
        result.position(0);
        return result;
    }
    public static IntBuffer int2IntBuffer(int[] data) {
        IntBuffer result;
        ByteBuffer bb = ByteBuffer.allocateDirect(data.length * 4);
        bb.order(ByteOrder.nativeOrder());
        result = bb.asIntBuffer();
        result.put(data);
        result.position(0);
        return result;
    }
}
