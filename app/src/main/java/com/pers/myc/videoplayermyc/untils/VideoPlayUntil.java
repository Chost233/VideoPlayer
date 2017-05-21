package com.pers.myc.videoplayermyc.untils;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.pers.myc.videoplayermyc.listeners.VideoCompleteListener;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2017/5/20.
 */

public class VideoPlayUntil implements MediaPlayer.OnBufferingUpdateListener,
        MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener,
        SurfaceHolder.Callback {
    //视频长宽
    private int videoWidth;
    private int videoHeight;
    public MediaPlayer mediaPlayer;
    private SurfaceHolder surfaceHolder;
    private SeekBar skbProgress;
    private Timer mTimer = new Timer();
    private VideoCompleteListener mCompleteListener = null;
    private SurfaceView mSurfaceView;
    private String url;

    public VideoPlayUntil(SurfaceView surfaceView, SeekBar skbProgress) {
        this.skbProgress = skbProgress;
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setKeepScreenOn(true);
        mTimer.schedule(mTimerTask, 0, 1000);
        this.mSurfaceView = surfaceView;
    }

    public void setVideoCompleteListener(VideoCompleteListener listener) {
        this.mCompleteListener = listener;
    }

    //通过计时器更新进度条
    TimerTask mTimerTask = new TimerTask() {
        @Override
        public void run() {
            if (mediaPlayer == null)
                return;
            if (mediaPlayer.isPlaying() && skbProgress.isPressed() == false) {
                handleProgress.sendEmptyMessage(0);
            }
        }
    };

    Handler handleProgress = new Handler() {
        public void handleMessage(Message msg) {

            try {
                int position = mediaPlayer.getCurrentPosition();
                int duration = mediaPlayer.getDuration();

                if (duration > 0) {
                    long pos = skbProgress.getMax() * position / duration;
                    skbProgress.setProgress((int) pos);
                }
            } catch (Exception e) {

            }

        }

        ;
    };

    public void play() {
        if (videoHeight != 0 && videoWidth != 0) {
            mediaPlayer.start();
        }
    }



    public void playUrl(String videoUrl) {
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(videoUrl);
            mediaPlayer.prepare();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void pause() {
        mediaPlayer.pause();
    }

    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder arg0) {
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDisplay(surfaceHolder);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnBufferingUpdateListener(this);
            mediaPlayer.setOnPreparedListener(this);
        } catch (Exception e) {

        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder arg) {
        if (mCompleteListener != null) {
            mCompleteListener.videoComplete(mediaPlayer.getCurrentPosition());
        }
        stop();

    }


    @Override
    public void onPrepared(MediaPlayer arg) {
        videoWidth = mediaPlayer.getVideoWidth();
        videoHeight = mediaPlayer.getVideoHeight();

        ViewGroup.LayoutParams layoutParams = mSurfaceView.getLayoutParams();
        layoutParams.width = videoWidth * mSurfaceView.getHeight() / videoHeight;
        if (layoutParams.width > mSurfaceView.getWidth()) {
            layoutParams.width = mSurfaceView.getWidth();
        }
        mSurfaceView.setLayoutParams(layoutParams);

        if (videoHeight != 0 && videoWidth != 0) {
            arg.start();
        }


    }

    @Override
    public void onCompletion(MediaPlayer arg) {

    }

    @Override
    public void onBufferingUpdate(MediaPlayer arg0, int bufferingProgress) {
        skbProgress.setSecondaryProgress(bufferingProgress);
        //int currentProgress = skbProgress.getMax() * mediaPlayer.getCurrentPosition() / mediaPlayer.getDuration();
    }

}
