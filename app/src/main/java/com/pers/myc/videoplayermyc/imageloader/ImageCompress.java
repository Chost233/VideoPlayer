package com.pers.myc.videoplayermyc.imageloader;

import android.graphics.Bitmap;
import android.graphics.Matrix;

/**
 * 图片压缩
 */

public class ImageCompress {

    //模式：
    //填充整个控件
    public static final int FILL_UP = 0;
    //按照原比例（设置宽）
    public static final int SCALE_WIDTH = 1;
    //按照原比例（设置高）
    public static final int SCALE_HEIGHT = 2;

    //压缩图片
    public static Bitmap compress(Bitmap bitmap, int width, int height, int model) {
        if (width == 0 || height == 0) {
            return bitmap;
        } else {
            Bitmap bmp = null;
            float w = 0f;
            float h = 0f;
            switch (model) {
                case 0:
                    w = ((float) width) / bitmap.getWidth();
                    h = ((float) height) / bitmap.getHeight();
                    break;
                case 1:
                    w = ((float) width) / bitmap.getWidth();
                    h = w;
                    break;
                case 2:
                    h = ((float) height) / bitmap.getHeight();
                    w = h;
                    break;
            }
            Matrix matrix = new Matrix();
            matrix.postScale(w, h);
            bmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            return bmp;
        }
    }
}
