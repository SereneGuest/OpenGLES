package com.example.texturedemo.utils;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by wenzhe on 9/9/16.
 */

public class GlslReader {
    public static String readSourceFromFile(Context context, int resId) {
        InputStream inputStream = context.getResources().openRawResource(resId);
        InputStreamReader reader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(reader);
        String str;
        StringBuilder builder = new StringBuilder();
        try {
            while ((str = bufferedReader.readLine()) != null) {
                builder.append(str);
                builder.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Log.e("Source", builder.toString());
        return builder.toString();
    }
}
