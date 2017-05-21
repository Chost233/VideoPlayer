package com.pers.myc.videoplayermyc.untils;

import android.app.DownloadManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Environment;

import com.pers.myc.videoplayermyc.listeners.BitmapCallBackListener;
import com.pers.myc.videoplayermyc.listeners.HttpCallbackListener;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

/**
 * 网络请求相关类
 */

//网络请求类
public class HttpUtil {
    //网络请求
    public static void sendHtttpRequest(final String address, final HttpCallbackListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    URL url = new URL(address);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    connection.setDoInput(true);
                    InputStream in = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuffer response = new StringBuffer();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    if (listener != null) {
                        listener.onFinish(response.toString(), in);
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    listener.onError(e);
                } catch (IOException e) {
                    e.printStackTrace();
                    listener.onError(e);
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

    //获取图片
    public static void downloadImage(final String imageUrl, final BitmapCallBackListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Bitmap bitmap;
                    URL url = new URL(imageUrl);
                    final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    bitmap = BitmapFactory.decodeStream(conn.getInputStream());
                    conn.disconnect();
                    listener.success(bitmap);
                } catch (Exception e) {
                    listener.err(e);
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //获取第一帧图片
    public static void createVideoThumbnail(final String url, final BitmapCallBackListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = null;
                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                try {
                    retriever.setDataSource(url, new HashMap<String, String>());
                    bitmap = retriever.getFrameAtTime();
                } catch (Exception e) {
                } finally {
                    try {
                        retriever.release();
                    } catch (RuntimeException e) {
                    }
                }
                listener.success(bitmap);
            }
        }).start();
    }

    //下载文件
    public static void downloadFile(String url, String title, String fileName, Context context) {
        // 创建下载请求
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        //通知栏可见
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        // 设置通知的标题和描述
        request.setTitle(title);
        request.setDescription(fileName);
        //设置wifi环境下载
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
        // 设置下载文件的保存位置
        File saveFile = new File(Environment.getExternalStorageDirectory(), fileName);
        request.setDestinationUri(Uri.fromFile(saveFile));
        //获取下载服务
        DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        // 加入下载队列，获取id
        long downloadId = manager.enqueue(request);
    }

    //获取下载视频路径
    public static String getVideoPath(String fileName) {
        File file = new File(Environment.getExternalStorageDirectory(), fileName);
        if (file.exists())
            return file.getPath();
        else
            return "";
    }
}
