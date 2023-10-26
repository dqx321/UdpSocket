package com.example.ui;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;
import com.example.radarmapview.R;

import java.io.File;

/**
 * @author dai.qx
 * @time 2023-4-18
 * QQ:2445930742
 */
public class PlayActivity extends Activity {
    String path = "";
    private VideoView videoView;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_play);
        path = getIntent().getStringExtra("path");
        videoView = findViewById(R.id.video);
        playVideo(path);

    }

    private void playVideo(String path) {
        try {


            //获取文件对象
            File file = new File(path);
            if (file.exists()) {
                //如果文件存在,则指定要播放的视频
                videoView.setVideoPath(file.getAbsolutePath());
            } else {
                Toast.makeText(this, "要播放的视频文件不存在", Toast.LENGTH_SHORT).show();
            }
            /**
             * 控制视频的播放 主要通过MediaController控制视频的播放
             */
            //创建MediaController对象
            MediaController mediaController = new MediaController(this);
            mediaController.setVisibility(View.INVISIBLE);
            videoView.setMediaController(mediaController); //让videoView 和 MediaController相关联
            videoView.setFocusable(true); //让VideoView获得焦点

            //给videoView添加完成事件监听器，监听视频是否播放完毕
            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    // 通过MediaPlayer设置循环播放
                    mp.setLooping(true);
                    // OnPreparedListener中的onPrepared方法是在播放源准备完成后回调的，所以可以在这里开启播放
                    mp.start();
                }
            });
            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
