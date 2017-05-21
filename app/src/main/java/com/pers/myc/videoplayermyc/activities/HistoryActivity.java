package com.pers.myc.videoplayermyc.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.pers.myc.videoplayermyc.R;
import com.pers.myc.videoplayermyc.adapter.HistoryAdapter;
import com.pers.myc.videoplayermyc.models.Video;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {

    private ArrayList<Video> mVideoList;

    private RecyclerView mRecyclerView;

    public static final String HISTORY = "com.pers.myc.videoplayermyc.activities.historyactivity";
    //读取浏览历史
    SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        initData();

        initView();

        String history = mSharedPreferences.getString(HISTORY, "");
        String[] ht = history.split(",");
        ArrayList<String> historyList = new ArrayList<>();
        for (int i = 0; i < ht.length; i++) {
            for (int j = 0; j < historyList.size(); j++) {
                boolean exist = false;
                if (ht[i] == historyList.get(j)) {
                    exist = true;
                }
                if (!exist) {
                    historyList.add(ht[i]);
                }
            }
        }
        ArrayList<Video> videoList = new ArrayList<>();
        for (int i = historyList.size() - 1; i >= 0; i--) {
            for (int j = 0; j < mVideoList.size(); j++) {
                if (historyList.get(i) == mVideoList.get(j).getId()) {
                    videoList.add(mVideoList.get(j));
                }
            }
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        HistoryAdapter adapter = new HistoryAdapter(videoList, this);
        mRecyclerView.setAdapter(adapter);

    }

    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.activity_collection_rv);
    }

    private void initData() {
        mVideoList = HomeActivity.mVideoList;
        mSharedPreferences = HistoryActivity.this.getSharedPreferences(HISTORY, 0);
    }
}
