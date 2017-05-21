package com.pers.myc.videoplayermyc.imageloader;

import android.graphics.Bitmap;

/**
 * 双缓存
 */

public class ImageDoubleCache implements ImageCache {
    //内存缓存
    ImageMemoryCache mImageMemoryCache = new ImageMemoryCache();
    //sd卡缓存
    ImageDiskCache mImageDiskCache = new ImageDiskCache();

    @Override
    public Bitmap get(String url) {
        Bitmap bitmap = mImageMemoryCache.get(url);
        if (bitmap != null) {
            return bitmap;
        }
        bitmap = mImageDiskCache.get(url);
        if (bitmap != null) {
            return bitmap;
        }
        return null;
    }

    @Override
    public void put(String url, Bitmap bmp) {
        mImageDiskCache.put(url, bmp);
        mImageMemoryCache.put(url, bmp);
    }
}
