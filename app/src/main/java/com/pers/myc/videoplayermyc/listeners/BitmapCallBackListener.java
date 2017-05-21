package com.pers.myc.videoplayermyc.listeners;

import android.graphics.Bitmap;

/**
 * 网络回调接口
 */

public interface BitmapCallBackListener {
    public void success(Bitmap bitmap);
    public void err(Exception e);
}
