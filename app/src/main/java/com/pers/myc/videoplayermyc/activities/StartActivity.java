package com.pers.myc.videoplayermyc.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.Toast;

import com.pers.myc.videoplayermyc.R;
import com.pers.myc.videoplayermyc.listeners.HttpCallbackListener;
import com.pers.myc.videoplayermyc.untils.HttpUtil;

import java.io.InputStream;

public class StartActivity extends AppCompatActivity {

    private ImageView mIVStartImage;
    private Boolean isWIFI;

    private final String HOME_URL = "http://route.showapi.com/255-1?showapi_appid=38539&showapi_sign=06e050db998c47fea778d0fd229d6bec&type=41&title=&page=&";
    public static final String HOME_ID = "Home Activity Content";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        isWIFI = isWifiConnected(this);

        //初始化视图
        initView();

        HttpUtil.sendHtttpRequest(HOME_URL, new HttpCallbackListener() {
            @Override
            public void onFinish(String response, InputStream inputStream) {
                final Intent intent = new Intent(StartActivity.this, HomeActivity.class);
                intent.putExtra(HOME_ID, response);
                try {
                    Thread.sleep(500);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (isWIFI)
                                startActivity(intent);
                            else {
                                Toast.makeText(StartActivity.this, "当前不是wifi环境\n（测试时候流量损失惨重，加个检测是否wifi环境保险点）", Toast.LENGTH_LONG).show();
                                startActivity(intent);
                            }
                        }
                    });
                    StartActivity.this.finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void initView() {
        mIVStartImage = (ImageView) findViewById(R.id.welcome_image);
    }

    //检测是否连接wifi
    public static boolean isWifiConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiNetworkInfo.isConnected()) {
            return true;
        }

        return false;
    }
}
