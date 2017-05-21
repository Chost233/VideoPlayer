package com.pers.myc.videoplayermyc.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pers.myc.videoplayermyc.R;
import com.pers.myc.videoplayermyc.activities.HistoryActivity;
import com.pers.myc.videoplayermyc.imageloader.ImageLoader;
import com.pers.myc.videoplayermyc.models.Video;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/5/21.
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    ArrayList<Video> mVideoList;
    Activity mActivity;
    ImageLoader mImageLoader = new ImageLoader(mActivity);


    public HistoryAdapter(ArrayList<Video> videoList, HistoryActivity activity) {
        this.mVideoList = videoList;
        this.mActivity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history_layout, parent, false);
        HistoryAdapter.ViewHolder holder = new HistoryAdapter.ViewHolder(view);
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        mImageLoader.displayFirstFrame(mVideoList.get(position).getVideo_url(), holder.mImageView);
        holder.mTextView.setText(mVideoList.get(position).getText());
    }

    @Override
    public int getItemCount() {
        return mVideoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        View mHistoryView;
        ImageView mImageView;
        TextView mTextView;


        public ViewHolder(View itemView) {
            super(itemView);
            mHistoryView = itemView;
            mImageView = (ImageView) mHistoryView.findViewById(R.id.item_history_img);
            mTextView = (TextView) mHistoryView.findViewById(R.id.item_history_text);
        }
    }
}
