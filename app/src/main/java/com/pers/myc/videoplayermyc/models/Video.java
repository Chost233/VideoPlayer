package com.pers.myc.videoplayermyc.models;

import android.graphics.Bitmap;

/**
 * Created by Administrator on 2017/5/20.
 */

public class Video {
    //id
    private String id;

    //正文
    private String text;
    //点赞数
    private String love;
    //点踩数
    private String hate;
    //作者头像
    private String profile_image;
    //作者名
    private String name;
    //微信链接
    private String weixin_url;
    //发布时间
    private String create_time;
    //第一帧图片
    private Bitmap firstFrame;

    public Bitmap getFirstFrame() {
        return firstFrame;
    }

    public void setFirstFrame(Bitmap firstFrame) {
        this.firstFrame = firstFrame;
    }

    public Video(String text, String love, String hate, String profile_image, String name, String weixin_url, String create_time, String video_uri, String id) {
        this.text = text;
        this.love = love;
        this.hate = hate;
        this.profile_image = profile_image;
        this.name = name;
        this.weixin_url = weixin_url;
        this.create_time = create_time;
        this.video_uri = video_uri;
        this.id = id;
        firstFrame = null;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getLove() {
        return love;
    }

    public void setLove(String love) {
        this.love = love;
    }

    public String getHate() {
        return hate;
    }

    public void setHate(String hate) {
        this.hate = hate;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWeixin_url() {
        return weixin_url;
    }

    public void setWeixin_url(String weixin_url) {
        this.weixin_url = weixin_url;
    }

    public String getVideo_url() {
        return video_uri;
    }

    public void setVideo_uri(String video_uri) {
        this.video_uri = video_uri;
    }

    //视频链接
    private String video_uri;
}
