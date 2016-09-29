package com.example.texturedemo.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES30;
import android.opengl.GLUtils;

/**
 * Created by wenzhe on 9/9/16.
 */

public class TextureHelper {
    public static int createAndLoadTexture(Context context, int resId) {
        int[] texturesHandle = new int[1];
        GLES30.glGenTextures(1, texturesHandle, 0);
        if (texturesHandle[0] != 0) {
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resId);
            GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, texturesHandle[0]);
            GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MAG_FILTER, GLES30.GL_LINEAR);
            GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER, GLES30.GL_LINEAR);
            GLUtils.texImage2D(GLES30.GL_TEXTURE_2D, 0, bitmap, 0);
            GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, 0);
            bitmap.recycle();
        } else {
            throw new RuntimeException("texture error");
        }
        return texturesHandle[0];
    }
}
