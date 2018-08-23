package com.jct.oshri.videostreamingapp;

import android.content.Intent;
import android.media.projection.MediaProjection;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button ex1Button;
    Button ex2Button;
Button ex3Button;
    Button ex4Button;

    void init()
    {
        ex1Button = findViewById(R.id.ex1Button);
        ex1Button.setOnClickListener(this);

        ex2Button = findViewById(R.id.ex2Button);
        ex2Button.setOnClickListener(this);



        ex3Button = findViewById(R.id.ex3Button);
        ex3Button.setOnClickListener(this);

        ex4Button = findViewById(R.id.ex4Button);
        ex4Button.setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    @Override
    public void onClick(View v) {
        if(v==ex1Button)
            startActivity(new Intent(this,ex1Activity.class));
        if(v==ex2Button)
            startActivity(new Intent(this,Ex2Activity.class));
        if(v==ex3Button)
            startActivity(new Intent(this,FullscreenPlayActivity.class));
        if(v==ex4Button)
            startActivity(new Intent(this,NavigationActivity.class));
    }


}
