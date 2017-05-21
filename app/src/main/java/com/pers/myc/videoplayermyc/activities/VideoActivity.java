package com.pers.myc.videoplayermyc.activities;

import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import com.pers.myc.videoplayermyc.R;
import com.pers.myc.videoplayermyc.listeners.VideoCompleteListener;
import com.pers.myc.videoplayermyc.untils.HttpUtil;
import com.pers.myc.videoplayermyc.untils.VideoPlayUntil;

import java.util.ArrayList;

public class VideoActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    //视频播放状态
    private int mVideoState;
    //播放按钮
    private ImageView mFirstStartButton;
    //下载按钮
    private ImageView mVideoDownloadButton;
    //视频状态条
    private LinearLayout mStateLayout;
    //播放暂停
    private ImageView mStartPausrButton;
    //上下一个视频按钮
    private ImageView mLastVideo;
    private ImageView mNextVideo;
    //VideoPlayer
    private VideoPlayUntil mVideoPlayUntil;
    //播放界面
    private SurfaceView mVideoSFV;
    //进度条
    private SeekBar mSeekBar;
    //视频链接列表
    private ArrayList<String> mVideoUrlList;
    //视频id列表
    private ArrayList<String> mVideoIdList;
    //视频位置
    private int mPosition;
    //视频准备
    private boolean isReady;
    //SharedPreferences保存视频播放时间
    private SharedPreferences mSharedPreferences;
    //SharedPreferences保存播放记录
    private SharedPreferences mHistorySharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        //初始化数据
        initData();

        //初始化视图
        initView();

        //初始化videoplayer
        mVideoPlayUntil = new VideoPlayUntil(mVideoSFV, mSeekBar);
        mVideoPlayUntil.setVideoCompleteListener(new VideoCompleteListener() {
            @Override
            public void videoComplete(int position) {
                saveSchedule(mVideoUrlList.get(mPosition), position);
            }
        });
        //隐藏状态条
        mStateLayout.setVisibility(View.GONE);
        //隐藏下载按钮
        mVideoDownloadButton.setVisibility(View.GONE);
    }

    private void initView() {
        //初始化view
        mStartPausrButton = (ImageView) findViewById(R.id.activity_video_start_pause);
        mVideoSFV = (SurfaceView) findViewById(R.id.activity_video_sv);
        mSeekBar = (SeekBar) findViewById(R.id.activity_video_seekbar);
        mNextVideo = (ImageView) findViewById(R.id.activity_video_next_video);
        mLastVideo = (ImageView) findViewById(R.id.activity_video_last_video);
        mFirstStartButton = (ImageView) findViewById(R.id.activity_video_first_start);
        mStateLayout = (LinearLayout) findViewById(R.id.activity_video_state);
        mVideoDownloadButton = (ImageView) findViewById(R.id.activity_video_download);

        //注册点击事件
        mVideoDownloadButton.setOnClickListener(this);
        mNextVideo.setOnClickListener(this);
        mLastVideo.setOnClickListener(this);
        mStartPausrButton.setOnClickListener(this);
        mFirstStartButton.setOnClickListener(this);

        //注册滑动事件
        mSeekBar.setOnSeekBarChangeListener(this);
    }

    private void initData() {
        mSharedPreferences = VideoActivity.this.getPreferences(0);
        mHistorySharedPreferences = VideoActivity.this.getSharedPreferences(HistoryActivity.HISTORY, 0);
        isReady = false;
        mVideoState = 1;
        mVideoUrlList = (ArrayList) getIntent().getExtras().get("urlList");
        mVideoIdList = (ArrayList) getIntent().getExtras().get("idList");
        mPosition = (int) getIntent().getExtras().get("index");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_video_start_pause:
                if (mVideoState == 0) {
                    //改变视频播放状态
                    mVideoState = 1;
                    mVideoPlayUntil.pause();
                    mStartPausrButton.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_video_start));
                } else if (mVideoState == 1) {
                    mVideoState = 0;
                    if (isReady)
                        mVideoPlayUntil.play();
                    mStartPausrButton.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_video_pause));
                }
                break;
            case R.id.activity_video_next_video:
                //切换视频（下一个）
                if (mVideoPlayUntil.mediaPlayer.isPlaying()) {
                    mVideoPlayUntil.pause();
                    mStartPausrButton.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_video_pause));
                    Toast.makeText(this, "已是第一个视频", Toast.LENGTH_SHORT).show();
                }
                if (mPosition < mVideoUrlList.size() - 1) {
                    saveSchedule(mVideoUrlList.get(mPosition), mVideoPlayUntil.mediaPlayer.getCurrentPosition());
                    mPosition++;
                    playVideo(mVideoPlayUntil, mVideoUrlList.get(mPosition), mVideoIdList.get(mPosition));
                    mVideoState = 0;
                    mStartPausrButton.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_video_pause));
                }
                break;
            case R.id.activity_video_last_video:
                //切换视频（上一个）
                if (mVideoPlayUntil.mediaPlayer.isPlaying()) {
                    mVideoPlayUntil.pause();
                    mStartPausrButton.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_video_pause));
                    Toast.makeText(this, "已是最后一个视频", Toast.LENGTH_SHORT).show();
                }
                if (mPosition > 0) {
                    saveSchedule(mVideoUrlList.get(mPosition), mVideoPlayUntil.mediaPlayer.getCurrentPosition());
                    mPosition--;
                    playVideo(mVideoPlayUntil, mVideoUrlList.get(mPosition), mVideoIdList.get(mPosition));
                    mVideoState = 0;
                    mStartPausrButton.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_video_pause));
                }
                break;
            case R.id.activity_video_first_start:
                //第一次播放
                playVideo(mVideoPlayUntil, mVideoUrlList.get(mPosition), mVideoIdList.get(mPosition));
                isReady = true;
                mStartPausrButton.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_video_pause));
                mStateLayout.setVisibility(View.VISIBLE);
                mFirstStartButton.setVisibility(View.GONE);
                mVideoDownloadButton.setVisibility(View.VISIBLE);
                break;
            case R.id.activity_video_download:
                Log.e("view", "点击下载");
                //下载视频
                HttpUtil.downloadFile(mVideoUrlList.get(mPosition),
                        "视频下载", genereteFileName(mVideoUrlList.get(mPosition)),
                        VideoActivity.this);
                break;
        }
    }


    private int progress;

    //监听进度条滑动
    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        this.progress = i * mVideoPlayUntil.mediaPlayer.getDuration() / seekBar.getMax();
    }

    //监听进度条开始滑动
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    //监听进度条停止滑动
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mVideoPlayUntil.mediaPlayer.seekTo(progress);
    }

    //文件名处理
    private String genereteFileName(String url) {
        String fileName = "";
        url = url.replace("/", "");
        url = url.replace("http:", "");
        url = url.replace("https:", "");
        fileName = url;
        return fileName;
    }

    //视频播放
    public void playVideo(VideoPlayUntil player, String url, String id) {
        //判断本地是否存在此视频
        String path = HttpUtil.getVideoPath(genereteFileName(url));
        if (path != "") {
            //存在播放本地视频
            player.playUrl(path);
            int position = mSharedPreferences.getInt(url, 0);
            if (position != 0) {
                Toast.makeText(this, "继续上次播放", Toast.LENGTH_SHORT).show();
            }
            player.mediaPlayer.seekTo(position);
        } else {
            //不存在播放网络视频
            player.playUrl(url);
            int position = mSharedPreferences.getInt(url, 0);
            if (position != 0) {
                Toast.makeText(this, "继续上次播放", Toast.LENGTH_SHORT).show();
            }
            player.mediaPlayer.seekTo(position);
        }
        saveHistory(id);
    }

    //储存进度
    public void saveSchedule(String url, int schedule) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(url, schedule);
        editor.apply();
    }

    //添加播放记录
    public void saveHistory(String id) {
        SharedPreferences.Editor editor = mHistorySharedPreferences.edit();
        String history = mHistorySharedPreferences.getString(HistoryActivity.HISTORY, "");
        history = history + "," + id;
        editor.putString(HistoryActivity.HISTORY, history);
        editor.apply();
    }
}
