package com.jct.oshri.videostreamingapp;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class CatalogActivity extends AppCompatActivity {

    Button playButton;
    String user;
    private String urlContetn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        Intent intent = getIntent();
        if(intent != null) {
            user = intent.getStringExtra(SignInActivity.USER_NAME);
            urlContetn = intent.getStringExtra(SignInActivity.PLAY_URL);
        }


        playButton = findViewById(R.id.playButton);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getBaseContext(), NavigationActivity.class);
                intent.putExtra(SignInActivity.USER_NAME, user);
                intent.putExtra(SignInActivity.PLAY_URL, urlContetn);
             //   intent.putExtra(SESSIONS_URL, "http://192.168.1.100:8081/sessions/open?id=" + user);

                startActivity(intent);


            }
        });

    }



}
