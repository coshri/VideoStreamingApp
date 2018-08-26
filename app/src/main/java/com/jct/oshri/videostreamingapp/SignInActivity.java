package com.jct.oshri.videostreamingapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONObject;

public class SignInActivity extends AppCompatActivity {

    static final String USER_NAME = "user name";
    static final String PLAY_URL = "play url";
    static final String SESSIONS_URL = "sessions url";


    ListView usersListView;
    String[] users;
    ProgressBar progressBar;
    ImageButton settingsImageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        usersListView = findViewById(R.id.usersListView);
        progressBar = findViewById(R.id.progressBar);


        settingsImageButton = findViewById(R.id.settingsImageButton);
        settingsImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), SettingsActivity.class));
            }
        });


        users = getResources().getStringArray(R.array.userNameList);

        usersListView.setAdapter(new ArrayAdapter<String>(this,
                R.layout.user_item
                , users));

        usersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                String user = users[position];
                Toast.makeText(getBaseContext(), "select " + user, Toast.LENGTH_LONG).show();

                new sidnInAsync().execute(user);


            }
        });

    }


    class sidnInAsync extends AsyncTask<String, Integer, String> {

        String user = null;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {

            String urlResult = null;

            try {
                //  Thread.sleep(3000);
                user = strings[0];

                String url = "http://192.168.1.100:8080/url/LIVE-NBA";

                JSONObject postData = new JSONObject();
                postData.put("uname", user);
                postData.put("pwd", user);

                urlResult = HttpTools.PostJson(url, postData);


            } catch (Exception e) {
                e.printStackTrace();
                user = null;
            }

            return urlResult;
        }

        @Override
        protected void onPostExecute(String url) {
            super.onPostExecute(user);
            progressBar.setVisibility(View.INVISIBLE);

            if (url != null) {
                Intent intent = new Intent(getBaseContext(), CatalogActivity.class);
                intent.putExtra(USER_NAME, user);
                intent.putExtra(PLAY_URL, url);
                //intent.putExtra(SESSIONS_URL, "http://192.168.1.100:8081/sessions/open?id=" + user);

                startActivity(intent);
            } else {
                // Toast.makeText(getba)
            }

        }
    }

}