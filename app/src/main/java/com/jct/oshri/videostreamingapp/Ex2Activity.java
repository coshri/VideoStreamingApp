package com.jct.oshri.videostreamingapp;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.VideoView;

import static com.jct.oshri.videostreamingapp.Utilities.milliSecondsToTimer;

public class Ex2Activity extends AppCompatActivity implements View.OnClickListener {

  //  Uri streamUri = Uri.parse("http://10.56.186.198:10180/nba.mp4");
  Uri streamUri = Uri.parse("http://10.56.186.198:10188/chK_kDMMF1cwBlKvia8EiMpp_va9CvpDS2-p2sJYJhwp-o2kPLuljdM3L8oi-MBz/LINEAR/NBA/latest.m3u8");
    Button playButton;
    Button pauseButton;
    Button stopButton;
    VideoView mainVideoView;
    private boolean isPlay =false;


    void init() {
        playButton = findViewById(R.id.playButton);
        pauseButton = findViewById(R.id.pauseButton);
        stopButton = findViewById(R.id.stopButton);
        mainVideoView = findViewById(R.id.mainVideoView);


        playButton.setOnClickListener(this);
        pauseButton.setOnClickListener(this);
        stopButton.setOnClickListener(this);

        mainVideoView.setVideoURI(streamUri);
       // mainVideoView.stopPlayback();
        isPlay = true;

//        DisplayMetrics metrics = new DisplayMetrics(); getWindowManager().getDefaultDisplay().getMetrics(metrics);
//        android.widget.LinearLayout.LayoutParams params = (android.widget.LinearLayout.LayoutParams) mainVideoView.getLayoutParams();
//        params.width =  metrics.widthPixels;
//        params.height = metrics.heightPixels;
//        params.leftMargin = 0;
//        mainVideoView.setLayoutParams(params);




        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(mainVideoView);
        mainVideoView.setMediaController(mediaController);


        mainVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                Toast.makeText(getBaseContext(),"onPrepared",Toast.LENGTH_LONG).show();
            }
        });
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ex2);
        init();
    }

    @Override
    public void onClick(View v) {
        if (v == playButton)
            play();
        else if (v == pauseButton)
            pause();
        else if (v == stopButton)
            stop();
    }

    private void play() {
        playButton.setEnabled(false);
        mainVideoView.start();
//        new Handler().postDelayed(new Runnable() {
//            public void run() {
//                position = mainVideoView.getCurrentPosition();
//                // videoSeekBar.setProgress(position);
//                seekTextView.setText("" + position + " / " + duration);
//            }
//        }, 1000);
        isPlay = true;


        pauseButton.setEnabled(true);
        stopButton.setEnabled(true);
    }

    private void pause() {
        pauseButton.setEnabled(false);
        mainVideoView.pause();
        isPlay = false;
        playButton.setEnabled(true);
        stopButton.setEnabled(true);
    }

    private void stop() {
        stopButton.setEnabled(false);
        mainVideoView.stopPlayback();
        isPlay = false;
        pauseButton.setEnabled(false);
        playButton.setEnabled(true);
    }

}
