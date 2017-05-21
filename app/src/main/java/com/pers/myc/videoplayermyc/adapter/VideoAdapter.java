package com.pers.myc.videoplayermyc.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pers.myc.videoplayermyc.R;
import com.pers.myc.videoplayermyc.activities.VideoActivity;
import com.pers.myc.videoplayermyc.imageloader.ImageDoubleCache;
import com.pers.myc.videoplayermyc.imageloader.ImageLoader;
import com.pers.myc.videoplayermyc.models.Video;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/5/20.
 */

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {

    private List<Video> mVideoList;
    private Activity mActivity;
    private ArrayList<String> mVideoUrlList;
    private ArrayList<String> mVideoIdList;

    public VideoAdapter(List<Video> videoList, Activity activity) {
        mVideoList = videoList;
        mActivity = activity;
        mVideoUrlList = new ArrayList<>();
        mVideoIdList = new ArrayList<>();
        for (int i = 0; i < mVideoList.size(); i++) {
            mVideoUrlList.add(i, mVideoList.get(i).getVideo_url());
            mVideoIdList.add(i, mVideoList.get(i).getId());
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        View mVideoView;
        ImageView mProfileImageIV;
        TextView mProfileNameTV;
        TextView mReleaseTimeTV;
        TextView mVideoTextTV;
        ImageView mVideoImageIV;
        TextView mLoveAmountTV;
        TextView mHateAmountTV;

        public ViewHolder(View itemView) {
            super(itemView);
            mVideoView = itemView;
            mProfileImageIV = (ImageView) mVideoView.findViewById(R.id.item_video_profile_image);
            mProfileNameTV = (TextView) mVideoView.findViewById(R.id.item_video_profile_name);
            mReleaseTimeTV = (TextView) mVideoView.findViewById(R.id.item_video_release_time);
            mVideoTextTV = (TextView) mVideoView.findViewById(R.id.item_video_text);
            mVideoImageIV = (ImageView) mVideoView.findViewById(R.id.item_video_image);
            mLoveAmountTV = (TextView) mVideoView.findViewById(R.id.item_video_love_tv);
            mHateAmountTV = (TextView) mVideoView.findViewById(R.id.item_video_hate_tv);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video_layout, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        //当前video
        final Video video = mVideoList.get(position);
        //显示作者头像
        ImageLoader imageLoader = new ImageLoader(mActivity);
        imageLoader.setCache(new ImageDoubleCache());
        holder.mProfileImageIV.setImageBitmap(BitmapFactory.decodeResource(mActivity.getResources(), R.mipmap.ic_placeholder));
        imageLoader.displayImage(video.getProfile_image(), holder.mProfileImageIV);
        //显示作者名
        holder.mProfileNameTV.setText(video.getName());
        //显示发布时间
        holder.mReleaseTimeTV.setText(video.getCreate_time());
        //显示视频正文
        holder.mVideoTextTV.setText(video.getText());
        //显示赞踩数
        holder.mLoveAmountTV.setText(video.getLove());
        holder.mHateAmountTV.setText(video.getHate());
        //显示视频缩略图
        holder.mVideoImageIV.setImageBitmap(BitmapFactory.decodeResource(mActivity.getResources(), R.mipmap.ic_placeholder));
        imageLoader.displayFirstFrame(video.getVideo_url(), holder.mVideoImageIV);
        //监听点击事件
        holder.mVideoImageIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mActivity, VideoActivity.class);
                intent.putExtra("urlList", mVideoUrlList);
                intent.putExtra("idList", mVideoIdList);
                intent.putExtra("index", position);
                mActivity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mVideoList.size();
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);
    }
}
