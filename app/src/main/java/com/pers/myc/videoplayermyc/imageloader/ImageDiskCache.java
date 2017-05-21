package com.pers.myc.videoplayermyc.imageloader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 图片sd卡缓存
 */

public class ImageDiskCache implements ImageCache {
    //内存卡缓存地址
    private final String CACHE_DIR = "/sdcard/ImageCache/ImageLoading/";

    //判断文件夹是否存在
    public ImageDiskCache() {
        File file = new File(CACHE_DIR);
        if (!file.exists()) {
            boolean iscreate = file.mkdirs();
        }
    }

    //获取图片
    public Bitmap get(String url) {
        File file = null;
        if ((file = new File(CACHE_DIR + genereteFileName(url))).exists()) {
            return BitmapFactory.decodeFile(CACHE_DIR + genereteFileName(url));
        }
        return null;
    }

    //缓存图片
    public void put(String url, Bitmap bmp) {
        FileOutputStream fileOutputStream = null;
        try {
            Log.e("path:", CACHE_DIR + genereteFileName(url));
            fileOutputStream = new FileOutputStream(CACHE_DIR + genereteFileName(url));
            bmp.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //文件名处理
    private String genereteFileName(String url) {
        String fileName = "";
        url = url.replace("/", "");
        url = url.replace("http:", "");
        url = url.replace("https:", "");
        url = url.replace(".", "");
        fileName = url;
        return fileName;
    }
}
