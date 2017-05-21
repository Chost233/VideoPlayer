package com.pers.myc.videoplayermyc.imageloader;

import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * 图片内存缓存
 */

public class ImageMemoryCache implements ImageCache{

    //图片列表缓存
    LruCache<String, Bitmap> mImageCache;

    public ImageMemoryCache() {
        initImageCache();
    }

    private void initImageCache() {
        //计算可用最大内存
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        //取四分之一的可用内存作为缓存
        final int cacheSize = maxMemory / 4;
        mImageCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight() / 1024;
            }
        };
    }

    public Bitmap get(String url) {
        return mImageCache.get(url);
    }

    public void put(String url, Bitmap bitmap) {
        mImageCache.put(url, bitmap);
    }
}
