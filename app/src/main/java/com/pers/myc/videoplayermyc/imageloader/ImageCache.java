package com.pers.myc.videoplayermyc.imageloader;

import android.graphics.Bitmap;

/**
 * 图片缓存接口
 */

public interface ImageCache {
    //从缓存中获取图片
    public Bitmap get(String url);

    //将图片存入缓存
    public void put(String url, Bitmap bmp);
}
