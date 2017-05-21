package com.pers.myc.videoplayermyc.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.pers.myc.videoplayermyc.R;
import com.pers.myc.videoplayermyc.adapter.VideoAdapter;
import com.pers.myc.videoplayermyc.models.Video;
import com.pers.myc.videoplayermyc.models.VideoAnalysis;
import com.pers.myc.videoplayermyc.untils.JsonUntil;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    //第一页数据
    private VideoAnalysis mVideoAnalysis;
    //视频列表
    public static ArrayList<Video> mVideoList;
    //视频RecyclerView
    private RecyclerView mVideoRV;
    //打开浏览列表
    private ImageView mHistoryButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //初始化数据
        initData();
        //初始化视图
        initView();


        //配置recyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mVideoRV.setLayoutManager(layoutManager);
        VideoAdapter adapter = new VideoAdapter(mVideoList, this);
        mVideoRV.setAdapter(adapter);

        mHistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, HistoryActivity.class);
                startActivity(intent);
            }
        });

    }

    private void initView() {
        mHistoryButton = (ImageView) findViewById(R.id.activity_home_collection);
        mVideoRV = (RecyclerView) findViewById(R.id.activity_home_rv);
    }

    private void initData() {
        //获取第一页json数据
        String response = (String) getIntent().getExtras().get(StartActivity.HOME_ID);
        //解析第一页json数据
        mVideoAnalysis = (VideoAnalysis) JsonUntil.analysis(response, VideoAnalysis.class);
        //根据返回标志判断信息获取是否成功
        if (Integer.valueOf(mVideoAnalysis.showapi_res_code) != 0) {
            Toast.makeText(this, "信息获取失败", Toast.LENGTH_SHORT).show();
        } else {
            mVideoList = new ArrayList<>();
            final ArrayList<VideoAnalysis.Showapi_res_body.Pagebean.Contentlist> list = (ArrayList) mVideoAnalysis.showapi_res_body.pagebean.contentlist;
            for (int i = 0; i < list.size(); i++) {
                mVideoList.add(i, new Video(list.get(i).text,
                        list.get(i).love,
                        list.get(i).hate,
                        list.get(i).profile_image,
                        list.get(i).name,
                        list.get(i).weixin_url,
                        list.get(i).create_time,
                        list.get(i).video_uri,
                        list.get(i).id));
            }
        }
    }
}
