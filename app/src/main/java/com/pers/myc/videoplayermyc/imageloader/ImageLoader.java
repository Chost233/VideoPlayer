package com.pers.myc.videoplayermyc.imageloader;

import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

import com.pers.myc.videoplayermyc.listeners.BitmapCallBackListener;
import com.pers.myc.videoplayermyc.untils.HttpUtil;

/**
 * 图片加载类
 */

public class ImageLoader {
    //图片缓存
    private ImageCache mCache = new ImageMemoryCache();

    //上下文
    private Activity mActivity;
    //图片大小
    private int mImageHeight = 0;
    private int mImageWidth = 0;

    public void setCache(ImageCache cache) {
        mCache = cache;
    }

    public void setCompressSize(int height) {
        this.mImageHeight = height;
    }

    public ImageLoader(Activity activity) {
        this.mActivity = activity;
    }

    public void displayImage(final String url, final ImageView imageView) {
        //初始化图片大小
        mImageHeight = imageView.getHeight();
        mImageWidth = imageView.getWidth();
        //内存中是否缓存
        Bitmap bitmap = mCache.get(url);
        if (bitmap != null) {
            Bitmap bmp = ImageCompress.compress(bitmap, mImageWidth, mImageHeight, ImageCompress.SCALE_HEIGHT);
            imageView.setImageBitmap(bmp);
            return;
        }
        imageView.setTag(url);

        HttpUtil.downloadImage(url, new BitmapCallBackListener() {
            @Override
            public void success(final Bitmap bitmap) {
                if (bitmap == null) {
                    return;
                }
                if (imageView.getTag().equals(url)) {
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Bitmap bmp = ImageCompress.compress(bitmap, mImageWidth, mImageHeight, ImageCompress.FILL_UP);
                            imageView.setImageBitmap(bmp);
                        }
                    });
                }
                mCache.put(url, bitmap);
            }

            @Override
            public void err(Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void displayFirstFrame(final String url, final ImageView imageView) {
        //初始化图片大小
        mImageHeight = imageView.getHeight();
        mImageWidth = imageView.getWidth();
        //内存中是否缓存
        Bitmap bitmap = mCache.get(url);
        if (bitmap != null) {
            Bitmap bmp = ImageCompress.compress(bitmap, mImageWidth, mImageHeight, ImageCompress.SCALE_HEIGHT);
            imageView.setImageBitmap(bmp);
            Log.e("图片","存在缓存中");
            return;
        }else {
            Log.e("图片","不存在缓存中");
        }
        imageView.setTag(url);

        HttpUtil.createVideoThumbnail(url, new BitmapCallBackListener() {
            @Override
            public void success(final Bitmap bitmap) {
                if (bitmap == null) {
                    return;
                }
                if (imageView.getTag().equals(url)) {
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Bitmap bmp = ImageCompress.compress(bitmap, mImageWidth, mImageHeight, ImageCompress.FILL_UP);
                            imageView.setImageBitmap(bmp);
                        }
                    });
                }
                mCache.put(url, bitmap);
            }

            @Override
            public void err(Exception e) {
                e.printStackTrace();
            }
        });
    }
}
