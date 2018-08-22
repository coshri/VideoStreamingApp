package com.jct.oshri.videostreamingapp;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import static com.jct.oshri.videostreamingapp.Utilities.*;

public class ex1Activity extends AppCompatActivity implements View.OnClickListener {


   Uri streamUri = Uri.parse("https://www.rmp-streaming.com/media/bbb-360p.mp4");
   //Uri streamUri = Uri.parse("http://10.56.186.198:10188/chK_kDMMF1cwBlKvia8EiMpp_va9CvpDS2-p2sJYJhwp-o2kPLuljdM3L8oi-MBz/LINEAR/NBA/latest.m3u8");
    Button playButton;
    Button pauseButton;
    Button stopButton;
    VideoView mainVideoView;
    SeekBar videoSeekBar;
    TextView seekTextView;
    int position = -1;
    int duration = -1;

    Handler handler;

    boolean isPlay = false;

    void init() {
        playButton = findViewById(R.id.playButton);
        pauseButton = findViewById(R.id.pauseButton);
        stopButton = findViewById(R.id.stopButton);
        mainVideoView = findViewById(R.id.mainVideoView);
        videoSeekBar = findViewById(R.id.videoSeekBar);
        seekTextView = findViewById(R.id.seekTextView);


        playButton.setOnClickListener(this);
        pauseButton.setOnClickListener(this);
        stopButton.setOnClickListener(this);

        mainVideoView.setVideoURI(streamUri);
       mainVideoView.stopPlayback();

        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(mainVideoView);
        mainVideoView.setMediaController(mediaController);


        mainVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                position = mainVideoView.getCurrentPosition();
                duration = mainVideoView.getDuration();

                seekTextView.setText("" + milliSecondsToTimer(position) + " / " + milliSecondsToTimer(duration));
                videoSeekBar.setMax(duration);
                videoSeekBar.setProgress(position);
            }
        });

        videoSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
              //  mainVideoView.seekTo(progress);
                position = progress;
                seekTextView.setText("" + milliSecondsToTimer(position) + " / " + milliSecondsToTimer(duration));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mainVideoView.seekTo(seekBar.getProgress());
            }
        });
    }


    void runUpdateTime() {

        handler = new Handler();

        new Thread() {
            @Override
            public void run() {
                while (isPlay) {
                    handler.post(new Runnable() {
                        public void run() {
                            position = mainVideoView.getCurrentPosition();

                          //    seekTextView.setText("" + milliSecondsToTimer( position) + " / " + milliSecondsToTimer(duration));
                            videoSeekBar.setProgress(position);
                        }
                    });
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ex1);
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
        runUpdateTime();

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
