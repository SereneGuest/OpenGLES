package com.example.texturedemo.data;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLES30;

import com.example.texturedemo.R;
import com.example.texturedemo.utils.GlslReader;
import com.example.texturedemo.utils.ProgramHelper;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

/**
 * Created by wenzhe on 9/21/16.
 */

public class HelloTriangel2 {

    private int program;
    private FloatBuffer verticesBuffer;
    private IntBuffer indicesBuffer;

    private int[] VBO = new int[1];
    private int[] VAO = new int[1];

    private float[] vertices = {
            0.5f, 0.5f, 0.0f,       1.0f, 0.0f, 0.0f,
            0.5f, -0.5f, 0.0f,      0.0f, 1.0f, 0.0f,
            -0.5f, -0.5f, 0.0f,     0.0f, 0.0f, 1.0f,
            -0.5f, 0.5f, 0.0f,      0.0f, 0.0f, 0.0f,
    };

    private int[] indices = {
            0, 1, 3,
            1, 2, 3
    };

    public HelloTriangel2(Context context) {
        String vertexSource = GlslReader.readSourceFromFile(context, R.raw.ht_vertex);
        int vertexShader = ProgramHelper.getShaderFromSource(GLES30.GL_VERTEX_SHADER, vertexSource);
        String fragmentSource = GlslReader.readSourceFromFile(context, R.raw.ht_fragment);
        int fragmentShader = ProgramHelper.getShaderFromSource(GLES30.GL_FRAGMENT_SHADER, fragmentSource);

        program = ProgramHelper.createProgram(vertexShader, fragmentShader);

        verticesBuffer = ProgramHelper.float2FloatBuffer(vertices);
        indicesBuffer = ProgramHelper.int2IntBuffer(indices);

        GLES30.glGenVertexArrays(1, VAO, 0);
        GLES30.glGenBuffers(1, VBO, 0);

        GLES30.glBindVertexArray(VAO[0]);

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, VBO[0]);
        GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER, vertices.length * 4, verticesBuffer, GLES30.GL_STATIC_DRAW);

        GLES30.glVertexAttribPointer(0, 3, GLES30.GL_FLOAT, false, 24, 0);
        GLES30.glEnableVertexAttribArray(0);
        GLES30.glVertexAttribPointer(1, 3, GLES20.GL_FLOAT, false, 24, 12);
        GLES30.glEnableVertexAttribArray(1);

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, 0);
        GLES30.glBindVertexArray(0);

    }

    public void draw() {
        GLES30.glUseProgram(program);
        GLES30.glBindVertexArray(VAO[0]);
        GLES30.glDrawElements(GLES30.GL_TRIANGLES, 6, GLES30.GL_UNSIGNED_INT, indicesBuffer);
        GLES30.glBindVertexArray(0);
    }

    public void release() {
        GLES30.glDeleteVertexArrays(1, VAO, 0);
        GLES30.glDeleteBuffers(1, VBO, 0);
    }
}
